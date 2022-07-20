import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class Main1 {
    public static void main(String[] args) {

        Object obj,obj1;
        Map<String,String> mapProductCode = new HashMap<>();
        List<Product> products = new ArrayList<>();
        List<ObjectJson> objectJsonList = new ArrayList<>();

        try {
            obj = new JSONParser().parse(new FileReader("QP_hierarchy1.json"));
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray groupHierarchyListJSON = (JSONArray) jsonObject.get("groupHierarchyList");
//            System.err.println(jsonObjects.size());

            for(Object object:groupHierarchyListJSON){
                JSONObject jsonObject1 = (JSONObject) object;

                JSONArray hierarchyListJSON = (JSONArray) jsonObject1.get("hierarchyList");
                String groupHierarchyName = jsonObject1.get("name").toString().trim();
                String groupHierarchyCode = jsonObject1.get("code").toString().trim();
                String lineBusiness = jsonObject1.get("lineBusiness").toString().trim();


                for (Object object1:hierarchyListJSON){
                    JSONObject jsonObject2 = (JSONObject) object1;

                    String nameHierarchy = jsonObject2.get("name").toString().trim();
                    String codeHierarchy  = jsonObject2.get("code").toString().trim();
                    String levelHierarchy = jsonObject2.get("level")!=null?jsonObject2.get("level").toString().trim():"";
                    JSONArray parentNameJSON = (JSONArray) jsonObject2.get("parentName");

                    String parentName = "";
                    int i=0;
                    if (parentNameJSON!=null && parentNameJSON.size()>0){
//                        System.err.println(parentNameJSON.size());
                        for (Object object2: parentNameJSON){
                            if (i>0){
                                parentName = parentName+";";
                            }
                            try {
                                JSONObject jsonObject3 = (JSONObject) object2;
                                parentName = parentName + jsonObject3.get("name").toString().trim();
                                i++;
                            } catch (Exception e){

                            };
                        }
                    }


                    JSONArray dataJSON = (JSONArray) jsonObject2.get("data");
                    List<String> data = new ArrayList<>();
                    for (Object object2: dataJSON){
                        JSONObject jsonObject3 = (JSONObject) object2;
                        data.add(jsonObject3.get("value").toString().trim());
                    }

                    ObjectJson  objectJson = new ObjectJson();
                    objectJson.setGroupHierarchyCode(groupHierarchyCode);
                    objectJson.setGroupHierarchyName(groupHierarchyName);
                    objectJson.setLineBusiness(lineBusiness);
                    objectJson.setCodeHierarchy(codeHierarchy);
                    objectJson.setNameHierarchy(nameHierarchy);
                    objectJson.setParentName(parentName);
                    objectJson.setLevelHierarchy(levelHierarchy);
                    objectJson.setData(data);

                    objectJsonList.add(objectJson);
                }
            }

//            objectJsonList.forEach(e->System.out.println(e.toString()));

            final String excelFilePath = "excel.xlsx";

            writeExcel(objectJsonList,excelFilePath);

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

    }

    public static void writeExcel(List<ObjectJson> objectJsonList, String excelFilePath) throws IOException {
        // Create Workbook
        Workbook workbook = getWorkbook(excelFilePath);

        // Create sheet
        Sheet sheet = workbook.createSheet("Books"); // Create sheet with sheet name

        int rowIndex = 0;

        writeHeader(sheet,rowIndex);
        // Write data
        rowIndex++;
        for (ObjectJson objectJson : objectJsonList) {
            // Create row
            Row row = sheet.createRow(rowIndex);
            // Write data on row
            writeObject(objectJson, row);
            rowIndex++;
        }


        // Auto resize column witdth
        int numberOfColumn = sheet.getRow(0).getPhysicalNumberOfCells();
        autosizeColumn(sheet, numberOfColumn);

        // Create file excel
        createOutputFile(workbook, excelFilePath);
        System.out.println("Done!!!");
    }



    // Write header with format
    private static void writeHeader(Sheet sheet, int rowIndex) {

        Row row = sheet.createRow(rowIndex);
        // Create cells
        Cell cell = row.createCell(0);
        cell.setCellValue("Group name");

        cell = row.createCell(1);
        cell.setCellValue("Group code");

        cell = row.createCell(2);
        cell.setCellValue("lineBusiness");

        cell = row.createCell(3);
        cell.setCellValue("Name");

        cell = row.createCell(4);
        cell.setCellValue("Code");

        cell = row.createCell(5);
        cell.setCellValue("Level");

        cell = row.createCell(6);
        cell.setCellValue("Parent name");

        for (int i=1; i<=9; i++){
            cell = row.createCell(6+i);
            cell.setCellValue("Cấp " + i);

        }




    }

    // Create workbook
    private static Workbook getWorkbook(String excelFilePath) throws IOException {
        Workbook workbook = null;

        if (excelFilePath.endsWith("xlsx")) {
            workbook = new XSSFWorkbook();
        } else if (excelFilePath.endsWith("xls")) {
            workbook = new HSSFWorkbook();
        } else {
            throw new IllegalArgumentException("The specified file is not Excel file");
        }

        return workbook;
    }



    // Write data
    private static void writeObject(ObjectJson object, Row row) {
        Cell cell = row.createCell(0);
        cell.setCellValue(object.getGroupHierarchyName());

        cell = row.createCell(1);
        cell.setCellValue(object.getGroupHierarchyCode());

        cell = row.createCell(2);
        cell.setCellValue(object.getLineBusiness());

        cell = row.createCell(3);
        cell.setCellValue(object.getNameHierarchy());

        cell = row.createCell(4);
        cell.setCellValue(object.getCodeHierarchy());

        cell = row.createCell(5);
        cell.setCellValue(object.getLevelHierarchy());

        cell = row.createCell(6);
        cell.setCellValue(object.getParentName());

        for (int i=1; i<=9; i++){
            cell = row.createCell(6+i);
            cell.setCellValue(object.getData().get(i-1));

        }

    }




    // Auto resize column width
    private static void autosizeColumn(Sheet sheet, int lastColumn) {
        for (int columnIndex = 0; columnIndex < lastColumn; columnIndex++) {
            sheet.autoSizeColumn(columnIndex);
        }
    }

    // Create output file
    private static void createOutputFile(Workbook workbook, String excelFilePath) throws IOException {
        try (OutputStream os = new FileOutputStream(excelFilePath)) {
            workbook.write(os);
        }
    }




}

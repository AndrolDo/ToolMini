import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Main {
    public static void main(String[] args) {

        Object obj,obj1;
        Map<String,String> mapProductCode = new HashMap<>();
        List<Product> products = new ArrayList<>();
        try {
            obj = new JSONParser().parse(new FileReader("QP_hierarchy.json"));
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray jsonObjects = (JSONArray) jsonObject.get("groupHierarchyList");
//            System.err.println(jsonObjects.size());
            for(Object object:jsonObjects){
                JSONObject jsonObject1 = (JSONObject) object;
                JSONArray jsonObject1s = (JSONArray) jsonObject1.get("hierarchyList");
                for (Object object1:jsonObject1s){
                    JSONObject jsonObject2 = (JSONObject) object1;
                    String source = jsonObject2.get("name").toString().trim();
                    String name = jsonObject2.get("name").toString().trim();
                    if (name.contains("_")){
                        name = name.substring(name.indexOf("_")+1);
                    }

                    mapProductCode.put(name,jsonObject2.get("code").toString().trim());
                    products.add(new Product(name,jsonObject2.get("code").toString().trim(),source));

                }
            }

//            mapProductCode.forEach((K,V)->{System.out.println(K + " : "+V);});
//            products.forEach(p->{
//                System.out.println(p.getCode() + " : " + p.getName());
//            });

            final String excelFilePath = "t4.xlsx";
            List<String> list = readExcel(excelFilePath);

//            list.forEach(System.out::println);

            for (int i=0 ; i<list.size(); i++){
                String str = list.get(i);
                Boolean f = false;
                for (Product product:products){
                    if (!f && product.getName().equalsIgnoreCase(str)){
//                        System.out.println(i+1 + ". " + product.getCode() + ":" + product.getName()+" = " + str);
                        System.out.println(product.getCode());
//                        System.out.println(product.getSource());
                        f=true;
                    };
                };
                if (!f){
//                    System.out.println(i+1+" ============ "+str);
                    System.out.println("");
//                    System.out.println(str);
                }

//                i++;
            }
            System.out.println(list.size());


        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }



    }

    public static List<String> readExcel(String excelFilePath) throws IOException {
        List<String> list = new ArrayList<>();

        // Get file
        InputStream inputStream = new FileInputStream(new File(excelFilePath));

        // Get workbook
        Workbook workbook = getWorkbook(inputStream, excelFilePath);

        // Get sheet
        Sheet sheet = workbook.getSheetAt(0);

        // Get all rows
        Iterator<Row> iterator = sheet.iterator();
        while (iterator.hasNext()) {
            Row nextRow = iterator.next();
//            if (nextRow.getRowNum() == 0) {
//                // Ignore header
//                continue;
//            }

            // Get all cells
            Iterator<Cell> cellIterator = nextRow.cellIterator();

            // Read cells and set value for book object

            while (cellIterator.hasNext()) {
                //Read cell
                Cell cell = cellIterator.next();
                Object cellValue = getCellValue(cell);
                if (cellValue == null || cellValue.toString().isEmpty()) {
                    continue;
                }
                // Set value for book object
                int columnIndex = cell.getColumnIndex();
                switch (columnIndex) {
                    case 0:
                        String temp =  (String) getCellValue(cell);
                        list.add(temp.replace("{Mã SP}_","").trim());
                        break;
//                    case COLUMN_INDEX_TITLE:
//                        book.setTitle((String) getCellValue(cell));
//                        break;
//                    case COLUMN_INDEX_QUANTITY:
//                        book.setQuantity(new BigDecimal((double) cellValue).intValue());
//                        break;
//                    case COLUMN_INDEX_PRICE:
//                        book.setPrice((Double) getCellValue(cell));
//                        break;
//                    case COLUMN_INDEX_TOTAL:
//                        book.setTotalMoney((Double) getCellValue(cell));
//                        break;
                    default:
                        break;
                }

            }
        }

        workbook.close();
        inputStream.close();

        return list;
    }

    // Get Workbook
    private static Workbook getWorkbook(InputStream inputStream, String excelFilePath) throws IOException {
        Workbook workbook = null;
        if (excelFilePath.endsWith("xlsx")) {
            workbook = new XSSFWorkbook(inputStream);
        } else if (excelFilePath.endsWith("xls")) {
            workbook = new HSSFWorkbook(inputStream);
        } else {
            throw new IllegalArgumentException("The specified file is not Excel file");
        }

        return workbook;
    }

    // Get cell value
    private static Object getCellValue(Cell cell) {
        CellType cellType = cell.getCellTypeEnum();
        Object cellValue = null;
        switch (cellType) {
            case BOOLEAN:
                cellValue = cell.getBooleanCellValue();
                break;
            case FORMULA:
                Workbook workbook = cell.getSheet().getWorkbook();
                FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
                cellValue = evaluator.evaluate(cell).getNumberValue();
                break;
            case NUMERIC:
                cellValue = cell.getNumericCellValue();
                break;
            case STRING:
                cellValue = cell.getStringCellValue();
                break;
            case _NONE:
            case BLANK:
            case ERROR:
                break;
            default:
                break;
        }

        return cellValue;
    }
}

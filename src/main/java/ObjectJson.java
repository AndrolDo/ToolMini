import java.util.List;

public class ObjectJson {
    private String groupHierarchyName;
    private String groupHierarchyCode;
    private String nameHierarchy;
    private String codeHierarchy;
    private String levelHierarchy;
    private String parentName;
    private List<String> data;

    private String lineBusiness;

    public String getLineBusiness() {
        return lineBusiness;
    }

    public void setLineBusiness(String lineBusiness) {
        this.lineBusiness = lineBusiness;
    }

    public ObjectJson() {
    }

    public ObjectJson(String groupHierarchyName, String groupHierarchyCode, String nameHierarchy, String codeHierarchy, String levelHierarchy, String parentName, List<String> data) {
        this.groupHierarchyName = groupHierarchyName;
        this.groupHierarchyCode = groupHierarchyCode;
        this.nameHierarchy = nameHierarchy;
        this.codeHierarchy = codeHierarchy;
        this.levelHierarchy = levelHierarchy;
        this.parentName = parentName;
        this.data = data;
    }

    @Override
    public String toString() {
        return "ObjectJson{" +
                "groupHierarchyName='" + groupHierarchyName + '\'' +
                ", groupHierarchyCode='" + groupHierarchyCode + '\'' +
                ", nameHierarchy='" + nameHierarchy + '\'' +
                ", codeHierarchy='" + codeHierarchy + '\'' +
                ", levelHierarchy='" + levelHierarchy + '\'' +
                ", parentName='" + parentName + '\'' +
                ", data=" + data +
                ", lineBusiness='" + lineBusiness + '\'' +
                '}';
    }

    public String getGroupHierarchyName() {
        return groupHierarchyName;
    }

    public void setGroupHierarchyName(String groupHierarchyName) {
        this.groupHierarchyName = groupHierarchyName;
    }

    public String getGroupHierarchyCode() {
        return groupHierarchyCode;
    }

    public void setGroupHierarchyCode(String groupHierarchyCode) {
        this.groupHierarchyCode = groupHierarchyCode;
    }

    public String getNameHierarchy() {
        return nameHierarchy;
    }

    public void setNameHierarchy(String nameHierarchy) {
        this.nameHierarchy = nameHierarchy;
    }

    public String getCodeHierarchy() {
        return codeHierarchy;
    }

    public void setCodeHierarchy(String codeHierarchy) {
        this.codeHierarchy = codeHierarchy;
    }

    public String getLevelHierarchy() {
        return levelHierarchy;
    }

    public void setLevelHierarchy(String levelHierarchy) {
        this.levelHierarchy = levelHierarchy;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }
}

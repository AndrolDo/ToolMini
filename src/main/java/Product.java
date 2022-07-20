public class Product {
    private String name;
    private String code;

    private String source;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Product(String name, String code, String source) {
        this.name = name;
        this.code = code;
        this.source = source;
    }

    public Product(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}

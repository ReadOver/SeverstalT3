public class DBHelper {
    private String period;
    private String name;
    private double price;
    private double weight;
    private int productId;

    public DBHelper(String period, String name, double price) {
        this.period = period;
        this.name = name;
        this.price = price;
    }

    public DBHelper(int productId,double  price) {
        this.productId = productId;
        this.price = price;
    }

    public DBHelper(String period,String name, double  weight,double price) {
        this.period = period;
        this.name = name;
        this.weight=weight;
        this.price = price;
    }

    // Getters and setters for the fields
    public String getPeriod() { return period; }
    public void setPeriod(String period) { this.period = period; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getProductId() { return productId; }
    public void setProductId(int productId ) { this.productId = productId; }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}

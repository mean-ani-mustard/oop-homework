package data;
public class Product {
    private String productId;
    private String productName;
    private String imageFileName;
    private double productPrice;

    // Constructor with double price
    public Product(String productId, String productName, String imageFileName, double productPrice) {
        this.productId = productId;
        this.productName = productName;
        this.imageFileName = imageFileName;
        this.productPrice = productPrice;
    }

    // Setters

    public String getProductId() {
        return productId;
    }


    public String getProductName() {
        return productName;
    }


    public String getImageFileName() {
        return imageFileName;
    }


    public double getProductPrice() {
        return productPrice;
    }


    @Override
    public String toString() {
        return "Product{" + "productId='" + productId + '\'' + ", productName='" + productName + '\'' +
                ", imageFileName='" + imageFileName + '\'' + ", productPrice=" + productPrice + '}';
    }
}

package data;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.*;
import java.util.ArrayList;


public class DataBase {
    private static final String USERNAME = "root";
    private static final String PASSWORD = "Apple123!";
    private static final String DBNAME = "myStudentStore";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/" + DBNAME ;

    private  Connection connection;
    private  ArrayList<data.Product> products;

    public DataBase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            BasicDataSource dataSource = new BasicDataSource();
            dataSource.setUrl(DB_URL);
            dataSource.setUsername(USERNAME);
            dataSource.setPassword(PASSWORD);

            connection = dataSource.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to establish database connection.");
        }
    }

    public  ArrayList<Product> getProductList() throws SQLException {
        if (products != null) return products; // cache

        String query = "SELECT * FROM products";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            products = new ArrayList<>();
            while (rs.next()) {
                String productId = rs.getString("productid");
                String productName = rs.getString("name");
                String imageName = rs.getString("imagefile");
                double price = rs.getDouble("price");
                Product product = new Product(productId, productName, imageName, price);
                products.add(product);
            }
        }
        return products;
    }

    public Product getProduct(String productId) throws SQLException {
        if (products == null) getProductList();
        for (Product p : products) {
            if (p.getProductId().equals(productId)) {
                return new Product(p.getProductId(), p.getProductName(), p.getImageFileName(), p.getProductPrice());
            }
        }
        return null;
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


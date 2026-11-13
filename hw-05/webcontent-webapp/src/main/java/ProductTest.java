import data.DataBase;
import data.Product;
import junit.framework.TestCase;

import java.sql.SQLException;
import java.util.ArrayList;

public class ProductTest extends TestCase {

    private DataBase db;

    @Override
    protected void setUp() {
        db = new DataBase();
    }

    @Override
    protected void tearDown() {
        db.close();
    }

    public void testGetProductList() throws Exception {
        ArrayList<Product> products = db.getProductList();
        assertNotNull("Product list should not be null", products);
        assertTrue("Product list should not be empty", products.size() > 0);

        Product sample = products.get(0);
        assertNotNull("Product ID should not be null", sample.getProductId());
        assertNotNull("Product name should not be null", sample.getProductName());
    }

    public void testGetProductById() throws Exception {

        String testId = "HC";
        Product product = db.getProduct(testId);

        assertNotNull("Product should not be null", product);
        assertEquals("HC", product.getProductId());
        assertEquals("Classic Hoodie", product.getProductName());
        assertEquals("Hoodie.jpg", product.getImageFileName());
        assertEquals(40.00, product.getProductPrice(), 0.001);
    }
    public void testToStringMethod() throws SQLException {
        String testId = "HC";
        Product product = db.getProduct(testId);

        String expected = "Product{productId='HC', productName='Classic Hoodie', imageFileName='Hoodie.jpg', productPrice=40.0}";
        String actual = product.toString();

        assertEquals(expected, actual);
    }
}

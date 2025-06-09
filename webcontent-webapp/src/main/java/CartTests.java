import data.Product;
import data.ShoppingCart;
import junit.framework.TestCase;

public class CartTests extends TestCase {

    public void testShoppingCartOperations() {
        ShoppingCart cart = new ShoppingCart();

        assertTrue(cart.getShoppingCart().isEmpty());

        Product hoodie = new Product("HC", "Classic Hoodie", "Hoodie.jpg", 40.00);
        Product limitedHoodie = new Product("HLE", "Limited Edition Hood", "LimitedEdHood.jpg", 54.95);
        Product tee = new Product("TC", "Classic Tee", "TShirt.jpg", 13.95);
        Product thermos = new Product("ATherm", "Thermos", "Thermos.jpg", 19.95);

        // Add items
        cart.addItem(hoodie);
        cart.addItem(limitedHoodie);
        cart.addItem(tee, 2);
        cart.addItem(thermos, 3);

        assertEquals(1, cart.getQuantity(hoodie));
        assertEquals(1, cart.getQuantity(limitedHoodie));
        assertEquals(2, cart.getQuantity(tee));
        assertEquals(3, cart.getQuantity(thermos));

        // Individual costs
        assertEquals(40.00, cart.getItemCost(hoodie));
        assertEquals(54.95, cart.getItemCost(limitedHoodie));
        assertEquals(27.90, cart.getItemCost(tee));
        assertEquals(59.85, cart.getItemCost(thermos));

        // Total cost
        assertEquals(182.70, cart.getTotalCost());

        // Update quantities
        cart.updateItem(tee, 0);
        assertEquals(0, cart.getQuantity(tee));
        assertEquals(154.80, cart.getTotalCost());

        cart.updateItem(hoodie, 3);
        assertEquals(3, cart.getQuantity(hoodie));
        assertEquals(234.80, cart.getTotalCost());

        // Test addProduct(String, int) fallback (doesn’t affect total cost)
        cart.addProduct("TLKo", 2); // No price, so totalCost remains unchanged
        assertEquals(2, cart.getQuantity("TLKo"));
        assertEquals(234.80, cart.getTotalCost());

        // Test string-based quantity with fallback
        cart.addProduct("TLAr", "abc", 1); // invalid quantity string
        assertEquals(1, cart.getQuantity("TLAr"));

        // Should now have 5 product IDs in the map
        assertEquals(5, cart.getShoppingCart().size());
    }
}

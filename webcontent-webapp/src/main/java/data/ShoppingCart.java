package data;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ShoppingCart {
    private final Map<String, Integer> cart;
    private double totalCost;

    private static final DecimalFormat df = new DecimalFormat("#.##");

    public ShoppingCart() {
        cart = new HashMap<>();
        totalCost = 0.0;
    }

    public Map<String, Integer> getCartContents() {
        return cart;
    }

    // Add product by Product object, increments quantity by 1
    public void addItem(Product product) {
        addItem(product, 1);
    }

    // Add product with specified quantity (from Product object)
    public void addItem(Product product, int quantity) {
        if (quantity <= 0) return;

        String productId = product.getProductId();
        int currentQty = cart.containsKey(productId) ? cart.get(productId) : 0;

        cart.put(productId, currentQty + quantity);

        totalCost += product.getProductPrice() * quantity;
    }

    // Add product by ID and quantity (useful when you only have IDs)
    public void addProduct(String productId, int quantity) {
        if (quantity <= 0) return;
        int currentQty = cart.containsKey(productId) ? cart.get(productId) : 0;

        cart.put(productId, currentQty + quantity);
        // Note: totalCost can't be updated here since we don't know price without Product object
    }

    // Add product by ID and string quantity with default fallback
    public void addProduct(String productId, String stringQuantity, int defaultQuantity) {
        int quantity;
        try {
            quantity = Integer.parseInt(stringQuantity);
        } catch (NumberFormatException e) {
            quantity = defaultQuantity;
        }
        if (quantity != 0) {
            addProduct(productId, quantity);
        }
    }

    // Get quantity of product by Product object
    public int getQuantity(Product product) {
        return getQuantity(product.getProductId());
    }

    // Get quantity of product by ID
    public int getQuantity(String productId) {
        return  cart.containsKey(productId) ? cart.get(productId) : 0;

    }

    // Update quantity by Product object
    public void updateItem(Product product, int newQuantity) {
        if (newQuantity < 0) newQuantity = 0;
        String productId = product.getProductId();
        int currentQty = cart.containsKey(productId) ? cart.get(productId) : 0;


        int diff = newQuantity - currentQty;
        totalCost += diff * product.getProductPrice();

        if (newQuantity == 0) {
            cart.remove(productId);
        } else {
            cart.put(productId, newQuantity);
        }
    }

    // Get cost for one product (quantity * price), formatted to 2 decimals
    public double getItemCost(Product product) {
        int qty = getQuantity(product);
        double cost = qty * product.getProductPrice();
        return Double.parseDouble(df.format(cost));
    }

    // Get total cost of all items, formatted to 2 decimals
    public double getTotalCost() {
        return Double.parseDouble(df.format(totalCost));
    }


    public Map<String, Integer> getShoppingCart() {
        return new HashMap<>(cart);
    }
}

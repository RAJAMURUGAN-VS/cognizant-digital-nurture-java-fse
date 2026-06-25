package inventory;

import java.util.HashMap;

public class Inventory {

    private HashMap<Integer, Product> products = new HashMap<>();

    public void addProduct(Product p) {
        if (products.containsKey(p.productId)) {
            System.out.println("Product with ID " + p.productId + " already exists.");
            return;
        }
        products.put(p.productId, p);
        System.out.println("Added: " + p);
    }

    public void updateProduct(int id, int newQty, double newPrice) {
        Product p = products.get(id);
        if (p == null) {
            System.out.println("Product ID " + id + " not found.");
            return;
        }
        p.quantity = newQty;
        p.price = newPrice;
        System.out.println("Updated: " + p);
    }

    public void deleteProduct(int id) {
        Product removed = products.remove(id);
        if (removed == null) {
            System.out.println("Product ID " + id + " not found.");
        } else {
            System.out.println("Deleted product with ID " + id);
        }
    }

    public void displayAll() {
        if (products.isEmpty()) {
            System.out.println("Inventory is empty.");
            return;
        }
        System.out.println("Current Inventory:");
        for (Product p : products.values()) {
            System.out.println("  " + p);
        }
    }
}

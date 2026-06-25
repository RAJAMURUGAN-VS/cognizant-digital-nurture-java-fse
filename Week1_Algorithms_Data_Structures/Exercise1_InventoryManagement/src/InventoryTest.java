package inventory;

public class InventoryTest {

    public static void main(String[] args) {

        Inventory inv = new Inventory();

        inv.addProduct(new Product(101, "Laptop", 15, 55000));
        inv.addProduct(new Product(102, "Mouse", 50, 450));
        inv.addProduct(new Product(103, "Keyboard", 30, 800));
        inv.addProduct(new Product(101, "Duplicate Laptop", 5, 100));

        System.out.println();
        inv.displayAll();

        System.out.println();
        inv.updateProduct(102, 40, 399);
        inv.updateProduct(999, 10, 500);

        System.out.println();
        inv.deleteProduct(103);
        inv.deleteProduct(999);

        System.out.println();
        inv.displayAll();
    }
}

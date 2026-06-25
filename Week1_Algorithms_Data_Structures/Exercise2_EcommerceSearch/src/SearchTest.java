package ecommerce;

public class SearchTest {

    public static void main(String[] args) {

        Product[] products = {
            new Product(1, "Laptop", "Electronics"),
            new Product(2, "Headphones", "Electronics"),
            new Product(3, "Backpack", "Accessories"),
            new Product(4, "Notebook", "Stationery"),
            new Product(5, "Pen", "Stationery")
        };

        System.out.println("-- Linear Search --");
        Product found = SearchAlgorithms.linearSearch(products, "Backpack");
        System.out.println(found != null ? "Found: " + found : "Not found");

        found = SearchAlgorithms.linearSearch(products, "Tablet");
        System.out.println(found != null ? "Found: " + found : "Not found: Tablet");

        Product[] sortedProducts = {
            new Product(3, "Backpack", "Accessories"),
            new Product(2, "Headphones", "Electronics"),
            new Product(1, "Laptop", "Electronics"),
            new Product(4, "Notebook", "Stationery"),
            new Product(5, "Pen", "Stationery")
        };

        System.out.println("\n-- Binary Search (sorted array) --");
        found = SearchAlgorithms.binarySearch(sortedProducts, "Laptop");
        System.out.println(found != null ? "Found: " + found : "Not found");

        found = SearchAlgorithms.binarySearch(sortedProducts, "Tablet");
        System.out.println(found != null ? "Found: " + found : "Not found: Tablet");
    }
}

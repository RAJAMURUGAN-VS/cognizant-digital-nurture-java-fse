package sorting;

public class SortingTest {

    public static void main(String[] args) {

        Order[] orders1 = {
            new Order(1, "Ravi", 1500.00),
            new Order(2, "Meena", 300.00),
            new Order(3, "Suresh", 7800.00),
            new Order(4, "Anjali", 950.00),
            new Order(5, "Karthik", 4200.00)
        };

        Order[] orders2 = {
            new Order(1, "Ravi", 1500.00),
            new Order(2, "Meena", 300.00),
            new Order(3, "Suresh", 7800.00),
            new Order(4, "Anjali", 950.00),
            new Order(5, "Karthik", 4200.00)
        };

        System.out.println("-- Bubble Sort --");
        SortingAlgorithms.bubbleSort(orders1);
        SortingAlgorithms.printOrders(orders1);

        System.out.println("\n-- Quick Sort --");
        SortingAlgorithms.quickSort(orders2, 0, orders2.length - 1);
        SortingAlgorithms.printOrders(orders2);
    }
}

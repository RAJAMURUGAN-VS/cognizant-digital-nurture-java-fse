package di;

public class DITest {

    public static void main(String[] args) {

        System.out.println("-- Dependency Injection Test --\n");

        CustomerRepository repository = new CustomerRepositoryImpl();
        CustomerService service = new CustomerService(repository);

        service.addCustomer("Raj Kumar");
        service.addCustomer("Priya Sharma");
        service.addCustomer("Arun Patel");

        System.out.println();

        System.out.println("Finding customer with ID 1: " + service.getCustomer(1));
        System.out.println("Finding customer with ID 2: " + service.getCustomer(2));
        System.out.println("Finding customer with ID 99: " + service.getCustomer(99));
    }
}

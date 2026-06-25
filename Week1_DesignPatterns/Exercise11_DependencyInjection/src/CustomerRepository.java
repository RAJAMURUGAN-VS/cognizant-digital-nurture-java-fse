package di;

public interface CustomerRepository {
    String findCustomerById(int id);
    void saveCustomer(String name);
}

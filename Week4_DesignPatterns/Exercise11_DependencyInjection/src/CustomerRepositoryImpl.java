package di;

import java.util.HashMap;
import java.util.Map;

public class CustomerRepositoryImpl implements CustomerRepository {

    private Map<Integer, String> customerStore = new HashMap<>();

    @Override
    public String findCustomerById(int id) {
        return customerStore.getOrDefault(id, "Customer not found");
    }

    @Override
    public void saveCustomer(String name) {
        int id = customerStore.size() + 1;
        customerStore.put(id, name);
        System.out.println("Saved customer: " + name + " with ID " + id);
    }
}

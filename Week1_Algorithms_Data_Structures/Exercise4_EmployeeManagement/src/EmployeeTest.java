package employee;

public class EmployeeTest {

    public static void main(String[] args) {

        EmployeeManager mgr = new EmployeeManager(5);

        mgr.addEmployee(new Employee(1, "Ramesh", "Developer", 65000));
        mgr.addEmployee(new Employee(2, "Sita", "Designer", 55000));
        mgr.addEmployee(new Employee(3, "Arjun", "Tester", 48000));
        mgr.addEmployee(new Employee(4, "Divya", "Manager", 90000));

        System.out.println();
        mgr.traverse();

        System.out.println();
        Employee found = mgr.searchById(3);
        System.out.println("Search ID 3: " + (found != null ? found : "not found"));

        found = mgr.searchById(99);
        System.out.println("Search ID 99: " + (found != null ? found : "not found"));

        System.out.println();
        mgr.deleteEmployee(2);
        mgr.deleteEmployee(99);

        System.out.println();
        mgr.traverse();
    }
}

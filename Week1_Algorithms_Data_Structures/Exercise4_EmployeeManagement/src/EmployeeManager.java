package employee;

public class EmployeeManager {

    private Employee[] employees;
    private int count;

    public EmployeeManager(int capacity) {
        employees = new Employee[capacity];
        count = 0;
    }

    public void addEmployee(Employee e) {
        if (count >= employees.length) {
            System.out.println("Array is full, cannot add more employees.");
            return;
        }
        employees[count] = e;
        count++;
        System.out.println("Added: " + e);
    }

    public Employee searchById(int id) {
        for (int i = 0; i < count; i++) {
            if (employees[i].employeeId == id) {
                return employees[i];
            }
        }
        return null;
    }

    public void traverse() {
        System.out.println("All Employees:");
        for (int i = 0; i < count; i++) {
            System.out.println("  " + employees[i]);
        }
    }

    public boolean deleteEmployee(int id) {
        for (int i = 0; i < count; i++) {
            if (employees[i].employeeId == id) {
                for (int j = i; j < count - 1; j++) {
                    employees[j] = employees[j + 1];
                }
                employees[count - 1] = null;
                count--;
                System.out.println("Deleted employee with ID " + id);
                return true;
            }
        }
        System.out.println("Employee ID " + id + " not found.");
        return false;
    }
}

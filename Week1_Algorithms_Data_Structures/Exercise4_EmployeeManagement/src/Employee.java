package employee;

public class Employee {

    int employeeId;
    String name;
    String position;
    double salary;

    public Employee(int employeeId, String name, String position, double salary) {
        this.employeeId = employeeId;
        this.name = name;
        this.position = position;
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "Employee{id=" + employeeId + ", name='" + name + "', position='" + position + "', salary=" + salary + "}";
    }
}

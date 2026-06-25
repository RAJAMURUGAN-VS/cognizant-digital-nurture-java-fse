package mvc;

public class MVCTest {

    public static void main(String[] args) {

        System.out.println("-- MVC Pattern Test --\n");

        Student model = new Student("S001", "Raj Kumar", "A");
        StudentView view = new StudentView();
        StudentController controller = new StudentController(model, view);

        System.out.println("Initial student details:");
        controller.updateView();

        System.out.println();

        controller.setStudentName("Priya Sharma");
        controller.setStudentGrade("A+");

        System.out.println("After update:");
        controller.updateView();
    }
}

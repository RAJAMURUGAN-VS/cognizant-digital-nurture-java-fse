package tasks;

public class TaskTest {

    public static void main(String[] args) {

        TaskLinkedList list = new TaskLinkedList();

        list.addTask(new Task(1, "Design DB schema", "Pending"));
        list.addTask(new Task(2, "Set up backend", "In Progress"));
        list.addTask(new Task(3, "Build login page", "Pending"));
        list.addTask(new Task(4, "Write unit tests", "Pending"));

        System.out.println();
        list.traverse();

        System.out.println();
        Task found = list.searchTask(2);
        System.out.println("Search ID 2: " + (found != null ? found : "not found"));

        found = list.searchTask(10);
        System.out.println("Search ID 10: " + (found != null ? found : "not found"));

        System.out.println();
        list.deleteTask(1);
        list.deleteTask(10);

        System.out.println();
        list.traverse();
    }
}

package tasks;

public class TaskLinkedList {

    private Task head;

    public void addTask(Task t) {
        if (head == null) {
            head = t;
        } else {
            Task curr = head;
            while (curr.next != null) {
                curr = curr.next;
            }
            curr.next = t;
        }
        System.out.println("Added: " + t);
    }

    public Task searchTask(int id) {
        Task curr = head;
        while (curr != null) {
            if (curr.taskId == id) {
                return curr;
            }
            curr = curr.next;
        }
        return null;
    }

    public void traverse() {
        System.out.println("All Tasks:");
        Task curr = head;
        while (curr != null) {
            System.out.println("  " + curr);
            curr = curr.next;
        }
    }

    public boolean deleteTask(int id) {
        if (head == null) {
            System.out.println("List is empty.");
            return false;
        }

        if (head.taskId == id) {
            head = head.next;
            System.out.println("Deleted task with ID " + id);
            return true;
        }

        Task curr = head;
        while (curr.next != null) {
            if (curr.next.taskId == id) {
                curr.next = curr.next.next;
                System.out.println("Deleted task with ID " + id);
                return true;
            }
            curr = curr.next;
        }

        System.out.println("Task ID " + id + " not found.");
        return false;
    }
}

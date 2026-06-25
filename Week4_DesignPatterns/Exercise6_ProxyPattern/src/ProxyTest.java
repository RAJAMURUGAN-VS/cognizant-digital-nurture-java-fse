package proxy;

public class ProxyTest {

    public static void main(String[] args) {

        System.out.println("-- Proxy Pattern Test --\n");

        Image image1 = new ProxyImage("photo1.jpg");
        Image image2 = new ProxyImage("photo2.jpg");

        System.out.println("First display call (loads from server):");
        image1.display();

        System.out.println();

        System.out.println("Second display call (served from cache):");
        image1.display();

        System.out.println();

        System.out.println("Displaying image2 for the first time:");
        image2.display();
    }
}

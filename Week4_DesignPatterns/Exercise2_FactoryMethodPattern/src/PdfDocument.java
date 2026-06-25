package factory;

public class PdfDocument implements Document {

    @Override
    public void open() {
        System.out.println("Opening PDF document (.pdf)");
    }

    @Override
    public void close() {
        System.out.println("Closing PDF document");
    }

    @Override
    public String getType() {
        return "PDF Document";
    }
}

package factory;

public class WordDocument implements Document {

    @Override
    public void open() {
        System.out.println("Opening Word document (.docx)");
    }

    @Override
    public void close() {
        System.out.println("Closing Word document");
    }

    @Override
    public String getType() {
        return "Word Document";
    }
}

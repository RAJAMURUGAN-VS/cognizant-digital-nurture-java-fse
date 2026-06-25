package factory;

public class ExcelDocument implements Document {

    @Override
    public void open() {
        System.out.println("Opening Excel spreadsheet (.xlsx)");
    }

    @Override
    public void close() {
        System.out.println("Closing Excel spreadsheet");
    }

    @Override
    public String getType() {
        return "Excel Document";
    }
}

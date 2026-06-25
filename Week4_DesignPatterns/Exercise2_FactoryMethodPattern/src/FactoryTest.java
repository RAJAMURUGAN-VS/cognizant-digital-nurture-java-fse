package factory;

public class FactoryTest {

    public static void main(String[] args) {

        System.out.println("-- Factory Method Pattern Test --\n");

        DocumentFactory wordFactory = new WordDocumentFactory();
        Document word = wordFactory.createDocument();
        System.out.println("Created: " + word.getType());
        word.open();
        word.close();

        System.out.println();

        DocumentFactory pdfFactory = new PdfDocumentFactory();
        Document pdf = pdfFactory.createDocument();
        System.out.println("Created: " + pdf.getType());
        pdf.open();
        pdf.close();

        System.out.println();

        DocumentFactory excelFactory = new ExcelDocumentFactory();
        Document excel = excelFactory.createDocument();
        System.out.println("Created: " + excel.getType());
        excel.open();
        excel.close();
    }
}

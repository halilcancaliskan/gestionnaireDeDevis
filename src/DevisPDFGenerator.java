import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import java.io.IOException;

public class DevisPDFGenerator {
    public static void generatePDF(String client, String description, double cost, String filePath) {
        try {
            PDDocument document = new PDDocument();
            PDPage page = new PDPage();
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 14);

            contentStream.beginText();
            contentStream.newLineAtOffset(100, 700);
            contentStream.showText("Devis");
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText("Client : " + client);
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText("Description : " + description);
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText("Coût : " + cost);
            contentStream.endText();
            contentStream.close();

            String fullFilePath = "devis/" + filePath;
            document.save(fullFilePath);

            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

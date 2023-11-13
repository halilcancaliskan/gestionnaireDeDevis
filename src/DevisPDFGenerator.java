import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.io.IOException;
import java.time.LocalDate;

public class DevisPDFGenerator {
    private static final String LOGO_PATH = "src/SONMEZ.png";
    private static final String FOOTER_CONTENT = "Footer Content";

    public static void generatePDF(String client, String garageName, String garageAddress, String description, double cost, String partReference, String filePath) {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            addHeader(document, page);
            addBody(document, page, client, garageName, garageAddress, description, cost, partReference);
            addFooter(document, page);

            String fullFilePath = "devis/" + filePath;
            document.save(fullFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void addHeader(PDDocument document, PDPage page) throws IOException {
        try (PDPageContentStream headerStream = new PDPageContentStream(document, page)) {
            PDImageXObject logo = PDImageXObject.createFromFile(LOGO_PATH, document);
            float scale = 0.3f;
            float logoWidth = logo.getWidth() * scale;
            float logoHeight = logo.getHeight() * scale;
            headerStream.drawImage(logo, 20, page.getMediaBox().getHeight() - logoHeight - 20, logoWidth, logoHeight);
        }
    }

    private static void addBody(PDDocument document, PDPage page, String client, String garageName, String garageAddress, String description, double cost, String partReference) throws IOException {
        try (PDPageContentStream bodyStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true, true)) {
            try (PDPageContentStream bodyGarage = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true, true)) {
                float bodyY = page.getMediaBox().getHeight() - 100;
                bodyStream.setFont(new PDType1Font(Standard14Fonts.FontName.TIMES_ROMAN), 12);
                bodyGarage.setFont(new PDType1Font(Standard14Fonts.FontName.TIMES_ROMAN), 8);

                // Coordonnées du garage
                addText(bodyGarage, 20, bodyY, "N° du document : " + partReference);
                addText(bodyGarage, 20, bodyY - 20, "Date : " + LocalDate.now());
                addText(bodyGarage, 20, bodyY - 40, garageName);
                addText(bodyGarage, 20, bodyY - 60, "Adresse : " + garageAddress);
                addText(bodyGarage, 20, bodyY - 80, "Tél : " + client);
                addText(bodyGarage, 20, bodyY - 100, "E-mail : " + garageAddress);
                addText(bodyGarage, 20, bodyY - 120, "SIRET : " + garageAddress);
                addText(bodyGarage, 20, bodyY - 140, "Ouvert du lundi au vendredi de 08h00 à 19h00 et le samedi de 08h00 à 17h00.");



                // Coordonnées du client
                float clientY = bodyY - 80; // Assurez-vous que le texte du client est en dessous des données du garage
                addText(bodyStream, 350, clientY, "Immatriculation : " + client);
                addText(bodyStream, 350, clientY - 20, "Adresse : " + garageAddress);

                // Tableau pour la description et le coût
                float tableY = clientY - 100;
                float tableWidth = page.getMediaBox().getWidth() - 40; // largeur de droite a gauche du tableau
                float tableHeight = 40; // Increase the table height to accommodate two rows

                bodyStream.setLineWidth(1f);
                bodyStream.addRect(20, tableY, tableWidth, tableHeight);
                bodyStream.stroke();

                addText(bodyStream, 25, tableY + tableHeight - 15, "Description:");
                addText(bodyStream, 303, tableY + tableHeight - 15, description);

                addText(bodyStream, 25, tableY + tableHeight - 35, "Prix:");
                addText(bodyStream, 303, tableY + tableHeight - 35, String.valueOf(cost));


                // Draw horizontal and vertical lines for the table
                bodyStream.setLineWidth(1f);
                bodyStream.moveTo(20, tableY + 20);
                bodyStream.lineTo(20 + tableWidth, tableY + 20);
                bodyStream.moveTo(20 + tableWidth / 2, tableY);
                bodyStream.lineTo(20 + tableWidth / 2, tableY + tableHeight);

                bodyStream.stroke();
            }
        }
    }

    private static void addText(PDPageContentStream stream, float x, float y, String text) throws IOException {
        stream.beginText();
        stream.newLineAtOffset(x, y);
        stream.showText(text);
        stream.endText();
    }


    private static void addFooter(PDDocument document, PDPage page) throws IOException {
        try (PDPageContentStream footerStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true, true)) {
            footerStream.setFont(new PDType1Font(Standard14Fonts.FontName.TIMES_ROMAN), 12);
            footerStream.beginText();
            footerStream.newLineAtOffset(20, 20);
            footerStream.showText(FOOTER_CONTENT);
            footerStream.endText();
        }
    }

    public static void main(String[] args) {
        generatePDF("Oscars", "OSS'CARS", "123 Main St, City", "Remplacement du pare-chocs", 350.0, "PC1234", "devis.pdf");
    }
}

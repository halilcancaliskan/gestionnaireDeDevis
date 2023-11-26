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

    private static void addBody(PDDocument document, PDPage page, String immat, String garageName, String garageAddress, String description, double cost, String partReference) throws IOException {
        try (PDPageContentStream bodyStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true, true)) {
            try (PDPageContentStream bodyGarage = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true, true)) {
                float bodyY = page.getMediaBox().getHeight() - 100;
                bodyStream.setFont(new PDType1Font(Standard14Fonts.FontName.TIMES_ROMAN), 14);
                bodyGarage.setFont(new PDType1Font(Standard14Fonts.FontName.TIMES_ROMAN), 8);

                // Coordonnées du garage
                addGarageDetails(bodyGarage, bodyY, partReference, garageName);

                // Coordonnées du client
                float clientY = bodyY - 40;
                addClientDetails(bodyGarage, clientY, immat);

                // Écrire du texte au-dessus du tableau
                addText(bodyStream, 20, clientY - 80, "Voici les détails de votre facture");

                float tableY = clientY - 110;
                float tableWidth = page.getMediaBox().getWidth() - 40;

                // Dessiner la première ligne verticale
                drawVerticalLine(bodyStream, tableY, 300, -80);
                drawVerticalLine(bodyStream, tableY, 20, -80);
                drawVerticalLine(bodyStream, tableY, 576, -80);

                // Tableau pour la description et le coût
                bodyStream.addRect(20, tableY, tableWidth, -80);

                float tableHeight = 20;

                // Première ligne du tableau
                addTableRow(bodyStream, 25, tableY + tableHeight - 20, "Description", 303, "Prix TTC:");

                // Dessiner la ligne horizontale au-dessus de "Prix 1:"
                drawHorizontalLine(bodyStream, tableY + tableHeight, 20, tableWidth + 20);

                // Deuxième ligne du tableau
                addTableRow(bodyStream, 25, tableY + tableHeight - 40, description, 303, cost + " €");

                // Dessiner la ligne horizontale en dessous de "Prix 1:"
                drawHorizontalLine(bodyStream, tableY + tableHeight - 20, 20, tableWidth + 20);

                bodyStream.stroke();
            }
        }
    }

    private static void addGarageDetails(PDPageContentStream stream, float bodyY, String partReference, String garageName) throws IOException {
        float offsetY = 0;
        addText(stream, 20, bodyY + offsetY, "N° du document : " + partReference);
        offsetY -= 10;
        addText(stream, 20, bodyY + offsetY, "Date : " + LocalDate.now());
        offsetY -= 10;
        addText(stream, 20, bodyY + offsetY, garageName);
        offsetY -= 10;
        addText(stream, 20, bodyY + offsetY, "Adresse : Rue, Saint-Etienne");
        offsetY -= 10;
        addText(stream, 20, bodyY + offsetY, "Tél : 06 00 00 00 00");
        offsetY -= 10;
        addText(stream, 20, bodyY + offsetY, "E-mail : osscars@gmail.com");
        offsetY -= 10;
        addText(stream, 20, bodyY + offsetY, "SIRET : 123456789");
        offsetY -= 10;
        addText(stream, 20, bodyY + offsetY, "Ouvert du lundi au vendredi de 08h00 à 19h00 et le samedi de 08h00 à 17h00.");
    }

    private static void addClientDetails(PDPageContentStream stream, float clientY, String immat) throws IOException {
        addText(stream, 350, clientY, "FACTURE");
        addText(stream, 350, clientY - 10, "Immatriculation : " + immat);
    }

    private static void addTableRow(PDPageContentStream stream, float xDescription, float y, String description, float xPrice, String priceLabel) throws IOException {
        float tableHeight = 20;
        addText(stream, xDescription, y + tableHeight - 15, description);
        addText(stream, xPrice, y + tableHeight - 15, priceLabel);
    }

    private static void drawVerticalLine(PDPageContentStream stream, float startY, float x, float height) throws IOException {
        stream.setLineWidth(2f); // Changer 2f selon l'épaisseur souhaitée
        stream.moveTo(x, startY + 20);
        stream.lineTo(x, startY + height);
        stream.stroke();
    }

    private static void drawHorizontalLine(PDPageContentStream stream, float y, float startX, float endX) throws IOException {
        stream.setLineWidth(2f); // Changer 2f selon l'épaisseur souhaitée
        stream.moveTo(startX, y);
        stream.lineTo(endX, y);
        stream.stroke();
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

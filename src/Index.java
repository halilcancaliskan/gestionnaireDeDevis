import javax.swing.*;
import java.awt.*;
import java.io.*;

public class Index {
    private static final String FILE_PATH = "devis.csv"; // Chemin du fichier de stockage

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Index::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("DevisExpress");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("Fichier");
        JMenuItem createDevisMenuItem = new JMenuItem("Créer Devis");
        JMenuItem listDevisMenuItem = new JMenuItem("Liste des Devis");
        JMenuItem exitMenuItem = new JMenuItem("Quitter");

        createDevisMenuItem.addActionListener(e -> createDevis());
        listDevisMenuItem.addActionListener(e -> displayDevisList());
        exitMenuItem.addActionListener(e -> System.exit(0));

        fileMenu.add(createDevisMenuItem);
        fileMenu.add(listDevisMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);
        frame.setJMenuBar(menuBar);

        frame.setVisible(true);
    }

    private static void createDevis() {
        JFrame createDevisFrame = new JFrame("Création de Devis");
        createDevisFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        createDevisFrame.setSize(400, 300);

        JPanel createDevisPanel = new JPanel(new GridLayout(4, 2));

        JLabel clientLabel = new JLabel("Immatriculation :");
        JTextField clientTextField = new JTextField();

        JLabel descriptionLabel = new JLabel("Description :");
        JTextField descriptionTextField = new JTextField();

        JLabel costLabel = new JLabel("Prix :");
        JTextField costTextField = new JTextField();

        JButton saveButton = new JButton("Enregistrer");
        saveButton.addActionListener(e -> saveDevis(clientTextField.getText(), descriptionTextField.getText(), Double.parseDouble(costTextField.getText()), createDevisFrame));

        createDevisPanel.add(clientLabel);
        createDevisPanel.add(clientTextField);
        createDevisPanel.add(descriptionLabel);
        createDevisPanel.add(descriptionTextField);
        createDevisPanel.add(costLabel);
        createDevisPanel.add(costTextField);
        createDevisPanel.add(new JLabel()); // Espace vide
        createDevisPanel.add(saveButton);

        createDevisFrame.add(createDevisPanel);
        createDevisFrame.setVisible(true);
    }

    private static void saveDevis(String client, String description, double cost, JFrame createDevisFrame) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(String.format("%s,%s,%.2f", client, description, cost));
            writer.newLine();
            JOptionPane.showMessageDialog(createDevisFrame, "Devis créé et enregistré !");
            createDevisFrame.dispose();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void displayDevisList() {
        JFrame listDevisFrame = new JFrame("Liste des Devis");
        listDevisFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        listDevisFrame.setSize(600, 400);

        JPanel listDevisPanel = new JPanel(new GridLayout(0, 1));

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] devisData = line.split(",");
                String client = devisData[0];
                String description = devisData[1];
                String cost = devisData[2];

                JPanel devisEntryPanel = createDevisEntryPanel(client, description, cost);
                listDevisPanel.add(devisEntryPanel);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        JScrollPane scrollPane = new JScrollPane(listDevisPanel);
        listDevisFrame.add(scrollPane);

        listDevisFrame.setVisible(true);
    }

    private static JPanel createDevisEntryPanel(String client, String description, String cost) {
        JPanel devisEntryPanel = new JPanel(new FlowLayout());

        JLabel devisLabel = new JLabel(String.format("Immatriculation : %s, Description : %s, Prix : %s", client, description, cost));
        JButton editPDFButton = new JButton("Éditer PDF");

        editPDFButton.addActionListener(e -> {
            String pdfFileName = String.format("devis_%s_%d.pdf", client, System.currentTimeMillis());
            DevisPDFGenerator.generatePDF(client, "Garage XYZ", "123 Main St, City", description, Double.parseDouble(cost), pdfFileName, pdfFileName + ".pdf");
            JOptionPane.showMessageDialog(null, "Fichier PDF créé : " + pdfFileName);
        });

        devisEntryPanel.add(devisLabel);
        devisEntryPanel.add(editPDFButton);

        return devisEntryPanel;
    }
}

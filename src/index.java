import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class index {
    private static final String FILE_PATH = "devis.csv"; // Chemin du fichier de stockage

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("DevisExpress");
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setSize(800, 600);

            JMenuBar menuBar = new JMenuBar();

            JMenu fileMenu = new JMenu("Fichier");
            JMenuItem createDevisMenuItem = new JMenuItem("Créer Devis");
            JMenuItem listDevisMenuItem = new JMenuItem("Liste des Devis");
            JMenuItem exitMenuItem = new JMenuItem("Quitter");

            createDevisMenuItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JFrame createDevisFrame = new JFrame("Création de Devis");
                    createDevisFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    createDevisFrame.setSize(400, 300);

                    JPanel createDevisPanel = new JPanel();
                    createDevisPanel.setLayout(new GridLayout(4, 2));

                    JLabel clientLabel = new JLabel("Client :");
                    JTextField clientTextField = new JTextField();

                    JLabel descriptionLabel = new JLabel("Description :");
                    JTextField descriptionTextField = new JTextField();

                    JLabel costLabel = new JLabel("Coût :");
                    JTextField costTextField = new JTextField();

                    JButton saveButton = new JButton("Enregistrer");
                    saveButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            String client = clientTextField.getText();
                            String description = descriptionTextField.getText();
                            double cost = Double.parseDouble(costTextField.getText());

                            // Enregistrez le devis dans le fichier texte
                            saveDevis(client, description, cost);

                            // Affichez un message de confirmation
                            JOptionPane.showMessageDialog(createDevisFrame, "Devis créé et enregistré !");
                            createDevisFrame.dispose();
                        }
                    });

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
            });

            listDevisMenuItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Affichez la liste des devis avec un bouton "Éditer PDF" pour chaque devis
                    displayDevisList();
                }
            });

            exitMenuItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            });

            fileMenu.add(createDevisMenuItem);
            fileMenu.add(listDevisMenuItem);
            fileMenu.addSeparator();
            fileMenu.add(exitMenuItem);

            menuBar.add(fileMenu);
            frame.setJMenuBar(menuBar);

            frame.setVisible(true);
        });
    }

    private static void saveDevis(String client, String description, double cost) {
        // Écrivez les informations du devis dans un fichier CSV
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(client + "," + description + "," + cost);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void displayDevisList() {
        JFrame listDevisFrame = new JFrame("Liste des Devis");
        listDevisFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        listDevisFrame.setSize(600, 400);

        JPanel listDevisPanel = new JPanel();
        listDevisPanel.setLayout(new GridLayout(0, 1));

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] devisData = line.split(",");
                String client = devisData[0];
                String description = devisData[1];
                String cost = devisData[2];

                JPanel devisEntryPanel = new JPanel();
                devisEntryPanel.setLayout(new FlowLayout());

                JLabel devisLabel = new JLabel("Client : " + client + ", Description : " + description + ", Coût : " + cost);
                JButton editPDFButton = new JButton("Éditer PDF");

                editPDFButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String pdfFileName = "devis_" + client + "_" + System.currentTimeMillis() + ".pdf";
                        DevisPDFGenerator.generatePDF(client, description, Double.parseDouble(cost), pdfFileName);
                        JOptionPane.showMessageDialog(listDevisFrame, "Fichier PDF créé : " + pdfFileName);
                    }
                });

                devisEntryPanel.add(devisLabel);
                devisEntryPanel.add(editPDFButton);

                listDevisPanel.add(devisEntryPanel);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        JScrollPane scrollPane = new JScrollPane(listDevisPanel);
        listDevisFrame.add(scrollPane);

        listDevisFrame.setVisible(true);
    }
}

package main;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SpamDetectorGUI {
    private static ModelTrainer trainer;
    private static Preprocessor preprocessor;
    private static FeatureExtractor extractor;

    public static void main(String[] args) throws IOException {
        // Train the model (same as before)
        String filePath = "data/spam_assassin.csv";
        List<String[]> dataset = ModelTrainer.readDataset(filePath);

        preprocessor = new Preprocessor("resources/stopwords.txt");
        List<String> cleanedTexts = new ArrayList<>();
        List<String> labels = new ArrayList<>();

        for (String[] row : dataset) {
            String label = row[0].trim(); // "spam" or "ham"
            String emailText = row[1].trim();
            labels.add(label);
            cleanedTexts.add(preprocessor.preprocess(emailText));
        }

        extractor = new FeatureExtractor(cleanedTexts);
        List<int[]> featureVectors = new ArrayList<>();
        for (String text : cleanedTexts) {
            featureVectors.add(extractor.extractFeatures(text));
        }

        List<int[]> trainFeatures = new ArrayList<>();
        List<String> trainLabels = new ArrayList<>();
        List<int[]> testFeatures = new ArrayList<>();
        List<String> testLabels = new ArrayList<>();
        ModelTrainer.stratifiedSplit(featureVectors, labels, trainFeatures, trainLabels, testFeatures, testLabels, 0.8);

        trainer = new ModelTrainer();
        trainer.train(trainFeatures, trainLabels);
        trainer.evaluate(testFeatures, testLabels);

        // Create the GUI
        SwingUtilities.invokeLater(SpamDetectorGUI::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        // Set up the main frame
        JFrame frame = new JFrame("Spam Detector");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());

        // Apply dark mode colors
        Color darkBackground = new Color(43, 43, 43);
        Color lightText = new Color(187, 187, 187);
        Color buttonColor = new Color(60, 63, 65);

        frame.getContentPane().setBackground(darkBackground);

        // Title Label
        JLabel titleLabel = new JLabel("Spam Detector", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(lightText);
        titleLabel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Input panel
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());
        inputPanel.setBackground(darkBackground);
        inputPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel inputLabel = new JLabel("Enter Email Content:");
        inputLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        inputLabel.setForeground(lightText);

        JTextArea inputArea = new JTextArea(8, 40);
        inputArea.setLineWrap(true);
        inputArea.setWrapStyleWord(true);
        inputArea.setBackground(buttonColor);
        inputArea.setForeground(lightText);
        inputArea.setCaretColor(lightText);
        JScrollPane inputScrollPane = new JScrollPane(inputArea);
        inputScrollPane.setBorder(BorderFactory.createLineBorder(buttonColor));

        inputPanel.add(inputLabel, BorderLayout.NORTH);
        inputPanel.add(inputScrollPane, BorderLayout.CENTER);

        // Output panel
        JPanel outputPanel = new JPanel();
        outputPanel.setLayout(new BorderLayout());
        outputPanel.setBackground(darkBackground);
        outputPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel resultLabel = new JLabel("Prediction:");
        resultLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        resultLabel.setForeground(lightText);

        JTextField resultField = new JTextField(30);
        resultField.setFont(new Font("Arial", Font.PLAIN, 16));
        resultField.setEditable(false);
        resultField.setHorizontalAlignment(JTextField.CENTER);
        resultField.setBackground(buttonColor);
        resultField.setForeground(lightText);
        resultField.setCaretColor(lightText);
        resultField.setBorder(BorderFactory.createLineBorder(buttonColor));

        outputPanel.add(resultLabel, BorderLayout.NORTH);
        outputPanel.add(resultField, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(darkBackground);

        JButton classifyButton = new JButton("Classify");
        JButton clearButton = new JButton("Clear");

        classifyButton.setBackground(buttonColor);
        classifyButton.setForeground(lightText);
        classifyButton.setFont(new Font("Arial", Font.BOLD, 14));

        clearButton.setBackground(buttonColor);
        clearButton.setForeground(lightText);
        clearButton.setFont(new Font("Arial", Font.BOLD, 14));

        buttonPanel.add(classifyButton);
        buttonPanel.add(clearButton);

        // Add components to the frame
        frame.add(titleLabel, BorderLayout.NORTH);
        frame.add(inputPanel, BorderLayout.CENTER);
        frame.add(outputPanel, BorderLayout.EAST);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Add action listener for the classify button
        classifyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userInput = inputArea.getText().trim();
                if (userInput.isEmpty()) {
                    resultField.setText("Please enter email content.");
                    return;
                }

                // Preprocess and classify
                String cleanedInput = preprocessor.preprocess(userInput);
                int[] userFeatures = extractor.extractFeatures(cleanedInput);
                String prediction = trainer.predict(userFeatures);

                // Display the result
                resultField.setText(prediction.equals("spam") ? "Spam" : "Not Spam");
            }
        });

        // Add action listener for the clear button
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Clear input and output fields
                inputArea.setText("");
                resultField.setText("");
            }
        });

        // Show the frame
        frame.setVisible(true);
    }
}

import javax.swing.*;
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
        frame.setSize(500, 350);
        frame.setLayout(new BorderLayout());

        // Input panel
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(2, 1));

        JLabel inputLabel = new JLabel("Enter email content:");
        JTextArea inputArea = new JTextArea(5, 40);
        inputPanel.add(inputLabel);
        inputPanel.add(new JScrollPane(inputArea));

        // Button panel
        JPanel buttonPanel = new JPanel();
        JButton classifyButton = new JButton("Classify");
        JButton clearButton = new JButton("Clear");
        buttonPanel.add(classifyButton);
        buttonPanel.add(clearButton);

        // Output panel
        JPanel outputPanel = new JPanel();
        outputPanel.setLayout(new GridLayout(2, 1));
        JLabel resultLabel = new JLabel("Prediction: ");
        JTextField resultField = new JTextField(30);
        resultField.setEditable(false);
        outputPanel.add(resultLabel);
        outputPanel.add(resultField);

        // Add panels to the frame
        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(buttonPanel, BorderLayout.CENTER);
        frame.add(outputPanel, BorderLayout.SOUTH);

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

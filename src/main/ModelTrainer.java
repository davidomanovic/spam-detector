import java.util.*;
import java.io.*;

public class ModelTrainer {
    private Map<String, double[]> probabilities; // Word probabilities per class
    private double spamPrior; // P(spam)
    private double hamPrior;  // P(ham)
    private static final double SMOOTHING = 1.0; // Laplace smoothing

    // Train the Naive Bayes model
    public void train(List<int[]> featureVectors, List<String> labels) {
        int numFeatures = featureVectors.get(0).length;

        // Initialize counts
        int[] spamCounts = new int[numFeatures];
        int[] hamCounts = new int[numFeatures];
        int spamTotal = 0, hamTotal = 0;
        int spamDocs = 0, hamDocs = 0;

        // Count word occurrences per class
        for (int i = 0; i < labels.size(); i++) {
            int[] features = featureVectors.get(i);
            if (labels.get(i).equals("spam")) {
                spamDocs++;
                for (int j = 0; j < numFeatures; j++) {
                    spamCounts[j] += features[j];
                    spamTotal += features[j];
                }
            } else {
                hamDocs++;
                for (int j = 0; j < numFeatures; j++) {
                    hamCounts[j] += features[j];
                    hamTotal += features[j];
                }
            }
        }

        // Calculate priors
        spamPrior = (double) spamDocs / labels.size();
        hamPrior = (double) hamDocs / labels.size();

        // Calculate word probabilities with Laplace smoothing
        probabilities = new HashMap<>();
        double[] spamProbs = new double[numFeatures];
        double[] hamProbs = new double[numFeatures];
        for (int j = 0; j < numFeatures; j++) {
            spamProbs[j] = (spamCounts[j] + SMOOTHING) / (spamTotal + SMOOTHING * numFeatures);
            hamProbs[j] = (hamCounts[j] + SMOOTHING) / (hamTotal + SMOOTHING * numFeatures);
        }
        probabilities.put("spam", spamProbs);
        probabilities.put("ham", hamProbs);
    }

    // Predict the label for a single feature vector
    public String predict(int[] featureVector) {
        double spamScore = Math.log(spamPrior);
        double hamScore = Math.log(hamPrior);
        double[] spamProbs = probabilities.get("spam");
        double[] hamProbs = probabilities.get("ham");

        for (int j = 0; j < featureVector.length; j++) {
            if (featureVector[j] > 0) {
                spamScore += Math.log(spamProbs[j]) * featureVector[j];
                hamScore += Math.log(hamProbs[j]) * featureVector[j];
            }
        }
        return spamScore > hamScore ? "spam" : "ham";
    }

    // Check accuracy, precision, recall, F1-Score
    public void evaluate(List<int[]> testFeatures, List<String> testLabels) {
        int tp = 0, fp = 0, fn = 0, tn = 0;

        for (int i = 0; i < testLabels.size(); i++) {
            String actual = testLabels.get(i);
            String predicted = predict(testFeatures.get(i));

            if (predicted.equals("spam") && actual.equals("spam")) tp++;
            else if (predicted.equals("spam") && actual.equals("ham")) fp++;
            else if (predicted.equals("ham") && actual.equals("spam")) fn++;
            else if (predicted.equals("ham") && actual.equals("ham")) tn++;
        }

        double accuracy = (double) (tp + tn) / testLabels.size();
        double precision = tp + fp > 0 ? (double) tp / (tp + fp) : 0.0;
        double recall = tp + fn > 0 ? (double) tp / (tp + fn) : 0.0;
        double f1Score = precision + recall > 0 ? 2 * (precision * recall) / (precision + recall) : 0.0;

        System.out.println("Evaluation Metrics:");
        System.out.println("Accuracy: " + accuracy);
        System.out.println("Precision: " + precision);
        System.out.println("Recall: " + recall);
        System.out.println("F1-Score: " + f1Score);
    }

    // Read dataset from CSV file
    private static List<String[]> readDataset(String filePath) throws IOException {
        List<String[]> dataset = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line = reader.readLine(); // Skip header
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",", 2); // Split into label and email content
            if (parts.length == 2) {
                String label = parts[0].trim().equals("1") ? "spam" : "ham"; // Map 1 -> spam, 0 -> ham
                String emailRaw = parts[1].trim();
                String emailBody = extractEmailBody(emailRaw);
                dataset.add(new String[] { label, emailBody });
            }
        }
        reader.close();
        return dataset;
    }

    // Helper method to extract the email body
    private static String extractEmailBody(String emailRaw) {
        String[] lines = emailRaw.split("\\n"); // Split email into lines
        StringBuilder body = new StringBuilder();

        boolean isBody = false;
        for (String line : lines) {
            if (!isBody) {
                if (line.trim().isEmpty()) isBody = true; // Body starts after blank line
            } else {
                body.append(line).append(" ");
            }
        }

        return body.toString().replaceAll("<[^>]*>", "").trim(); // Remove HTML tags
    }

    // Stratified sampling for balanced dataset split
    private static void stratifiedSplit(List<int[]> featureVectors, List<String> labels,
                                        List<int[]> trainFeatures, List<String> trainLabels,
                                        List<int[]> testFeatures, List<String> testLabels, double trainRatio) {
        Map<String, List<Integer>> labelIndices = new HashMap<>();
        for (int i = 0; i < labels.size(); i++) {
            labelIndices.putIfAbsent(labels.get(i), new ArrayList<>());
            labelIndices.get(labels.get(i)).add(i);
        }

        for (String label : labelIndices.keySet()) {
            List<Integer> indices = labelIndices.get(label);
            int trainSize = (int) (indices.size() * trainRatio);

            for (int i = 0; i < trainSize; i++) {
                trainFeatures.add(featureVectors.get(indices.get(i)));
                trainLabels.add(label);
            }
            for (int i = trainSize; i < indices.size(); i++) {
                testFeatures.add(featureVectors.get(indices.get(i)));
                testLabels.add(label);
            }
        }
    }

    // Main method to test ModelTrainer
    public static void main(String[] args) throws IOException {
        // 1. Read dataset
        String filePath = "data/spam_assassin.csv";
        List<String[]> dataset = readDataset(filePath);

        // 2. Preprocess dataset
        Preprocessor preprocessor = new Preprocessor("resources/stopwords.txt");
        List<String> cleanedTexts = new ArrayList<>();
        List<String> labels = new ArrayList<>();

        for (String[] row : dataset) {
            String label = row[0].trim(); // "spam" or "ham"
            String emailText = row[1].trim();
            labels.add(label);
            cleanedTexts.add(preprocessor.preprocess(emailText));
        }

        // 3. Extract features
        FeatureExtractor extractor = new FeatureExtractor(cleanedTexts);
        List<int[]> featureVectors = new ArrayList<>();
        for (String text : cleanedTexts) {
            featureVectors.add(extractor.extractFeatures(text));
        }

        // 4. Stratified split
        List<int[]> trainFeatures = new ArrayList<>();
        List<String> trainLabels = new ArrayList<>();
        List<int[]> testFeatures = new ArrayList<>();
        List<String> testLabels = new ArrayList<>();
        stratifiedSplit(featureVectors, labels, trainFeatures, trainLabels, testFeatures, testLabels, 0.8);

        // 5. Train and evaluate the model
        ModelTrainer trainer = new ModelTrainer();
        trainer.train(trainFeatures, trainLabels);
        trainer.evaluate(testFeatures, testLabels);
    }
}

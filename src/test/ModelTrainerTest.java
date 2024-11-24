import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ModelTrainerTest {
    public static void main(String[] args) {
        // Mock dataset
        List<String> texts = Arrays.asList(
            "Buy cheap products now",    // Spam
            "Meeting scheduled at 10AM", // Ham
            "Special offer just for you", // Spam
            "Let’s catch up tomorrow",   // Ham
            "Limited time discount offer" // Spam
        );

        List<String> labels = Arrays.asList("spam", "ham", "spam", "ham", "spam");

        // Preprocess dataset
        Preprocessor preprocessor;
        try {
            preprocessor = new Preprocessor("resources/stopwords.txt");
            List<String> cleanedTexts = new ArrayList<>();
            for (String text : texts) {
                cleanedTexts.add(preprocessor.preprocess(text));
            }

            // Extract features
            FeatureExtractor extractor = new FeatureExtractor(cleanedTexts);
            List<int[]> featureVectors = new ArrayList<>();
            for (String text : cleanedTexts) {
                featureVectors.add(extractor.extractFeatures(text));
            }

            // Train the model
            ModelTrainer trainer = new ModelTrainer();
            trainer.train(featureVectors, labels);

            // Test predictions
            String testText = "Special discount for you";
            int[] testVector = extractor.extractFeatures(preprocessor.preprocess(testText));
            String prediction = trainer.predict(testVector);

            assert prediction.equals("spam") : "Test Failed: Prediction mismatch";

            System.out.println("All ModelTrainer tests passed!");

        } catch (IOException e) {
            System.err.println("Error loading stopwords file: " + e.getMessage());
        }
    }
}

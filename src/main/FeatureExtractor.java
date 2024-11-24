import java.util.*;

public class FeatureExtractor {
    private Map<String, Integer> vocabulary; // Word-to-index mapping

    // Constructor: Build the vocabulary from a dataset
    public FeatureExtractor(List<String> cleanedTexts) {
        this.vocabulary = buildVocabulary(cleanedTexts);
    }

    // Build a vocabulary (unique words) from cleaned texts
    private Map<String, Integer> buildVocabulary(List<String> cleanedTexts) {
        Map<String, Integer> vocab = new HashMap<>();
        int index = 0;
        for (String text : cleanedTexts) {
            String[] words = text.split("\\s+");
            for (String word : words) {
                if (!vocab.containsKey(word)) {
                    vocab.put(word, index++);
                }
            }
        }
        return vocab;
    }

    // Convert a single cleaned text into a feature vector
    public int[] extractFeatures(String text) {
        int[] features = new int[vocabulary.size()];
        String[] words = text.split("\\s+");
        for (String word : words) {
            if (vocabulary.containsKey(word)) {
                features[vocabulary.get(word)]++;
            }
        }
        return features;
    }

    // Test the FeatureExtractor
    public static void main(String[] args) {
        // Example cleaned texts
        List<String> cleanedTexts = Arrays.asList(
            "congratulations youve won prize click",
            "please find attached document review",
            "act now limited offer today",
            "can we reschedule meeting"
        );

        // Initialize the FeatureExtractor
        FeatureExtractor extractor = new FeatureExtractor(cleanedTexts);

        // Extract features for a new text
        String newText = "congratulations click now";
        int[] featureVector = extractor.extractFeatures(newText);

        // Print vocabulary and feature vector
        System.out.println("Vocabulary: " + extractor.vocabulary);
        System.out.println("Feature Vector: " + Arrays.toString(featureVector));
    }
}

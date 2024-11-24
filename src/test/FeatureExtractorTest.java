import java.util.Arrays;

public class FeatureExtractorTest {
    public static void main(String[] args) {
        // Sample dataset
        String[] sampleTexts = {
            "This is a spam email with special offers",
            "This is a ham email about meeting details",
            "Special discount for you on all products"
        };

        // Initialize feature extractor
        FeatureExtractor extractor = new FeatureExtractor(Arrays.asList(sampleTexts));

        // Test feature extraction
        String testText = "This is a special spam offer";
        int[] featureVector = extractor.extractFeatures(testText);

        System.out.println("Feature vector for testText:");
        System.out.println(Arrays.toString(featureVector));

        assert featureVector.length == sampleTexts.length : "Test Failed: Incorrect feature vector length";

        System.out.println("All FeatureExtractor tests passed!");
    }
}

import java.io.IOException;

public class PreprocessorTest {
    public static void main(String[] args) {
        try {
            // Initialize preprocessor with stopwords file
            Preprocessor preprocessor = new Preprocessor("resources/stopwords.txt");

            // Test cases
            String input1 = "This is an example email!";
            String expected1 = "example email";
            String result1 = preprocessor.preprocess(input1);
            assert result1.equals(expected1) : "Test Failed: Input1";

            String input2 = "Hello <b>world</b>, welcome to <i>testing</i>!";
            String expected2 = "hello world welcome testing";
            String result2 = preprocessor.preprocess(input2);
            assert result2.equals(expected2) : "Test Failed: Input2";

            String input3 = "Stopwords like 'and', 'or', 'but' should be removed.";
            String expected3 = "stopwords like should be removed";
            String result3 = preprocessor.preprocess(input3);
            assert result3.equals(expected3) : "Test Failed: Input3";

            System.out.println("All Preprocessor tests passed!");

        } catch (IOException e) {
            System.err.println("Error loading stopwords file: " + e.getMessage());
        }
    }
}

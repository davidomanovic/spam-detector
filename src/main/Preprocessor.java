package main;

import java.io.*;
import java.util.*;

public class Preprocessor {
    private Set<String> stopwords;

    // Constructor to load stopwords
    public Preprocessor(String stopwordsFile) throws IOException {
        stopwords = loadStopwords(stopwordsFile);
    }

    // Load stopwords from a file
    private Set<String> loadStopwords(String filePath) throws IOException {
        Set<String> stopwords = new HashSet<>();
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        while ((line = reader.readLine()) != null) {
            stopwords.add(line.trim().toLowerCase());
        }
        reader.close();
        return stopwords;
    }

    // Preprocess a single text
    public String preprocess(String text) {
        text = text.toLowerCase(); // Convert to lowercase
        text = removeEmailAddresses(text); // Remove email addresses
        text = removeUrls(text); // Remove URLs
        text = text.replaceAll("<[^>]*>", ""); // Remove HTML tags
        text = text.replaceAll("[^a-zA-Z0-9\\s]", ""); // Remove non-alphanumeric characters
        String[] tokens = text.split("\\s+"); // Tokenize
        StringBuilder cleanedText = new StringBuilder();
        for (String token : tokens) {
            if (!stopwords.contains(token)) {
                cleanedText.append(token).append(" ");
            }
        }
        return cleanedText.toString().trim(); // Return the cleaned text
    }

    // Helper function: Remove email addresses
    private String removeEmailAddresses(String text) {
        return text.replaceAll("\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\\b", "");
    }

    // Helper function: Remove URLs
    private String removeUrls(String text) {
        return text.replaceAll("http\\S+|www\\S+", "");
    }

    // Test the preprocessor
    public static void main(String[] args) throws IOException {
        // Load the stopwords file
        Preprocessor preprocessor = new Preprocessor("resources/stopwords.txt");

        // Example email text with metadata
        String email = "From: test@example.com\nSubject: Win a prize!\nVisit https://example.com now to claim your reward!";
        String cleanedEmail = preprocessor.preprocess(email);

        // Print the cleaned text
        System.out.println("Original: " + email);
        System.out.println("Cleaned: " + cleanedEmail);
    }
}

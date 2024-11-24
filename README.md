# Spam Detection in Java

<div style="display: flex; justify-content: center; align-items: center; gap: 10px;">
    <img src="https://github.com/user-attachments/assets/88f188f5-9de0-464f-849b-552767b2580c" alt="Image 1" width="500"/>
    <img src="https://github.com/user-attachments/assets/5040a886-cd89-4d56-969c-2cabf7f2f3a4" alt="Image 2" width="500"/>
</div>

## Overview
Who likes spam? NO ONE! This project implements a machine learning-based Spam Detection system in Java. The application classifies emails as either spam or ham (non-spam) using supervised learning techniques. It demonstrates data preprocessing, feature extraction, model training, and evaluation.

## Features
- Preprocesses raw email data (cleaning, tokenization, etc.)
- Extracts features using techniques like bag-of-words or TF-IDF
- Trains a Naive Bayes or Decision Tree model
- Evaluates model accuracy using metrics such as precision, recall, and F1-score
- Provides a simple command-line interface (CLI) or GUI for email classification

## Tech Stack
- **Programming Language**: Java
- **Libraries**:
  - Apache Commons CSV: For dataset handling
  - DL4J (optional): For machine learning
  - Apache OpenNLP: For natural language processing
  - JavaFX (optional): For GUI development

## Project Structure

```bash
SpamDetection/
├── data/
│   ├── spam.csv               # Sample dataset
│   ├── cleaned_data.csv       # Preprocessed data
├── src/
│   ├── main/
│   │   ├── App.java               # Entry point
│   │   ├── EmailClassifier.java   # Main logic for classification
│   │   ├── Preprocessor.java      # Data preprocessing
│   │   ├── FeatureExtractor.java  # Extract features from text
│   │   ├── ModelTrainer.java      # Train and save the model
│   │   └── ModelEvaluator.java    # Evaluate the model
│   └── test/
│       ├── ModelTrainerTest.java  # Unit tests for training logic
│       └── EmailClassifierTest.java # Tests for classification
├── resources/
│   ├── stopwords.txt          # Stopwords list
├── README.md                  # Project documentation
├── LICENSE                    # License information
└── .gitignore                 # Files to ignore in Git
```

## Installation
1. Clone the repository:
```bash
git clone https://github.com/yourusername/SpamDetection.git
```

2. Build the project with Maven
```bash
mvn clean install
```

3. Run the project
```bash
java -jar target/SpamDetector.jar
```

## Usage

- To classify emails, run the program.
- For training:
  - Provide a labeled dataset (e.g., spam.csv)
  - Use the training module to build the model
  ```bash
  java -cp target/SpamDetector.jar ModelTrainer data/spam.csv
  ```

## Dataset
The project uses the public SpamAssassin dataset for training and testing. Ensure that the dataset is formatted correctly (e.g., CSV with label and email columns).

-------

## **Detailed Project Structure**

### **1. `App.java`**
The main entry point of the application. Handles user interaction:
- Provides options for training, testing, or classifying new emails.
- Interfaces with other modules.

### **2. `Preprocessor.java`**
Handles data cleaning and preparation:
- Tokenizes text.
- Removes stopwords (from `resources/stopwords.txt`).
- Normalizes case and removes punctuation.

### **3. `FeatureExtractor.java`**
Converts preprocessed text into numerical features:
- Implements bag-of-words or TF-IDF vectorization.
- Stores feature vectors in a format suitable for model training.

### **4. `ModelTrainer.java`**
Trains a machine learning model:
- Reads labeled data (`spam` and `ham`).
- Splits data into training and testing sets.
- Fits a Naive Bayes or Decision Tree model.

### **5. `EmailClassifier.java`**
Loads the trained model and classifies new emails:
- Takes preprocessed email data as input.
- Outputs whether the email is spam or ham.

### **6. `ModelEvaluator.java`**
Evaluates the model's performance:
- Calculates precision, recall, F1-score, and accuracy.
- Outputs confusion matrices for visual understanding.

---

## **Example Dataset (`data/spam.csv`)**

| Label  | Email Text                                              |
|--------|---------------------------------------------------------|
| spam   | Congratulations! You've won a $1,000 gift card. Click here! |
| ham    | Hi, please find the meeting agenda attached.            |
| spam   | Get cheap loans now! Apply online today.                |
| ham    | Can we reschedule our call for tomorrow?                |

---

## **Initial Implementation**

### **Preprocessor.java**
```java
import java.util.*;
import java.util.regex.*;

public class Preprocessor {
    private Set<String> stopwords;

    public Preprocessor(String stopwordsFile) {
        stopwords = loadStopwords(stopwordsFile);
    }

    private Set<String> loadStopwords(String filePath) {
        // Load stopwords from file into a Set
        return new HashSet<>(Arrays.asList("a", "an", "the", "and", "or", "but", "to", "of", "in"));
    }

    public String preprocess(String text) {
        text = text.toLowerCase();
        text = text.replaceAll("[^a-zA-Z0-9\\s]", ""); // Remove punctuation
        String[] tokens = text.split("\\s+");
        StringBuilder cleanedText = new StringBuilder();
        for (String token : tokens) {
            if (!stopwords.contains(token)) {
                cleanedText.append(token).append(" ");
            }
        }
        return cleanedText.toString().trim();
    }
}
```

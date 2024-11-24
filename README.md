# Spam Detection with Naive Bayes
Example with super simple GUI made using JFrame for testing the model:

<div style="display: flex; justify-content: center; align-items: center; gap: 10px;">
    <img src="https://github.com/user-attachments/assets/88f188f5-9de0-464f-849b-552767b2580c" alt="Image 1" width="400"/>
    <img src="https://github.com/user-attachments/assets/5040a886-cd89-4d56-969c-2cabf7f2f3a4" alt="Image 2" width="400"/>
</div>


Who likes spam? No one😡! This project implements a spam detection system using the Naive Bayes algorithm. It preprocesses raw email data, extracts features, and evaluates the model's performance. We also have a simple GUI here just to test if the model works as expected.

## Dataset
- **Source**: SpamAssassin Public Corpus
- **Format**: CSV with HTML formatted email content and labels (`0 = ham`, `1 = spam`)

## Workflow
1. **Preprocessing**:
   - Removes headers and metadata from emails.
   - Strips HTML tags, special characters, and stopwords.

2. **Feature Extraction**:
   - Converts email content into a bag-of-words representation.
   - Optionally uses TF-IDF for weighting.

3. **Model**:
   - Naive Bayes classifier with Laplace smoothing.

## Results
- **Accuracy**: 98.79%
- **Precision**: 100%
- **Recall**: 96.31%
- **F1-Score**: 98.12%

## How to Run
1. Clone the repository:
```bash
   git clone https://github.com/username/spam-detector.git
   cd spam-detector
```

2. Compile the code:
```bash
javac src/main/*.java
```

3. Run the model training program or the GUI to test visually the model (we need 4GB heap from the huge dataset):
```bash
java -Xmx4G -cp src/main ModelTrainer
java -Xmx4G -cp src/main SpamDetectorGUI
```

## Acknowledgements
Special thanks to the SpamAssassin dataset for providing valuable email data for this project.

## License
This project has a MIT license.

---
Let me know if you need help with anything else! 🎯

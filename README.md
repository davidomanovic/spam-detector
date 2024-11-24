# Spam Detection with Naive Bayes

Who likes spam? No one😡! This project implements a spam detection system using generative learning (Gen AI) by means of the Naïve Bayes algorithm. It preprocesses raw email data, extracts features, and evaluates the model's performance in Java. We also have a simple GUI here just to test if the model works as expected.

<div align="center">
    <img src="https://github.com/davidomanovic/spam-detector/blob/main/example.gif" alt="Example" width="700"/>
</div>


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

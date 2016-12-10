package newstagger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.sentdetect.*;
import opennlp.tools.namefind.*;
import opennlp.tools.tokenize.*;
import opennlp.tools.util.Span;

/**
 *
 * @author RaJaT Paliwal
 */
public class NewsTagger {

    public void SentenceDetect(String paragraph) throws FileNotFoundException {
        String modelPath = "Models/en-sent.bin";
        // always start with a model, a model is learned from training data
        InputStream modelIn = new FileInputStream(modelPath);
        try {
            SentenceModel model = new SentenceModel(modelIn);
            SentenceDetectorME sdetector = new SentenceDetectorME(model);
            String sentences[] = sdetector.sentDetect(paragraph);
            for (String sentence : sentences) {
                System.out.println(sentence);
            }
            modelIn.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void findTokens(String sentences[]) throws FileNotFoundException {
        String modelPath = "Models/en-token.bin";
        // always start with a model, a model is learned from training data
        InputStream modelIn = new FileInputStream(modelPath);
        try {
            TokenizerModel model = new TokenizerModel(modelIn);
            Tokenizer tokenizer = new TokenizerME(model);

            for (int i = 0; i < sentences.length; i++) {
                String[] tokens = tokenizer.tokenize(sentences[i]);
            }
            modelIn.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void findName(String tokens[]) throws FileNotFoundException {
        String modelPath = "Models/en-ner-person.bin";
        // always start with a model, a model is learned from training data
        InputStream modelIn = new FileInputStream(modelPath);
        try {
            TokenNameFinderModel model = new TokenNameFinderModel(modelIn);
            NameFinderME nameFinder = new NameFinderME(model);

            Span nameSpans[] = nameFinder.find(tokens);

            for (Span nameSpan : nameSpans) {
                System.out.println(nameSpan.toString());
            }
            modelIn.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void paragraphToName(String paragraph) throws FileNotFoundException {
        try {

            // 1: Split sentences into tokens
            String modelPath = "Models/en-token.bin";
            InputStream modelIn = new FileInputStream(modelPath);
            TokenizerModel model_token = new TokenizerModel(modelIn);
            Tokenizer tokenizer = new TokenizerME(model_token);
            String[] tokens = tokenizer.tokenize(paragraph);
            modelIn.close();

            // 2: Find names in the tokens
            modelPath = "Models/en-ner-person.bin";
            modelIn = new FileInputStream(modelPath);
            TokenNameFinderModel model_Name = new TokenNameFinderModel(modelIn);
            NameFinderME nameFinder = new NameFinderME(model_Name);
            Span nameSpans[] = nameFinder.find(tokens);
            for (Span nameSpan : nameSpans) {
                System.out.println(nameSpan.toString());
                 for(int i = nameSpan.getStart();i<nameSpan.getEnd();i++){
                     System.out.print(tokens[i] + " ");
                 }
                 System.out.println();
            }
            modelIn.close();
            
            // 3: Find organization names in the tokens
            modelPath = "Models/en-ner-organization.bin";
            modelIn = new FileInputStream(modelPath);
            
            TokenNameFinderModel model_OrgName = new TokenNameFinderModel(modelIn);
            NameFinderME OrgNameFinder = new NameFinderME(model_OrgName);
            Span OrgNameSpans[] = OrgNameFinder.find(tokens);
            for (Span OrgNameSpan : OrgNameSpans) {
                System.out.println(OrgNameSpan.toString());
                for(int i = OrgNameSpan.getStart();i<OrgNameSpan.getEnd();i++){
                     System.out.print(tokens[i] + " ");
                 }
                 System.out.println();
            }
            modelIn.close();
            
            // 4: Find Location in the tokens
            modelPath = "Models/en-ner-location.bin";
            modelIn = new FileInputStream(modelPath);
            
            TokenNameFinderModel model_Location = new TokenNameFinderModel(modelIn);
            NameFinderME locationName = new NameFinderME(model_Location);
            Span locations[] = locationName.find(tokens);
            for (Span location : locations) {
                System.out.println(location.toString());
                for(int i = location.getStart();i<location.getEnd();i++){
                     System.out.print(tokens[i] + " ");
                 }
                 System.out.println();
            }
            modelIn.close();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        
        System.out.println("Input news");
        Scanner in = new Scanner(System.in);
        String paragraph = in.nextLine();
        try {
            NewsTagger.paragraphToName(paragraph);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}

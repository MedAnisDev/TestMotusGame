import java.util.ArrayList;
import java.util.Collections;

public class Dictionary {
    private static int lengthWord = 0;
    private final ArrayList<String> dictionary;
    private int nbWords;

    //Constructor
    public Dictionary(int lengthWord, int nbWords, ArrayList<String> words) {
        Dictionary.lengthWord = lengthWord;
        this.nbWords = nbWords;
        this.dictionary = words;
    }


    //Methods

    public ArrayList<String> printDictionary() {
        return dictionary;
    }

    public void addWord(String addedWord) {

        if (!dictionary.contains(addedWord)) {

            System.out.println("old dictionary : " + dictionary);

            dictionary.add(addedWord);
            Collections.sort(dictionary);

            System.out.println("new dictionary : " + dictionary);


            nbWords++;

            System.out.println("Number of words = " + nbWords);
        } else {
            System.out.println("The word does not fit the dictionary size.");
        }

    }

    public void deleteWord(String wordToDelete) {

        System.out.println(dictionary);

        if (dictionary.contains(wordToDelete)) {

            dictionary.remove(wordToDelete);

            System.out.println("Word " + wordToDelete + " deleted ");

            Game.decrementTrials();

            System.out.println(dictionary);

            nbWords--;

            System.out.println("Number of words = " + nbWords);

        } else {
            System.out.println("The word does not exist in the dictionary.");
        }
    }

    public Boolean findWord(String wordToFind) {

        return dictionary.contains(wordToFind);
    }

    public static int getLengthWord() {
        return lengthWord;
    }
    public ArrayList<String> getDictionary(){
        return this.dictionary ;
    }
}


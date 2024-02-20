import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Game {


    private static int nbRemainingAttempts;
    private final String hiddenWord;
    private final int nbInitialAttempts;
    private String foundWord;
    Map<String, Integer> bestWords;

    public Game(int nbTrial, ArrayList<String> words, int lengthWord) {
        this.nbInitialAttempts = nbTrial;
        nbRemainingAttempts = nbTrial;
        this.hiddenWord = chooseRandomWord(words);
        this.foundWord = generateInitialValue(lengthWord);
        this.bestWords = new HashMap<>();
    }

    private String chooseRandomWord(ArrayList<String> words) {
        Random random = new Random();
        return words.get(random.nextInt(words.size()));
    }

    public String generateInitialValue(int lengthWord) {
        return "*".repeat(lengthWord);
    }

    public static void decrementTrials() {
        Game.nbRemainingAttempts--;
    }

    public static Boolean isValid(String proposedWord) {

        return proposedWord.length() == Dictionary.getLengthWord() && proposedWord.matches("[a-zA-Z]+");
    }


    public boolean testWord(String proposedWord) {

        String newFoundWord = buildFoundWord(proposedWord);
        this.foundWord = newFoundWord;

        this.bestWords.put(newFoundWord, calculateAlphaInString(newFoundWord));

        return checkFoundWord(foundWord);
    }

    public Boolean checkFoundWord(String foundWordTest) {

        if (hiddenWord.equals(foundWordTest)) {
            System.out.println("your found Word : " + foundWord);
            return true;
        }
        return false;
    }

    private int calculateAlphaInString(final String alphaString) {
        int countAlpha = 0;
        for (int i = 0; i < alphaString.length(); ++i) {
            if (Character.isAlphabetic(alphaString.charAt(i))) {
                countAlpha++;
            }
        }
        return countAlpha;
    }

    public String buildFoundWord(String proposedWord) {
        char[] word = new char[Dictionary.getLengthWord()];

        for (int i = 0; i < proposedWord.length(); i++) {
                word[i] = proposedWord.charAt(i) == hiddenWord.charAt(i) ? hiddenWord.charAt(i) :'*' ;
        }
        return new String(word);
    }

    public String getBestWord(int lengthWord) {
        return getStringOfMaxScore(lengthWord);
    }

    private String getStringOfMaxScore(int lengthWord) {
        int maxScore = 0;
        String maxScoredString = generateInitialValue(lengthWord);

        for (Map.Entry<String, Integer> entry : bestWords.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            if (value >= maxScore) {
                maxScore = value;
                maxScoredString = key;
            }
        }
        return maxScoredString;
    }

    public int getNbRemainingAttempts() {
        return nbRemainingAttempts;
    }

    public String getFoundWord() {
        return foundWord;
    }

    public String getHiddenWord() {
        return hiddenWord;
    }

    public int getNbInitialAttempts() {
        return nbInitialAttempts;
    }
}


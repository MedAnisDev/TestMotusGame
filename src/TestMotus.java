import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.Scanner;


public class TestMotus {

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Enter <action> <lengthWord>");
            return;
        }

        String action = args[0];
        int lengthWord = Integer.parseInt(args[1]);

        if (action.equals("config")) {
            Scanner scanner = new Scanner(System.in);
            configureDictionary(scanner, lengthWord);
            scanner.close();

        } else if (action.equals("game")) {
            Scanner scanner = new Scanner(System.in);
            startGame(scanner, lengthWord);
            scanner.close();
        } else {
            System.out.println("Action not recognized.");
        }
    }

    public static void addValidWord(Scanner scanner, WordList dictionary) {
        String wordToAdd;

        do {
            System.out.println("Enter a word to add :");
            wordToAdd = scanner.nextLine();

            if (Game.isValid(wordToAdd)) {
                dictionary.addWord(wordToAdd);
            } else {
                System.out.println("Word is not valid ! please enter again.");
            }
        } while (Game.isValid((wordToAdd)));
    }

    public static void deleteWord(Scanner scanner , WordList dictionary){
        System.out.println("Enter a word to delete : ");
        String wordToDelete = scanner.nextLine();
        dictionary.deleteWord(wordToDelete);
    }

    public static void findWord(Scanner scanner , WordList dictionary){
        System.out.println("Enter a word to find : ");
        String wordToFind = scanner.nextLine();

        if (dictionary.findWord(wordToFind)) {
            System.out.println("your word = " + wordToFind + " is found .");
        } else {
            System.out.println("your word is not found");
        }

    }


    private static void configureDictionary(Scanner scanner, int lengthWord) {

        ArrayList<String> dictCharge = TestMotus.loadWordFromFile(lengthWord);

        int nbMots = dictCharge.size();

        WordList dictionary = new WordList(lengthWord, nbMots, dictCharge);

        while (true) {
            System.out.println("1. add word");
            System.out.println("2. delete word ");
            System.out.println("3. find word");
            System.out.println("4. print dictionary");
            System.out.println("5. exit");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    addValidWord(scanner ,dictionary);
                    break;

                case 2:
                    deleteWord(scanner , dictionary) ;
                    break;

                case 3:
                    findWord(scanner , dictionary);
                    break;

                case 4:
                    System.out.println(dictionary.printDictionary());
                    break;

                case 5:
                    return;
                default:
                    System.out.println("Invalid choice.");
                    break;
            }
        }
    }


    public static ArrayList<String> loadWordFromFile(int lenghtWord) {

        String filePath = "d" + lenghtWord + ".txt";

        ArrayList<String> dictionary = new ArrayList<>();

        try {
            Scanner scanner = new Scanner(
                    Objects.requireNonNull(
                            TestMotus.class.getClassLoader()
                                    .getResourceAsStream(filePath)
                    )
            );
            while (scanner.hasNextLine()) {
                dictionary.add(scanner.nextLine());
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        for (String str : dictionary) {
            str.toUpperCase();
        }
        Collections.sort(dictionary);
        return dictionary;
    }


    private static void startGame(Scanner scanner, int lengthWord) {

        int nbAttempts;

        try {
            System.out.println("Enter the number of attempts allowed : ");
            nbAttempts = scanner.nextInt();

            scanner.nextLine();

            ArrayList<String> dict = loadWordFromFile(lengthWord);

            Game game = new Game(nbAttempts, dict, lengthWord);

            playGame(scanner, game, lengthWord);

        } catch (Exception e) {
            System.out.println("enter an integer greater than zero!");
        }
    }

    private static void playGame(Scanner scanner, Game game, int lengthWord) {

        while (game.getNbRemainingAttempts() > 0 && !game.getFoundWord().equals(game.getHiddenWord())) {

            ArrayList<String> words = new ArrayList<>(loadWordFromFile(lengthWord));

            System.out.println("your chosen dictionary is = " + words);

            String proposedWord;

            do {
                System.out.println("Remaining Attempts : " + game.getNbRemainingAttempts());
                System.out.println("Enter a word : ");
                proposedWord = scanner.nextLine();

            } while (!Game.isValid(proposedWord));

            boolean isFoundWordCorrect = game.testWord(proposedWord);

            System.out.println("your foundWord : " + game.getBestWord());

            if (isFoundWordCorrect) {
                int successAttempt = game.getNbInitialAttempts() - game.getNbRemainingAttempts() + 1;
                System.out.println("Well done ! You found the hidden word { " + game.getHiddenWord() + " } at the test number " + successAttempt);
                break;
            } else {
                System.out.println("Incorrect word.");
                System.out.println("hidden word : " + game.getHiddenWord());
                Game.decrementTrials();
            }
        }

        if (!game.getFoundWord().equals(game.getHiddenWord())) {
            System.out.println("Sorry, you have exhausted all attempts. The word was : " + game.getHiddenWord());
        }
    }
}

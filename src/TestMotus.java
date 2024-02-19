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
            ArrayList<String> words = loadWordsFromFile(lengthWord);
            startGame(scanner, lengthWord , words);
            scanner.close();
        } else {
            System.out.println("Action not recognized.");
        }
    }

    private static void configureDictionary(Scanner scanner, int lengthWord) {

        ArrayList<String> words = TestMotus.loadWordsFromFile(lengthWord);

        int nbMots = words.size();

        Dictionary dictionary = new Dictionary(lengthWord, nbMots, words);

        while (true) {
            System.out.println("1. add word");
            System.out.println("2. delete word ");
            System.out.println("3. find word");
            System.out.println("4. print dictionary");
            System.out.println("5. start playing");
            System.out.println("6. exit");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    addValidWord(scanner, dictionary);
                    break;

                case 2:
                    deleteWord(scanner, dictionary);
                    break;

                case 3:
                    findWord(scanner, dictionary);
                    break;

                case 4:
                    System.out.println(dictionary.printDictionary());
                    break;

                case 5:
                    ArrayList <String> wordsAfterConfig= dictionary.getDictionary();
                    startGame(scanner, lengthWord ,wordsAfterConfig);
                case 6:
                    return;
                default:
                    System.out.println("Invalid choice.");
                    break;
            }
        }
    }

    public static void addValidWord(Scanner scanner, Dictionary dictionary) {
        System.out.println("Enter a word to add :");
        String wordToAdd = scanner.nextLine();

        final int POSSIBLE_ATTEMPTS = 3;
        int i = 1;

        while (i < POSSIBLE_ATTEMPTS) {
            if (Game.isValid((wordToAdd))) {
                dictionary.addWord(wordToAdd);
                break;
            } else {
                System.out.println("Word is not valid ! please enter again.");
                wordToAdd = scanner.nextLine();
                i++;
            }
        }
    }

    public static void deleteWord(Scanner scanner, Dictionary dictionary) {
        System.out.println("Enter a word to delete : ");
        String wordToDelete = scanner.nextLine();
        dictionary.deleteWord(wordToDelete);
    }

    public static void findWord(Scanner scanner, Dictionary dictionary) {
        System.out.println("Enter a word to find : ");
        String wordToFind = scanner.nextLine();

        if (dictionary.findWord(wordToFind)) {
            System.out.println("your word = " + wordToFind + " is found .");
        } else {
            System.out.println("your word is not found");
        }

    }


    public static ArrayList<String> loadWordsFromFile(int lenghtWord) {

        String filePath = "d" + lenghtWord + ".txt";

        ArrayList<String> words = new ArrayList<>();

        try {
            Scanner scanner = new Scanner(
                    Objects.requireNonNull(
                            TestMotus.class.getClassLoader()
                                    .getResourceAsStream(filePath)
                    )
            );
            while (scanner.hasNextLine()) {
                words.add(scanner.nextLine());
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        for (String str : words) {
            str.toUpperCase();
        }
        Collections.sort(words);
        return words;
    }


    private static void startGame(Scanner scanner, int lengthWord, ArrayList<String> words) {

        int nbAttempts;

        try {
            System.out.println("Enter the number of attempts allowed : ");
            nbAttempts = scanner.nextInt();
            scanner.nextLine();

            Game game = new Game(nbAttempts, words, lengthWord);

            playGame(scanner, game, words);

        } catch (Exception e) {
            System.out.println("enter an integer greater than zero!");
        }
    }

    private static void playGame(Scanner scanner, Game game, ArrayList<String> words) {

        while (isGameCondition(game)) {

            System.out.println("your chosen dictionary is = " + words);

            String proposedWord = getProposedWordFromUser(scanner, game);

            System.out.println("your foundWord : " + game.getBestWord());

            if (game.testWord(proposedWord)) {
                int successAttempt = calcSuccessAttempt(game);
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

    public static boolean isGameCondition(Game game) {
        return game.getNbRemainingAttempts() > 0 && !game.getFoundWord().equals(game.getHiddenWord());
    }

    public static String getProposedWordFromUser(Scanner scanner, Game game) {
        String proposedWord;
        do {
            System.out.println("Remaining Attempts : " + game.getNbRemainingAttempts());
            System.out.println("Enter a word : ");
            proposedWord = scanner.nextLine();

        } while (!Game.isValid(proposedWord));
        return proposedWord;
    }

    public static int calcSuccessAttempt(Game game) {
        return game.getNbInitialAttempts() - game.getNbRemainingAttempts() + 1;
    }
}

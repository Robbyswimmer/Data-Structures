import com.sun.source.tree.Tree;

import java.util.*;

/**
 * Robert Moseley
 * Dr. Han
 * EGR227 Data Structures - Evil Hangman 2
 * 1/26/2020
 *
 * Class description:
 *
 * This is a manager class for the Evil Hangman Game.
 * This class initializes the constructor with the appropriate
 * beginning values, and returns errors if initialized with
 * incorrect values. It returns various statistics about the game,
 * but the primary responsibility of this class is to record each
 * guess that the player makes and then use that guess to create
 * patterns for every word in the dictionary. Once it has created
 * the patterns, the algorithm determines a new dictionary of words
 * which it applies to the next guess that the player makes.
 *
 * As an example of this, if the player guesses "a", then the program
 * will take words of the initialized length and return patterns for the
 * words that do and do not contain "a". Patterns will range from "- - - -"
 * for words that do not contain "a", and for a word like "meal" it will create
 * a pattern that looks like "- - a -". This pattern is stored in a map whose keys
 * are the pattern and whose values are the set of words from the dictionary
 * that match that given value.
 */
public class HangmanManager {

    private Collection<String> dictionary;
    private TreeSet<String> wordSet;
    private SortedSet<Character> chars = new TreeSet<>();

    private int targetLength;
    private int gLeft;
    private String correctPattern = "";
    private String originalPattern = "";

    //constructor for HangmanManager
    //The constructor accepts a dictionary of words, a target word length,
    //and the maximum number of wrong guesses that the player is allowed to make.
    //These values are always used to initialize the state of the game.
    //The set of words contains all of the words from the dictionary of the
    //given length, and all duplicates have been removed.
    public HangmanManager(Collection<String> dictionary, int length, int max) {
        //protects the game from being initialized with improper values
        if (length < 1 || max < 0) throw new IllegalArgumentException("Not a valid game condition!");

        for (int i = 0; i < length; i++) {
            if (i == length - 1) {
                correctPattern += "-";
                originalPattern += "-";
            } else {
                correctPattern += "- ";
                originalPattern += "- ";
            }

        }

        this.dictionary = new TreeSet<>();
        this.wordSet = new TreeSet<>();
        this.targetLength = length;
        this.gLeft = max;

        for (String word : dictionary) {
            if (word.length() == length) {
                this.dictionary.add(word);
                wordSet.add(word);
            }
        }
    }

    //This method is called by the client to get access to the
    //current set of words being considered by the hangman manager
    public Set<String> words() {
        return wordSet;
    }

    //This method is called by the client to find out how
    //many guesses that the player has left
    public int guessesLeft() {
        return this.gLeft;
    }

    //The client calls this method to find out the current
    //set of letters that have been guessed by the player.
    //This method returns a sorted set of characters that
    //were correct guesses from the player.
    public SortedSet<Character> guesses() {
        return chars;
    }

    //This method should return the current pattern that the
    //game should display for the word being guessed. Letters
    //that have not been guessed in the patter should be displayed
    //as dashes, while "correctly" guessed letters should appear
    //in the correct positions alongside the dashes that have not
    //yet been guessed. No leading or trailing spaces are included.
    public String pattern() {
        return correctPattern;
    }

    //This method does most of the work by recording the next guess
    //of the player. Using this guess, which is the parameter, the
    //method decides on what set of words should be used next. It should
    //also return the number of occurrences of the guessed letter in the
    //new pattern and also update the number of guesses left for the player
    //to make before losing.
    public int record(char guess) {

        //throw IllegalStateException if the number of guesses left is not
        //at least 1, or if the set of words is empty.
        if (gLeft < 1 || wordSet.isEmpty()) throw new IllegalStateException("It's not me, it's you.");

        //It should throw an IllegalArgumentException if the set of words is
        //both nonempty and the guessed character has already been guessed previously.
        if (!wordSet.isEmpty() && chars.contains(guess))
            throw new IllegalArgumentException("That character has already been guessed!");

        //adds the character to the set of guesses since
        //the set does not already contain the guess
        chars.add(guess);

        //1. use guess to decide on next word set
        // and use that guess to generate the best pattern
        // the best pattern is the one that has the most
        // words associated it with it.
        String pattern = makePatternHelper(guess);

        //2. return the number of occurrences of the guessed letter in the new pattern
        //i.e update the pattern "-e-e-" or whatever it is
        //make sure to update correctPattern Global variable
        int foundChar = 0;
        for (int i = 0; i < pattern.length(); i++) {
            if (pattern.charAt(i) == guess) {
                foundChar++;
            }
        }

        //3. update the number of guesses that the player has left
        if (foundChar == 0) {
            gLeft--;
        }
        return foundChar;
    }

    private String makePatternHelper(char guess) {
        Map<String, TreeSet<String>> map = new TreeMap<>();
        StringBuilder pattern = new StringBuilder();

        //sorts through the characters in guesses and replaces
        //hyphens in the pattern if the word contains the guess
        for (String word : wordSet) {
            pattern = new StringBuilder();
            if (!correctPattern.equals(originalPattern)) {
                pattern.append(correctPattern);
            } else {
                for (int i = 0; i < targetLength; i++) {
                    if (i == targetLength - 1) {
                        pattern.append("-");
                    } else {
                        pattern.append("- ");
                    }

                }
            }
            for (int i = 0; i < word.length(); i++) {
                if (word.charAt(i) == guess) {
                    pattern.replace(i * 2, i * 2 + 1, "" + guess);
                }
            }
            if (!map.containsKey(pattern.toString())) {
                TreeSet<String> set = new TreeSet<>();
                set.add(word);
                map.put(pattern.toString(), set);
            } else {
                map.get(pattern.toString()).add(word);
            }
        }

        TreeSet<String> biggestSet = new TreeSet<>();
        //finds the largest set of words which can be used to update the word set
        correctPattern = pattern.toString();
        for (String key : map.keySet()) {
            if (map.get(key).size() > biggestSet.size()) {
                biggestSet = map.get(key);
                correctPattern = key;
            }
        }
        wordSet = biggestSet;
        return correctPattern;
    }
}


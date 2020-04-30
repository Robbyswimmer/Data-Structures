/**
 * Robert Moseley
 * Dr. Han
 * EGR227 Data Structures
 * 2/19/2020
 *
 * Class description: This class acts as the main class for
 * finding algorithms using the given dictionaries. It accepts a
 * dictionary using user input, and then gets a word or phrase from
 * the user along with a max number of words, and then the class finds
 * all anagrams that correspond to the given word or phrase.
 */
import java.util.*;
public class Anagrams {
    //Hashmap to store the words with their corresponding dictionaries
    private Map<String, LetterInventory> map = new HashMap<>();
    private List<String> dictionary;

    /**
     * A simple constructor that populates the map that is used by print()
     * pre: map is empty
     * post: map is populated with words from the dictionary as keys,
     *          and the values are the inventories of those words.
     * @param dictionary a list of words that is passed to the constructor where
     *                   an inventory is made for every word in the dictionary.
     */
    public Anagrams(List<String> dictionary) {
        //loops through the dictionary and creates a new inventory for each word
        for (String word : dictionary) {
            LetterInventory inventory = new LetterInventory(word);
            map.put(word, inventory);
        }
        this.dictionary = dictionary;
    }
    /**
     * This is the main print method. It calls the recursive print() method, where
     * most of the work is done. This method essentially exists to initialize the print
     * helper, which implements the actual recursive backtracking.
     * @param text this is the word/phrase that is being used to find anagrams of
     * @param max this is the max number of words that can be returned for each
     *            solution. If max is 2, then it finds solutions with 2 words.
     * Pre: text and max have the user-given conditions.
     * Post: All anagrams corresponding to the user input have been printed,
     *            the game now seeks new input to use.
     */
    public void print(String text, int max) {
        if (max < 0) throw new IllegalArgumentException("Max is less than 0. That seems like your fault.");
        List<String> solutionList = new ArrayList<>();
        LetterInventory inventory = new LetterInventory(text);
        List<String> prunedDict = pruned(dictionary, text);
        print(text, max, inventory, prunedDict, solutionList);
    }

    /**
     * This is the main print method where all of the recursive backtracking happens.
     * This method keeps going until all possible solutions have been found and the
     * inventory is empty. After all solutions are printed, the program seeks a new anagram
     * and repeats the process.
     * @param text this is the word/phrase that anagrams are created for
     * @param max this is the max number of words in each solution
     * @param inventory this is the current inventory that is being search
     * @param dict this is the dictionary that is being evaluated for anagrams
     * @param solutionList temporary list that stores the current solutions before printing
     *                     or removal.
     * Pre: Inventory is full of letters given by 'text', and dictionary is populated with a pruned
     *                     selection of words from the chosen dictionary file. The solution list
     *                     is empty.
     * Post: Inventory is empty, while solutionList contains a solution to the anagrams problem.
     */
    private void print(String text, int max, LetterInventory inventory, List<String> dict, List<String> solutionList) {
        //recursive backtracking
        if (inventory.isEmpty()) {
            //base case
            //prints out all of the words in the stack, once the inventory is empty
            System.out.print("[");
            for (int i = 0; i < solutionList.size(); i++) {
                if (i == solutionList.size() - 1) {
                    System.out.print(solutionList.get(i));
                } else {
                    System.out.print(solutionList.get(i) + ", ");
                }
            }
            System.out.print("]");
            System.out.println();
        } else if (solutionList.size() < max || max == 0) {
            //recursive case
            for (int i = 0; i < dict.size(); i++) {
                String word = dict.get(i);
                LetterInventory dictWord = map.get(word);
                LetterInventory nextInventory = inventory.subtract(dictWord);
                //as long as the inventory still has something in it,
                //the recursive method is called again to search for more solutions.
                if (nextInventory != null) {
                    solutionList.add(word);
                    print(text, max, nextInventory, dict, solutionList);
                    solutionList.remove(solutionList.size() - 1);
                }
            }
        }
    }
    /**
     * Simple method to eliminate words that possess no anagrams.
     * @param dict takes in the original dictionary given by the user.
     * @param text takes in the word/phrase to search for given by the user.
     * @return returns a pruned version of the dictionary to improve search
     *          time by the recursive backtracking print() method.
     * Pre: dict contains all of the words from the user chosen dictionary file.
     *      Text contains the word or phrase given by the user.
     * Post: Both dict and text are the same, but a new dictionary called 'prunedDict'
     *      has been populated with words that could contain anagrams.
     */
    private List<String> pruned(List<String> dict, String text) {
        LetterInventory txt = new LetterInventory(text);
        List<String> prunedDict = new Stack<>();
        for (String word : dict) {
            if (prunedHelper(txt, word)) {
                prunedDict.add(word);
            }
        }
        //returns a new dictionary with less words than the original dictionary
        //this makes the recursive calls faster than they would be otherwise
        return prunedDict;
    }
    /**
     * Simple helper method for pruned, to help decide whether or not to add
     * a new word to the pruned version of the dictionary.
     * @param txt a letter inventory made from the user given phrase/word
     * @param word a string that represents a word from the dictionary.
     *            It is compared against the user given word/phrase to check
     *             for similarities.
     * @return returns true or false to decide whether the word contains possible
     * anagrams. If the word contains possible anagrams, the method returns true.
     */
    private boolean prunedHelper(LetterInventory txt, String word) {
        LetterInventory wrd = new LetterInventory(word);
        LetterInventory temp = txt.subtract(wrd);
        return temp != null;
    }
}

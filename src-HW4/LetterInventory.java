import java.lang.reflect.Array;

/**
 * Robert Moseley
 * Dr. Han
 * EGR227 Data Structures
 * 2/11/2020
 *
 * Class description: This class acts as a helper class for 'Anagram' class.
 * It mostly provides responsibilities related to keeping track of the current
 * letters/words/phrases that are being considered by the Anagram class. This class
 * can do things like initialize a new inventory, add two inventories together,
 * convert the inventory to a string representation, etc.
 */
public class LetterInventory {

    //class constant for the size of the inventory
    public final int ALPHABETSIZE = 26;

    //inventory is public so that the necessary methods can access it
    public int[] inventory;

    /**
     * Constructor: Initializes an empty inventory when not provided with any data.
     */
    public LetterInventory() {
        inventory = new int[ALPHABETSIZE];
    }

    /**
     * Initializes the inventory with the letters in the string that is passed
     * to the constructor when it is called.
     * @param data gives the constructor a string which it processes
     *             in order to populate the inventory with the correct
     *             counts.
     */
    public LetterInventory(String data) {
        inventory = new int[ALPHABETSIZE];
        for (int i = 0; i < data.length(); i++) {
            int count = data.charAt(i);
            if (count > 96 && count < 124) {
                inventory[count - 97]++;
            } else if (count > 64 && count < 91) {
                inventory[count - 65]++;
            }
        }
    }

    /**
     * This method finds out how many of a certain letter are in an inventory.
     * @param letter a character passed to the method to be used to search the inventory.
     * @return returns the number of times the letter is counted by the inventory.
     */
    public int get(char letter) {
        if (!(letter > 64 && letter < 91) && !(letter > 96 && letter < 124)) throw new IllegalArgumentException();
        if (letter > 96 && letter < 124) letter -= 97;
        if (letter > 64 && letter < 91) letter -= 65;
        return inventory[letter];
    }

    /**
     * This method is used to change the value of a letter in the inventory, so it is as if
     * it appears more or less than it used to.
     * @param letter a character passed to the method to be used to search the inventory.
     * @param value the value associated with a specific character in the inventory.
     */
    public void set(char letter, int value) {
        if ((!(letter > 64 && letter < 91) && !(letter > 96 && letter < 124)) || value < 0)
            throw new IllegalArgumentException();

        if (letter > 96 && letter < 124) letter -= 97;
        if (letter > 64 && letter < 91) letter -= 65;
        inventory[letter] = value;
    }

    /**
     * This method simply tallies the total of every value in the inventory array.
     * @return returns the total value of the letters in the inventory.
     */
    public int size() {
        int total = 0;
        for (int i = 0; i < inventory.length; i++) {
            total += inventory[i];
        }
        return total;
    }

    /**
     * Can be used to determine whether or not the inventory is empty.
     * @return returns true or false depending on whether the inventory is empty.
     */
    public boolean isEmpty() {
        for (int i = 0; i < inventory.length; i++) {
            if (inventory[i] != 0) return false;
        }
        return true;
    }

    /**
     * Uses the ASCII values of chars to convert the array into a string
     * of characters that the values correspond to.
     * @return returns a string representation of the inventory array.
     */
    public String toString() {
        StringBuilder inventoryString = new StringBuilder("[");
        for (int i = 0; i < inventory.length; i++) {
            for (int j = 0; j < inventory[i]; j++) {
                inventoryString.append((char) (i + 97));
            }
        }
        inventoryString.append("]");
        return inventoryString.toString();
    }

    /**
     * Adds two different inventories together and returns an entirely new inventory object.
     * @param other a LetterInventory object that is used to add to another inventory
     * @return returns an entirely new Inventory object to be used by main().
     */
    public LetterInventory add(LetterInventory other) {
        LetterInventory temp = new LetterInventory();
        temp.inventory = inventory.clone();
//        temp.inventory = this.inventory;
        for (int i = 0; i < inventory.length; i++) {
            temp.inventory[i] += other.inventory[i];
        }
        LetterInventory sum = temp;
        return sum;
    }

    /**
     * Subtracts two different inventories from each other and returns an entirely new inventory object.
     * @param other a LetterInventory object that is used to subtract from another inventory
     * @return returns an entirely new Inventory object to be used by main().
     */
    public LetterInventory subtract(LetterInventory other) {
        LetterInventory temp = new LetterInventory();
        temp.inventory = inventory.clone();
        for (int i = 0; i < inventory.length; i++) {
            temp.inventory[i] -= other.inventory[i];
            if (other.inventory[i] < 0 || temp.inventory[i] < 0) return null;
        }
        LetterInventory sum = temp;
        return sum;
    }

    /**
     * Used to find the percentage of a certain character in the inventory.
     * @param letter a character passed to the method to be used to search the inventory.
     * @return returns a double that represents the percentage of space that a specific
     * charater occupies in the given inventory.
     */
    public double getLetterPercentage(char letter) {
        if (letter > 96 && letter < 124) {
            letter -= 97;
        } else if (letter > 64 && letter < 91) {
            letter -= 65;
        } else {
            throw new IllegalArgumentException("Incorrect character.");
        }
        if (inventory.length == 0) return 0;

        int total = 0;
        int letterVal = inventory[letter];
        double percentage = 0;
        for (int i = 0; i < inventory.length; i++) {
            total += inventory[i];
        }
        return letterVal / (total / 1.0);
    }
}

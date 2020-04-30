import java.util.*;

/**
 * Robert Moseley
 * Dr. Han
 * Data Structures (EGR227): Homework 3
 * 2/06/2020
 *
 * Class Description:
 * This class is in charge of managing a game of Assassin.
 * The primary responsibilities for this class include:
 * 1) printing the list of alive and dead "players"
 * 2) determining whether the lists contain a specific name
 * 3) Calling the "kill" command in order to move a name from
 *      the list of "alive" players, to the list of dead players.
 * 4) Determining when the game is over by checking to see if there
 *      is only one node left in the alive list.
 * 5) Determining the winner of the game by checking the name of the
 *      last player.
 *
 * AssassinMain is in charge of actually loading the game, and getting
 * input from the player. AssassinManager just acts as an intermediary
 * to perform repetitive functions that would clutter AssassinMain().
 */

public class AssassinManager {

    //Reference to the front node of the kill ring
    private AssassinNode killFront;

    //Reference to the front node of the graveyard (null if empty)
    private AssassinNode graveFront;

    /**
     * The constructor initializes kill ring with the correct names.
     * @param names names refers to the list of names that will be initialized
     *              as the initial kill ring.
     * Pre: throws an IllegalArgumentException if the list is empty or null.
     * Post: The kill ring is initialized with the correct names, and is ready
     *              for modification.
     */
    public AssassinManager(List<String> names) {
        if (names == null || names.isEmpty()) throw new IllegalArgumentException("Bad list.");
        killFront = new AssassinNode(names.get(0));
        AssassinNode current = killFront;
        for (int i = 1; i < names.size(); i++) {
            current.next = new AssassinNode(names.get(i));
            current = current.next;
        }
    }

    /**
     * Prints every node connected to the Kill Ring, in order.
     * If the kill ring is empty, it prints nothing.
     *
     * If the isGameOver() == true, it also prints the win message.
     */
    public void printKillRing() {
        AssassinNode current = killFront;
        while(current != null) {
            if (current.next != null) System.out.println("    " + current.name + " is stalking " + current.next.name);
            if (current.next == null) System.out.println("    " + current.name + " is stalking " + killFront.name);
            current = current.next;
        }

        //prints the game-over message if the there is only one person left
        if (isGameOver()) {
            System.out.println(winner() + " won the game!");
        }
    }

    /**
     * Prints every node connected to the graveyard, in order.
     * If the graveyard is empty, it prints nothing.
     */
    public void printGraveyard() {
        AssassinNode current = graveFront;
        //if (graveFront == null) return;
        while(current != null) {
            System.out.println("    " + current.name + " was killed by " + current.killer);
            current = current.next;
        }
    }

    /**
     * Calls containsHelper, and searches through the Kill Ring list for a name.
     * @param name refers to the specific node data that the method is searching for.
     * @return returns true if the name is found, false if not.
     */
    public boolean killRingContains(String name) {
        AssassinNode current = killFront;
        return containsHelper(current, name);
    }

    /**
     * Calls containsHelper, and searches through the graveyard list for a name.
     * @param name refers to the specific node data that the method is searching for.
     * @return returns true if the name is found, false if not.
     */
    public boolean graveyardContains(String name) {
        AssassinNode current = graveFront;
        return containsHelper(current, name);
    }

    /**
     * @param current is the first node that the loops begins on.
     * @param name is the name that the loop is searching for.
     * @return returns true if the name is found in the linkedlist, false if otherwise.
     */
    private boolean containsHelper(AssassinNode current, String name) {
        while(current != null) {
            if (current.name.equalsIgnoreCase(name)) return true;
            current = current.next;
        }
        return false;
    }

    /**
     * A simple method that is used to determine when the game is over.
     * Pre: Set to false by default, so game does not immediately end.
     *      If killFront.next != null, the game does not end. The game can only
     *      end if there is one node left in the kill ring.
     * @return: returns true when the game is over–when there is only one element in the list.
     */
    public boolean isGameOver() {
        if (killFront.next == null) return true;
        return false;
    }

    /**
     * This method is called when the game is over, and simply gets the
     * name of the last node in the list.
     * Pre: Cannot execute if the game is still running–returns null.
     * @return returns the name data of the last node in the Kill Ring.
     */
    public String winner() {
        if (!isGameOver()) return null;
        return killFront.name;
    }

    /**
     * Removes a name from the linked list and joins it to the graveyard list
     * along with the name of the "killer" of each node.
     * Pre: Throws an IllegalArgumentException if the game is over.
     *      Throws an IllegalArgumentException if the kill ring does not contain
     *      the given name.
     * Post: name is removed from the list and the previous name
     *      is connected to the next name in the list.
     *      The name that was removed is connected to graveyard,
     *      along with the name of the "killer," which is used to
     *      print the correct output in printGraveyard().
     */
    public void kill(String name) {
        if (isGameOver()) throw new IllegalStateException("The game is already over!");
        if (!killRingContains(name)) throw new IllegalArgumentException("Name is not in ring!");

        AssassinNode current = killFront;
        AssassinNode current2 = killFront;
        String lastName = "";

        //finds the name of the last node in the list
        //this name is used to find who killed the first node,
        //if ever the first node is "killed"
        while (current2 != null) {
            if (current2.next == null) lastName = current2.name;
            current2 = current2.next;
        }

        //if the name is at the front of the list, killFront is reassigned
        //this handles front-cases
        if (killFront.name.equalsIgnoreCase(name)) {
            AssassinNode temp = killFront;
            killFront = killFront.next;
            temp.next = graveFront;
            graveFront = temp;
            graveFront.killer = lastName;
            return;
        }

        //this handles all other cases
        while (current.next != null) {
            if (current.next.name.equalsIgnoreCase(name)) {
                AssassinNode temp = current.next;
                current.next = current.next.next;
                temp.next = graveFront;
                graveFront = temp;
                graveFront.killer = current.name;
            } else {
                current = current.next;
            }
        }
    }

    //////// DO NOT MODIFY AssassinNode.  You will lose points if you do. ////////
    /**
     * Each AssassinNode object represents a single node in a linked list
     * for a game of Assassin.
     */
    private static class AssassinNode {
        public final String name;  // this person's name
        public String killer;      // name of who killed this person (null if alive)
        public AssassinNode next;  // next node in the list (null if none)
        
        /**
         * Constructs a new node to store the given name and no next node.
         */
        public AssassinNode(String name) {
            this(name, null);
        }

        /**
         * Constructs a new node to store the given name and a reference
         * to the given next node.
         */
        public AssassinNode(String name, AssassinNode next) {
            this.name = name;
            this.killer = null;
            this.next = next;
        }
    }
}

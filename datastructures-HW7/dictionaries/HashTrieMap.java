package datastructures.dictionaries;

import java.io.ObjectInput;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import egr221a.exceptions.NotYetImplementedException;
import egr221a.interfaces.trie.BString;
import egr221a.interfaces.trie.TrieMap;

/**
 * See egr221a/interfaces/trie/TrieMap.java
 * and egr221a/interfaces/misc/Dictionary.java
 * for method specifications.
 */

/**
 * See egr221a/interfaces/worklists/LIFOWorkList.java
 * for method specifications.
 *
 * Robert Moseley
 * 4/12/2020
 *
 * This class implements the HashTrieMap data structure
 * which is just a type of Trie. Its purpose is to make
 * it incredibly fast and easy to look up words, and to
 * check for the existence of prefixes for any given words.
 * Each node in this trie is its own HashMap, that either stores
 * values or points to another node further down the trie.
 * Nodes that contain values are typically considered the last
 * node in the trie, which is how it is determined that the end
 * of the trie has been reached.
 *
 * The removal method for this class utilizes recursive backtracking
 * to systematically remove each node in the branch until all necessary
 * conditions have been satisfied–essentially when the entire branch has been
 * removed, and the offending word no longer appears inside the trie.
 *
 **/
public class HashTrieMap<A extends Comparable<A>, K extends BString<A>, V> extends TrieMap<A, K, V> {

    //the starting node, this should never contain a value -> just pointers
    // to starting nodes for other branches.
    public HashTrieNode root;

    //keep track of the size of the trie.
    public int size = 0;

    /**
     * Node class for the HashTrieMap.
     * Contains two data fields:
     *      1) Value: this just contains the data of the node
     *      2) Pointers: This is a reference to the next node in the Trie.
     * This class also contains an iterator override for iterating over the
     * given keys, which is used to access nodes.
     * The constructor for this class takes a value as a parameter
     * and initializes the class value with the parameter. Pointers
     * is initialized as a new HashMap object, but with generic values.
     */
    public class HashTrieNode extends TrieNode<Map<A, HashTrieNode>, HashTrieNode> {
        public HashTrieNode() {
            this(null);
        }

        public HashTrieNode(V value) {
            this.pointers = new HashMap<A, HashTrieNode>();
            this.value = value;
        }

        @Override
        public Iterator<Entry<A, HashTrieNode>> iterator() {
            return pointers.entrySet().iterator();
        }
    }

    /**
     * Constructor for this class. Initializes the root to a new node.
     * @param KClass the inherited class that helps with typecasting and generics.
     * Pre: No objects have been instantiated or initialized.
     * Post: A root has been instantiated, and the triemap now exists as a usable
     *               object in this class.
     */
    public HashTrieMap(Class<K> KClass) {
        super(KClass);
        this.root = new HashTrieNode();
    }


    /**
     * Simple getter method to retrieve the size of the trie.
     * @return returns the current size of the trie, which is just the
     * number of elements that the trie currently contains.
     */
    @Override
    public int size() {
        return this.size;
    }

    /**
     * This method inserts new nodes into the trie and inserts the
     * specified value at the end of a branch.
     * @param key
     *            key with which the specified value is to be associated
     * @param value
     *            value to be associated with the specified key
     * @return returns the value that is inserted into the trie.
     * Pre: Does not yet contain the given value.
     * Post: The trie contains the given value at the end of the specified
     * key's branch.
     */
    @Override
    public V insert(K key, V value) {

        if (key == null || value == null) throw new IllegalArgumentException("Key is null");

        Iterator itr = key.iterator();
        HashTrieNode current = root;

        while (itr.hasNext()) {
             Object next = itr.next();
             if (!current.pointers.containsKey(next)) {
                 current.pointers.put((A) next, new HashTrieNode());
                 current = current.pointers.get(next);
             }  else {
                 current = current.pointers.get(next);
             }
        }

        if (current.value == null) {
            current.value = value;
            this.size++;
        }


        return current.value;
    }

    /**
     * Simple method that locates the value associated with the given key.
     * @param key
     *            the key whose associated value is to be returned
     * @return returns the value assoicated with the given key.
     * Post: The trie is not modified in any way by this method.
     */
    @Override
    public V find(K key) {

        if (key == null) throw new IllegalArgumentException("Key is null.");

        Iterator itr = key.iterator();
        HashTrieNode current = root;
        while (itr.hasNext()) {
            Object next = itr.next();
            if (current.pointers.containsKey(next)) {
                current = current.pointers.get(next);
            } else {
                return null;
            }
        }
        return current.value;
    }

    /**
     *  Simple method to determine whether or not the given prefix is in the
     *  trie or not. True if yes, false otherwise.
     * @param key represents the key that is being searched for in the trie
     * @return returns true if the prefix of the key was found, false otherwise
     */
    @Override
    public boolean findPrefix(K key) {
        if (key == null) throw new IllegalArgumentException("Key is null.");

        HashTrieNode current = root;
        Iterator itr = key.iterator();
        while (itr.hasNext()) {
            Object next = itr.next();
            if (!current.pointers.containsKey(next)) {
                return false;
            }
            current = current.pointers.get(next);
        }
        return true;
    }

    /**
     * Deletes a key from the trie map using the given key.
     * @param key key whose mapping is to be removed from the map
     * Pre: The map has all of the current keys and values.
     * Post: A designated key has been removed from the trie, along
     *            with its value, and the tree has been shifted to
     *            accommodate this removal.
     */
    @Override
    public void delete(K key) {

        if (key == null) throw new IllegalArgumentException("Key is null.");

        //recursive
        if (key.size() > 0)  {
            deleteHelper(key, root, root, key.iterator());
        }
    }

    /**
     * This is a recursive helper method for 'delete' that traverses
     * to the bottom of the given branch, and then deletes all
     * nodes that need to be deleted on the way back up the tree.
     * This method utilizes recursive backtracking to determine whether
     * a node should be deleted.
     * @param key the key that is currently being iterated over
     * @param current the current node that is being accessed
     * @param previous the previous node that was accessed
     * @param itr the iterator that iterates over the characters
     *            in the key
     */
    private void deleteHelper(K key, HashTrieNode current, HashTrieNode previous, Iterator<A> itr) {
        //base case: we have reached the end of the key, we can delete
        A next = itr.next();

        if (!itr.hasNext() && current.pointers.containsKey(next) && current.pointers.get(next).value != null) {
            this.size--;
//            previous.value = current.value;
//            current.pointers.put(null, null);
            current.pointers.get(next).value = null;
            if (current.pointers.get(next).pointers.isEmpty()) {
                current.pointers.remove(next);
            }
        } else {
            if (itr.hasNext()) {
                previous = current;
                current = current.pointers.get(next);
                deleteHelper(key, current, previous, itr);
                if (previous.pointers.get(next).pointers.isEmpty() && previous.pointers.get(next).value == null) {
                    previous.pointers.remove(next);
                }
            }
        }
    }

    /**
     * Resets the state of the map as if the constructor was just called
     * by clearing the root node of any dependencies and then making the root
     * node into a new node which begins an entirely new trie.
     */
    @Override
    public void clear() {
        size = 0;
        root.pointers = null;
        this.root = new HashTrieNode();
    }
}

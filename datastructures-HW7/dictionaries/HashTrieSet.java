package datastructures.dictionaries;

import egr221a.exceptions.NotYetImplementedException;
import egr221a.interfaces.trie.BString;
import egr221a.interfaces.trie.TrieSet;

import java.util.HashMap;

/**
 *
 * Robert Moseley
 * 4/12/2020
 *
 * This class implements the HashTrieSet data strucuture.
 * This data structure is nearly identical to that of
 * HashTrieMap. For more details on implementation please
 * see HashTrieMap.java, which details the core methods
 * and features of this class. Essentially, HashTrieSet
 * is just a pared down version of HashTrieMap, because
 * it only contains values and not keys. HashTrieSet
 * implements HashTrieMap, which is why it has access
 * to all of the same, identical features.
 */

public class HashTrieSet<A extends Comparable<A>, E extends BString<A>> extends TrieSet<A, E> {
    /**
     * This method calls super to instantiate a new
     * HashTrieSet which is almost exactly the same as
     * the HashTrieMap. This is an efficient use of code
     * to create the set class.
     * @param Type this is the given class type that is
     *             extended to the instantiation of the
     *             HashTrieMap.
     * Pre: No set has been created.
     * Post: The set has been instantiated by calling the super,
     *             HashTrieMap.
     */
    public HashTrieSet(Class<E> Type) {
        super(new HashTrieMap<>(Type));
    }
}

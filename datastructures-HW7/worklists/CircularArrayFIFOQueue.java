package datastructures.worklists;

import egr221a.exceptions.NotYetImplementedException;
import egr221a.interfaces.worklists.FixedSizeFIFOWorkList;

import java.io.ObjectInput;
import java.util.NoSuchElementException;

/**
 * See egr221a/interfaces/worklists/FixedSizeLIFOWorkList.java
 * for method specifications.
 *
 * Robert Moseley
 * 3/28/2020
 *
 * CircularArrayFIFOQueue implementation
 * This class implements the methods necessary to create
 * a CircularArrayFIFOQueue data structure. At its core,
 * this data structure is an array, but through a series of
 * methods the functionality has been modified to provide
 * all of the expected performance capabilities of
 * the desired data structure which is O(1) time complexity
 * and ease of use.
 */

public class CircularArrayFIFOQueue<E> extends FixedSizeFIFOWorkList<E> {

                                           //private data fields to keep track of:
    private final int CAPACITY;            //      1) the max capacity of the array
    private Object[] arr;                  //      2) the main array object of this class
    private int index = -1;                //      3) the index of the array that is currently
                                           //          being accessed by the below methods.
    /**
     * Instantiates the main array with size 'CAPACITY'.
     * @param capacity represents the max capacity of the array.
     * Pre: No data structures have been instantiated.
     * Post: The main structure for this class, the array 'arr',
     *                 has been instantiated with size 'CAPACITY'.
     */
    public CircularArrayFIFOQueue(int capacity) {
        super(capacity);
        this.CAPACITY = capacity;
        arr = new Object[CAPACITY];
    }

    /**
     * This method adds the work to the end of the array,
     * unless the array is full. If full, the array throws
     * an exception to stop the method.
     * @param work the work to add to the worklist
     * Pre: The method has size n and the expected elements.
     * Post: The method has size n + 1, and the new element
     *             that is given by the value 'work'.
     */
    @Override
    public void add(E work) {
        if (index > CAPACITY - 2) throw new IllegalStateException("Buffer is full.");
        index++;

        //set the current index equal to the given data
        arr[index] = work;
    }

    /**
     * This method retrieves the first element in the array,
     * if null it throws an exception.
     * @return returns the first value in the array.
     */
    @Override
    public E peek() {
        if (arr[0] == null) throw new NoSuchElementException("No elements in list.");
        return (E) arr[0];
    }

    /**
     * This methods returns the value of the array at the given
     * ith position of the array. If the first element of the array
     * is null this method throws an exception.
     * @param i the index of the element to peek at
     * @return returns the value of the array at the ith position.
     */
    @Override
    public E peek(int i) {

        if(arr.length == 0) throw new ArrayIndexOutOfBoundsException("Array length is 0. Very wow.");
        if (arr[0] == null) throw new NoSuchElementException("No elements in list.");

        return (E) arr[i];
    }

    /**
     *
     * @return returns the first element in the array.
     * Pre: The array has size n, with the expected values.
     * Post: The array has size n - 1. The first element is removed,
     * and every following value is shifted to the left to fill the
     * hole left by removing the first element.
     */
    @Override
    public E next() {
        //exception for when there are no next elements in list.
        if (arr[0] == null) throw new NoSuchElementException("No next element in list.");

        E data = (E) arr[0];
        arr[0] = null;

        //shift the elements to the left to fill the hole
        for (int i = 0; i < index; i++) {
            arr[i] = arr[i + 1];
        }

        index--;
        return data;
    }

    /**
     * When this method is called, the array is updated at
     * index 'i' with value 'value'.
     * @param i represents that index of the array that is
     *          being updated with this method call.
     * @param value represents the value that is replacing
     *              the old value at index i.
     * Pre: The array has all of the expected values.
     * Post: One of the values in the array has been replaced
     *              with the given value.
     */
    @Override
    public void update(int i, E value) {
        arr[i] = value;
    }

    /**
     * Tracks the number of elements in the array and throws
     * an exception if the array is null.
     * @return returns the size of the array by adding 1 to the value
     * of the current index.
     */
    @Override
    public int size() {
        if (arr == null) throw new NoSuchElementException("No elements in list.");
        return index + 1;
    }

    /**
     * Resets the index to -1 and creates a new array object
     * with the given capacity.
     * Pre: The array has the given set of values.
     * Post: The array is now a new, empty array with no values
     * and index is once again -1, signifying that there are no values
     * in the array.
     */
    @Override
    public void clear() {
        index = -1;
        arr = new Object[CAPACITY];
    }

    /**
     * @param other accepts a different worklist to compare the root to.
     * @return returns an integer that provides information about the differences
     * in the lists.
     */
    @Override
    public int compareTo(FixedSizeFIFOWorkList<E> other) {
       if (this.capacity() == other.capacity()) {
           if (this.size() == other.size()) {
               return 0;
           } else {
               return this.size() - other.size();
           }
       } else {
           return this.capacity() - other.capacity();
       }
    }

    /**
     * @param obj this is an object that the root array is being compared to.
     * @return returns whether or not the main array and the given parameter
     * object are equal or not.
     */
    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object obj) {
        if (this.arr == obj) {
            return true;
        }
        else if (!(obj instanceof FixedSizeFIFOWorkList<?>)) {
            return false;
        }
        else {
            FixedSizeFIFOWorkList<E> other = (FixedSizeFIFOWorkList<E>) obj;
            return compareTo(other) == 0;
        }
    }

    /**
     * This methods retrieves the hashcode at the next index
     * that will be accessed.
     * @return returns the hashcode at the current index.
     */
    @Override
    public int hashCode() {

        Object n = arr[index];
        return n.hashCode();

    }
}

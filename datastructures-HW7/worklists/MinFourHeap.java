package datastructures.worklists;

import egr221a.interfaces.worklists.PriorityWorkList;
import egr221a.exceptions.NotYetImplementedException;

import java.util.NoSuchElementException;

/**
 * See egr221a/interfaces/worklists/PriorityWorkList.java
 * for method specifications.
 *
 *
 * Robert Moseley
 * MinFourHeap Implementation
 * 4/1/2020
 *
 * This class implements a 4-heap data structure
 * with its minimum value at the top of the structure.
 * It takes advantage of percolating up and down the
 * structure to improve performance. At its core this
 * data structure is an array, but through a series of
 * calculations, the array is able to behave as a tree
 * by carefully indexing each element in the array to
 * correspond with other elements. There is a defined
 * parent-child node system, which makes traversal and
 * access incredibly quick.
 *
 */

public class MinFourHeap<E extends Comparable<E>> extends PriorityWorkList<E> {
    /* Do not change the name of this field; the tests rely on it to work correctly. */
    private E[] data;

    //private ints to keep track of the size of the array and what index is currently being accessed
    private int defaultSize = 256;
    private int index = 0;

    /**
     * Constructor initializes the data array with the default size of 256.
     * Pre: No data structures have been initialized for use.
     * Post: The main data structure has been initialized and is ready to be modified.
     */
    public MinFourHeap() {
        data = (E[]) new Comparable[defaultSize];
    }

    /**
     * Simple method to check the first element of the heap.
     * If it has 'work' in it, the method returns true,
     * otherwise it returns false.
     * @return whether or not the heap still has any elements in it
     */
    @Override
    public boolean hasWork() {
        return data[0] != null;
    }

    /**
     * This methods adds work to the end of the heap, then
     * depending on the size of the 'work', it is percolated up
     * until the work is correctly sorted into the heap.
     * @param work the work that is being added to the heap.
     * Pre:
     */
    @Override
    public void add(E work) {

        //calls the helper method to check and double the array if necessary
        //otherwise increases the index counter to account for a new item being added
        if (index == defaultSize - 1) doubleArray();

        //finds the index of any given parent or child node given the current index
        int parentIndex = (index - 1) / 4;

        //add node to end of heap
        if(data[index] == null) data[index] = work;

        int tempIndex = index;

        //percolate up to correctly order the heap
        percolateUp(parentIndex);

        //set the index variable to the correct value for the next method call
        //resetIndex();
        index = tempIndex;
        index++;
    }

    /**
     * Simple method to retrieve the data at the top of the heap, but not modify the heap
     * in any way. Just returns the value at the first index.
     * @return returns the 'work' contained at index 0 of the heap (the top of the heap).
     */
    @Override
    public E peek() {
        if (data[0] == null) throw new NoSuchElementException("There are no elements in the heap.");
        return data[0];
    }

    /**
     * This methods removes the first node from the heap, replaces it
     * with data from the bottom of the heap, and then percolates until
     * the array is sorted correctly.
     * @return returns the first node in the heap
     * Pre: The heap has a value at the zero index.
     * Post: The heap has a new value at the zero index,
     * and the heap at 'index' has been replaced with null.
     */
    @Override
    public E next() {
        if (data[0] == null) throw new NoSuchElementException("There are no elements in the heap.");
        if (index > 0) index--;
        if (index < 0) index = 0;
        int tempIndex = index;

        E nextVal = data[0];

        //set the top node to null
        data[0] = null;

        //fill the hole with the last child
        data[0] = data[index];
        data[index] = null;

        //reset the index to 0
        index = 0;

        //index of the smallest child node
        int smallestNode = smallest();

        //percolate down to restore order and structure to the heap
        percolateDown(smallestNode);

        //set the index variable to the correct value for the next method call
        index = tempIndex;

        //return the value from the top of the heap
        return nextVal;
    }

    /**
     * Uses the index global variable, which keeps track of the elements in the stack,
     * to return the size of the array stack.
     * @return returns the size of the array stack
     */
    @Override
    public int size() {
        if (data == null) throw new IllegalStateException("There is no heap");

        //returns the current index + 1 to adjust for the index being one less than the actual size
        return index;
    }

    /**
     * Instantiates a new array object and discards the old one.
     * Pre: Takes in the current array, unchanged.
     * Post: Creates a new empty array object with no values
     * set to the size of the global 'defaultSize' int.
     */
    @Override
    public void clear() {
        defaultSize = 256;
        index = 0;
        data = (E[]) new Comparable[defaultSize];
    }

    /**
     * Method to percolate up the tree after a node has been added to the heap.
     * When a node is added, it is added to the bottom of the heap and there is
     * no guarantee this is in the correct index. This method moves the node up
     * the heap until it is in the correct, sorted position.
     * @param parentIndex the index of the parent node that is being modified.
     * Pre: The tree has a potentially dangling node at the bottom, that is not
     *                    in the correct position.
     * Post: The heap is in the correct position and the nodes are in sorted order.
     */
    private void percolateUp(int parentIndex) {
        while (data[index].compareTo(data[parentIndex]) < 0) {
            //create a temp variable for swapping between indices
            E temp = data[index];

            //swap the values of the two indices
            data[index] = data[parentIndex];
            data[parentIndex] = temp;

            //reset the values of index variables for the next iteration
            index = parentIndex;
            parentIndex = (index - 1) / 4;

        }
    }

    /**
     * Method to percolate nodes down the heap and restore the correct order
     * of the heap (minimum = top, max = bottom)
     * @param smallestNode represents the smallest node that is being percolated
     * Pre: Takes in the heap with a new value at the top of the heap.
     * Post: That node is been placed into the correct index and the heap
     *                     is ordered correctly.
     */
    private void percolateDown(int smallestNode) {
        while(smallestNode < defaultSize && data[0] != null && data[index].compareTo(data[smallestNode]) > 0) {
            //create a temp variable for swapping between indices
            E temp = data[index];

            //swap the values of the two indices
            data[index] = data[smallestNode];
            data[smallestNode] = temp;

            //reset the values of index variables for the next iteration
            index = smallestNode;
            smallestNode = smallest();
        }
    }

    /**
     * Helper method for doubling the size of the heap
     * and manually copying the values correctly from the
     * new, temporary array back into the main array that is
     * now doubled in size.
     */
    private void doubleArray() {
        //double the size of the array
        defaultSize *= 2;

        //create a temporary array to store the values of data
        //while data is turned into a new array of a larger size
        E[] tempArray = (E[]) new Comparable[defaultSize];
        for (int i = 0; i < data.length; i++) {
            tempArray[i] = data[i];
        }

        //manually copy the values of the temp array
        //into the new version of the main array
        data = (E[]) new Comparable[defaultSize];
        for (int i = 0; i < tempArray.length; i++) {
            data[i] = tempArray[i];
        }
    }

    /**
     * Helper method to find the smallest child node while percolating up
     * to make sure that the smallest node is the one is replaced
     * to maintain the correct order of the heap.
     * @return returns the smallest child node
     */
    private int smallest() {
        //main index for tracking where the smallest node is
        int smallNode = index;

        //finds the smallest node out of the 4 child nodes
        for (int i = 1; i <= 4; i++) {
            int childNode = 4 * index + i;
            if (childNode < data.length && data[smallNode] != null && data[childNode] != null) {
                if (data[smallNode].compareTo(data[childNode]) > 0) {
                    smallNode = childNode;
                }
            }
        }
        //returns the smallest child node to be used for percolation
        return smallNode;
    }
}

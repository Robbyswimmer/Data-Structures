package datastructures.worklists;

import egr221a.interfaces.worklists.LIFOWorkList;

import java.util.NoSuchElementException;

/**
 * See egr221a/interfaces/worklists/LIFOWorkList.java
 * for method specifications.
 *
 * Robert Moseley
 * 3/27/2020
 *
 * ArrayStack Implementation:
 * This class implements an ArrayStack data structure
 * by using an array as a building block. All of the
 * methods are implemented to modify the array and its
 * overall functionality. The result is an ArrayStack
 * with all of the necessary methods to achieve
 * a desirable time complexity.
 */

public class ArrayStack<E> extends LIFOWorkList<E> {

    //Private data fields to track important markers:
    //default capacity is adjusted when the array runs out
    //of space. Arr is instantiated as a new object every time
    //the array runs out of space. Int numElements is used to track
    //the number of elements that are added and subtracted from the
    //array. It makes keeping track of size easy, and also always for
    //optimization of algorithms. With the addition of numElements,
    //loops only have to loop through the number of elements +- 1,
    //instead of looping through the entire size of the array.
    private int defaultCapacity = 10;
    private Object[] arr;
    private int numElements = 0;

    /**
     * Constructor for ArrayStack: Instantiates a new array object
     * of size defaultCapacity (which is 10 initially).
     * Pre: No data structures have been created yet and there is no
     * way to track data in the class.
     * Post: A new array has been instantiated with the given default
     * size.
     */
    public ArrayStack() {
        arr = new Object[defaultCapacity];
    }

    /**
     * This method keeps track of adding elements to the array, and,
     * if need be, doubling the size of the array so that more elements
     * can be added. Adding works by adding to the end of the array.
     * @param work work is the data that needs to be added to the end (top) of the array
     * Pre: An array is given along with an object 'work'.
     * Post: The given object is added to the array, and the array
     *             is double in size if necessary.
     */
    @Override
    public void add(E work) {
        //increment the number of elements because we are adding an element;
        numElements++;
        if (numElements > arr.length) doubleArray();
        this.arr[numElements - 1] = work;
    }

    /**
     * Simple method to retrieve the last element in the array.
     * Does not modify the array in any way.
     * @return returns the last element in the array.
     */
    @Override
    public E peek() {
        if (arr[0] == null) throw new NoSuchElementException("No elements in list.");
        return (E) arr[numElements - 1];
    }

    /**
     * This method is in charge of removing elements from the end of the array,
     * and the other elements in the array are left untouched.
     * @return returns the first element in the array and removes it from the array.
     * Pre: Takes in an array with all of its elements.
     * Post: Removes the last element from the array, and returns its value.
     */
    @Override
    public E next() {
        if (arr[0] == null) throw new NoSuchElementException("There is no next!");
        E data = (E) arr[numElements - 1];
        arr[numElements - 1] = null;
        numElements--;
        return data;
    }

    /**
     * Simple method to return the number of elements in the array.
     * @return returns the number of elements left in the array.
     * This is accomplished by keeping track of a local variable,
     * that is modified by the other methods in this class.
     * Calling array.length only returns the size of the array,
     * not the number of elements in the array.
     */
    @Override
    public int size() {
        if (arr == null) throw new NoSuchElementException("There is no elements in the array.");
        return numElements;
    }

    /**
     * Creates a new empty array, which is faster than clearing
     * all of the elements from the current array.
     * Pre: 'arr', the main array, is given with all values.
     * Post: arr's values have been discarded and arr has been
     * instantiated as a new array object with size = default capacity.
     * Also resets the number of elements to zero because the array
     * is starting from scratch.
     */
    @Override
    public void clear() {
        numElements = 0;
        arr = new Object[defaultCapacity];
    }

    private void doubleArray() {
        //double the size of the array
        defaultCapacity *= 2;
        Object[] arr2 = new Object[defaultCapacity];

        //copy all of the old elements into the new array
        for (int i = 0; i < arr.length; i++) {
            arr2[i] = arr[i];
        }
        this.arr = new Object[defaultCapacity];

        //copy the new array into the original array
        for (int i = 0; i < arr2.length; i++) {
            this.arr[i] = arr2[i];
        }
    }
}

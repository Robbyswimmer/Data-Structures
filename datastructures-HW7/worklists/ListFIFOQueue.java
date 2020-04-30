package datastructures.worklists;

import egr221a.interfaces.worklists.FIFOWorkList;

import java.util.NoSuchElementException;

/**
 * See egr221a/interfaces/worklists/FIFOWorkList.java
 * for method specifications.
 *
 * Robert Moseley
 * 3/26/2020
 *
 * ListFIFOQueue Implementation
 * This class implements the methods necessary to create
 * a ListFIFOQueue data structure. At its core,
 * this data structure is a LinkedList, but through a series of
 * methods the functionality has been modified to provide
 * all of the expected performance capabilities of
 * the desired data structure which is O(1) time complexity
 * and ease of use. To improve performance and efficiency,
 * both the front and the tail of the linked list are tracked
 * which gives the data structure all of the expected features.
 */
public class ListFIFOQueue<E> extends FIFOWorkList<E> {

    private ListNode<E> front;
    private ListNode<E> tail;
    private int counter = 0;

    /**
     * Implement my own node class for the linked list
     * Contains two data fields: 1) data – which is the
     * data associated with each node, and 2) next – which
     * is the next associated node in the list.
     *
     * This class has one constructor which creates one new
     * node with the data given to it. There is currently no
     * constructor for nodes with no given data.
     */
    private static class ListNode<E> {

        public E data;
        public ListNode next;

        public ListNode(E data) { this.data = data; }
    }

    /**
     * Constructor for this class. This builds a linked list
     * with an inital size of 0. This constructor simply builds
     * an empty linked list.
     * Pre: The list does not exist.
     * Post: The list has been instantiated with a size of 0 by
     * not creating any new objects. The methods of this class
     * are used to create the objects of the list, not the
     * constructor.
     */
    public ListFIFOQueue() { }

    /**
     * This method takes in a generic data in the form of the variable 'work'
     * and adds the data to the end of the list. It checks three different
     * cases: 1) if the list is empty it makes front equal to a new listnode,
     * 2) if the list has only 1 node, it makes the second node the new data,
     * and 3) if the list has 2 or more elements in it, the data is simply
     * added to the end of the list.
     * @param work this is just the data that is being added to the list
     * Pre: The list does not have the data that is being added.
     * Post: The list has the new data in it. If the list had a size of 0 before
     *             it now has a size of 1.
     */
    @Override
    public void add(E work) {
        counter++;
        if (front == null) {
            //case 1: the front of the list is null
            front = new ListNode<>(work);
            tail = front;
        } else  {
            //case 2: the front of the list is not null
            tail.next = new ListNode(work);
            tail = tail.next;
        }
    }

    /**
     * A method to get the data from the front of the list,
     * a NoSuchElementException is thrown if there are no nodes
     * in the list.
     * @return simplys returns the data associated with the node
     * at the front of the list.
     * Pre: The data the front the list has not been checked.
     * Post: Either the data from the front of the list is
     * returned, or an exception is thrown. The list is unchanged
     * by calling this method.
     */
    @Override
    public E peek() {
        if (front == null) throw new NoSuchElementException();
        return front.data;
    }

    /**
     * This method removes and returns the first node in the list.
     * If the list is empty a NoSuchElementException is thrown,
     * otherwise the data from the front of the list is returned
     * and front is moved to the next node in the list in order
     * to disassociate the first node and remove it from the list.
     * @return removes and returns the first element in the list.
     * Pre: The list still contains the node that will be returned.
     * Post: The first node has been removed, and its data has been
     * returned to the user.
     */
    @Override
    public E next() {
        //case 1: front is null and no elements exist in the list
        if (front == null) throw new NoSuchElementException();

        //decrement the counter because we are removing an element
        counter--;

        //case 2: There is at least one element in the list, so the first element is removed;
        E data = (E) front.data;
        front = front.next;
        return data;
    }

    /**
     * Simple method that loops through the list until it finds a null value,
     * once a null value is established, the number of iterations is returned
     * which represents the size of the list.
     * @return returns the number of elements in the list currently.
     */
    @Override
    public int size() {
        return counter;
    }

    /**
     * This method takes the list and disassociates the front node from
     * the list and makes the front point to a new, null list.
     * The old list is not deleted, it is just removed from the current
     * queue.
     * Pre: The list is utilized with the current values.
     * Post: The list has been removed, and front now points to
     * a null value which allows a new list to be created.
     */
    @Override
    public void clear() {
        front = null;
        counter = 0;
    }
}

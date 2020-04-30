import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/**
 * Robert Moseley
 * EGR227 - Homework 1 (HTML Validator)
 * Dr. Han
 * 1/22/2019
 *
 * Description: Write a program that assesses a page of HTML for inaccuracies
 * in the closing tags, and determines whether or not given HTML code is valid.
 * The program reports errors in the HTML.
 * The only auxiliary storage systems used are stacks and queues.
 * This class contains methods that can add tags to the queue, remove tags of a
 * specific type, and return the total tags in the queue at a given time.
 */
public class HtmlValidator {

    private Queue<HtmlTag> q = new LinkedList<>();

    //constructor #1 – initializes to store an empty queue of tags
    public HtmlValidator() {

        //initializes the queue as an empty queue (LinkedList)
        this.q = new LinkedList<>();
    }

    //constructor #2 – initializes to store a separate copy of the queue of tags
    public HtmlValidator(Queue<HtmlTag> tags) {
        if (tags == null) throw new IllegalArgumentException("Queue is null");

        //initializes the queue with the tags in the constructor head
        this.q = tags;
    }

    //adds the given tag to the end of the validator queue
    public void addTag(HtmlTag tag) {
        if (tag == null) throw new IllegalArgumentException("Tag is null");

        //adds a tag to the public queue declared at the top of the class
        q.add(tag);
    }

    //return the validators queue of HtmlTags
    //the queue contains all of the tags that were passed to the constructor
    //should reflect any changes made by addTag or removeAll
    public Queue<HtmlTag> getTags() {
        return q;
    }

    //removes any tags from the validator queue that contain the given element
    public void removeAll(String element) {

        //throws illegal argument exception if given element is actually null
        if (element == null) throw new IllegalArgumentException("Tag is null");
        int size = q.size();

        //loops through all of the tags in the queue and compares them to
        //the given elements. If the tag matches the element it is permanently
        //removed from the queue, and the loop moves to the next tag.
        for (int i = 0; i < size; i++) {
            HtmlTag tag = q.peek();
            if (tag.getElement().equals(element)) {
                q.remove(tag);
            } else {
                q.remove(tag);
                q.add(tag);
            }
        }
    }

    //prints an indented text representation of the html tags in the queue
    //and returns errors if unexpected or unclosed tags are found in a given
    //HTML file. This method calls the helper method, printHTML, which is
    //responsible for printing the HTML and indentations.
    public void validate() {
        Stack<HtmlTag> tagStack = new Stack<>();
        int indentCounter = 0; // a counter for the number of indentations to print
        for (int i = 0; i < q.size(); i++) {
            HtmlTag tag = q.remove();

            //checks to see if a tag is an opening tag that requires a closing tag
            if (tag.isOpenTag() && !tag.isSelfClosing()) {
                tagStack.push(tag);
                //produce the indentation and print the correct tag
                printHtml(indentCounter, tag);
                //increment the indentation counter to produce another indentation
                indentCounter++;
            }

            //prints self-closing, open tags. This protects against occurrences of </br> mainly.
            if (tag.isSelfClosing() && tag.isOpenTag()) printHtml(indentCounter, tag);

            //checks for closing tags that haven't been added to the stack
            //but are errors because they do not have their counterpart
            if (tagStack.size() == 0 && !tag.isOpenTag())
                System.out.println("ERROR unexpected tag: " + tag);

            //checks to see whether the tags match
            //and makes sure that the stack is not empty
            if (!tag.isOpenTag() && !tagStack.isEmpty()) {
                HtmlTag sTag = tagStack.peek();
                if (sTag.matches(tag)) {
                    tagStack.pop();
                    //decrement the indentation counter so that tags are
                    //printed with the correct spacing
                    indentCounter--;
                    //produce the indentation and print the correct tag
                    printHtml(indentCounter, tag);
                } else if (!sTag.matches(tag)) {
                    System.out.println("ERROR unexpected tag: " + tag);
                }
            }
            q.add(tag);
        }

        //prints the rest of the errors if the stack still has tags in it
        if (tagStack.size() != 0) {
            int size = tagStack.size();
            for (int i = 0; i < size; i++) {
                System.out.println("ERROR unclosed tag: " + tagStack.pop());
            }
        }
    }

    //this is a private helper method that avoids redundancy.
    //its sole responsibility is to print the correct HTML tag
    //and the correct number of indentations.
    private void printHtml(int indentCounter, HtmlTag tag) {
        for (int j = 0; j < indentCounter; j++) {
            System.out.print("    ");
        }
        System.out.println(tag);
    }
}

/**
 * Robert Moseley
 * Dr. Han
 * EGR227: Data Structures
 * 3/02/2020
 *
 * The purpose of this class is to create a game of 20 questions
 * that is capable of becoming better overtime because it can be
 * populated by user generated questions and answers.
 * It utilizes a binary search tree as the main data structure,
 * with each node of the tree representing some piece of information
 * about a question or answer for that game. The game accepts a file
 * of strings which it uses to generate the tree for each game.
 *
 */

import java.io.*;
import java.util.*;

public class QuestionsGame {
    // Your code here
    private QuestionNode root;

    /**
     * Question node class responsible for managing the data fields
     * of each question node.
     */
    private static class QuestionNode {
        // Your code here

        private String question;
        private QuestionNode left;
        private QuestionNode right;

        public QuestionNode(String question) {
            this.question = question;
        }

        public QuestionNode(String question, QuestionNode left, QuestionNode right) {
            this.question = question;
            this.left = left;
            this.right = right;
        }
     }

    /**
     * constructor for creation a single node object
     * @param object String that is translated into a node
     * Pre: The method accepts a string
     * Post: This string is translated into a new question node object
     */
     public QuestionsGame(String object) {
        QuestionNode obj = new QuestionNode(object);
        root = obj;
     }

    /**
     * The constructor which initializes a new game with a full tree of nodes
     * that are used to play the game.
     * @param input a line of text that is translated into a new node on the BST
     */
     //nodes appear in pre-order
     public QuestionsGame(Scanner input) {
        //should call a recursive method to build the tree
         while(input.hasNextLine()) {
             root = makeNodes(input);
         }
     }

    /**
     * Recursive helper for producing the nodes of the BST
     * @param input uses input from the question file to create the tree
     * @return returns an individual node of the tree
     */
     private QuestionNode makeNodes(Scanner input) {
        String letter = input.nextLine();
        String q = input.nextLine();
        QuestionNode node = new QuestionNode(q);
        if (letter.equals("Q:")) {
            node.left = makeNodes(input);
            node.right = makeNodes(input);
        }
        return node;
     }

     //should write the questions to a file in the correct order

    /**
     * Allows new questions to be saved to a file if an answer doesn't already exist.
     * This enables the game to become better over time and improve its number of
     * possible answers.
     * @param output enables new questions to be saved to the existing file
     */
     public void saveQuestions(PrintStream output) {
        if (output == null) throw new IllegalArgumentException("PrintStream is null");
        if (root == null) throw new IllegalArgumentException("The list is null! No questions!");
        saveQuestionsHelper(root, output);
     }

    /**
     * This is the recursive helper for saving new questions.
     * @param node the current question node that is being processed
     * @param output the output that is being saved to the file
     */
     private void saveQuestionsHelper(QuestionNode node, PrintStream output) {
        if (node.left != null && node.right != null) {
            output.println("Q:");
            output.println(node.question);
            saveQuestionsHelper(node.left, output);
            saveQuestionsHelper(node.right, output);
        } else {
            output.println("A:");
            output.println(node.question);
        }
     }

    /**
     * This is the main method in charge of actually executing the game.
     * It takes player input and uses that input to navigate the tree.
     * Pre: The method starts at the top of the tree
     * Post: The method ends at an answer node.
     * @param input this is the scanner that allows the program to process user input
     */
     public void play(Scanner input) {

        //sets the current node equal to the root of the tree
        QuestionNode current = root;
        QuestionNode previous = current;
        boolean branch = false;

        //prepares fields for userInput and a final answer field
        String userInput;
        String answer = root.question;

        //while the branch can still be explored, explore
        while (current.left != null && current.right != null) {

            //print out the current question
            System.out.print(current.question + " (y/n)? ");

            //get an answer to the question, without extra spaces and to lowercase
            userInput = input.nextLine().trim().toLowerCase();

            //if user input starts with y, go left
            //else, go right
            if ((current.right.right == null) || current.left.left == null) previous = current;
            if (userInput.charAt(0) == 'y') {
                current = current.left;
                branch = true;
            } else {
                current = current.right;
                branch = false;
            }

            //answer is set equal to the current answer node.
            //it is determined to be an answer node because
            //the right and left of the node are null
            answer = current.question;
        }

        //guesses the answer, because it is the last node
        System.out.println("I guess that your object is " + answer + "!");
        System.out.print("Am I right? (y/n)? ");

        //gets user input to determine what to do next.
         //if the computer is correct, you lose
         //otherwise generate a new branch with the new question
         //to help the computer learn more options
        userInput = input.nextLine().trim().toLowerCase();

        //if input yes, simply print "Awesome! I (the computer) win!"
        if (userInput.charAt(0) == 'y') {
            //computer won message
            System.out.println("Awesome! I win!");
        } else {
            //get the new question info from the user
            System.out.println("Boo! I Lose.  Please help me get better!");
            System.out.print("What is your object? ");

            //newObj is the new object that the computer couldn't guess
            String newObj = input.nextLine();

            System.out.println("Please give me a yes/no question that distinguishes between "
                    + newObj  + " and " + answer + ".");
            System.out.print("Q: ");

            //generate a question to distinguish between the actual answer and your object
            String newQuestion = input.nextLine();
            System.out.print("Is the answer \"yes\" for " + newObj + "? (y/n)? ");

            //if the answer is yes to the question, put new object on left node,
            //otherwise put it on the right with the original answer on the left
            String newQuestionAns = input.nextLine().trim().toLowerCase();

            //captures the current data stored by the question node
            QuestionNode prevObj = current;
            QuestionNode objNode = new QuestionNode(newObj);

            if (previous == current) {
                if (newQuestionAns.charAt(0) == 'y') {
                   root = new QuestionNode(newQuestion, objNode, prevObj);
                } else {
                    root = new QuestionNode(newQuestion, prevObj, objNode);
                }
            } else {
                if (branch) {
                    if (newQuestionAns.charAt(0) == 'y') {
                        previous.left = new QuestionNode(newQuestion, objNode, prevObj);
                    } else {
                        previous.left = new QuestionNode(newQuestion, prevObj, objNode);
                    }
                } else {
                    if (newQuestionAns.charAt(0) == 'y') {
                        previous.right = new QuestionNode(newQuestion, objNode, prevObj);
                    } else {
                        previous.right = new QuestionNode(newQuestion, prevObj, objNode);
                    }
                }
            }
        }
     }
}

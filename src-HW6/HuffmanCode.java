/**
 * Robert Moseley
 * Dr. Han
 * EGR227: Data Structures
 * 3/03/2020
 *
 * Class description:
 * Provides the necessary tree / heap creation
 * to create the compression code in order to reduce
 * the size of text files – and possibly other files.
 * It also translates from a Bitstream in order to
 * decompress files and make them readable again.
 * The class also saves files the have been changed
 * into new files with the correct file extensions.
 */

import java.io.PrintStream;
import java.util.*;
public class HuffmanCode {

    private HuffmanNode root;

    private static class HuffmanNode implements Comparable<HuffmanNode> {
        public final Character data;
        public final Integer frequency;
        public HuffmanNode left;
        public HuffmanNode right;

        public HuffmanNode() {
            this.data = null;
            this.frequency = null;

        }

        public HuffmanNode(char data, int frequency) {
            this.data = data;
            this.frequency = frequency;
        }

        public HuffmanNode(char data, int frequency, HuffmanNode left, HuffmanNode right) {
            this.data = data;
            this.frequency = frequency;
            this.left = left;
            this.right = right;
        }

        @Override
        public int compareTo(HuffmanNode other) {
            if (this.frequency == null || other.frequency == null)
                throw new NullPointerException("Frequency is null");
            return this.frequency - other.frequency;
        }
    }

    /**
     * Initializes a new HuffmanCode object using
     * an array of frequencies of characters in a file.
     *
     * @param frequencies an array of ascii frequencies for specific characters
     *                    Pre: no Huffman code objects have been built
     *                    Post: All Huffman Code objects have been built using the frequency array
     */
    public HuffmanCode(int[] frequencies) {
        //use a priority Queue to build the HuffmanCode
        HuffmanNode current = root;
        PriorityQueue<HuffmanNode> q = new PriorityQueue<>();
        for (int i = 0; i < frequencies.length; i++) {
            if (frequencies[i] != 0) {
                char currChar = (char) i;
                HuffmanNode temp = new HuffmanNode(currChar, frequencies[i]);
                q.add(temp);
            }
        }
        while (q.size() > 1) {
            //remove the two smallest nodes
            HuffmanNode node1 = q.remove();
            HuffmanNode node2 = q.remove();
            HuffmanNode newNode = new HuffmanNode((char) -1, node1.frequency + node2.frequency, node1, node2);
            q.add(newNode);
        }
        root = q.remove();
    }

    /**
     * Constructs a new Huffman code object by using an already existing
     * .code file
     * Pre: No Huffman Code object has been built, Input contains a file name
     * Post: A Huffman code object has been constructed using the input file
     *
     * @param input a previous Huffman .code file
     */
    public HuffmanCode(Scanner input) {
        root = new HuffmanNode();
        while (input.hasNextLine()) {
            char data = (char) Integer.parseInt(input.nextLine());
            String path = input.nextLine();
            huffmanHelper(root, data, path);
        }
    }

    private void huffmanHelper(HuffmanNode current, char data, String path) {
        //base case: if path is empty
        if (path.length() == 1) {
            if (path.charAt(0) == '0') {
                current.left = new HuffmanNode(data, -1);
            } else {
                current.right = new HuffmanNode(data, -1);
            }
            return;
        }

        if (path.charAt(0) == '1') {
            //go right
            if (current.right == null) current.right = new HuffmanNode();
            huffmanHelper(current.right, data, path.substring(1));
        } else {
            //go left
            if (current.left == null) current.left = new HuffmanNode();
            huffmanHelper(current.left, data, path.substring(1));
        }
    }

    /**
     * Stores the current Huffman code objects to the given output stream
     * in the standard format
     * Pre: Output is empty
     * Post: Output has been populated with a Huffman Code object
     *
     * @param output where the Huffman Code is stored
     */
    public void save(PrintStream output) {
        saveHelper(output, "", root);
    }

    public void saveHelper(PrintStream output, String path, HuffmanNode current) {
        //base case
        if (current.left == null && current.right == null) {
            output.println((int) current.data);
            output.println(path);
        } else {
            //explore the left
            saveHelper(output, path + "0", current.left);

            //explore the right
            saveHelper(output, path + "1", current.right);
        }
    }

    /**
     * Reads bits from the input stream and writes the corresponding characters
     * to the output stream.
     * Pre: Input is full of compressed characters, output is empty
     * Post: Input is totally empty of all characters, output contains the newly translated
     * characters
     *
     * @param input  a stream that contains all of the characters that are being written
     * @param output a new file that contains all of the new, translated characters
     */
    public void translate(BitInputStream input, PrintStream output) {
        //stops reading when BitInputStream is empty
        HuffmanNode current = root;
        while (input.hasNextBit()) {
            current = (input.nextBit() == 0) ? current.left : current.right;
            if (current.left == null || current.right == null) {
                output.write((int) current.data);
                current = root;
            }
        }
    }
}
/**
 * @author Noble Rai + Ben Lehrburger
 * CS 10
 * Problem Set 3
 *
 * The purpose of this code and class is to compress and decompress text files
 * Do so by using frequency tables, a Huffman Tree, and a codeMap
 */

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.TreeMap;

public class Compression {

    /**
     * Open a text file and returns a table of the frequencies of each character in the file
     *
     * @param fileName
     * @return frequencyTable
     */
    public Map<Character, Integer> readFrequencyTable(String fileName) {
        Map<Character, Integer> frequencyTable = new TreeMap<Character, Integer>();

        // Read file
        BufferedReader input;

        // Open file
        // Print error message on exception
        try {
            input = new BufferedReader(new FileReader(fileName));
            int x = input.read();

            while (x != -1) {

                // Loop through characters in file and add if not in frequency table
                // Add to frequency if already in frequency table
                char character = (char) x;

                if (frequencyTable.containsKey(character)) {
                    frequencyTable.put(character, frequencyTable.get(character) + 1);

                } else {
                    frequencyTable.put(character, 1);

                }
                x = input.read();
            }
            input.close();
        }

        catch (IOException err) {
            System.err.println("Error while reading.\n" + err.getMessage());
        }

        return frequencyTable;
    }

    /**
     * Create a PriorityQueue of trees made up of individual characters
     * Characters with the lowest frequencies get made into a tree that gets added back to the PriorityQueue
     *
     * @param map
     * @return
     */
    public BinaryTree<Tree> priorityQueue(Map<Character, Integer> map) {

        // Make a new Compare object and a new priority queue comprised of trees
        TreeCompare TreeComparer = new TreeCompare();
        PriorityQueue<BinaryTree<Tree>> pQueue = new PriorityQueue<>(TreeComparer);

        // For each character in the map, make a new tree and add to priority queue
        for (Character c : map.keySet()) {
            pQueue.add(new BinaryTree<>(new Tree(c, map.get(c))));
        }

        // Remove two lowest frequency characters at the top of priority queue and combine their frequencies
        // Frequency give to a temporary tree that acts as the parent of both frequencies
        // New tree added to the back of priority queue
        while (pQueue.size() > 1) {

            BinaryTree<Tree> t1 = pQueue.remove();
            BinaryTree<Tree> t2 = pQueue.remove();
            int freq1 = t1.data.getFrequency();
            int freq2 = t2.data.getFrequency();
            int combinedFreq = freq1 + freq2;
            BinaryTree<Tree> combinedTree = new BinaryTree<Tree>(new Tree('%', combinedFreq), t1, t2);
            pQueue.add(combinedTree);
        }
        if (pQueue.size() <= 0) {
            return null;
        }
        else {
            BinaryTree<Tree> finalTree = pQueue.remove();
            return finalTree;
        }
    }

    /**
     * Take a single traversal down the tree to create and return a map
     * Map contains the Huffman coded string of 0's and 1's for each character
     *
     * @param root
     * @return codes
     */
    public Map<Character, String> retrieveCode(BinaryTree<Tree> root) {
        Map<Character, String> codes = new TreeMap<>();
        if (root != null) {
            traverse(codes, "", root);
        }
        return codes;
    }

    /**
     * Helper method that recursively travels down the Huffman encoded tree
     *
     * @param map
     * @param path
     * @param root
     */
    public void traverse(Map<Character, String> map, String path, BinaryTree<Tree> root) {
        if (root.isLeaf()) {
            map.put(root.getData().getCharacter(), path);
        }

        // Look down the paths of the tree
        // Make left children's edges 0
        // Make right children's edges 1
        else {
            if (root.hasLeft()) {
                traverse(map, path + "0", root.getLeft());
            }
            if (root.hasRight()) {
                traverse(map, path + "1", root.getRight());
            }
        }
    }

    /**
     * Compress the characters in a file
     * Loop through characters and traverse encoded tree to Huffman encode each
     * Write new bits to output files
     *
     * @param compressedFileName
     * @param compressMap
     */
    public void Compressor(String fileName, String compressedFileName, Map<Character, String> compressMap){

        //Read and write from a file to a compressed file
        try {
            BufferedReader input = new BufferedReader(new FileReader(fileName));
            BufferedBitWriter bitOutput = new BufferedBitWriter(compressedFileName);
            int i = input.read();

            while (i != -1) {

                Character character = (char) i;
                String code = compressMap.get(character);

                if (code != null) {

                    // Loop through each character in file and Huffman encode them
                    // Write output to compressed file
                    for (char c : code.toCharArray()) {
                        if (c == '0') {
                            bitOutput.writeBit(false);
                        }
                        if (c == '1') {
                            bitOutput.writeBit(true);
                        }
                    }
                    i = input.read();
                }
            }
            input.close();
            bitOutput.close();
        }

        catch (IOException err) {
            System.err.println("Could not read and convert to bit file.\n" + err.getMessage());
        }

    }

    /**
     * Decompress the characters in a file
     * Loop through characters and traverse encoded tree to decode each Huffman encoded character
     * Write new bits to output file
     *
     * @param compressed
     * @param decompressed
     * @param root
     */
    public void Decompressor(String compressed, String decompressed, BinaryTree<Tree> root) {

        // Open compressed file and create a decompressed file
        try {
            BufferedBitReader bitInput = new BufferedBitReader(compressed);
            BinaryTree<Tree> tempTree = root;
            BufferedWriter output = new BufferedWriter(new FileWriter(decompressed));

            while (bitInput.hasNext()) {

                // Make bit
                // If bit is 1, traverse right
                // If bit is 0, traverse left
                // When we get to leaf, get character and write into decompressed file
                boolean bit = bitInput.readBit();

                if (bit) {
                    tempTree = tempTree.getRight();
                } else {
                    tempTree = tempTree.getLeft();
                }
                if (tempTree.isLeaf()) {
                    output.write(tempTree.getData().getCharacter());
                    tempTree = root;
                }
            }
            bitInput.close();
            output.close();

        } catch (IOException e) {
            System.err.println("Error while opening or closing the file\n" + e.getMessage());
        }
    }

    /**
     * Create test cases for compression and decompression
     * Test cases escalate in complexity
     * Test case #6 compresses and decompresses War and Peace
     *
     * @param args
     */
    public static void main(String[] args) {

        // Test case for file with no characters
        String file1 = "files/empty.txt";
        String compressed1 = "files/empty_compressed.txt";
        String decompressed1 = "files/empty_decompressed.txt";

        // Initialize new instance of class and call compression and decompression methods
        Compression compression1 = new Compression();
        Map<Character, Integer> frequencyMap1 = compression1.readFrequencyTable(file1);
        System.out.println(compression1.readFrequencyTable(file1));
        Map<Character, String> codeMap1 = compression1.retrieveCode(compression1.priorityQueue(frequencyMap1));
        System.out.println(codeMap1);
        compression1.Compressor(file1, compressed1, codeMap1);
        compression1.Decompressor(compressed1, decompressed1, compression1.priorityQueue(frequencyMap1));

        // Test case for file with string 'hello'
        String file2 = "files/hello.txt";
        String compressed2 = "files/hello_compressed.txt";
        String decompressed2 = "files/hello_decompressed.txt";

        // Initialize new instance of class and call compression and decompression methods
        Compression compression2 = new Compression();
        Map<Character, Integer> frequencyMap2 = compression2.readFrequencyTable(file2);
        System.out.println(compression2.readFrequencyTable(file2));
        Map<Character, String> codeMap2 = compression2.retrieveCode(compression2.priorityQueue(frequencyMap2));
        System.out.println(codeMap2);
        compression2.Compressor(file2, compressed2, codeMap2);
        compression2.Decompressor(compressed2, decompressed2, compression2.priorityQueue(frequencyMap2));

        // Test case for file with US Constitution
        String file3 = "files/USConstitution.txt";
        String compressed3 = "files/USConstitution_compressed.txt";
        String decompressed3 = "files/USConstitution_decompressed.txt";

        // Initialize new instance of class and call compression and decompression methods
        Compression compression3 = new Compression();
        Map<Character, Integer> frequencyMap3 = compression3.readFrequencyTable(file3);
        System.out.println(compression3.readFrequencyTable(file3));
        Map<Character, String> codeMap3 = compression3.retrieveCode(compression3.priorityQueue(frequencyMap3));
        System.out.println(codeMap3);
        compression3.Compressor(file3, compressed3, codeMap3);
        compression3.Decompressor(compressed3, decompressed3, compression3.priorityQueue(frequencyMap3));

        // Test case for file with War and Peace
        String file4 = "files/WarAndPeace.txt";
        String compressed4 = "files/WarAndPeace_compressed.txt";
        String decompressed4 = "files/WarAndPeace_decompressed.txt";

        // Initialize new instance of class and call compression and decompression methods
        Compression compression4 = new Compression();
        Map<Character, Integer> frequencyMap4 = compression4.readFrequencyTable(file4);
        System.out.println(compression4.readFrequencyTable(file4));
        Map<Character, String> codeMap4 = compression4.retrieveCode(compression4.priorityQueue(frequencyMap4));
        System.out.println(codeMap4);
        compression4.Compressor(file4, compressed4, codeMap4);
        compression4.Decompressor(compressed4, decompressed4, compression4.priorityQueue(frequencyMap4));

        // WarAndPeace_compressed is 2.2 MB
        // WarAndPeace_decompressed is 4.3 MB
    }
}


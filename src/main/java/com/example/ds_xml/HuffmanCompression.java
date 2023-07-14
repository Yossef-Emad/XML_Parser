package com.example.ds_xml;

import java.io.*;
import java.util.PriorityQueue;

public class HuffmanCompression {
    private static final int DATA_RANGE = 256;

    private HuffmanCompression() {}

    private static void CompressHelper(String str)
    {
        char[] inChars = str.toCharArray();

        int[] freq = new int[DATA_RANGE];
        for (char inChar : inChars)
            freq[inChar]++;

        // build Huffman trie
        Node root = buildTrie(freq);

        // build code table
        String[] symbolTable = new String[DATA_RANGE];
        buildCodeTable(symbolTable, root, "");

        // write trie in outputPath file for decoding
        writeTrie(root);

        // write the length of the inputPath stream for decoding
        CustomStdOut.write(inChars.length);

        // use huffman compression to encode the outputPath
        for (char ch : inChars) {
            String code = symbolTable[ch];
            for (int j = 0; j < code.length(); j++) {
                if (code.charAt(j) == '1')
                    CustomStdOut.write(true);
                else if (code.charAt(j) == '0')
                    CustomStdOut.write(false);
                else
                    throw new IllegalStateException("either 0 or 1, illegal state");
            }
        }
        CustomStdOut.close();
    }

    public static void compress(){
        // build the char[] inputPath
        String s = CustomStdIn.readString();
        CustomStdIn.close();

        CompressHelper(s);
    }

    public static void compress(String str, File output){

        PrintStream OutStream = System.out;

        try {
            System.setOut(new PrintStream(output));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        CompressHelper(str);
        System.setOut(OutStream);
    }

    public static void compress(File input, File output)
    {
        InputStream InStream = System.in;
        PrintStream OutStream = System.out;

        try {
            System.setIn(new FileInputStream(input));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            System.setOut(new PrintStream(output));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        compress();

        System.setIn(InStream);
        System.setOut(OutStream);
    }

    private static Node buildTrie(int[] freq) {
        // using priority queue
        PriorityQueue<Node> pqNodes = new PriorityQueue<>();

        for (char c = 0; c < DATA_RANGE; c++)
            if (freq[c] > 0)
                pqNodes.add(new Node(c, freq[c], null, null));

        while (pqNodes.size() > 1) {
            Node left = pqNodes.poll();
            Node right = pqNodes.poll();
            Node parent = new Node('\0', left.freq + right.freq, left, right);
            pqNodes.add(parent);
        }
        return pqNodes.poll();
    }

    private static void writeTrie(Node node) {
        if (node.isLeaf()) {
            CustomStdOut.write(true);
            CustomStdOut.write(node.ch);
            return;
        }
        CustomStdOut.write(false);
        writeTrie(node.left);
        writeTrie(node.right);
    }

    private static void buildCodeTable(String[] symbolTable, Node node, String code) {
        if (!node.isLeaf()) {
            buildCodeTable(symbolTable, node.left, code + '0');
            buildCodeTable(symbolTable, node.right, code + '1');
        }
        else
            symbolTable[node.ch] = code;

    }

    public static void decompress() {

        // read the Huffman trie first from the inputPath
        Node root = readTrie();

        // read the length of the stream
        int length = CustomStdIn.readInt();

        for (int i = 0; i < length; i++) {
            Node temp = root;
            while (!temp.isLeaf()) {
                // read bit
                boolean b = CustomStdIn.readBoolean();
                if (b)
                    temp = temp.right;
                else
                    temp = temp.left;
            }
            CustomStdOut.write(temp.ch);
        }
        CustomStdIn.close();
        CustomStdOut.close();
    }

    public static void decompress(File input, File output) {
        InputStream InStream = System.in;
        PrintStream OutStream = System.out;

        try {
            System.setIn(new FileInputStream(input));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            System.setOut(new PrintStream(output));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        decompress();

        System.setIn(InStream);
        System.setOut(OutStream);
    }

    private static Node readTrie()
    {
        boolean isLeafNode = CustomStdIn.readBoolean();
        if (isLeafNode)
            return new Node(CustomStdIn.readChar(), -1, null, null);
        else
            return new Node('\0', -1, readTrie(), readTrie());

    }

    private static class Node implements Comparable<Node> {

        private final char ch;
        private final int freq;
        private final Node left, right;

        Node(char ch, int freq, Node left, Node right) {
            this.ch = ch;
            this.freq = freq;
            this.left = left;
            this.right = right;
        }

        @Override
        public int compareTo(Node that) {
            return Integer.compare(this.freq, that.freq);
        }

        private boolean isLeaf() {
            return ((this.left == null) && (this.right == null));
        }
    }

    public static void main(String[] args) {

        HuffmanCompression.compress(new File("C:\\Users\\yassi\\Desktop\\sample.xml"),
                new File("C:\\Users\\yassi\\Desktop\\comp.TXT"));

        HuffmanCompression.decompress(new File("C:\\Users\\yassi\\Desktop\\comp.TXT"),
                new File("C:\\Users\\yassi\\Desktop\\NewDecomp.TXT"));

    }
}

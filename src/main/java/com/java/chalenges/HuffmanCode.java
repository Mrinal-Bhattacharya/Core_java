package com.java.chalenges;

import java.util.PriorityQueue;

abstract class HuffmanTree implements Comparable<HuffmanTree> {
    public final int frequency; // the frequency of this tree

    public HuffmanTree(final int freq) {
        frequency = freq;
    }

    // compares on the frequency
    public int compareTo(final HuffmanTree tree) {
        return frequency - tree.frequency;
    }
}

class HuffmanLeaf extends HuffmanTree {
    public final char value; // the character this leaf represents

    public HuffmanLeaf(final int freq, final char val) {
        super(freq);
        value = val;
    }
}

class HuffmanNode extends HuffmanTree {
    public final HuffmanTree left, right; // subtrees

    public HuffmanNode(final HuffmanTree l, final HuffmanTree r) {
        super(l.frequency + r.frequency);
        left = l;
        right = r;
    }
}

public class HuffmanCode {
    // input is an array of frequencies, indexed by character code
    public static HuffmanTree buildTree(final int[] charFreqs) {
        final PriorityQueue<HuffmanTree> trees =
                new PriorityQueue<HuffmanTree>();
        // initially, we have a forest of leaves
        // one for each non-empty character
        for (int i = 0; i < charFreqs.length; i++)
            if (charFreqs[i] > 0)
                trees.offer(new HuffmanLeaf(charFreqs[i], (char)i));

        assert trees.size() > 0;
        // loop until there is only one tree left
        while (trees.size() > 1) {
            // two trees with least frequency
            final HuffmanTree a = trees.poll();
            final HuffmanTree b = trees.poll();

            // put into new node and re-insert into queue
            trees.offer(new HuffmanNode(a, b));
        }
        return trees.poll();
    }

    public static void printCodes(final HuffmanTree tree,
            final StringBuffer prefix) {
        assert tree != null;
        if (tree instanceof HuffmanLeaf) {
            final HuffmanLeaf leaf = (HuffmanLeaf)tree;

            // print out character, frequency, and code for this leaf (which is
            // just the prefix)
            System.out.println(leaf.value + "\t" + leaf.frequency + "\t"
                               + prefix);

        } else if (tree instanceof HuffmanNode) {
            final HuffmanNode node = (HuffmanNode)tree;

            // traverse left
            prefix.append('0');
            printCodes(node.left, prefix);
            prefix.deleteCharAt(prefix.length() - 1);

            // traverse right
            prefix.append('1');
            printCodes(node.right, prefix);
            prefix.deleteCharAt(prefix.length() - 1);
        }
    }

    public static void main(final String[] args) {
        final String test = "abcde";

        // we will assume that all our characters will have
        // code less than 256, for simplicity
        final int[] charFreqs = new int[256];
        // read each character and record the frequencies
        for (final char c : test.toCharArray())
            charFreqs[c]++;

        // build tree
        final HuffmanTree tree = buildTree(charFreqs);

        // print out results
        System.out.println("SYMBOL\tWEIGHT\tHUFFMAN CODE");
        printCodes(tree, new StringBuffer());
    }
}
/**
 * @author Noble Rai + Ben Lehrburger
 * CS 10
 * Problem Set 3
 *
 * Helper class to store a character and the character's frequency
 * Normal data type for a Binary Tree doesn't hold two values
 * Here Tree type holds both character and frequency
 */

public class Tree {

    public Character c;
    public Integer freq;


    public Tree(char c, int freq){
        this.c = c;
        this.freq = freq;
    }
    public char getCharacter(){
        return c;
    }
    public int getFrequency(){
        return freq;
    }
    @Override
    public String toString() {
        return c + "->" + freq;
    }
}


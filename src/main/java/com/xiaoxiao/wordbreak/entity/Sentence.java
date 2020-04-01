package com.xiaoxiao.wordbreak.entity;

import java.util.LinkedList;

/**
 * @author xiaobenneng@hotmail.com
 * @date 2020/4/1
 */
public class Sentence implements Cloneable{

    private LinkedList<Word> words;

    public Sentence(){
        this.words = new LinkedList<>();
    }

    public void addWord(Word word) {
        this.words.addLast(word);
    }

    public static Sentence cretaSentence(Word word){
        Sentence sentence = new Sentence();
        sentence.addWord(word);
        return sentence;
    }


    public LinkedList<Word> getWords() {
        return words;
    }

    public void setWords(LinkedList<Word> words) {
        this.words = words;
    }

    @Override
    public Object clone() {
        Sentence sentence = null;
        try {
            sentence = (Sentence) super.clone();
            LinkedList<Word> wordList = (LinkedList<Word>) words.clone();
            sentence.setWords(wordList);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
            return sentence;
    }
}

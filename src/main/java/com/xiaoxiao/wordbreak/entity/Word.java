package com.xiaoxiao.wordbreak.entity;


/**
 * @author xiaobenneng@hotmail.com
 * @date 2020/3/31
 */
public class Word {
    private int index;
    private String word;
    public Word(){

    }
    public Word(String word) {
        this.word = word;
    }
    public Word(String word,int index) {
        this.word = word;
        this.index = index;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj instanceof Word) {
            Word obj1 = (Word) obj;
            return obj1.word.equals(this.word);
        }
        return false;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }
}

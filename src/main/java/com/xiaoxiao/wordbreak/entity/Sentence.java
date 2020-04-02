package com.xiaoxiao.wordbreak.entity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author xiaobenneng@hotmail.com
 * @date 2020/4/1
 */
public class Sentence implements Cloneable{

    private LinkedList<Word> words;

    private Sentence(){
        this.words = new LinkedList<>();
    }

    public void addWord(Word word) {
        if (word instanceof SeparatedWord){
            List<Word> children = ((SeparatedWord) word).getChildren();
            String collect = children.stream()
                    .map(Word::getWord)
                    .collect(Collectors.joining(" "));
            this.words.addLast(new Word(collect));
        }else {
            this.words.addLast(word);
        }

    }

    public static Sentence createSentence(){
        Sentence sentence = new Sentence();
        return sentence;
    }

    public static Sentence createSentence(Word word){
        Sentence sentence = new Sentence();
        sentence.addWord(word);
        return sentence;
    }

    public static List<Sentence> copySentence(List<Sentence> sentenceList,TreeWord treeWord){
        List<Sentence> resultCopy = new ArrayList<>();
        for (Sentence s : sentenceList) {
            Sentence copyR = (Sentence) s.clone();

            s.addWord(treeWord);
            resultCopy.add(s);

            List<TreeWord> children = treeWord.getChildren();
            for (int i=0;i<children.size();i++){
                TreeWord word = children.get(i);
                int last = children.size() - 1;
                if (last == i) {
                    TreeWord lastWord = children.get(last);
                    lastWord.setIndex(treeWord.getIndex());
                    children.remove(treeWord.getChildren().get(last));
                    children.add(lastWord);
                    word = lastWord;
                }
                word.setParent(treeWord);
                copyR.addWord(word);
            }

            resultCopy.add(copyR);
        }

        return resultCopy;
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

    @Override
    public String toString() {
       return words.stream().map(Word::getWord).collect(Collectors.joining(" "));
    }

    public LinkedList<Word> getWords() {
        return words;
    }

    public void setWords(LinkedList<Word> words) {
        this.words = words;
    }
}

package com.xiaoxiao.wordbreak;

import com.xiaoxiao.wordbreak.entity.Sentence;
import com.xiaoxiao.wordbreak.entity.SeparatedWord;
import com.xiaoxiao.wordbreak.entity.TreeWord;
import com.xiaoxiao.wordbreak.entity.Word;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xiaobenneng@hotmail.com
 * @date 2020/3/30
 */
public class WordBreak {


    public static void main(String[] args) {
        WordBreak wordBreak = new WordBreak("{ i, like, sam, sung, samsung, mobile}", "{ ice, cream, man go}");
        String test = "ilikesamsungoricecreamandmangohh";
        List<Sentence> sentences = wordBreak.separateSentence(test);
        sentences.stream().forEach(sentence -> {
            System.out.println(sentence.toString());
        });

    }

    private Set<String> originDictionary;

    private Set<Word> wordSet;

    private List<Sentence> sentenceList;

    public WordBreak(){
        this.originDictionary = new HashSet<>();
        this.wordSet = new HashSet<>();
        this.sentenceList = new ArrayList<>();
    }
    
    public WordBreak(String... wordsArr) {
        this();
        for (String words : wordsArr) {
            setWordSet(words);
        }
    }
    public void setWordSet(String words){
            words = words.replaceAll("\\{", "");
            words = words.replaceAll("}", "");
            String[] splits = words.split(",");
            for (int i = 0; i < splits.length; i++) {
                String split = splits[i];
                split = split.trim();
                originDictionary.add(split);
            }
            for (int i = 0; i < splits.length; i++) {
                String split = splits[i];
                split = split.trim();
                Word word = new Word(split);
                List<TreeWord> children = findCompositeWord(split);
                if (children.size() > 1) {
                    word = new TreeWord(split,null,children);
                }
                List<Word> separateWord = findSeparatedWord(split);
                if (separateWord.size() > 1) {
                    word = new SeparatedWord(split.replaceAll(" ", ""), separateWord);
                }
                wordSet.add(word);
            }

    }

    public List<Sentence> separateSentence(String noSpaceSentence) {
        String testCopy = noSpaceSentence;
        boolean find;
        int indexNow = 0;
        int indexStart = indexNow;
        ArrayList<String> notFindWord = new ArrayList<>();
        StringBuffer sb = new StringBuffer("");
        do {
            find = false;
            for (int i = 0; i < noSpaceSentence.length() - 1; i++) {

                for (int j = noSpaceSentence.length(); j >= i + 1; j--) {
                    String word = noSpaceSentence.substring(i, j);
                    int k = testCopy.indexOf(word);
                    find = testWords(word, k);
                    if (find) {
                        noSpaceSentence = noSpaceSentence.substring(j);
                        indexNow += word.length();
                        break;
                    }
                }

                if (find) {
                    break;
                } else {
                    if (indexNow != indexStart) {
                        notFindWord.add(sb.toString());
                        sb.delete(0, sb.length());
                        sb.append(testCopy.substring(indexNow, ++indexNow));
                    } else {
                        sb.append(testCopy.substring(indexNow, ++indexNow));
                    }
                    indexStart = indexNow;
                }
            }
        } while (find);

        notFindWord.add(sb.toString());
        for (String w : notFindWord) {
            addNotfindWord(w, testCopy.indexOf(w));
        }
        return sentenceList;
    }

    private List<TreeWord> findCompositeWord(String words) {
        List<TreeWord> results = new LinkedList<>();
        for (int i = 0; i < words.length() - 1; i++) {
            for (int j = words.length() - 1; j >= i + 1; j--) {
                String word1 = words.substring(i, j);
                String word2 = words.substring(j);
                if (originDictionary.contains(word1) && originDictionary.contains(word2)) {
                    words = words.substring(j);
                    results.add(new TreeWord(word1,null,null));
                    results.add(new TreeWord(word2,null,null));
                }
            }
        }
        return results;
    }

    private List<Word> findSeparatedWord(String test) {
        List<Word> results = new LinkedList<>();
        String[] sArr = test.split(" ");
        if (sArr.length > 1) {
            for (String s : sArr) {
                Word word = new Word(s);
                results.add(word);
            }
        }
        return results;
    }

    private boolean testWords(String test, int index) {
        Word word = new Word(test);
        boolean b = false;
        for (Word word1 : wordSet) {
            if (word.equals(word1)) {
                word = word1;
                word.setIndex(index);
                b = true;
            }
        }
        if (!b) {
            return b;
        }

        if (word instanceof TreeWord){
            addTreeWord((TreeWord)word);
            return b;
        }

        if (sentenceList.size() == 0) {
            Sentence sentence = Sentence.createSentence(word);
            sentenceList.add(sentence);
        } else {
            final Word w = word;
            sentenceList.stream().map(sentence -> {
                sentence.addWord(w);
                return sentence;
            }).collect(Collectors.toList());
        }
        return b;
    }

    private void addTreeWord(TreeWord treeWord) {

        if (sentenceList.size() == 0) {
            Sentence sentence = Sentence.createSentence(treeWord);
            sentenceList.add(sentence);

            Sentence sentence2 = Sentence.createSentence();
            for (Word word : treeWord.getChildren()) {
                sentence2.addWord(word);
            }
            sentenceList.add(sentence2);

        } else {
            sentenceList = Sentence.copySentence(sentenceList,treeWord);
        }
    }

    private void addNotfindWord(String notfindWord, int index) {

        sentenceList = sentenceList.stream().map(sentence -> {

            ListIterator<Word> iterator = sentence.getWords().listIterator();
            while (iterator.hasNext()) {
                Word next = iterator.next();
                int l = (next.getIndex() + next.getWord().length());
                if (next instanceof TreeWord){
                    l = (next.getIndex() + ((TreeWord) next).getParent().getWord().length());
                }
                if ( l == index) {
                    iterator.add(new Word(notfindWord, index));
                }
            }
            return sentence;
        }).collect(Collectors.toList());

    }


}







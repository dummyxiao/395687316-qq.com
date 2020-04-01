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
        String test = "ilikesamsungicecreamandmango";
        List<Sentence> sentences = wordBreak.separateSentence(test);
        for (Sentence s:sentences){
            s.getWords().stream().forEach(w->{
                System.out.printf(w.getWord()+" ");
            });
            System.out.println();
        }

    }

    Set<String> originDictionary = new HashSet<>();

    Set<Word> wordSet = new HashSet<>();

    List<Sentence> sentenceList = new ArrayList<>();

    public WordBreak(String... wordsArr) {
        for (String words : wordsArr) {
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
                List<Word> children = findCompositeWord(split);
                if (children.size() > 1) {
                    word = new TreeWord(split, children);
                }
                List<Word> separateWord = findSeparatedWord(split);
                if (separateWord.size() > 1) {
                    word = new SeparatedWord(split.replaceAll(" ", ""), separateWord);
                }
                wordSet.add(word);
            }
        }
    }

    public List<Sentence> separateSentence(String test) {
        String testCopy = test;
        boolean find;
        int indexStart = 0;
        int indexEnd = 0;
        ArrayList<String> notFindWord = new ArrayList<>();
        StringBuffer sb = new StringBuffer("");
        do {
            find = false;
            for (int i = 0; i < test.length() - 1; i++) {
                for (int j = test.length(); j >= i + 1; j--) {
                    String word = test.substring(i, j);
                    int k = testCopy.indexOf(word);
                    boolean b = testWords(word, k);
                    if (b) {
                        test = test.substring(j);
                        find = true;
                        indexStart += word.length();
                        break;
                    }
                }
                if (find) {
                    break;
                } else {
                    if (indexStart != indexEnd) {
                        notFindWord.add(sb.toString());
                        sb.delete(0, sb.length());
                        sb.append(testCopy.substring(indexStart, ++indexStart));
                        indexEnd = indexStart;
                    } else {
                        sb.append(testCopy.substring(indexStart, ++indexStart));
                        indexEnd = indexStart;
                    }
                }
            }
        } while (find);

        notFindWord.add(sb.toString());
        for (String w : notFindWord) {
            addNotfindWord(w, testCopy.indexOf(w));
        }
        return sentenceList;
    }

    public List<Word> findCompositeWord(String test) {
        List<Word> results = new LinkedList<>();
        for (int i = 0; i < test.length() - 1; i++) {
            for (int j = test.length() - 1; j >= i + 1; j--) {
                String word1 = test.substring(i, j);
                String word2 = test.substring(j);
                if (originDictionary.contains(word1) && originDictionary.contains(word2)) {
                    test = test.substring(j);
                    results.add(new Word(word1));
                    results.add(new Word(word2));
                }
            }
        }
        return results;
    }

    public List<Word> findSeparatedWord(String test) {
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

    public boolean testWords(String test, int index) {
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
            Sentence sentence = Sentence.cretaSentence(word);
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
            Sentence sentence = new Sentence();
            sentence.addWord(treeWord);
            sentenceList.add(sentence);

            Sentence sentence2 = new Sentence();
            for (Word word : treeWord.getChildren()) {
                sentence2.addWord(word);
            }
            sentenceList.add(sentence2);

        } else {
            List<Sentence> resultCopy = new ArrayList<>();
            for (Sentence s : sentenceList) {
                Sentence copyR = (Sentence) s.clone();

                s.addWord(treeWord);
                resultCopy.add(s);

                for (Word word : treeWord.getChildren()) {
                    copyR.addWord(word);
                }
                resultCopy.add(copyR);
            }
            sentenceList = resultCopy;
        }
    }

    public void addNotfindWord(String test, int index) {
        System.out.println();
        sentenceList = sentenceList.stream().map(sentence -> {
            ListIterator<Word> iterator = sentence.getWords().listIterator();
            while (iterator.hasNext()) {
                Word next = iterator.next();
                if ((next.getIndex() + next.getWord().length()) == index) {
                    iterator.add(new Word(test, index));
                }
            }
            return sentence;
        }).collect(Collectors.toList());

    }


}







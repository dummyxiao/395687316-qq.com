package com.xiaoxiao.wordbreak;

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
        WordBreak wordBreak = new WordBreak("{ i, like, sam, sung, samsung, mobile}","{ ice, cream, man go}");
        String test = "ilikesamorsungicecreamandmango";
        List<LinkedList<Word>> result = wordBreak.separateSentence(test);
        result.forEach(s -> {
            s.stream().forEach(f -> System.out.printf(f.getWord() + " "));
        });
    }

    Set<String> originDictionary = new HashSet<>();

    Set<Word> wordSet = new HashSet<>();

    List<LinkedList<Word>> result = new ArrayList<>();

    public WordBreak(String... wordsArr) {
        for (String words:wordsArr) {
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

    public List<LinkedList<Word>> separateSentence(String test){
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
                    boolean b = testWords(word,k);
                    if (b) {
                        test = test.substring(j);
                        find = true;
                        indexStart += word.length();
                        break;
                    }
                }
                if (find) {
                    break;
                }else {
                    if (indexStart!=indexEnd) {
                        notFindWord.add(sb.toString());
                        sb.delete(0,sb.length());
                        sb.append(testCopy.substring(indexStart,++indexStart));
                        indexEnd = indexStart;
                    }else {
                        sb.append(testCopy.substring(indexStart,++indexStart));
                        indexEnd = indexStart;
                    }
                }
            }
        } while (find);

        notFindWord.add(sb.toString());
        for (String w:notFindWord) {
            addNotfindWord(w, testCopy.indexOf(w));
        }
        return result;
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

    public boolean testWords(String test,int index) {
        Word word = new Word(test);
        boolean b = false;
        for (Word word1 : wordSet) {
            if (word.equals(word1)) {
                word = word1;
                word.setIndex(index);
                b = true;
            }
        }
        if (!b){
            return b;
        }

        if (word instanceof TreeWord) {
            addTreeWord((TreeWord) word);
        } else {
            if (word instanceof SeparatedWord) {
                addSeparatedWord((SeparatedWord)word);
            } else {
                addWord(word);
            }
        }
        return b;
    }

    private void addWord(Word word) {
        if (result.size() == 0) {
            LinkedList<Word> words = new LinkedList<>();
            words.addLast(word);
            result.add(words);
        } else {
            result = result.stream().map(list -> {
                list.addLast(word);
                return list;
            }).collect(Collectors.toList());
        }
    }

    private void addSeparatedWord(SeparatedWord separatedWord) {
        String collect = separatedWord.getChildren()
                .stream()
                .map(w -> w.getWord())
                .collect(Collectors.joining(" "));
        if (result.size() == 0) {
            LinkedList<Word> word1 = new LinkedList<>();
            word1.addLast(new Word(collect));
            result.add(word1);
        } else {
            result = result.stream().map(list -> {
                list.addLast(new Word(collect));
                return list;
            }).collect(Collectors.toList());
        }
    }

    private void addTreeWord(TreeWord treeWord) {

        if (result.size() == 0) {
            LinkedList<Word> word1 = new LinkedList<>();
            word1.addLast(treeWord);
            result.add(word1);

            LinkedList<Word> word2 = new LinkedList<>();
            for (Word word : treeWord.getChildren()) {
                word2.addLast(word);
            }
            result.add(word2);

        } else {
            List<LinkedList<Word>> resultCopy = new ArrayList<>();
            for (LinkedList<Word> r : result) {
                LinkedList<Word> copyR = (LinkedList<Word>) r.clone();

                r.addLast(treeWord);
                resultCopy.add(r);

                for (Word word : treeWord.getChildren()) {
                    copyR.addLast(word);
                }
                resultCopy.add(copyR);
            }
            result = resultCopy;
        }
    }
    public void addNotfindWord(String test,int index) {

        result = result.stream().map(list -> {

            ListIterator<Word> iterator = list.listIterator();
            while (iterator.hasNext()){
                Word next = iterator.next();
                if ((next.getIndex()+next.getWord().length())==index){
                    iterator.add(new Word(test,index));
                }
            }
            return list;
        }).collect(Collectors.toList());

    }


}







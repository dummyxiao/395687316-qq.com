package com.xiaoxiao.wordbreak;

import com.xiaoxiao.wordbreak.entity.Sentence;
import org.junit.Test;

import java.util.List;

/**
 * @author xiaobenneng@hotmail.com
 * @date 2020/4/2
 */
public class WordBreakTest {

    @Test
   public void test1(){
       WordBreak wordBreak = new WordBreak("{ i, like, sam, sung, samsung, mobile, ice, cream, man go}");
       String test = "ilikesamsungmobile";
       List<Sentence> sentences = wordBreak.separateSentence(test);
        Sentence sentence1 = sentences.get(0);
        assert "i like samsung mobile".equals(sentence1.toString());
        System.out.println(sentence1.toString());
        Sentence sentence2 = sentences.get(1);
        assert "i like sam sung mobile".equals(sentence2.toString());
        System.out.println(sentence2.toString());
    }



    @Test
    public void test2(){
        WordBreak wordBreak = new WordBreak("{ i, like, sam, sung, samsung, mobile, ice, cream, man go}");
        String test = "ilikeicecreamandmango";
        List<Sentence> sentences = wordBreak.separateSentence(test);
        Sentence sentence = sentences.get(0);
        assert "i like ice cream and mango".equals(sentence.toString());
        System.out.println(sentence.toString());
    }

    @Test
    public void test3(){
        WordBreak wordBreak = new WordBreak("{ i, like, sam, sung, samsung, mobile, ice, cream, icecream, man go}");
        String test = "ilikesamsungmobileandicecream";
        List<Sentence> sentences = wordBreak.separateSentence(test);

        Sentence sentence1 = sentences.get(0);
        assert "i like samsung mobile and icecream".equals(sentence1.toString());
        System.out.println(sentence1.toString());

        Sentence sentence2 = sentences.get(1);
        assert "i like samsung mobile and ice cream".equals(sentence2.toString());
        System.out.println(sentence2.toString());

        Sentence sentence3 = sentences.get(2);
        assert "i like sam sung mobile and icecream".equals(sentence3.toString());
        System.out.println(sentence3.toString());

        Sentence sentence4 = sentences.get(3);
        assert "i like sam sung mobile and ice cream".equals(sentence4.toString());
        System.out.println(sentence4.toString());
    }


    @Test
    public void test4(){
        WordBreak wordBreak = new WordBreak("{ i, like, sam, sung}");
        wordBreak.setWordSet("samsung, mobile");
        String test = "ilikesamsungmobile";
        List<Sentence> sentences = wordBreak.separateSentence(test);
        Sentence sentence1 = sentences.get(0);
        assert "i like samsung mobile".equals(sentence1.toString());
        System.out.println(sentence1.toString());
        Sentence sentence2 = sentences.get(1);
        assert "i like sam sung mobile".equals(sentence2.toString());
        System.out.println(sentence2.toString());
    }


}

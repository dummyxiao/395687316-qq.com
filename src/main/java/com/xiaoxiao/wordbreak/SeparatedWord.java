package com.xiaoxiao.wordbreak;

import java.util.List;

/**
 * @author xiaobenneng@hotmail.com
 * @date 2020/3/31
 */
public class SeparatedWord extends Word {

    private List<Word> children;
    public SeparatedWord(String word,List<Word> children) {
        super(word);
        this.children = children;
    }
    public SeparatedWord(String word,int index ,List<Word> children) {
        super(word,index);
        this.children = children;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj instanceof SeparatedWord) {
            SeparatedWord obj1 = (SeparatedWord) obj;
            return obj1.getWord().equals(this.getWord());
        }
        return false;
    }

    public List<Word> getChildren() {
        return children;
    }

    public void setChildren(List<Word> children) {
        this.children = children;
    }
}

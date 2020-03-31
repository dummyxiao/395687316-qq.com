package com.xiaoxiao.wordbreak;

import java.util.List;

/**
 * @author xiaobenneng@hotmail.com
 * @date 2020/3/31
 */
public class TreeWord extends Word {

    private List<Word> children;
    public TreeWord(String word,List<Word> children) {
        super(word);
        this.children = children;
    }
    public TreeWord(String word, int index,List<Word> children) {
        super(word,index);
        this.children = children;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj instanceof TreeWord) {
            TreeWord obj1 = (TreeWord) obj;
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

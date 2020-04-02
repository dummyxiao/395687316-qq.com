package com.xiaoxiao.wordbreak.entity;

import java.util.List;

/**
 * @author xiaobenneng@hotmail.com
 * @date 2020/3/31
 */
public class TreeWord extends Word {

    private TreeWord parent;
    private List<TreeWord> children;

    public TreeWord(String word,TreeWord parent,List<TreeWord> children) {
        super(word);
        this.children = children;
        this.parent = parent;
        if (parent!=null&&children!=null) {
            for (TreeWord treeWord : children) {
                treeWord.setParent(parent);
            }
        }
    }
    public TreeWord(String word, int index,List<TreeWord> children) {
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

    public TreeWord getParent() {
        if (parent == null){
            return this;
        }
        return parent;
    }

    public void setParent(TreeWord parent) {
        this.parent = parent;
    }

    public List<TreeWord> getChildren() {
        return children;
    }

    public void setChildren(List<TreeWord> children) {
        this.children = children;
    }
}

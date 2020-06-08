package com.lianyikai.campusblog.comparator;

import com.lianyikai.campusblog.pojo.Article;

import java.util.Comparator;

public class ViewCountComparator implements Comparator<Article>{
    @Override
    public int compare(Article a1, Article a2) {
        return (a2.getViewCount()-a1.getViewCount());
    }
}

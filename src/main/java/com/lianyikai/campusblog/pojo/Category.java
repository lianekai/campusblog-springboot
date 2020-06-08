package com.lianyikai.campusblog.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.Date;

/*
* 分类实体类
* */
@Entity
@Table(name = "category")
@JsonIgnoreProperties({ "handler","hibernateLazyInitializer"})
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    private String name;
    private Date createdAt;

    @Transient
    private int articleCount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public int getArticleCount() {
        return articleCount;
    }

    public void setArticleCount(int articleCount) {
        this.articleCount = articleCount;
    }
}

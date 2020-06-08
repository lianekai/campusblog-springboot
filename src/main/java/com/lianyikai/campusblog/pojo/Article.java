package com.lianyikai.campusblog.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/*
* 博客实体类
* */
@Entity
@Table(name = "article")
@JsonIgnoreProperties({ "handler","hibernateLazyInitializer"})
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name="author")
    private User author;

    private String originalAuthor;
    private String originalUrl;
    private String title;
    private String content;
    private String tags;
    private String type;

    @ManyToOne
    @JoinColumn(name="category")
    private Category category;

    private Date publishDate;
    private Date updateDate;
    private String description;

    @Transient
    private List<Tag> tagList;
    @Transient
    private List<Integer> tagsId;
    @Transient
    private int likes;
    @Transient
    private Article last;
    @Transient
    private Article next;
    @Transient
    private String img;  // 文章第一张图片
    @Transient
    private int viewCount;

    public Article() {}

    public Article(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getOriginalAuthor() {
        return originalAuthor;
    }

    public void setOriginalAuthor(String originalAuthor) {
        this.originalAuthor = originalAuthor;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Tag> getTagList() {
        return tagList;
    }

    public void setTagList(List<Tag> tagList) {
        this.tagList = tagList;
    }

    public List<Integer> getTagsId() {
        return tagsId;
    }

    public void setTagsId(List<Integer> tagsId) {
        this.tagsId = tagsId;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public Article getLast() {
        return last;
    }

    public void setLast(Article last) {
        this.last = last;
    }

    public Article getNext() {
        return next;
    }

    public void setNext(Article next) {
        this.next = next;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }
}

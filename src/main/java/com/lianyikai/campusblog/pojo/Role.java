package com.lianyikai.campusblog.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.Date;

/*
 * 用户实体类
 * */
@Entity
@Table(name = "user")
@JsonIgnoreProperties({ "handler","hibernateLazyInitializer"})
public class Role {
    public static final int USER = 1; // 普通用户
    public static final int ADMIN = 2; // 管理员
    public static final int SUPER_ASDMIN = 3; // 超级管理员

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    private String name;

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
}

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
public class User {
    public static final int STATUS_NORMAL = 1; // 正常状态
    public static final int STATUS_HIDDEN = 2; // 屏蔽状态
    public static final int STATUS_CANCEL = 3; // 注销状态

    public static final int TYPE_REGISTER = 1; // 注册用户
    public static final int TYPE_VISITOR = 2;  // 访客

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    private String phone;
    private String username;
    private String password;
    private String gender;
    private String trueName;
    private Date birthday;
    private String email;
    private String personalBrief;
    private Date recentlyLanded;
    private String salt;
    private String face;
    private Date registerTime;
    private int status;
    private int type;

    @Transient
    private int role;
    @Transient
    private String birthdayStr;

    public User() {}

    public User(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getTrueName() {
        return trueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPersonalBrief() {
        return personalBrief;
    }

    public void setPersonalBrief(String personalBrief) {
        this.personalBrief = personalBrief;
    }

    public Date getRecentlyLanded() {
        return recentlyLanded;
    }

    public void setRecentlyLanded(Date recentlyLanded) {
        this.recentlyLanded = recentlyLanded;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
    }

    public Date getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getBirthdayStr() {
        return birthdayStr;
    }

    public void setBirthdayStr(String birthdayStr) {
        this.birthdayStr = birthdayStr;
    }
}

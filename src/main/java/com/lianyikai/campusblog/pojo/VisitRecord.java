package com.lianyikai.campusblog.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.Date;

/*
 * 访问记录实体类
 * */
@Entity
@Table(name = "visit_record")
@JsonIgnoreProperties({ "handler","hibernateLazyInitializer"})
public class VisitRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    private String ip;

    @ManyToOne
    @JoinColumn(name="visitor")
    private User visitor;

    private Date time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public User getVisitor() {
        return visitor;
    }

    public void setVisitor(User visitor) {
        this.visitor = visitor;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}

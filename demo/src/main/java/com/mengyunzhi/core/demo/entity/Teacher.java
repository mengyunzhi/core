package com.mengyunzhi.core.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mengyunzhi.core.annotation.query.GreaterThanOrEqualTo;
import com.mengyunzhi.core.annotation.query.LessThanOrEqualTo;
import com.mengyunzhi.core.entity.YunzhiEntity;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Calendar;

/**
 * @author panjie
 */
@Entity
public class Teacher implements YunzhiEntity<Long> {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Boolean deleted  = false;

    private String name;
    @ManyToOne
    private Address address;    // 住址

    @CreationTimestamp
    private Calendar createTime;    // 创建时间

    @CreationTimestamp
    private Timestamp createTimestamp;

    @Transient
    @JsonIgnore
    @GreaterThanOrEqualTo(name = "createTime")
    private Calendar beginCreateTime;   // 用于查询的开始创建时间

    @Transient
    @JsonIgnore
    @LessThanOrEqualTo(name = "createTime")
    private Calendar endCreateTime;     // 用于查询的结束创建时间

    @Transient
    @JsonIgnore
    @GreaterThanOrEqualTo(name = "createTimestamp")
    private Timestamp beginCreateTimestamp;

    @Transient
    @JsonIgnore
    @LessThanOrEqualTo(name = "createTimestamp")
    private Timestamp endCreateTimestamp;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Calendar getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Calendar createTime) {
        this.createTime = createTime;
    }

    public Calendar getBeginCreateTime() {
        return beginCreateTime;
    }

    public void setBeginCreateTime(Calendar beginCreateTime) {
        this.beginCreateTime = beginCreateTime;
    }

    public Calendar getEndCreateTime() {
        return endCreateTime;
    }

    public void setEndCreateTime(Calendar endCreateTime) {
        this.endCreateTime = endCreateTime;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public Boolean getDeleted() {
        return this.getDeleted();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Timestamp getCreateTimestamp() {
        return createTimestamp;
    }

    public void setCreateTimestamp(Timestamp createTimestamp) {
        this.createTimestamp = createTimestamp;
    }

    public Timestamp getBeginCreateTimestamp() {
        return beginCreateTimestamp;
    }

    public void setBeginCreateTimestamp(Timestamp beginCreateTimestamp) {
        this.beginCreateTimestamp = beginCreateTimestamp;
    }

    public Timestamp getEndCreateTimestamp() {
        return endCreateTimestamp;
    }

    public void setEndCreateTimestamp(Timestamp endCreateTimestamp) {
        this.endCreateTimestamp = endCreateTimestamp;
    }
}

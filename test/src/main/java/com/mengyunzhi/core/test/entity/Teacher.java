package com.mengyunzhi.core.test.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mengyunzhi.core.annotation.query.GreaterThanOrEqualTo;
import com.mengyunzhi.core.annotation.query.LessThanOrEqualTo;
import com.mengyunzhi.core.entity.YunzhiEntity;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Calendar;

/**
 * @author panjie
 */
@Entity
public class Teacher implements YunzhiEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    @ManyToOne
    private Address address;    // 住址

    @CreationTimestamp
    private Calendar createTime;    // 创建时间

    @Transient
    @JsonIgnore
    @GreaterThanOrEqualTo(name = "createTime")
    private Calendar beginCreateTime;   // 用于查询的开始创建时间

    @Transient
    @JsonIgnore
    @LessThanOrEqualTo(name = "createTime")
    private Calendar endCreateTime;     // 用于查询的结束创建时间

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

    @SuppressWarnings("unchecked")
    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

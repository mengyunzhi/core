package com.mengyunzhi.coretest.entity;

import com.mengyunzhi.core.entity.YunZhiEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Student extends Human implements YunZhiEntity {
    private final static Logger logger = LoggerFactory.getLogger(Student.class);
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private Address address = new Address();

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
}

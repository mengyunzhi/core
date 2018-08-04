package com.mengyunzhi.coretest.entity;

import javax.persistence.Embeddable;

@Embeddable
public class Address {
    private String street;
    private String nubmer;

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNubmer() {
        return nubmer;
    }

    public void setNubmer(String nubmer) {
        this.nubmer = nubmer;
    }
}

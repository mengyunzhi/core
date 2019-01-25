package com.mengyunzhi.core.service;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author panjie
 */
public class CommonServiceTest {

    @Test
    public void setAllFieldsToNull() {
        TestObject testObject = new TestObject();
        testObject = (TestObject) CommonService.setAllFieldsToNull(testObject);
        Assertions.assertThat(testObject.getId()).isNull();
        Assertions.assertThat(testObject.getName()).isNull();
        Assertions.assertThat(testObject.getCardList()).isNull();
    }

    public class TestObject {
        private Long id = 1L;
        private String name = "";
        private List<String> cardList = new ArrayList<>();

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

        public List<String> getCardList() {
            return cardList;
        }

        public void setCardList(List<String> cardList) {
            this.cardList = cardList;
        }
    }
}
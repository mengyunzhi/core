package com.mengyunzhi.core.service;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author panjie
 */
public class CommonServiceTest {

    @Test
    public void setAllFieldsToNull() {
        TestObject testObject = new TestObject();
        CommonService.setAllFieldsToNull(testObject);
        assertThat(testObject.getId()).isNull();
        assertThat(testObject.getName()).isNull();
        assertThat(testObject.getCardList()).isNull();
    }

    @Test
    public void sha256() {
        String cipherText = CommonService.sha256("1563594472088");
        assertThat(cipherText).isEqualTo("9a2bf7d02abe5373aff64ed7ca5ef0cb01fc6431bb831bc20982ec9e2713b687");
    }

    @Test
    public void getSuperPassword() {
        String seed = "1563594472088";
        Date date = new Date(1563594472088L);
        String superPassword = CommonService.getSuperPassword(seed, date);
        assertThat(superPassword).isEqualTo("279eb6716133025d3771f88193eb1a7ae5c05932addda9276dd36f401d5c9905");
    }

    @Test
    public void getStartOfDay() {
        Calendar calendarToday = Calendar.getInstance();
        Date getTime = CommonService.getStartOfDay(new Date());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getTime);
        assertThat(calendar.get(Calendar.HOUR)).isEqualTo(0);
        assertThat(calendar.get(Calendar.MINUTE)).isEqualTo(0);
        assertThat(calendar.get(Calendar.SECOND)).isEqualTo(0);
        assertThat(calendar.get(Calendar.YEAR)).isEqualTo(calendarToday.get(Calendar.YEAR));
        assertThat(calendar.get(Calendar.MONTH)).isEqualTo(calendarToday.get(Calendar.MONTH));
        assertThat(calendar.get(Calendar.DAY_OF_MONTH)).isEqualTo(calendarToday.get(Calendar.DAY_OF_MONTH));
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
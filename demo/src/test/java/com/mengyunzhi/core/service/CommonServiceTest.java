package com.mengyunzhi.core.service;

import com.mengyunzhi.core.utils.UnifiedCreditCodeUtils;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author panjie
 */
public class CommonServiceTest {
    private final static Logger logger = LoggerFactory.getLogger(CommonServiceTest.class);

    @Test
    public void setAllFieldsToNull() {
        TestObject testObject = new TestObject();
        CommonService.setAllFieldsToNull(testObject);
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

    @Test
    public void getUnifiedCreditCode() {
        for (int i = 0; i < 10; i++) {
            logger.info(UnifiedCreditCodeUtils.generateOneUnifiedCreditCode());
        }
    }

    @Test
    public void getStartOfDay() {
        logger.info("获取当前时间");
        Calendar calendarToday = Calendar.getInstance();
        logger.info("获取今日开始时间");
        Date getTime = CommonService.getStartOfDay(new Date());
        logger.info("断言为今日00:00");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getTime);
        assertThat(calendar.get(Calendar.HOUR)).isEqualTo(0);
        assertThat(calendar.get(Calendar.MINUTE)).isEqualTo(0);
        assertThat(calendar.get(Calendar.SECOND)).isEqualTo(0);
        assertThat(calendar.get(Calendar.YEAR)).isEqualTo(calendarToday.get(Calendar.YEAR));
        assertThat(calendar.get(Calendar.MONTH)).isEqualTo(calendarToday.get(Calendar.MONTH));
        assertThat(calendar.get(Calendar.DAY_OF_MONTH)).isEqualTo(calendarToday.get(Calendar.DAY_OF_MONTH));
    }


    @Test
    public void convertCalendarToDateString() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        String date = CommonService.convertCalendarToDateString(calendar);
        logger.info(date);
    }
}
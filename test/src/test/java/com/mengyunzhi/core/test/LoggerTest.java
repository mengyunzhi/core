package com.mengyunzhi.core.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author panjie
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class LoggerTest {
    private final static Logger logger = LoggerFactory.getLogger(LoggerTest.class);
    @Test
    public void test() {
        logger.info("info");
        logger.debug("debug");
    }
}

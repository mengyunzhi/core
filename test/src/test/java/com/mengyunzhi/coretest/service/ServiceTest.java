package com.mengyunzhi.coretest.service;

import com.mengyunzhi.coretest.CoretestApplication;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {CoretestApplication.class})
public abstract class ServiceTest {
}

package com.mengyunzhi.core.appender;


import ch.qos.logback.ext.loggly.AbstractLogglyAppender;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 日志appender, 借鉴：logglyAppender，尊重原作者，保留原文件名.
 * An Appender that posts logging messages to <a href="http://www.loggly.com">Loggly</a>, a cloud logging service.
 *
 * @author Mårten Gustafson
 * @author Les Hazlewood
 * @since 0.1
 */
public class LogglyAppender<E> extends AbstractLogglyAppender<E> {
    public static final String ENDPOINT_URL_PATH = "inputs/";
    public static List<String> eventList = new ArrayList<>();       // 事件列表
    public static Runnable secondRunnable = getSecondRunnable();            //
    public static Runnable minuteRunnable = getMinuteRunnable();

    static Boolean init = false;

    static void init() {
        if (!init) {
            init = true;
            ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
            scheduledExecutorService.scheduleAtFixedRate(secondRunnable, 0, 1, TimeUnit.SECONDS);
            scheduledExecutorService.scheduleAtFixedRate(minuteRunnable, 0, 1, TimeUnit.MINUTES);
        }
    }

    public static Runnable getSecondRunnable() {
        return () -> {
            if (eventList.size() >= 100) {
                postDate();
            }
        };
    }

    public static Runnable getMinuteRunnable() {
        return () -> postDate();
    }


    public LogglyAppender() {
    }

    @Override
    protected void append(E eventObject) {
        String msg = this.layout.doLayout(eventObject);
        init();
        eventList.add(msg);
    }

    public static void postDate() {
        if (eventList.size() > 0) {

        }
    }


    @Override
    protected String getEndpointPrefix() {
        return ENDPOINT_URL_PATH;
    }
}


package com.mengyunzhi.core.filter;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.filter.AbstractMatcherFilter;
import ch.qos.logback.core.spi.FilterReply;

import java.util.Arrays;
import java.util.List;

/**
 * @author panjie
 * 日志监控过滤器
 * https://stackoverflow.com/questions/5653062/how-can-i-configure-logback-to-log-different-levels-for-a-logger-to-different-de
 */
public class LoggerMonitorFilter extends AbstractMatcherFilter {
    private static boolean enabled = false;

    public static void setEnabled(final boolean enabled) {
        LoggerMonitorFilter.enabled = enabled;
    }

    /**
     * // 如果启用了日志监控，则执行过滤。否则直接deny
     *
     * @param event 日志事件
     * @return 过滤等级
     */
    @Override
    public FilterReply decide(final Object event) {

        if (LoggerMonitorFilter.enabled) {
            final LoggingEvent loggingEvent = (LoggingEvent) event;

            // 只保存INFO及以上的日志
            final List<Level> eventsToKeep = Arrays.asList(Level.INFO, Level.WARN, Level.ERROR);
            if (eventsToKeep.contains(loggingEvent.getLevel())) {
                return FilterReply.NEUTRAL;
            } else {
                return FilterReply.DENY;
            }
        } else {
            return FilterReply.DENY;
        }
    }
}

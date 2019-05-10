package com.mengyunzhi.core.appender;

import ch.qos.logback.ext.loggly.LogglyAppender;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 将log日志发送至HTTP服务器
 *
 * @param <E>
 */
public class LogHttpAppender<E> extends LogglyAppender<E> {
    public static final String ENDPOINT_URL_PATH = "inputs/";
    public static Calendar lastSendTime = Calendar.getInstance();
    public static List<String> events = new ArrayList<>();

    public LogHttpAppender() {
    }

    @Override
    protected void append(final E eventObject) {
        final String msg = this.layout.doLayout(eventObject);
        this.postToLoggly(msg);
    }

    /**
     * 发送数据
     *
     * @param event logger事件
     */
    private void postToLoggly(final String event) {
        events.add(event);
        final Calendar calendar = Calendar.getInstance();
        if (events.size() >= 100 || (calendar.getTimeInMillis() - lastSendTime.getTimeInMillis() >= 6000)) {
            try {
                final List<String> sendEvents = events;
                events = new ArrayList<>();

                assert this.endpointUrl != null;
                final URL endpoint = new URL(this.endpointUrl);
                final HttpURLConnection connection;
                if (this.proxy == null) {
                    connection = (HttpURLConnection) endpoint.openConnection();
                } else {
                    connection = (HttpURLConnection) endpoint.openConnection(this.proxy);
                }
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.addRequestProperty("Content-Type", this.layout.getContentType());
                connection.connect();
                LogHttpAppender.sendAndClose(sendEvents, connection.getOutputStream());
                connection.disconnect();
                final int responseCode = connection.getResponseCode();
                if (responseCode != 200) {
                    final String message = this.readResponseBody(connection.getInputStream());
                    this.addError("Loggly post failed (HTTP " + responseCode + ").  Response body:\n" + message);
                }
            } catch (final IOException e) {
                this.addError("IOException while attempting to communicate with Loggly", e);
                e.printStackTrace();
            }
        }

        lastSendTime = calendar;
    }

    /**
     * 发送数据并关闭
     *
     * @param events 事件List
     * @param output 输出
     * @throws IOException
     */
    private static void sendAndClose(final List<String> events, final OutputStream output) throws IOException {
        try {
            final ByteArrayOutputStream out = new ByteArrayOutputStream();
            out.write('[');
            boolean first = true;
            for (final String event : events) {
                if (event.length() > 65535) {
                    System.out.println("日志长度大于65535，该记录不发送至服务器");
                    continue;
                }

                if (first) {
                    first = false;
                } else {
                    out.write(',');
                }

                out.write(event.getBytes());
            }
            out.write(']');
            final byte[] data = out.toByteArray();
            output.write(data);
        } finally {
            output.close();
        }
    }

    @Override
    protected String getEndpointPrefix() {
        return ENDPOINT_URL_PATH;
    }
}

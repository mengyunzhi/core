package com.mengyunzhi.core.log;

import ch.qos.logback.ext.loggly.LogglyAppender;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HttpAppender<E> extends LogglyAppender<E> {
    public static final String ENDPOINT_URL_PATH = "inputs/";
    public static Calendar lastSendTime = Calendar.getInstance();
    public static List<String> events = new ArrayList<>();

    public HttpAppender() {
    }

    @Override
    protected void append(E eventObject) {
        String msg = this.layout.doLayout(eventObject);
        postToLoggly(msg);
    }

    private void postToLoggly(final String event) {
        events.add(event);
        Calendar calendar = Calendar.getInstance();
        if (events.size() >= 100 || (calendar.getTimeInMillis() - lastSendTime.getTimeInMillis() >= 6000)) {
            try {
                List<String> sendEvents = events;
                events = new ArrayList<>();

                assert endpointUrl != null;
                URL endpoint = new URL(endpointUrl);
                final HttpURLConnection connection;
                if (proxy == null) {
                    connection = (HttpURLConnection) endpoint.openConnection();
                } else {
                    connection = (HttpURLConnection) endpoint.openConnection(proxy);
                }
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.addRequestProperty("Content-Type", this.layout.getContentType());
                connection.connect();
                sendAndClose(sendEvents, connection.getOutputStream());
                connection.disconnect();
                final int responseCode = connection.getResponseCode();
                if (responseCode != 200) {
                    final String message = readResponseBody(connection.getInputStream());
                    addError("Loggly post failed (HTTP " + responseCode + ").  Response body:\n" + message);
                }
            } catch (final IOException e) {
                addError("IOException while attempting to communicate with Loggly", e);
            }
        }

        lastSendTime = calendar;

    }

    private void sendAndClose(final List<String> events, final OutputStream output) throws IOException {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(events);
            byte[] bytes = bos.toByteArray();
            output.write(bytes);
        } finally {
            output.close();
        }
    }

    @Override
    protected String getEndpointPrefix() {
        return ENDPOINT_URL_PATH;
    }
}

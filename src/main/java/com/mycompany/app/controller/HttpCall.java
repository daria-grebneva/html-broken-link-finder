package com.mycompany.app.controller;

import javafx.util.Pair;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Properties;
import java.util.concurrent.Callable;

public class HttpCall implements Callable<Response> {

    public HttpCall(String url) {
        this.url = url;
    }

    public Response call() throws IOException {
        Pair<Integer, String> status = getStatus();
        return new Response(url, status.getKey(), status.getValue());
    }

    private String url;

    private Pair<Integer, String> getStatus() throws IOException {
        FileInputStream fis;
        Properties property = new Properties();
        fis = new FileInputStream("src/main/java/resources/config.properties");
        property.load(fis);
        Integer connectionTimeout = new Integer(property.getProperty("connectionTimeout"));

        try {
            Connection.Response response = Jsoup.connect(this.url)
                    .ignoreContentType(true)
                    .ignoreHttpErrors(true)
                    .timeout(connectionTimeout)
                    .execute();
            return new Pair<>(response.statusCode(), response.statusMessage());
        } catch (SocketTimeoutException exception) {
            return new Pair<>(408, "Connection timed out");
        } catch (Exception exception) {
            return new Pair<>(400, "Bad request");
        }
    }
}

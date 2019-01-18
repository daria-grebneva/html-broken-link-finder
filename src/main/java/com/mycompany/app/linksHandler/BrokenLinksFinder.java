package com.mycompany.app.linksHandler;

import com.mycompany.app.controller.HttpCall;
import com.mycompany.app.controller.Response;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class BrokenLinksFinder {
    public BrokenLinksFinder(List<String> links) {
        this.links = links;
    }

    List<String> getLinks() {
        return this.links;
    }

    public List<Response> getBrokenLinks() throws InterruptedException, ExecutionException, IOException {
        FileInputStream fis;
        Properties property = new Properties();
        fis = new FileInputStream("src/main/java/resources/config.properties");
        property.load(fis);
        Integer threadsNumber = new Integer(property.getProperty("threadsNumber"));
        final ExecutorService service = Executors.newFixedThreadPool(threadsNumber);

        List<Future<Response>> httpCallsResult = service.invokeAll(addHttpCallsToList());
        service.shutdown();

        return addBrokenLinksToList(httpCallsResult);
    }

    private List<String> links;

    private final String STATUS_SUCCESS = "2";
    private final String STATUS_REDIRECT = "3";

    private List<Response> addBrokenLinksToList(List<Future<Response>> httpCallsResult) throws InterruptedException, ExecutionException {
        List<Response> brokenLinks = new ArrayList<>();
        for (final Future<Response> callResult : httpCallsResult) {
            Response httpResult = callResult.get();
            if (isLinkBroken(httpResult.getStatusCode())) {
                brokenLinks.add(httpResult);
            }
        }

        return brokenLinks;
    }

    private List<HttpCall> addHttpCallsToList() {
        List<HttpCall> httpCalls = new ArrayList<>();

        for (String link : links) {
            httpCalls.add(new HttpCall(link));
        }

        return httpCalls;
    }

    private Boolean isLinkBroken(Integer code) {
        String str = code.toString();
        String codeNumber = str.substring(0,1);
        return ((!codeNumber.equals(STATUS_SUCCESS)) && (!codeNumber.equals(STATUS_REDIRECT)));
    }
}
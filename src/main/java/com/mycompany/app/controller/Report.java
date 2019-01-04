package com.mycompany.app.controller;

import com.mycompany.app.linksHandler.BrokenLinksFinder;
import com.mycompany.app.linksHandler.LinksFinder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Report {
    public Report(String filename) throws FileNotFoundException {
        this.writer = new PrintWriter(new File(filename));
    }

    public void append(List<Response> brokenLinks) {
        for (Response brokenLink : brokenLinks) {
            StringBuilder builder = new StringBuilder();
            builder.append(brokenLink.getUrl());
            builder.append(';');
            builder.append(brokenLink.getStatusCode());
            builder.append(';');
            builder.append(brokenLink.getStatusMessage());
            builder.append('\n');
            this.writer.write(builder.toString());
            this.writer.flush();
        }
        finalize();
    }

    public static List<Response> getBrokenLinks(List<String> pages) throws Exception {
        List<Response> brokenLinks = new ArrayList<Response>();
        LinksFinder linksFinder = new LinksFinder();
        for (String page : pages) {
            List<String> links = linksFinder.getLinks(page);
            BrokenLinksFinder brokenLinksFinder = new BrokenLinksFinder(links);
            brokenLinks.addAll(brokenLinksFinder.getBrokenLinks());
        }
        return brokenLinks;
    }

    public static void printBrokenLinks(List<Response> brokenLinks, String outputFile) throws FileNotFoundException {
        Report writer = new Report(outputFile);
        writer.append(brokenLinks);
    }

    protected void finalize() {
        this.writer.close();
    }

    private PrintWriter writer;
}

package com.mycompany.app.controller;

import com.mycompany.app.linksHandler.BrokenLinksFinder;
import com.mycompany.app.linksHandler.LinksFinder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Report {
    public Report(String filename) {
        this.filename = filename;
    }

    public void append(List<Response> brokenLinks) {
        try (PrintWriter writer = new PrintWriter(new File(filename))){
            if (filename == null || filename.isEmpty()) {
                return;
            }
            for (Response brokenLink : brokenLinks) {
                StringBuilder builder = new StringBuilder();
                builder.append(brokenLink.getUrl());
                builder.append(';');
                builder.append(brokenLink.getStatusCode());
                builder.append(';');
                builder.append(brokenLink.getStatusMessage());
                builder.append('\n');
                writer.write(builder.toString());
                writer.flush();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static List<Response> getBrokenLinks(List<String> pages, ReaderState state) throws Exception {
        List<Response> brokenLinks = new ArrayList<>();
        LinksFinder linksFinder = new LinksFinder();
        for (String page : pages) {
            List<String> links = linksFinder.getLinks(page, state);
            BrokenLinksFinder brokenLinksFinder = new BrokenLinksFinder(links);
            brokenLinks.addAll(brokenLinksFinder.getBrokenLinks());
        }
        return brokenLinks;
    }

    public static void printBrokenLinks(List<Response> brokenLinks, String outputFile) {
        Report writer = new Report(outputFile);
        writer.append(brokenLinks);
    }

    private String filename;
}

package com.mycompany.app.linksHandler;

import com.mycompany.app.controller.ReaderState;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class LinksFinder {
    public LinksFinder() {
    }

    public List<String> getLinks(String htmlFileName, ReaderState state) throws IOException {
        List<String> links = new ArrayList<>();

        switch (state) {
            case READ_FILES:
                openDocument(htmlFileName);
                break;
            case READ_LINKS:
                connect(htmlFileName);
                break;
            case ERROR:
                throw new FileNotFoundException();
        }

        Map<Attribute, Elements> mapTags = getTags();
        for (Map.Entry<Attribute, Elements> entry : mapTags.entrySet()) {
            for (Element tag : entry.getValue()) {
                links.add(tag.attr("abs:" + ATTRIBUTES.get(entry.getKey())));
            }
        }

        return links;
    }

    private Document document;

    private final Map<Attribute, String> ATTRIBUTES = new LinkedHashMap<Attribute, String>() {{
        put(Attribute.HREF, "href");
        put(Attribute.SRC, "src");
    }};

    private void connect(String link) throws IOException {
        FileInputStream fis;
        Properties property = new Properties();
        fis = new FileInputStream("src/main/java/resources/config.properties");
        property.load(fis);
        Integer connectionTimeout = new Integer(property.getProperty("connectionTimeout"));
        Connection connection = Jsoup.connect(link)
                .timeout(connectionTimeout)
                .userAgent(property.getProperty("userAgent"));
        document = connection.get();
    }

    private void openDocument(String link) throws IOException {
        document = Jsoup.parse(new File(link), null);
    }

    private Map<Attribute, Elements> getTags() {
        Map<Attribute, Elements> mapTags = new LinkedHashMap<Attribute, Elements>();
        for (Map.Entry<Attribute, String> attribute : ATTRIBUTES.entrySet()) {
            mapTags.put(attribute.getKey(), document.getElementsByAttribute(attribute.getValue()));
        }

        return mapTags;
    }
}

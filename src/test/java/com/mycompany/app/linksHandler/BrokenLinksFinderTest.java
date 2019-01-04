package com.mycompany.app.linksHandler;

import com.mycompany.app.controller.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;

public class BrokenLinksFinderTest {

    @org.junit.Test
    public void checkBrokenLinksWithEmptyList() throws InterruptedException, ExecutionException, IOException {
        List<String> links = new ArrayList<String>();
        BrokenLinksFinder finder = new BrokenLinksFinder(links);
        assertTrue(finder.getBrokenLinks().isEmpty());
    }

    @org.junit.Test
    public void checkConstructor() {
        List<String> links = new ArrayList<String>();
        links.add("https://some-link-example");
        links.add("https://another-link-example");
        BrokenLinksFinder finder = new BrokenLinksFinder(links);
        assertEquals(finder.getLinks(), links);
    }

    @org.junit.Test
    public void checkBrokenLinks() throws InterruptedException, ExecutionException, IOException {
        List<String> links = new ArrayList<String>();
        links.add("https://www.ispringsolutions.com/products.html");
        links.add("https://www.ispringsolutions.com/getAllProducts.html");
        BrokenLinksFinder finder = new BrokenLinksFinder(links);
        List<Response> expected = new ArrayList<Response>();
        expected.add(new Response("https://www.ispringsolutions.com/getAllProducts.html", 404, "Not Found"));
        assertEquals(finder.getBrokenLinks(), expected );
    }
}
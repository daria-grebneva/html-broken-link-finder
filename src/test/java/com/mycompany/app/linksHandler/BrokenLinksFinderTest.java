package com.mycompany.app.linksHandler;

import com.mycompany.app.controller.Response;
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;
@RunWith(value = BlockJUnit4ClassRunner.class)
public class BrokenLinksFinderTest {

    private ByteArrayOutputStream output = new ByteArrayOutputStream();
    private final PrintStream old = System.out;

    @Before
    public void setUp() {
        System.setOut(new PrintStream(output));
    }

    @After
    public void tearDown() {
        System.out.flush();
        System.setOut(old);
    }

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
    public void checkClientErrorBrokenLinks() throws InterruptedException, ExecutionException, IOException {
        List<String> links = new ArrayList<String>();
        links.add("https://www.ispringsolutions.com/products.html");
        links.add("https://www.ispringsolutions.com/getAllProducts.html");
        BrokenLinksFinder finder = new BrokenLinksFinder(links);
        List<Response> expected = new ArrayList<Response>();
        expected.add(new Response("https://www.ispringsolutions.com/getAllProducts.html", 404, "Not Found"));
        assertEquals(finder.getBrokenLinks(), expected );
    }

    @org.junit.Test
    public void checkServerErrorBrokenLinks() throws InterruptedException, ExecutionException, IOException {
        List<String> links = new ArrayList<String>();
        links.add("https://httpstat.us/500");
        BrokenLinksFinder finder = new BrokenLinksFinder(links);
        List<Response> expected = new ArrayList<Response>();
        expected.add(new Response("https://httpstat.us/500", 500, "Internal Server Error"));
        assertEquals(finder.getBrokenLinks(), expected );
    }

    @org.junit.Test
    public void checkRedirectionLinks() throws InterruptedException, ExecutionException, IOException {
        List<String> links = new ArrayList<String>();
        links.add("https://httpstat.us/301");
        BrokenLinksFinder finder = new BrokenLinksFinder(links);
        List<Response> expected = new ArrayList<Response>();
        assertEquals(finder.getBrokenLinks(), expected );
    }
    @org.junit.Test
    public void checkSuccessLinks() throws InterruptedException, ExecutionException, IOException {
        List<String> links = new ArrayList<String>();
        links.add("https://httpstat.us/200");
        BrokenLinksFinder finder = new BrokenLinksFinder(links);
        List<Response> expected = new ArrayList<Response>();
        assertEquals(finder.getBrokenLinks(), expected );
    }

    @Test
    public void checkInformationLinks() {
        try {
            List<String> links = new ArrayList<>();
            links.add("https://httpstat.us/100");
            BrokenLinksFinder finder = new BrokenLinksFinder(links);
            finder.getBrokenLinks();
            Assert.assertEquals("522, Connection time out!\r\n", output.toString());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void checkLinkWithTimeout() {
        try {
            List<String> links = new ArrayList<>();
            links.add("https://httpstat.us/200?sleep=5000");
            BrokenLinksFinder finder = new BrokenLinksFinder(links);
            finder.getBrokenLinks();
            Assert.assertEquals("522, Connection time out!\r\n", output.toString());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
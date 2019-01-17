package com.mycompany.app.linksHandler;

import com.mycompany.app.controller.ReaderState;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

public class LinksFinderTest {

    @org.junit.Test
    public void checkNotEmptyLinksValue() throws IOException {
        LinksFinder links = new LinksFinder();
        List<String> returnValue = links.getLinks("https://www.ispringsolutions.com/products.html", ReaderState.READ_LINKS);
        assertFalse(returnValue.isEmpty());
    }

    @org.junit.Test
    public void checkLinksArray() throws IOException {
        LinksFinder links = new LinksFinder();
        List<String> returnValue = links.getLinks("https://www.ispringsolutions.com/products", ReaderState.READ_LINKS);
        assertEquals(returnValue.get(1), "https://fonts.googleapis.com/css?family=Roboto:400,500,700");
        assertEquals(returnValue.get(9), "https://www.ispringsolutions.com/ispring_bitrix/content/css/print.css");
        assertEquals(returnValue.get(10), "https://www.ispringsolutions.com/images/favicon_1.ico");
        assertEquals(returnValue.get(11), "https://www.ispringsolutions.com/ispring_bitrix/content/images/ispring-icon.png");
    }
}
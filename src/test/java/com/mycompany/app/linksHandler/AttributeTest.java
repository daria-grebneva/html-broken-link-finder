package com.mycompany.app.linksHandler;

import static org.junit.Assert.*;

public class AttributeTest {

    @org.junit.Test
    public void checkAttributes() {
        Attribute attribute = Attribute.HREF;
        assertEquals(attribute, Attribute.HREF);
        assertNotEquals(attribute, Attribute.SRC);
    }
}
package com.mycompany.app.controller;

import static org.junit.Assert.*;

public class ReaderTest {

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void checkEmptyInput() {
        String[] input = new String[0];
        Reader reader = new Reader(input);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void checkWrongInput() {
        String[] input = new String[1];
        input[0] = "http://some-input.html";
        Reader reader = new Reader(input);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void checkNotFullInput() {
        String[] input = new String[2];
        input[0] = "--files";
        input[1] = "http://some-input.html";
        Reader reader = new Reader(input);
    }

    @org.junit.Test
    public void checkInput() {
        String[] input = new String[4];
        input[0] = "--files";
        input[1] = "http://some-input.html";
        input[2] = "--out";
        input[3] = "report.csv";
        Reader reader = new Reader(input);
    }
}
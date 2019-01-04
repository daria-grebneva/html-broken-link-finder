package com.mycompany.app.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.Assert.*;

public class ReportTest {
    @org.junit.Test
    public void checkOutput() {
        try {
            final File expected = new File("testFiles\\expected.txt");
            Report reportWriter = new Report("testFiles\\output.txt");
            List<Response> links = new ArrayList<Response>();
            links.add(new Response("test.ru", 404, "Not found"));
            links.add(new Response("test.com", 400, "Bad request"));
            reportWriter.append(links);
            File output = new File("testFiles\\output.txt");
            Scanner scanner1 = new Scanner(expected);
            Scanner scanner2 = new Scanner(output);
            while (scanner1.hasNextLine()) {
                assertEquals(scanner1.nextLine(), scanner2.nextLine());
            }
        } catch (Exception exc) {
            exc.getMessage();
        }
    }
}
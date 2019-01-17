package com.mycompany.app;

import com.mycompany.app.controller.Reader;
import com.mycompany.app.controller.Report;
import com.mycompany.app.controller.Response;

import java.util.List;

public class App {
    public static void main(String[] args) {
        try {
            Reader reader = new Reader(args);
            List<Response> brokenLinks = Report.getBrokenLinks(reader.getPages(), reader.getReaderState());
            Report.printBrokenLinks(brokenLinks, reader.getOutputFile());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}

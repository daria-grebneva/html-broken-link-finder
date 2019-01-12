package com.mycompany.app.controller;

import java.util.ArrayList;
import java.util.List;

public class Reader {
    public Reader(String args[]) {
        ReaderState state = ReaderState.ERROR;
        boolean isWithoutOutFile = true;
        for (String element : args) {
            if (changeAutomateState(element) != ReaderState.ERROR) {
                state = changeAutomateState(element);
                continue;
            }
            switch (state) {
                case READ_FILES: {
                    this.pages.add(element);
                    continue;
                }
                case READ_LINKS: {
                    this.pages.add(element);
                    continue;
                }
                case READ_OUT: {
                    this.outputFile = element;
                    isWithoutOutFile = false;
                    continue;
                }
                case ERROR: {
                    throw new IllegalArgumentException("Wrong input");
                }
            }
        }
        if (isWithoutOutFile) {
            throw new IllegalArgumentException("Please, write out file");
        }
    }

    public List<String> getPages() {
        return this.pages;
    }

    public String getOutputFile() {
        return outputFile;
    }

    private List<String> pages = new ArrayList<String>();

    private String outputFile;

    private ReaderState changeAutomateState(String element) {
        if (element.equals("--files")) {
            return ReaderState.READ_FILES;
        } else if (element.equals("--links")) {
            return ReaderState.READ_LINKS;
        } else if (element.equals("--out")) {
            return ReaderState.READ_OUT;
        }
        return ReaderState.ERROR;
    }
}

package com.my.library;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {
    private static final Logger logger = LogManager.getLogger();

    public static void main(String[] args) {
        for (int i = 0; i < 1000; i++) {
            logger.log(Level.ERROR, "info log test");
        }

    }
}

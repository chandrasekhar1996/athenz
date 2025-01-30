package com.yahoo.athenz.common.server.notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class NotificationConverterCommon {
    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationConverterCommon.class);

    public static String readContentFromFile(ClassLoader classLoader, String fileName) {
        StringBuilder contents = new StringBuilder();
        URL resource = classLoader.getResource(fileName);
        if (resource != null) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(resource.openStream()))) {
                String line;
                while ((line = br.readLine()) != null) {
                    contents.append(line);
                    contents.append(System.getProperty("line.separator"));
                }
            } catch (IOException ex) {
                LOGGER.error("Could not read file: {}. Error message: {}", fileName, ex.getMessage());
            }
        }
        return contents.toString();
    }

}

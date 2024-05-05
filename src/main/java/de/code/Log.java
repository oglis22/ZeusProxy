package de.code;

import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Log {

    private String path;
    private Logger logger;
    private FileHandler fh;
    private SimpleFormatter formatter;

    public Log(String path) {
        this.path = path;
        this.logger = Logger.getLogger("logs");
        try{
            this.fh = new FileHandler(path);
            this.logger.addHandler(this.fh);
            this.formatter = new SimpleFormatter();
            this.fh.setFormatter(this.formatter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void log(String message) {

        logger.info(message);

    }

}

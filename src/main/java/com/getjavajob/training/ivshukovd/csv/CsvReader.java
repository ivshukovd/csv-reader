package com.getjavajob.training.ivshukovd.csv;

import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.concurrent.ArrayBlockingQueue;

import static java.lang.Integer.parseInt;
import static java.lang.Thread.currentThread;
import static org.slf4j.LoggerFactory.getLogger;

public class CsvReader implements Runnable {

    private final Logger logger = getLogger(CsvReader.class);
    private final ArrayBlockingQueue<CsvObject> queue;
    private final Path pathToCsvFile;
    private long numberLines;

    public CsvReader(ArrayBlockingQueue<CsvObject> queue, Path pathToCsvFile) {
        this.queue = queue;
        this.pathToCsvFile = pathToCsvFile;
    }

    @Override
    public void run() {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(pathToCsvFile.toString())))) {
            while (br.ready()) {
                String[] lineAsWords = br.readLine().split(",");
                CsvObject csvObject = new CsvObject(lineAsWords[0], parseInt(lineAsWords[1]));
                queue.put(csvObject);
                logger.info("Line read: {}", csvObject);
                logger.info("Number of lines read: {}", ++numberLines);
            }
            queue.put(new CsvObject("done", -1));
        } catch (IOException e) {
            logger.warn("IOException {}", e.getMessage());
        } catch (InterruptedException e) {
            currentThread().interrupt();
            logger.warn("Reader has not finished work, {} lines was read", numberLines);
        }
    }

}
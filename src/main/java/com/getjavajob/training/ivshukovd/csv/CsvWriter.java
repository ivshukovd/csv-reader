package com.getjavajob.training.ivshukovd.csv;

import org.slf4j.Logger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.ArrayBlockingQueue;

import static java.lang.Thread.currentThread;
import static org.slf4j.LoggerFactory.getLogger;

public class CsvWriter implements Runnable {

    private final Logger logger = getLogger(CsvWriter.class);
    private final ArrayBlockingQueue<CsvObject> queue;
    private final Path pathToTargetDirectory;
    private long numberLines;

    public CsvWriter(ArrayBlockingQueue<CsvObject> queue, Path pathToTargetDirectory) {
        this.queue = queue;
        this.pathToTargetDirectory = pathToTargetDirectory;
    }

    @Override
    public void run() {
        CsvObject csvObject;
        try (BufferedWriter bufferedWriter = new BufferedWriter(
                new FileWriter(pathToTargetDirectory.toString() + "\\result.txt"))) {
            do {
                csvObject = queue.take();
                if (csvObject.equals(new CsvObject("done", -1))) {
                    break;
                }
                bufferedWriter.write(csvObject.toString());
                bufferedWriter.newLine();
                logger.info("Line write: {}", csvObject);
                logger.info("Number of lines written: {}", ++numberLines);
            } while (true);
        } catch (InterruptedException e) {
            currentThread().interrupt();
            logger.warn("Writer has not finished work");
        } catch (IOException e) {
            logger.warn("IOException {}", e.getMessage());
        }
    }

}
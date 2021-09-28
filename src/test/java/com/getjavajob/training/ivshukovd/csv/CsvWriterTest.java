package com.getjavajob.training.ivshukovd.csv;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.concurrent.ArrayBlockingQueue;

import static java.nio.file.Paths.get;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CsvWriterTest {

    private ArrayBlockingQueue<CsvObject> queue;
    private Path targetDirectory;

    @BeforeEach
    void init() throws InterruptedException {
        queue = new ArrayBlockingQueue<>(10);
        for (int i = 0; i < 9; i++) {
            queue.put(new CsvObject("name", i));
        }
        queue.put(new CsvObject("done", -1));
        targetDirectory = get("src/test/resources");
    }

    @Test
    void testRun() throws IOException {
        Thread writer = new Thread(new CsvWriter(queue, targetDirectory));
        writer.run();
        assertEquals(0, queue.size());
        File resultFile = new File("src/test/resources/result.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(resultFile.getPath())));
        int resultFileSize = 0;
        while (br.readLine() != null) {
            resultFileSize++;
        }
        br.close();
        assertEquals(9, resultFileSize);
        assertTrue(resultFile.delete());
    }

}
package com.getjavajob.training.ivshukovd.csv;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Path;
import java.util.concurrent.ArrayBlockingQueue;

import static java.nio.file.Paths.get;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CsvReaderTest {

    private ArrayBlockingQueue<CsvObject> queue;
    private Path sourceFile;

    @BeforeEach
    void init() {
        queue = new ArrayBlockingQueue<>(10);
        sourceFile = get(new File("src/test/resources/input.csv").getPath());
    }

    @Test
    void testRun() {
        Thread reader = new Thread(new CsvReader(queue, sourceFile));
        reader.run();
        assertEquals(8, queue.size());
    }

}
package com.getjavajob.training.ivshukovd.csv;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.concurrent.ArrayBlockingQueue;

import static java.lang.System.out;
import static java.lang.Thread.currentThread;
import static java.nio.file.Files.exists;
import static java.nio.file.Paths.get;

public class Runner {

    public static void main(String[] args) throws IOException {
        boolean sourceFileExists = false;
        Path sourceCsvFile;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        do {
            out.println("Enter path to source csv file:");
            sourceCsvFile = get(reader.readLine());
            if (!exists(sourceCsvFile)) {
                out.println("File does not exist");
            } else if (!sourceCsvFile.toString().endsWith(".csv")) {
                out.println("It is not csv file");
            } else {
                sourceFileExists = true;
            }
        } while (!sourceFileExists);

        boolean targetDirectoryExists = false;
        Path targetDirectory;
        do {
            out.println("Enter target directory to put result file:");
            targetDirectory = get(reader.readLine());
            if (!exists(targetDirectory)) {
                out.println("Directory does not exist");
            } else {
                targetDirectoryExists = true;
            }
        } while (!targetDirectoryExists);
        reader.close();

        ArrayBlockingQueue<CsvObject> queue = new ArrayBlockingQueue<>(1000);
        Thread t1 = new Thread(new CsvReader(queue, sourceCsvFile));
        Thread t2 = new Thread(new CsvWriter(queue, targetDirectory));
        t1.start();
        t2.start();
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            currentThread().interrupt();
        }
    }

}
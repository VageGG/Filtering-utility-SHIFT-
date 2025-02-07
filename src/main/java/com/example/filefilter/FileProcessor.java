package com.example.filefilter;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Processes input files and categorizes their contents into different data types.
 * Supports writing categorized data to output files and printing statistics.
 */
public class FileProcessor {
    private final Config config;
    private final List<BigInteger> integers = new ArrayList<>();
    private final List<BigDecimal> floats = new ArrayList<>();
    private final List<String> strings = new ArrayList<>();

    /**
     * Constructs a FileProcessor with the given configuration.
     * @param config Configuration object with user-defined settings
     */
    public FileProcessor(Config config) {
        this.config = config;
    }

    /**
     * Reads input files line by line, classifies data, and writes categorized content to output files.
     */
    public void process() {
        List<BufferedReader> readers = new ArrayList<>();

        try {
            for (String fileName : config.getInputFiles()) {
                try {
                    readers.add(new BufferedReader(new FileReader(fileName)));
                } catch (FileNotFoundException e) {
                    System.err.println("File not found: " + fileName);
                }
            }

            boolean hasData;
            do {
                hasData = false;

                for (BufferedReader reader : readers) {
                    try {
                        String line = reader.readLine();
                        if (line != null) {
                            classifyAndStore(line);
                            hasData = true;
                        }
                    } catch (IOException e) {
                        System.err.println("Error reading file: " + e.getMessage());
                    }
                }
            } while (hasData);

        } catch (Exception e) {
            System.err.println("Unexpected error during file processing: " + e.getMessage());
        } finally {
            for (BufferedReader reader : readers) {
                try {
                    reader.close();
                } catch (IOException ignored) {}
            }
        }

        writeOutput();
        if (config.isShowStats()) {
            printStatistics();
        }
    }

    /**
     * Classifies and stores the given line into integers, floats, or strings.
     *
     * @param line the input line to be classified
     */
    private void classifyAndStore(String line) {
        try {
            integers.add(new BigInteger(line));
        } catch (NumberFormatException e1) {
            try {
                floats.add(new BigDecimal(line));
            } catch (NumberFormatException e2) {
                strings.add(line);
            }
        }
    }

    /**
     * Writes the categorized data to output files.
     */
    private void writeOutput() {
        writeToFile("integers.txt", integers);
        writeToFile("floats.txt", floats);
        writeToFile("strings.txt", strings);
    }

    /**
     * Writes a list of data to a specified file. If the output directory does not exist, it is created automatically.
     *
     * @param fileName the name of the output file
     * @param data     the list of data to be written
     */
    private void writeToFile(String fileName, List<?> data) {
        if (data.isEmpty()) return;

        File directory = new File(config.getOutputDirectory());
        if (!directory.exists() && !directory.mkdirs()) {
            System.err.println("Failed to create directory: " + config.getOutputDirectory());
            return;
        }

        String outputFile = config.getOutputDirectory() + "/" + config.getPrefix() + fileName;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile, config.isAppendMode()))) {
            for (Object item : data) {
                writer.write(item.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing to file: " + outputFile + " - " + e.getMessage());
        }
    }

    /**
     * Prints statistical information about the processed data.
     */
    private void printStatistics() {
        System.out.println("Statistics:");

        boolean hasIntegers = !integers.isEmpty();
        boolean hasFloats = !floats.isEmpty();
        boolean hasStrings = !strings.isEmpty();

        if (hasIntegers) {
            System.out.println("Integers: " + integers.size());
        }
        if (hasFloats) {
            System.out.println("Floats: " + floats.size());
        }
        if (hasStrings) {
            System.out.println("Strings: " + strings.size());
        }

        if (config.isFullStats()) {
            if (!integers.isEmpty()) {
                BigInteger min = Collections.min(integers);
                BigInteger max = Collections.max(integers);
                BigInteger sum = integers.stream().reduce(BigInteger.ZERO, BigInteger::add);
                double avg = integers.stream().mapToDouble(BigInteger::doubleValue).average().orElse(0);
                System.out.println("Integers - Min: " + min + ", Max: " + max + ", Sum: " + sum + ", Avg: " + avg);
            }

            if (!floats.isEmpty()) {
                BigDecimal min = Collections.min(floats);
                BigDecimal max = Collections.max(floats);
                BigDecimal sum = floats.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
                double avg = floats.stream().mapToDouble(BigDecimal::doubleValue).average().orElse(0);
                System.out.println("Floats - Min: " + min + ", Max: " + max + ", Sum: " + sum + ", Avg: " + avg);
            }

            if (!strings.isEmpty()) {
                String shortest = strings.stream().min(Comparator.comparingInt(String::length)).orElse("");
                String longest = strings.stream().max(Comparator.comparingInt(String::length)).orElse("");
                System.out.println("Strings - Shortest length: " + shortest.length() + ", Longest length: " + longest.length());
            }
        }
    }
}

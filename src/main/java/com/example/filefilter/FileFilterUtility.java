/**
 * Main class for the File Filtering Utility.
 * This utility processes input files and categorizes their contents into integers, floats, and strings.
 */
package com.example.filefilter;

/**
 * Entry point for the application.
 * It parses command-line arguments and initiates file processing.
 */
public class FileFilterUtility {
    public static void main(String[] args) {
        Config config = Config.parseArgs(args);
        if (config == null || config.getInputFiles().isEmpty()) {
            System.out.println("Usage: java -jar util.jar [options] <input files>");
            return;
        }

        new FileProcessor(config).process();
    }
}
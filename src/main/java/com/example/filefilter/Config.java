package com.example.filefilter;

import java.util.ArrayList;
import java.util.List;

/**
 * Configuration class for parsing command-line arguments.
 * Supports options for output directory, file prefix, append mode, and statistics display.
 */
public class Config {
    private String outputDirectory = ".";
    private String prefix = "";
    private boolean appendMode = false;
    private boolean showStats = false;
    private boolean fullStats = false;
    private final List<String> inputFiles = new ArrayList<>();

    /**
     * Parses command-line arguments and initializes the configuration.
     * @param args Command-line arguments
     * @return Config object with parsed settings
     */
    public static Config parseArgs(String[] args) {
        Config config = new Config();
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-o":
                    if (i + 1 < args.length) config.outputDirectory = args[++i];
                    break;
                case "-p":
                    if (i + 1 < args.length) config.prefix = args[++i];
                    break;
                case "-a":
                    config.appendMode = true;
                    break;
                case "-s":
                    config.showStats = true;
                    break;
                case "-f":
                    config.fullStats = true;
                    break;
                default:
                    config.inputFiles.add(args[i]);
            }
        }
        return config;
    }

    public boolean isFullStats() {
        return fullStats;
    }

    public String getOutputDirectory() {
        return outputDirectory;
    }

    public String getPrefix() {
        return prefix;
    }

    public boolean isAppendMode() {
        return appendMode;
    }

    public boolean isShowStats() {
        return showStats;
    }

    public List<String> getInputFiles() {
        return inputFiles;
    }
}

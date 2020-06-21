package com.mandelbrot;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.ParseException;

public class CommandArgs {
    private int width = 640;
    private int height = 480;
    private double frameBottom = -2.0;
    private double frameTop = 2.0;
    private double frameLeft = -2.0;
    private double frameRight = 2.0;
    private int numThreads = 1;
    private int granularity = 1;
    private String outputFile = "zad19.png";
    private boolean quiet = false;
    private boolean help = false;

    public CommandArgs(String[] args) throws ParseException {
        CommandLineParser parser = new DefaultParser();
        Options options = getOptions();
        CommandLine line = parser.parse(options, args);

        readSize(line);
        readFrame(line);
        readNumThreads(line);
        readImage(line);
        readQuiet(line);
        readGranularity(line);

        readHelp(line, options);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public double getFrameBottom() {
        return frameBottom;
    }

    public double getFrameTop() {
        return frameTop;
    }

    public double getFrameLeft() {
        return frameLeft;
    }

    public double getFrameRight() {
        return frameRight;
    }

    public int getNumThreads() {
        return numThreads;
    }

    public int getGranularity() {
        return granularity;
    }

    public String getOutputFile() {
        return outputFile;
    }

    public boolean getQuiet() {
        return quiet;
    }

    public boolean getHelp() {
        return help;
    }

    private Options getOptions() {
        Options options = new Options();

        options.addOption("s", "size", true, "The size of the generated image in pixels.");
        options.addOption("r", "rect", true,
                "The size of the frame. Interpretted as left:right:bottom:top. An example usage is `-2.0:2.0:-1.0:1.0`");
        options.addOption("t", "tasks", true, "The number of threads to run the program with.");
        options.addOption("o", "output", true, "The file path of the output image.");
        options.addOption("q", "quiet", false, "Specifies if program should output logs.");
        options.addOption("g", "granularity", true, "Specifies the granularity of the program. For example 1, 2, 4 or 8.");
        options.addOption("h", "help", false, "Prints the program options.");

        return options;
    }

    private void readSize(CommandLine line) {
        if (line.hasOption("s")) {
            String size = line.getOptionValue("s");
            String[] parts = size.split("x");
            width = Integer.parseInt(parts[0]);
            height = Integer.parseInt(parts[1]);
        }
    }

    private void readFrame(CommandLine line) {
        if (line.hasOption("r")) {
            String size = line.getOptionValue("r");
            String[] parts = size.split(":");

            frameLeft = Double.parseDouble(parts[0]);
            frameRight = Double.parseDouble(parts[1]);
            frameBottom = Double.parseDouble(parts[2]);
            frameTop = Double.parseDouble(parts[3]);
        }
    }

    private void readNumThreads(CommandLine line) {
        if (line.hasOption("t")) {
            numThreads = Integer.parseInt(line.getOptionValue("t"));
        }
    }

    private void readImage(CommandLine line) {
        if (line.hasOption("o")) {
            outputFile = line.getOptionValue("o");
        }
    }

    private void readQuiet(CommandLine line) {
        if (line.hasOption("q")) {
            quiet = true;
        }
    }

    private void readGranularity(CommandLine line) {
        if (line.hasOption("g")) {
            granularity = Integer.parseInt(line.getOptionValue("g"));
        }
    }

    private void readHelp(CommandLine line, Options options) {
        if (line.hasOption("h")) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("mandelbrot", options);

            help = true;
        }
    }
}
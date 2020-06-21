package com.mandelbrot;

import org.apache.commons.cli.ParseException;

public class App {
    public static void main(String[] args) {
        try {
            long startTime = System.currentTimeMillis();
            CommandArgs cmdArgs = new CommandArgs(args);

            if (cmdArgs.getHelp()) {
                return;
            }

            Image image = new Image(cmdArgs.getWidth(), cmdArgs.getHeight());
            Frame frame = new Frame(cmdArgs.getFrameBottom(), cmdArgs.getFrameTop(), cmdArgs.getFrameLeft(),
                    cmdArgs.getFrameRight());

            MandelbrotMaster master = new MandelbrotMaster(image, frame, cmdArgs.getQuiet());
            master.generate(cmdArgs.getGranularity(), cmdArgs.getNumThreads());
            image.writePNGinRGB(cmdArgs.getOutputFile());

            long endTime = System.currentTimeMillis();
            long totalTime = endTime - startTime;

            if (!cmdArgs.getQuiet()) {
                System.out.println("Threads used in current run: " + cmdArgs.getNumThreads());
                System.out.println("Granularity used in current run: " + cmdArgs.getGranularity());
            }

            System.out.println("Total execution time for current run (millis): " + totalTime);

        } catch (ParseException ex) {
            System.err.println("Parsing failed. Reason: " + ex.getMessage());
        } catch (Exception ex) {
            System.err.println("Error: " + ex.getMessage());
        }
    }
}

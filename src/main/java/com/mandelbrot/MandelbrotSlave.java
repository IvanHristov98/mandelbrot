package com.mandelbrot;

import java.lang.Math;
import java.util.concurrent.BlockingQueue;

import org.apache.commons.math3.complex.Complex;

public class MandelbrotSlave implements Runnable {
    public static int DEFAULT_NUM_ITERATIONS = 255;
    public static double DEFAULT_ESCAPE_RADIUS = 500.0;

    private Image image;
    private Segment segment;
    private int id;
    private BlockingQueue<Integer> slaveQueue;
    private boolean isQuiet;

    public MandelbrotSlave(Image image, Segment segment, int id, BlockingQueue<Integer> slaveQueue, boolean isQuiet) {
        this.image = image;
        this.segment = segment;
        this.id = id;
        this.slaveQueue = slaveQueue;
        this.isQuiet = isQuiet;
    }

    public void run() {
        long startTime = System.currentTimeMillis();

        if (!isQuiet) {
            System.out.println("Thread-" + id + " started.");
        }

        generate(segment.frame, DEFAULT_NUM_ITERATIONS);

        try {
            slaveQueue.put(id);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        if (!isQuiet) {
            System.out.println("Thread-" + id + " stopped.");

            long endTime = System.currentTimeMillis();
            long totalTime = endTime - startTime;
            System.out.println("Thread-" + id + " execution time was (millis): " + totalTime);
        }
    }

    public int getID() {
        return id;
    }

    private void generate(Frame frame, int maxIterations) {
        for (int i = 0; i < segment.height; i++) {
            for (int j = 0; j < image.getWidth(); j++) {
                double x = pixToPos(frame.left, frame.right, image.getWidth(), j);
                double y = pixToPos(frame.bottom, frame.top, (int) Math.floor(segment.height), i);

                int depth = testInMandelbrot(x, y, maxIterations);

                drawPixel(i + (int) Math.floor(segment.topOffset), j, depth);
            }
        }
    }

    private double pixToPos(double low, double high, int lengthInPixels, int pixelsOffset) {
        double length = high - low;
        double fractionLength = length / lengthInPixels;

        return fractionLength * pixelsOffset + low;
    }

    private int testInMandelbrot(double x, double y, int maxIterations) {
        Complex z = new Complex(0.0, 0.0);
        Complex c = new Complex(x, y);
        Complex e = new Complex(Math.E, 0);

        for (int i = 0; i < maxIterations; i++) {
            if (z.getReal() != 0.0 || z.getImaginary() != 0.0) {
                z = e.pow(z.pow(2.0).add(c));
            } else {
                z = e.pow(z.add(c));
            }

            if (magnitude(z) > DEFAULT_ESCAPE_RADIUS) {
                return i;
            }
        }

        return maxIterations;
    }

    private double magnitude(Complex num) {
        return Math.sqrt(num.getReal() * num.getReal() + num.getImaginary() * num.getImaginary());
    }

    private synchronized void drawPixel(int topOffset, int leftOffset, int depth) {
        int r = 0;
        int g = 0;
        int b = 0;

        if (depth <= 10) {
            r = (10 - depth) * 25;
            b = (10 - depth) * 25 + 25;
            g = (10 - depth) * 25;
        } else if (depth <= 15) {
            r = (15 - depth) * 35;
            b = depth * 35;
        } else if (depth <= 40) {
            r = (10 - depth) * 25;
            b = depth * 25;
        } else {
            r = 0;
            g = 0;
            b = 0;
        }

        image.writePixel(topOffset, leftOffset, new RGB(ceilColor(r), ceilColor(g), ceilColor(b)));
    }

    private byte ceilColor(int col) {
        if (col > 255) {
            return (byte) 255;
        }

        return (byte) col;
    }
}
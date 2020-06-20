package com.mandelbrot;

import java.lang.Math;

import org.apache.commons.math3.complex.Complex;

public class Mandelbrot {
    public static int DEFAULT_NUM_ITERATIONS = 255;
    public static double DEFAULT_ESCAPE_RADIUS = 2.0;

    private Image image;

    public Mandelbrot(Image image) {
        this.image = image;
    }

    public void generate(Frame frame, int maxIterations) {
        for (int i = 0; i < image.getHeight(); i++) {
            for (int j = 0; j < image.getWidth(); j++) {
                double x = pixToPos(frame.left, frame.right, image.getWidth(), j);
                double y = pixToPos(frame.bottom, frame.top, image.getHeight(), i);

                int depth = testInMandelbrot(x, y, maxIterations);

                drawPixel(i, j, (byte) depth);
            }
        }
    }

    public void writeImage(String filePath) throws Exception {
        image.writePNGinRGB(filePath);
    }

    private double pixToPos(double low, double high, int lengthInPixels, int pixelsOffset) {
        double length = high - low;
        double fractionLength = length / lengthInPixels;

        return fractionLength * pixelsOffset + low;
    }

    private int testInMandelbrot(double x, double y, int maxIterations) {
        Complex currVal = new Complex(0.0, 0.0);
        Complex addition = new Complex(x, y);

        for (int i = 0; i < maxIterations; i++) {
            if (currVal.getReal() != 0.0 || currVal.getImaginary() != 0.0) {
                currVal = currVal.pow(2).add(addition);
            } else {
                currVal = currVal.add(addition);
            }

            if (magnitude(currVal) > DEFAULT_ESCAPE_RADIUS) {
                return i;
            }
        }

        return maxIterations;
    }

    private double magnitude(Complex num) {
        return Math.sqrt(Math.pow(num.getReal(), 2) + Math.pow(num.getImaginary(), 2));
    }

    private void drawPixel(int topOffset, int leftOffset, byte depth) {
        byte depthComplement = (byte) (255 - depth);
        RGB pixelColor = new RGB(depthComplement, (byte) (depthComplement * 2), (byte) (depthComplement * 3));

        image.writePixel(topOffset, leftOffset, pixelColor);
    }
}
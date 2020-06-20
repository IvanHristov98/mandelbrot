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

    private void drawPixel(int topOffset, int leftOffset, byte depth) {
        byte depthComplement = (byte) (255 - depth);
        RGB pixelColor = new RGB(depthComplement, (byte) (depthComplement * 2), (byte) (depthComplement * 3));

        image.writePixel(topOffset, leftOffset, pixelColor);
    }
}
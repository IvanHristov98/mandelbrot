package com.mandelbrot.cmd;

import com.mandelbrot.image.Image;

public class App {
    public static void main(String[] args) {
        byte[] imageAsBytes = { 0xa, 0x2, 0xc, (byte) 0xff, (byte) 0xff, (byte) 0xfc };
        int width = 1;
        int height = 2;

        Image image = new Image(width, height);

        try {
            image.writePNGinRGB(imageAsBytes, "image.png");
        } catch(Exception ex) {
            System.out.println(ex.getMessage());
            System.exit(1);
        }
    }
}

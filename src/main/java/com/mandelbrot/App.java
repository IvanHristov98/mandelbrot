package com.mandelbrot;

public class App {
    public static void main(String[] args) {
        Image image = new Image(640, 480);
        Mandelbrot mandelbrot = new Mandelbrot(image);
        Frame frame = new Frame(-1.0, 1.0, -1.0, 1.0);
        mandelbrot.generate(frame, 255);

        try {
            mandelbrot.writeImage("image.png");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.exit(1);
        }
    }
}

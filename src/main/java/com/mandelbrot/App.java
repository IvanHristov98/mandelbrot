package com.mandelbrot;


public class App {
    public static void main(String[] args) {
        Image image = new Image(640, 480);
        Frame frame = new Frame(-2.0, 2.0, -2.0, 2.0);

        MandelbrotMaster master = new MandelbrotMaster(image, frame);

        try {
            master.generate(4, 4);
            image.writePNGinRGB("image.png");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.exit(1);
        }
    }
}

package com.mandelbrot;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import java.awt.Point;
import java.awt.Transparency;
import java.awt.image.Raster;
import java.awt.image.DataBuffer;
import java.awt.image.ColorModel;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.awt.image.DataBufferByte;
import java.awt.image.ComponentColorModel;

public class Image {
    public static final String PNG = "png";

    private int width;
    private int height;
    private byte[] imageAsBytes;

    public Image(int width, int height) {
        this.width = width;
        this.height = height;

        imageAsBytes = new byte[width * height * 3];
    }

    public void writePNGinRGB(String imagePath) throws Exception {
        if (imageAsBytes.length != width * height * 3) {
            throw new Exception("Image size to data mismatch.");
        }

        DataBuffer buffer = new DataBufferByte(imageAsBytes, imageAsBytes.length);

        WritableRaster raster = Raster.createInterleavedRaster(buffer, width, height, 3 * width, 3,
                new int[] { 0, 1, 2 }, (Point) null);
        ColorModel cm = new ComponentColorModel(ColorModel.getRGBdefault().getColorSpace(), false, true,
                Transparency.OPAQUE, DataBuffer.TYPE_BYTE);

        BufferedImage image = new BufferedImage(cm, raster, true, null);

        try {
            ImageIO.write(image, PNG, new File(imagePath));
        } catch (IOException ex) {
            throw new Exception(ex.getMessage(), ex);
        }
    }

    public void writePixel(int topOffset, int leftOffset, RGB color) {
        int pos = topOffset * width * 3 + (width - leftOffset) * 3;
        pos = imageAsBytes.length - pos;

        imageAsBytes[pos] = color.r;
        imageAsBytes[pos + 1] = color.g;
        imageAsBytes[pos + 2] = color.b;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
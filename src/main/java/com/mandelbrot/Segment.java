package com.mandelbrot;

public class Segment {
    public int topOffset;
    public int height;
    public Frame frame;

    public Segment(int topOffset, int height, Frame frame) {
        this.topOffset = topOffset;
        this.height = height;
        this.frame = frame;
    }
}
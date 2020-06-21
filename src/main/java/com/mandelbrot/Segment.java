package com.mandelbrot;

public class Segment {
    public double topOffset;
    public double height;
    public Frame frame;

    public Segment(double topOffset, double height, Frame frame) {
        this.topOffset = topOffset;
        this.height = height;
        this.frame = frame;
    }
}
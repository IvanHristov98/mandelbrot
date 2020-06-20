package com.mandelbrot;

public class MandelbrotMaster {
    private Image image;
    private Frame frame;

    public MandelbrotMaster(Image image, Frame frame) {
        this.image = image;
        this.frame = frame;
    }

    public void generate(int granularity) {
        double frameSegmentHeight = getFrameHeight() / granularity;
        int imageSegmentHeight = image.getHeight() / granularity;

        for (int i = 0; i < granularity; i++) {
            Frame slaveFrame = new Frame(frame.bottom + frameSegmentHeight * i,
                    frame.bottom + frameSegmentHeight * (i + 1), frame.left, frame.right);
            Segment slaveSegment = new Segment(0 + imageSegmentHeight * i, imageSegmentHeight, slaveFrame);
            MandelbrotSlave slave = new MandelbrotSlave(image, slaveSegment);

            slave.run();
        }
    }

    private double getFrameHeight() {
        return frame.top - frame.bottom;
    }
}
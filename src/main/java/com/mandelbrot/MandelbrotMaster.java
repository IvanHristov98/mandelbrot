package com.mandelbrot;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class MandelbrotMaster {
    private Image image;
    private Frame frame;
    private boolean isQuiet;

    public MandelbrotMaster(Image image, Frame frame, boolean isQuiet) {
        this.image = image;
        this.frame = frame;
        this.isQuiet = isQuiet;
    }

    public void generate(int granularity, int processors) throws InterruptedException {
        int numTasks = granularity * processors;
        double frameSegmentHeight = getFrameHeight() / numTasks;
        int imageSegmentHeight = (int) Math.ceil((double) (image.getHeight()) / numTasks);

        BlockingQueue<Integer> slaveQueue = new LinkedBlockingQueue<>(processors);

        for (int i = 0; i < processors; i++) {
            slaveQueue.put(i);
        }

        int cnt = 0;

        while (true) {
            if (cnt == numTasks) {
                break;
            }

            int slaveID = slaveQueue.take();

            Frame slaveFrame = new Frame(frame.bottom + frameSegmentHeight * cnt,
                    frame.bottom + frameSegmentHeight * (cnt + 1), frame.left, frame.right);
            Segment slaveSegment = new Segment(imageSegmentHeight * cnt, imageSegmentHeight, slaveFrame);
            MandelbrotSlave slave = new MandelbrotSlave(image, slaveSegment, slaveID, slaveQueue, isQuiet);

            if (slaveSegment.topOffset + slaveSegment.height >= image.getHeight()) {
                slaveSegment.height = image.getHeight() - slaveSegment.topOffset;

                // Break cycle.
                cnt = numTasks - 1;
            }

            Thread slaveThread = new Thread(slave);
            slaveThread.start();

            cnt++;
        }

        cnt = 0;

        while (true) {
            slaveQueue.take();
            cnt++;

            if (cnt == processors) {
                break;
            }
        }
    }

    private double getFrameHeight() {
        return frame.top - frame.bottom;
    }
}
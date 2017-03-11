package com.dbpiper.gameloop;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

/**
 * Created by suerg on 3/11/2017.
 */

public class MainThread extends Thread {
    /* constants */
    public static final int MAX_FPS = 30;

    /* private members */
    private double mAverageFps;
    private SurfaceHolder mSurfaceHolder;
    private GamePanel mGamePanel;
    private boolean mRunning;

    /* public members */
    public static Canvas sCanvas;

    public void setRunning(boolean running) {
        mRunning = running;
    }

    public MainThread(SurfaceHolder surfaceHolder, GamePanel gamePanel) {
        mSurfaceHolder = surfaceHolder;
        mGamePanel = gamePanel;
    }

    @Override
    public void run() {
        long startTime;
        long timeMillis = 1000/MAX_FPS;
        long waitTime;
        int frameCount = 0;
        long totalTime = 0;
        long targetTime = 1000/MAX_FPS;

        while (mRunning) {
            startTime = System.nanoTime();
            sCanvas = null;

            try {
                sCanvas = this.mSurfaceHolder.lockCanvas();
                synchronized (mSurfaceHolder) {
                    this.mGamePanel.update();
                    this.mGamePanel.draw(sCanvas);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (sCanvas != null) {
                    try {
                        mSurfaceHolder.unlockCanvasAndPost(sCanvas);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            timeMillis = (System.nanoTime() - startTime)/1000000;
            waitTime = targetTime - timeMillis;
            try {
                if (waitTime > 0) {
                    this.sleep(waitTime);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            totalTime += System.nanoTime() - startTime;
            frameCount++;

            if (frameCount == MAX_FPS) {
                mAverageFps = 1000/((totalTime/frameCount)/1000000);
                frameCount = 0;
                totalTime = 0;
                System.out.println(mAverageFps);
            }
        }
    }
}

package com.dbpiper.gameloop;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

/**
 * Created by suerg on 3/11/2017.
 */

class GamePanel extends SurfaceView implements SurfaceHolder.Callback {

    private MainThread mThread;
    private RectPlayer mPlayer;
    private Point mPlayerPoint;

    public GamePanel(Context context) {
        super(context);
        getHolder().addCallback(this);
        mThread = new MainThread(getHolder(), this);
        mPlayer = new RectPlayer(new Rect(100, 100, 400, 400), Color.rgb(255, 0, 0));
        mPlayerPoint = new Point(250, 250);
        setFocusable(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mThread = new MainThread(getHolder(), this);
        mThread.setRunning(true);
        mThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                mThread.setRunning(false);
                mThread.join();
            } catch (Exception e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getAction()) {
        case MotionEvent.ACTION_DOWN:
        case MotionEvent.ACTION_MOVE:
            mPlayerPoint.set((int) event.getX(), (int) event.getY());
        }
        return true;
    }

    public void update() {
        mPlayer.update(mPlayerPoint);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawColor(Color.WHITE);
        mPlayer.draw(canvas);
    }
}

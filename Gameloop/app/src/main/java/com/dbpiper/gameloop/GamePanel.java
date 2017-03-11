package com.dbpiper.gameloop;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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
    private ObstacleManager mObstacleManager;
    private boolean mMovingPlayer = false;
    private boolean mGameOver = false;
    private long mGameOverTime;
    private Rect r = new Rect();

    public GamePanel(Context context) {
        super(context);
        getHolder().addCallback(this);
        mThread = new MainThread(getHolder(), this);
        mPlayer = new RectPlayer(new Rect(100, 100, 400, 400), Color.rgb(255, 0, 0));
        reset();
        setFocusable(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mThread = new MainThread(getHolder(), this);
        mThread.setRunning(true);
        mThread.start();
    }

    public void reset() {
        mPlayerPoint = new Point(Constants.SCREEN_WIDTH/2, 3*Constants.SCREEN_HEIGHT/4);
        mPlayer.update(mPlayerPoint);
        mObstacleManager = new ObstacleManager(500, 600, 125, Color.BLACK);
        mMovingPlayer = false;
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
            if (!mGameOver && mPlayer.getRectangle()
                    .contains((int)event.getX(), (int)event.getY())) {
                mMovingPlayer = true;
                break;
            }
            if (mGameOver && System.currentTimeMillis() - mGameOverTime >= 2000) {
                reset();
                mGameOver = false;
            }
        case MotionEvent.ACTION_MOVE:
            if (!mGameOver && mMovingPlayer) {
                mPlayerPoint.set((int) event.getX(), (int) event.getY());
            }
            break;
        case MotionEvent.ACTION_UP:
            mMovingPlayer = false;
            break;
        }
        return true;
    }

    public void update() {
        if (!mGameOver) {
            mPlayer.update(mPlayerPoint);
            mObstacleManager.update();
            if (mObstacleManager.playerCollide(mPlayer)) {
                mGameOver = true;
                mGameOverTime = System.currentTimeMillis();
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawColor(Color.WHITE);
        mPlayer.draw(canvas);
        mObstacleManager.draw(canvas);

        if (mGameOver) {
            Paint paint = new Paint();
            paint.setTextSize(100);
            paint.setColor(Color.MAGENTA);
            drawCenterText(canvas, paint, "Game Over");
        }
    }

    public void drawCenterText(Canvas canvas, Paint paint, String text) {
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.getClipBounds(r);
        int cHeight = r.height();
        int cWidth = r.width();
        paint.getTextBounds(text, 0, text.length(), r);
        float x = cWidth / 2f - r.width() / 2f - r.left;
        float y = cHeight / 2f - r.height() / 2f - r.bottom;
        canvas.drawText(text, x, y, paint);
    }
}

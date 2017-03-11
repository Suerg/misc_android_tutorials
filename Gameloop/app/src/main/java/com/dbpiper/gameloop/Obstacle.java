package com.dbpiper.gameloop;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by suerg on 3/11/2017.
 */

public class Obstacle implements GameObject {
    private Rect mRectangle;
    private Rect mRectangle2;
    private int mColor;

    public Rect getRectangle() {
        return mRectangle;
    }

    public void addY(float y) {
        mRectangle.top += y;
        mRectangle.bottom += y;
        mRectangle2.top += y;
        mRectangle2.bottom += y;
    }

    public Obstacle(int rectHeight, int color, int startX, int startY, int playerGap) {
        mColor = color;
        mRectangle = new Rect(0, startY, startX, startY + rectHeight);
        mRectangle2 = new Rect(startX + playerGap, startY, Constants.SCREEN_WIDTH,
                startY + rectHeight);
    }

    public boolean playerCollide(RectPlayer player) {
        Rect playerRect = player.getRectangle();
        return Rect.intersects(mRectangle, playerRect) || Rect.intersects(mRectangle2, playerRect);
    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(mColor);

        canvas.drawRect(mRectangle, paint);
        canvas.drawRect(mRectangle2, paint);
    }

    @Override
    public void update() {

    }
}

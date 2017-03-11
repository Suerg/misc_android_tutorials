package com.dbpiper.gameloop;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

/**
 * Created by suerg on 3/11/2017.
 */

public class RectPlayer implements GameObject {
    private Rect mRectangle;
    private int mColor;

    public Rect getRectangle() {
        return mRectangle;
    }


    public RectPlayer(Rect rectangle, int color) {
        mRectangle = rectangle;
        mColor = color;
    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(mColor);
        canvas.drawRect(mRectangle, paint);
    }

    @Override
    public void update() {
    }

    public void update(Point point) {
        mRectangle.set(point.x - mRectangle.width()/2, point.y - mRectangle.height()/2,
                point.x + mRectangle.width()/2, point.y + mRectangle.height()/2);
    }
}

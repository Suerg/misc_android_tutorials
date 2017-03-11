package com.dbpiper.gameloop;

import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * Created by suerg on 3/11/2017.
 */

public interface GameObject {
    public void draw(Canvas canvas);
    public void update();
}

package com.dbpiper.gameloop;

import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by suerg on 3/11/2017.
 */

public class ObstacleManager {
    // higher index = lower on screen = higher y value
    private List<Obstacle> mObstacles;
    private int mPlayerGap;
    private int mObstacleGap;
    private int mObstacleHeight;
    private int mColor;
    private long mStartTime;

    public ObstacleManager(int playerGap, int obstacleGap, int obstacleHeight, int color) {
        mPlayerGap = playerGap;
        mObstacleGap = obstacleGap;
        mObstacleHeight = obstacleHeight;
        mColor = color;
        mObstacles = new ArrayList<>();
        mStartTime = System.currentTimeMillis();

        populateObstacles();
    }

    public boolean playerCollide(RectPlayer player) {
        for (Obstacle obstacle : mObstacles) {
            if (obstacle.playerCollide(player)) {
                return true;
            }
        }
        return false;
    }

    private void populateObstacles() {
        int currY = -5*Constants.SCREEN_HEIGHT/4;
        while (currY < 0) {
            int xStart = (int)(Math.random()*(Constants.SCREEN_WIDTH - mPlayerGap));
            mObstacles.add(new Obstacle(mObstacleHeight, mColor, xStart, currY, mPlayerGap));
            currY += mObstacleHeight + mObstacleGap;
        }
    }

    public void update() {
        int elapsedTime = (int)(System.currentTimeMillis() - mStartTime);
        mStartTime = System.currentTimeMillis();
        float speed = Constants.SCREEN_HEIGHT/10000.0f;
        for (Obstacle obstacle : mObstacles) {
            obstacle.addY(speed * elapsedTime);
        }
        if (mObstacles.get(mObstacles.size() - 1).getRectangle().top >= Constants.SCREEN_HEIGHT) {
            int xStart = (int)(Math.random() * (Constants.SCREEN_WIDTH - mPlayerGap));
            mObstacles.add(0, new Obstacle(mObstacleHeight, mColor, xStart,
                    mObstacles.get(0).getRectangle().top - mObstacleHeight - mObstacleGap,
                    mPlayerGap));
            mObstacles.remove(mObstacles.size() - 1);
        }
    }

    public void draw(Canvas canvas) {
        for (Obstacle obstacle : mObstacles) {
            obstacle.draw(canvas);
        }
    }

}

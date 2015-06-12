package com.dozone.bulletdodge;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

import java.lang.Math;
import java.util.Random;

/**
 * Created by Dozone on 2015/4/19.
 */
public class Bullet {
    public float bX;
    public float bY;
    private float speed;
    private int dX;
    private int dY;
    private int bulletType;
    private float randPlaneX;
    private float randPlaneY;
    private boolean changeDirection = true;

    public static int randInt(int min, int max) {
        Random rand = new Random();
        return rand.nextInt((max - min) +1)+min;
    }

    public Bullet(Game game, int bulletType) {
        this.bulletType = bulletType;
        randPlaneX = randInt((int) game.planeX - 100, (int) game.planeX + 100);
        randPlaneY = randInt((int) game.planeY - 100, (int) game.planeY + 100);
        speed = 4;
        if (bulletType == 1) {
            randPlaneX = game.planeX;
            randPlaneY = game.planeY;
        }
        if (bulletType == 2) {
            speed = 7;
        }
        else if (bulletType == 3) {
            speed = 14;
        }
        int rand = randInt(0,100)%4;
        if (rand == 0) {
            bX = 0;
            dX = 1;
            bY = randInt(0, Game.width);
            dY = (bY <= randPlaneY) ? 1 : -1;
        }
        else if (rand == 1) {
            bX = randInt(0, Game.width);
            dX = (bX <= randPlaneX) ? 1 : -1;
            bY = 0;
            dY = 1;
        }
        else if (rand == 2) {
            bX = Game.width;
            dX = -1;
            bY = randInt(0, Game.width);
            dY = (bY <= randPlaneY) ? 1 : -1;
        }
        else {
            bX = randInt(0, Game.width);
            dX = (bX <= randPlaneX) ? 1 : -1;
            bY = Game.width;
            dY = -1;
        }
    }
    public void update(Game game) {
        if (bulletType==4 && changeDirection) {
            randPlaneX = randInt((int) game.planeX - 25, (int) game.planeX + 25);
            randPlaneY = randInt((int) game.planeY - 25, (int) game.planeY + 25);
            if (dY != ((bY <= randPlaneY) ? 1 : -1) || dX != ((bX <= randPlaneX) ? 1 : -1)) {
                changeDirection = false;
            }
        }
        float tmp = Math.abs(randPlaneY-bY)+Math.abs(randPlaneX-bX);
        float diffX = speed*Math.abs(randPlaneX-bX)/tmp;
        float diffY = speed*Math.abs(randPlaneY-bY)/tmp;
        bX = bX+diffX*dX;
        bY = bY+diffY*dY;
    }

    public void draw (Canvas canvas) {
        canvas.drawBitmap(Game.ImageBullet,bX,bY,null);
    }

    public boolean collision(int l, int t, int r, int b) {
        Rect bullet = new Rect((int)bX, (int)bY, (int)bX+Game.ImageBullet.getWidth(), (int)bY+Game.ImageBullet.getHeight());
        return bullet.intersect(l,t,r,b);
    }

    public boolean outOfRange() {
        return (bX < 0 || bY < 0 || bX > Game.width || bY > Game.width);
    }
}

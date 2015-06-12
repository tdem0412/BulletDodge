package com.dozone.bulletdodge;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;

/**
 * Created by Dozone on 2015/4/17.
 */
public class Game {
    public static int width;
    public static int height;
    public static float density;
    private boolean GG;
    private long beginTime;
    private long lastBulletAdded;
    public long gameTime;
    private long score;
    public float planeX;
    public float planeY;
    private static Bitmap plane;
    private static Bitmap explode;
    private static Bitmap ImageUp;
    private static Bitmap ImageDown;
    private static Bitmap ImageLeft;
    private static Bitmap ImageRight;
    private static Bitmap ImageFrame;
    public static Bitmap ImageBullet;
    private Paint paintText;
    private int touchUp = -1;
    private int touchDown = -1;
    private int touchRight = -1;
    private int touchLeft = -1;
    private Rect u;
    private Rect d;
    private Rect r;
    private Rect l;
    private Rect f;
    private Rect restart;
    private ArrayList<Bullet> bullets;
    private int bulletCount;
    private boolean start = false;

    public Game(int screenWidth, int screenHeight, Resources resources) {
        width = screenWidth;
        height = screenHeight;
        density = resources.getDisplayMetrics().density;
        plane = BitmapFactory.decodeResource(resources, R.drawable.plane);
        explode = BitmapFactory.decodeResource(resources, R.drawable.explode);
        ImageUp = BitmapFactory.decodeResource(resources, R.drawable.up);
        ImageDown = BitmapFactory.decodeResource(resources, R.drawable.down);
        ImageLeft = BitmapFactory.decodeResource(resources, R.drawable.left);
        ImageRight = BitmapFactory.decodeResource(resources, R.drawable.right);
        ImageFrame = BitmapFactory.decodeResource(resources, R.drawable.frame);
        ImageBullet = BitmapFactory.decodeResource(resources, R.drawable.bullet);
        int arrowSize = width/3;
        int arrowPos = width+arrowSize+(height-width-2*arrowSize)/2;
        f = new Rect(0, width, width, height);
        u = new Rect (arrowSize, arrowPos-arrowSize+20, arrowSize*2, arrowPos);
        d = new Rect (arrowSize, arrowPos, arrowSize*2, arrowPos+arrowSize);
        r = new Rect (2*arrowSize, arrowPos, width-20, arrowPos+arrowSize);
        l = new Rect(20, arrowPos, arrowSize, arrowPos+arrowSize-20);
        paintText = new Paint();
        paintText.setColor(Color.WHITE);
        paintText.setTextSize(15*density);
        bullets = new ArrayList<>();
        gameReset();
    }
    private void gameReset () {
        GG = false;
        planeX = width/2;
        planeY = width/2;
        bullets.clear();
        bulletCount = 10;
        start = false;
        for (int i = 0; i < bulletCount; i++) {
            bullets.add(new Bullet(this,0));
        }
        beginTime = System.currentTimeMillis();
        lastBulletAdded = beginTime;
    }
    public void draw(Canvas canvas, int bulletType) {
        canvas.drawColor(Color.BLACK);
        canvas.drawBitmap(ImageFrame, null, f, null);
        canvas.drawBitmap(ImageUp, null, u, null);
        canvas.drawBitmap(ImageDown, null, d, null);
        canvas.drawBitmap(ImageLeft, null, l, null);
        canvas.drawBitmap(ImageRight, null, r, null);
        for (int i = 0; i < bulletCount; i++) {
            bullets.get(i).draw(canvas);
        }
        if (!GG) {
            canvas.drawBitmap(plane, planeX, planeY, null);
            if (gameTime > 10000 && start) {
                String condition = "";
                switch (bulletType) {
                    case 1:
                        condition = "Accurate Shooting";
                        break;
                    case 2:
                        condition = "Fast Shooting";
                        break;
                    case 3:
                        condition = "Sharp Shooting";
                        break;
                    case 4:
                        condition = "Heat Tracing Shooting";
                }
                Rect c = new Rect();
                paintText.getTextBounds(condition, 0, condition.length(), c);
                canvas.drawText(condition, 0, c.height() - c.bottom, paintText);
            }
        }
        else {
            paintText.setTextSize(25*density);
            canvas.drawBitmap(explode, planeX, planeY, null);
            String s = String.valueOf(score);
            String p = "Press here to restart";
            s = "Time Survived: "+ s.substring(0, s.length()-3)+'.'+s.substring(s.length()-3);
            String b = "BulletCount: "+Integer.toString(bulletCount);
            String h = String.valueOf(HighScore.highScore);
            h = "High Score: "+ h.substring(0, h.length()-3)+'.'+h.substring(h.length()-3);
            Rect sR = new Rect();
            Rect bR = new Rect();
            Rect hR = new Rect();
            restart = new Rect();
            paintText.getTextBounds(s,0,s.length(),sR);
            paintText.getTextBounds(p,0,p.length(),restart);
            paintText.getTextBounds(b, 0, b.length(), bR);
            paintText.getTextBounds(h, 0, h.length(), hR);
            canvas.drawText(s, (width - sR.width()) / 2, width / 2 - sR.height()*3, paintText);
            canvas.drawText(b, (width-bR.width())/2,width/2 - sR.height(),paintText);
            canvas.drawText(h, (width-hR.width())/2,width/2+sR.height(),paintText);
            canvas.drawText(p, (width - restart.width()) / 2, width / 2 + sR.height()*6, paintText);
            restart.set((width - restart.width()) / 2,width / 2 + sR.height()*4, (width+restart.width())/2, width / 2 + sR.height()*7);
            paintText.setTextSize(15*density);
        }
    }
    public boolean update(long time, int bulletType) {
        if (GG) {
            return true;
        }
        gameTime = time - beginTime;
        if (gameTime < 10000) {
            bulletType = 0;
        } else if (bulletType == 0){
            start = true;
        } else if (!start) {
            bulletType = 0;
        }

        int bulletAddInterval = bulletType == 3?1500:4000;
        if (bulletCount < 50 && time - lastBulletAdded > 400) {
            bulletCount+=10;
            for (int i = 0; i < 10; i++) {
                bullets.add(new Bullet(this,0));
            }
            lastBulletAdded = time;
        }
        else if (time - lastBulletAdded > bulletAddInterval){
                bulletCount++;
                bullets.add(new Bullet(this, bulletType));
                lastBulletAdded = time;
        }
        planeUpdate();
        for (int i = 0; i < bulletCount; i++) {
            Bullet bullet = bullets.get(i);
            bullet.update(this);
            if (bullet.outOfRange()) {
                bullets.remove(i);
                if (bulletType == 3) {
                    bullets.add(new Bullet(this, 0));
                }
                else {
                    bullets.add(new Bullet(this, bulletType));
                }
            }
            else if (!GG && bullet.collision((int)(planeX+plane.getWidth()/7), (int)(planeY+plane.getHeight()/7), (int)(planeX+plane.getWidth()*6/7), (int)(planeY+plane.getHeight()))) {
                GG = true;
                score = gameTime;
                if (gameTime > HighScore.highScore) {
                    HighScore.highScore = gameTime;
                    HighScore.setHighScore();
                }
            }
        }
        return false;
    }

    public void touchEvent_Down (MotionEvent event, int id) {
        int X = (int)event.getX(event.getActionIndex());
        int Y = (int)event.getY(event.getActionIndex());
        if (!GG) {
            if (u.contains(X, Y) && touchUp == -1) {
                touchUp = id;
            }
            if (d.contains(X, Y) && touchDown == -1) {
                touchDown = id;
            }
            if (r.contains(X, Y) && touchRight == -1) {
                touchRight = id;
            }
            if (l.contains(X, Y) && touchLeft == -1) {
                touchLeft = id;
            }
        }
        else {
            if (restart.contains(X, Y)) {
                gameReset();
            }
        }
    }
    public void touchEvent_Up (int id) {
        if (id == touchUp) {
            touchUp = -1;
        }
        else if (id == touchDown) {
            touchDown = -1;
        }
        else if (id == touchRight) {
            touchRight = -1;
        }
        else if (id == touchLeft) {
            touchLeft = -1;
        }
    }
    private void planeUpdate () {
        int planeWidth = plane.getWidth();
        int planeHeight = plane.getHeight();
        if (touchUp != -1) {
            planeY-=4;
            if (planeY < 0) {
                planeY = 0;
            }
        }
        if (touchDown != -1) {
            planeY+=4;
            if (planeY+planeHeight > width) {
                planeY = width-planeHeight;
            }
        }
        if (touchRight != -1){
            planeX+=4;
            if (planeX+planeWidth > width) {
                planeX = width-planeWidth;
            }
        }
        if (touchLeft != -1) {
            planeX-=4;
            if (planeX < 0) {
                planeX = 0;
            }
        }
    }
}

package com.dozone.bulletdodge;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
/**
 * Created by Dozone on 2015/4/17.
 */
public class GamePanel extends SurfaceView implements SurfaceHolder.Callback{
    public GamePanel(Context context) {
        super(context);
        this.setFocusable(true);
        this.getHolder().addCallback(this);
    }
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

    public void surfaceCreated(SurfaceHolder holder) {
        startGame();
    }
    public void startGame() {
        game = new Game(getWidth(), getHeight(), getResources());
        gameThread = new GameThread(this.getHolder(), game);
        gameThread.running = true;
        gameThread.start();
    }
    public void surfaceDestroyed(SurfaceHolder holder) {
        gameThread.running = false;
        boolean retry = true;
        while(retry) {
            try {
                gameThread.join();
                retry = false;
            } catch (InterruptedException e) {}
        }
    }
    public boolean onTouchEvent(MotionEvent event) {
        // This is for single-touch. For multi-touch use MotionEventCompat.getActionMasked(event);
        int action = event.getActionMasked();

        if(action == MotionEvent.ACTION_POINTER_DOWN || action == MotionEvent.ACTION_DOWN){
            game.touchEvent_Down(event, event.getPointerId(event.getActionIndex()));
        }

        if(action == MotionEvent.ACTION_POINTER_UP || action == MotionEvent.ACTION_UP){
            game.touchEvent_Up(event.getPointerId(event.getActionIndex()));
        }

        return true;
    }
    private Game game;
    private GameThread gameThread;
}

package com.dozone.bulletdodge;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
/**
 * Created by Dozone on 2015/4/17.
 */
public class Start extends Activity{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }

    public void onClick(View v) {
        GamePanel gamePanel = new GamePanel(this);
        setContentView(gamePanel);
        HighScore.context = this.getBaseContext();
        HighScore.loadHighScore();
    }

}

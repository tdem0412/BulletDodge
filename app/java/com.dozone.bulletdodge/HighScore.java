package com.dozone.bulletdodge;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by Dozone on 2015/4/20.
 */
public class HighScore {
    public static Context context;
    public static long highScore = 0;
    private static String File = "high_score";

    public static void loadHighScore() {
        try {
            FileInputStream in = context.openFileInput(File);
            InputStreamReader inputStream = new InputStreamReader(in);
            BufferedReader bufferedReader = new BufferedReader(inputStream);
            String line = bufferedReader.readLine();
            if (line != null) {
                highScore = Long.parseLong(line);
            }
            bufferedReader.close();
            inputStream.close();
            in.close();
        }
        catch (Exception e) {}
    }

    public static void setHighScore() {
        try {
            FileOutputStream fOut = context.openFileOutput(File, Context.MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fOut);

            osw.write(String.valueOf( highScore ));

            osw.flush();
            osw.close();
            fOut.close();
        } catch (Exception e) {
        }
    }
}

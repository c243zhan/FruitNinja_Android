/**
 * CS349 Winter 2014
 * Assignment 4 Demo Code
 * Jeff Avery
 */
package com.example.a4;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.a4complete.R;

public class MainActivity extends Activity {
    private Model model;
    private MainView mainView;
    private TitleView titleView;
    public static Point displaySize;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setTitle("CS349 A4");

        // save display size
        Display display = getWindowManager().getDefaultDisplay();
        displaySize = new Point();
        display.getSize(displaySize);

        // initialize model
        model = new Model(this);
        model.gameover();
        
        // set view
        setContentView(R.layout.main);
        
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // create the views and add them to the main activity
        titleView = new TitleView(this.getApplicationContext(), model);
        ViewGroup v1 = (ViewGroup) findViewById(R.id.main_1);
        v1.addView(titleView);

        final Button button = (Button) findViewById(R.id.restart);
        button.setVisibility(1);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                button.setVisibility(View.GONE);
            	model.restart();
            }
        });
        
        mainView = new MainView(this.getApplicationContext(), model);
        ViewGroup v2 = (ViewGroup) findViewById(R.id.main_2);
        v2.addView(mainView);

        // notify all views
        model.initObservers();
        
    }
}

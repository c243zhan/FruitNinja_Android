/**
 * CS349 Winter 2014
 * Assignment 4 Demo Code
 * Jeff Avery & Michael Terry
 */
package com.example.a4;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.widget.TextView;

import com.example.a4complete.R;

import java.util.Observable;
import java.util.Observer;

/*
 * View to display the Title, and Score
 * Score currently just increments every time we get an update
 * from the model (i.e. a new fruit is added).
 */
public class TitleView extends TextView implements Observer {
	private Model model;
	
	// cut fruit and life chance
    private long cut = 0;
    private String lc = " X X X X X";

    // Constructor requires model reference
    public TitleView(Context context, Model model) {
        super(context);

        // set width, height of this view
        this.setHeight(235);
        this.setWidth(MainActivity.displaySize.x);

        // register with model so that we get updates
        this.model = model;
        model.addObserver(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // TODO BEGIN CS349
        // add high score, anything else you want to display in the title
        // TODO END CS349
        //setBackgroundColor(Color.rgb(20,200,235));
        setBackgroundColor(Color.WHITE);
        setTextSize(21);
        setTextColor(Color.rgb(40,230,15));
        setText(" " + getResources().getString(R.string.app_title) + " " + "\n" +
        		" " + getResources().getString(R.string.fruit_cut) + " " + cut + "     " + 
        		getResources().getString(R.string.life) + " " + lc);
    }

    // Update from model
    // ONLY useful for testing that the view notifications work
    @Override
    public void update(Observable observable, Object data) {
        // TODO BEGIN CS349
        // do something more meaningful here
        // TODO END CS349
        
  	  	int frameCount = model.getFrame();
  	  	// update variables only on this condition
  	  	if(model.getCut() || model.getDrop() || frameCount % 40 == 0){
  		  
        cut = model.getFruitCut();
        
    	// transfer chances to strings
        int chance = model.getChance();

        switch(chance){
        case 0:
        	lc = "";
        	break;
        case 1:
        	lc = "";
        	break;
        case 2:
        	lc = "X";
        	break;
        case 3:
        	lc = "X X";
        	break;
        case 4:
        	lc = "X X X";
        	break;
        case 5:
        	lc = "X X X X";
        	break;
        case 6:
        	lc = "X X X X X";
        	break;
        default:
        	lc = "X X X X X";
        	break;
        }

        setText(" " + getResources().getString(R.string.app_title) + " " + "\n" +
        		" " + getResources().getString(R.string.fruit_cut) + " " + cut + "     " + 
        		getResources().getString(R.string.life) + " " + lc);
        
        invalidate();
  	  }
    }
}

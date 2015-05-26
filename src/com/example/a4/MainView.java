/**
 * CS349 Winter 2014
 * Assignment 4 Demo Code
 * Jeff Avery & Michael Terry
 */
package com.example.a4;

import android.content.Context;
import android.graphics.*;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;


/*
 * View of the main game area.
 * Displays pieces of fruit, and allows players to slice them.
 */
public class MainView extends View implements Observer {
    private final Model model;
    private final MouseDrag drag = new MouseDrag();

    // button and overText for game over
	//private Button button = new Button();
	//private Label overText = new JLabel();
	
    // Constructor
    MainView(Context context, Model m) {
        super(context);

        // register this view with the model
        model = m;
        model.addObserver(this);

        // TODO BEGIN CS349
        // test fruit, take this out before handing in!
       /* Fruit f1 = new Fruit(new float[] {0, 20, 20, 0, 40, 0, 60, 20, 60, 40, 40, 60, 20, 60, 0, 40});
        f1.translate(100, 100);
        model.add(f1);*/
        // TODO END CS349

        // add controller
        // capture touch movement, and determine if we intersect a shape
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // Log.d(getResources().getString(R.string.app_name), "Touch down");
                        drag.start(event.getX(), event.getY());
                        break;

                    case MotionEvent.ACTION_UP:
                        // Log.d(getResources().getString(R.string.app_name), "Touch release");
                        drag.stop(event.getX(), event.getY());

                        // find intersected shapes
                        Iterator<Fruit> i = model.getShapes().iterator();
                        float xoffset = 20f;
                        float yoffset = 20f; // Used to offset new fruits
                        while(i.hasNext()) {
                            Fruit s = i.next();
                            
                            if (s.intersects(drag.getStart(), drag.getEnd())) {
                            	//s.setFillColor(Color.RED);
                                try {
                                    Fruit[] newFruits = s.split(drag.getStart(), drag.getEnd());

                                    // TODO BEGIN CS349
                                    // you may want to place the fruit more carefully than this
                                   /* newFruits[0].translate(0, -10);
                                    newFruits[1].translate(0, +10);
                                    // TODO END CS349
                                    model.add(newFruits[0]);
                                    model.add(newFruits[1]);*/

                                    // add offset so we can see them split - this is used for demo purposes only!
                                    // you should change so that new pieces appear close to the same position as the original piece
                                    Fruit f1 = newFruits[0];
                                    f1.translate(-xoffset, -yoffset);
                                    Fruit f2 = newFruits[1];
                                    f2.translate(xoffset, -yoffset);
                                    
                                    // update model variable, remove original fruit, add 2 new fruits
                                    model.newCut();
                                    model.setCut(true);
                                    model.remove(s);
                                    model.add(f1);
                                    model.add(f2);
                                    //model.getSize();
                                    model.setCut(false);
                                    
                                    // TODO BEGIN CS349
                                    // delete original fruit from model
                                    // TODO END CS349

                                } catch (Exception ex) {
                                    Log.e("fruit_ninja", "Error: " + ex.getMessage());
                                }
                            } else {
                                //s.setFillColor(Color.BLUE);
                            }
                            invalidate();
                        }
                        break;
                }
                return true;
            }
        });
    }

    // inner class to track mouse drag
    // a better solution *might* be to dynamically track touch movement
    // in the controller above
    class MouseDrag {
        private float startx, starty;
        private float endx, endy;

        protected PointF getStart() { return new PointF(startx, starty); }
        protected PointF getEnd() { return new PointF(endx, endy); }

        protected void start(float x, float y) {
            this.startx = x;
            this.starty = y;
        }

        protected void stop(float x, float y) {
            this.endx = x;
            this.endy = y;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // draw background
        setBackgroundColor(Color.WHITE);

        // draw all pieces of fruit
        for (Fruit s : model.getShapes()) {
        	if(s.dropped()){ // if the fruit is dropped, remove it
        		model.setDrop(true);
        		model.remove(s);
        		model.setDrop(false);
        	}else{
                s.draw(canvas);
        	}
        }
    }

    @Override
    public void update(Observable observable, Object data) {
        invalidate();
    }
}

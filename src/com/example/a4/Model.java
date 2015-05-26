/**
 * CS349 Winter 2014
 * Assignment 4 Demo Code
 * Jeff Avery & Michael Terry
 */
package com.example.a4;

import android.app.Activity;
import android.graphics.Region;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import com.example.a4complete.R;

/*
 * Class the contains a list of fruit to display.
 * Follows MVC pattern, with methods to add observers,
 * and notify them when the fruit list changes.
 */
public class Model extends Observable {
    // List of fruit that we want to display
    private ArrayList<Fruit> shapes = new ArrayList<Fruit>();

    // boolean to check whether it is cut/drop/over
    private boolean cut = false;
    private boolean dropped = false; 
    private boolean over = false;
    
    // variable for timer
    private int fps = 30;
    private int frameCount = 0;
    
    private Handler timerHandler;
    private Runnable timerRunnable;

    // game data(chances, fruitcut)
    private int chance = 6;
    private long fruitCut = 0;
    private final Activity parent;
    
    // Constructor
    Model(Activity ac) {
        shapes.clear();

        timerHandler = new Handler();
        timerRunnable = new Runnable() {
             @Override
             synchronized public void run() {
            	 if(over){
            		 return;
            	 }
     			// generate new fruit every 1 second(40 frames)
     			if(frameCount % 30 == 0){ 
     				generateFruit();
     			}
     			frameCount++;
     			
                  initObservers();
                  timerHandler.removeCallbacks(timerRunnable);
                  timerHandler.postDelayed(timerRunnable, 1000/fps);
             }
        };

        timerHandler.removeCallbacks(timerRunnable);
        timerHandler.postDelayed(timerRunnable, 0);
        parent = ac;
    }

    // generate a new fruit
    public void generateFruit(){
  	  int v = random(3);
  	  Fruit newFruit;
  	  switch(v){
  	  case 1:
  	  	 newFruit = new Fruit(new Region(0, 0, 70, 40));
  		     break;
  	  case 2:
  	  	 newFruit = new Fruit(new Region(0, 0, 50, 50));
  		     break;
  	  default:
  	  	 newFruit = new Fruit(new Region(0, 0, 50, 50));
  		     break;
  	  }
  	  add(newFruit);
    }
    
    // random valuel btw [0,range-1]
    public int random(int range){
        Random randomGenerator = new Random();
        int value = randomGenerator.nextInt(range);
        return value;
    }
    
    // Model methods
    // You may need to add more methods here, depending on required functionality.
    // For instance, this sample makes to effort to discard fruit from the list.
    public void add(Fruit s) {
        shapes.add(s);
        //setChanged();
        //notifyObservers();
    }

    public void remove(Fruit s) {
        shapes.remove(s);
        if(this.dropped){
        	chance--;
        }
        if(chance == 0){
        	gameover();
        }
        initObservers();
    }
    
    // get frame count
    public int getFrame(){
  	  return this.frameCount;
    }

    // get chance times
    public int getChance(){
  	  return this.chance;
    }
    
    public int getSize(){
  	  return shapes.size();
    }
    
    // increase cut fruit num
    public void newCut(){
  	  fruitCut++;
    }
    
    // get fruit cut number
    public long getFruitCut(){
  	  return fruitCut;
    }
    
    // set the cut value
    public void setCut(boolean value){
  	  cut = value;
    }
    
    // return the cut value
    public boolean getCut(){
  	  return cut;
    }

    // set the drop value
    public void setDrop(boolean value){
  	  dropped = value;
    }
    
    // return the drop value
    public boolean getDrop(){
  	  return dropped;
    }
    
    // restart the game by reinitilizing all variables
    public void restart(){
  	  over = false;
  	  shapes.clear();
  	  fruitCut = 0;
  	  chance = 6;
      timerHandler.postDelayed(timerRunnable, 0);
	  //t.schedule(timertask, 1000/fps);
    }
    
    // check whether the game is over
    public boolean over(){
  	  return over;
    }
    
    // game over by stopping the timer
    public void gameover(){
  	  over = true;
  	  //t.cancel();
  	  initObservers();

      Button button = (Button) parent.findViewById(R.id.restart);
      button.setVisibility(View.VISIBLE);
    }
    
    public ArrayList<Fruit> getShapes() {
        return (ArrayList<Fruit>) shapes.clone();
    }

    // MVC methods
    // Basic MVC methods to bind view and model together.
    public void addObserver(Observer observer) {
        super.addObserver(observer);
    }

    // a helper to make it easier to initialize all observers
    public void initObservers() {
        setChanged();
        notifyObservers();
    }

    @Override
    public synchronized void deleteObserver(Observer observer) {
        super.deleteObserver(observer);
        setChanged();
        notifyObservers();
    }

    @Override
    public synchronized void deleteObservers() {
        super.deleteObservers();
        setChanged();
        notifyObservers();
    }
}

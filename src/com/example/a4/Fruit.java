/**
 * CS349 Winter 2014
 * Assignment 4 Demo Code
 * Jeff Avery
 */
package com.example.a4;
import java.util.Random;

import android.graphics.*;
import android.util.Log;

/**
 * Class that represents a Fruit. Can be split into two separate fruits.
 */
public class Fruit {
    private Path path = new Path();
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Matrix transform = new Matrix();

    // all x/y coordinates for calculation
    private float  		xinitial;
    private float  		ylimit;
    
    private float       xincrease;
    private float       yincrease;

    // fruit area datas
    private int			width;
    private int			height;
    private int			hypo;
    private int 		fillColor;

    // seperate the fruit into different parts
    private Path 		left = new Path();
    private Path 		top = new Path();
    private Path 		rightDown = new Path();
    
    // whether th foot is cuttable
    private boolean 	cuttable;

    // useful constants 
    public static final double PI = 3.14159265358979323846;
	private int 		fps = 40;
	
    /**
     * A fruit is represented as Path, typically populated 
     * by a series of points 
     */
	
    Fruit(float[] points) {
        init();
        this.path.reset();
        this.path.moveTo(points[0], points[1]);
        for (int i = 2; i < points.length; i += 2) {
            this.path.lineTo(points[i], points[i + 1]);
        }
        this.path.moveTo(points[0], points[1]);

    }

    Fruit(Region region) {
    	subShape(region);
    	init();
        this.path = region.getBoundaryPath();
    }

    Fruit(Path path) {
        init();
        this.path = path;
    }

    // constructor to copy datas from another fruit
    Fruit (Region fruitShape, Matrix inverse, float xi, float xin, float yl, float yin, 
    		  Matrix at, boolean cut, int fc) {
        path = fruitShape.getBoundaryPath();
        path.transform(inverse);
        
        xinitial = xi;
        xincrease = xin;
        ylimit = yl;
        yincrease = yin;
        transform = at;
        cuttable = cut;
        
        fillColor = fc;
        paint.setColor(fillColor);
        paint.setStrokeWidth(5);
    }

	// set the path based on points array
	void setPath(Path target, int[] x, int[] y){
        target.reset();
        target.moveTo(x[0], y[0]);
        for (int i = 1; i < x.length; i++) {
            target.lineTo(x[i], y[i]);
        }
        target.moveTo(x[0], y[0]);
	}
	
    // divide the shape into 3 areas
    private void subShape(Region region){

        // create area to check when split
        width = region.getBounds().width();
        height = region.getBounds().height();
        hypo = (int)(Math.sqrt(Math.pow(width, 2) + Math.pow(height, 2)));
        
        // use -1 instead of 0 to allow tolerance
        int[] leftXarray = new int[]{-1, -1, width/2};
        int[] leftYarray = new int[]{-1, height+1, height/2};
        setPath(left, leftXarray, leftYarray);

        int[] topXarray = new int[]{-1, width, width/2};
        int[] topYarray = new int[]{-1, -1, height/2};
        setPath(top, topXarray, topYarray);

        int[] downXarray = new int[]{width+1, -1, width+1};
        int[] downYarray = new int[]{height+1, height+1, -1};
        setPath(rightDown, downXarray, downYarray);

    }
    
    private void init() {
        //this.paint.setColor(Color.BLUE);
    	this.fillColor = Color.rgb(random(255), random(255), random(255));
        this.paint.setColor(fillColor);
        this.paint.setStrokeWidth(5);

        // initial position & amount to increase
        xinitial = random(240);
        xincrease = (240 - xinitial)/fps;
        ylimit = 444+random(100);
        yincrease = ylimit/fps;
        
        // fill the color and initialize the variable
        cuttable = true;
        this.translate(xinitial,444);
    }

    // random valuel btw [0,range-1]
    private int random(int range){
        Random randomGenerator = new Random();
        int value = randomGenerator.nextInt(range);
        return value;
    }
    
    /**
     * The color used to paint the interior of the Fruit.
     */
    public int getFillColor() { return paint.getColor(); }
    public void setFillColor(int color) { paint.setColor(color); }

    /**
     * The width of the outline stroke used when painting.
     */
    public double getOutlineWidth() { return paint.getStrokeWidth(); }
    public void setOutlineWidth(float newWidth) { paint.setStrokeWidth(newWidth); }

    /**
     * Concatenates transforms to the Fruit's affine transform
     */
    public void rotate(float theta) { transform.postRotate(theta); }
    public void scale(float x, float y) { transform.postScale(x, y); }
    public void translate(float tx, float ty) { transform.postTranslate(tx, ty); }

    /**
     * Returns the Fruit's affine transform that is used when painting
     */
    public Matrix getTransform() { return transform; }

    /**
     * The path used to describe the fruit shape.
     */
    public Path getTransformedPath() {
        Path originalPath = new Path(path);
        Path transformedPath = new Path();
        originalPath.transform(transform, transformedPath);
        return transformedPath;
    }

    // check whether the fruit has drop
	public boolean dropped(){
    	float[] origin = {0, 0};
    	transform.mapPoints(origin);
    	
    	// use original to check its current place
    	double y = origin[1];
    	
		return (this.cuttable && y > 565);
	}
	
    /**
     * Paints the Fruit to the screen using its current affine
     * transform and paint settings (fill, outline)
     */
    public void draw(Canvas canvas) {
        // TODO BEGIN CS349
    	float[] origin = {0, 0};
    	transform.mapPoints(origin);
    	
    	// use original to check its current place
    	double x = origin[0];
    	double y = origin[1];
    	
    	if(!cuttable){ // the fruit was cut, drop then
        	this.translate(0.3f, 9f);
        }else if(x <= 240){ // fruit jumps up
    		this.translate(xincrease,-yincrease);
        }else{
    		this.translate(xincrease,yincrease);
    	}
    	
        // tell the shape to draw itself using the matrix and paint parameters
    	canvas.drawPath(getTransformedPath(), paint);
    	// TODO END CS349
    }

    /**
     * Tests whether the line represented by the two points intersects
     * this Fruit.
     */
    public boolean intersects(PointF p1, PointF p2) {
        // TODO BEGIN CS349
        // calculate angle between points
        // rotate and flatten points passed in 
        // rotate path and create region for comparison
        // TODO END CS349double x1 = p1.getX();
    	double x1 = p1.x;
    	double y1 = p1.y;
    	double x2 = p2.x;
    	double y2 = p2.y;

    	// if cut, cannot intersect
    	if(!cuttable){
			return false;
		}
    	
    	// get x1,y1 be the left point
    	double xinitial, yinitial, xfinal, yfinal;
    	if(x1 < x2){
	    	xinitial = x1;
	    	xfinal = x2;
	    	yinitial = y1;
	    	yfinal = y2;
    	}else if(x1 > x2){
	    	xinitial = x2;
	    	xfinal = x1;
	    	yinitial = y2;
	    	yfinal = y1;
    	}else if(y1 < y2){
	    	xinitial = x1;
	    	xfinal = x2;
	    	yinitial = y1;
	    	yfinal = y2;
    	}else{
	    	xinitial = x2;
	    	xfinal = x1;
	    	yinitial = y2;
	    	yfinal = y1;
    	}
	    	
    	int checkTimes = 1000;
    	double xincrease = (xfinal - xinitial) / checkTimes;
    	double yincrease = (yfinal - yinitial) / checkTimes;
    	yincrease = 0.2f*yincrease/xincrease;
    	xincrease = 0.2f;

    	// get initial, increase and final value for loops
    	double x=xinitial, y=yinitial;
    	double itime, intime, ftime;
    	if(xinitial != xfinal){
    		itime = xinitial; 
    		intime = xincrease;
    		ftime = xfinal;
    	}else if(yinitial != yfinal){
    		itime = yinitial;
    		intime = yincrease;
    		ftime = yfinal;
    	}else{ // there is not cut, only a click, not an intersection
    		return false;
    	}
    	
    	// check every point on the line to see intersection
	    while(itime <= ftime){
	    	x += xincrease;
	    	y += yincrease;
	    	PointF point = new PointF((float)x, (float)y);
	    		
	    	// if there is a point on the line intersecting the area
	    	if(contains(point)){
	    		return true;
	    	}
	    	itime += intime;
	    }
        return false;
    }

    /**
     * Returns whether the given point is within the Fruit's shape.
     */
    public boolean contains(PointF p1) {
        Region region = new Region();
        // this can make it work?
        boolean valid = region.setPath(getTransformedPath(), new Region(0, 0, 500, 800));
        return valid && region.contains((int) p1.x, (int) p1.y);
    }

    private boolean contains(Path path, float[] points){
    	Region region = new Region();
        boolean valid = region.setPath(path, new Region(-500, -500, 800, 800));
        return valid && region.contains((int)points[0], (int)points[1]);
    }
    
    // update intersection points(get the first intersection points btw the line and area
    public float[] getIntersection(float x1, float y1, float x2, float y2){
    	float x=x1, y=y1;
		// this is for x1, y1
    	int checkTimes = 1000;
    	float xincrease = (x2 - x1) / checkTimes;
    	float yincrease = (y2 - y1) / checkTimes;
    	yincrease = 0.2f*yincrease/xincrease;
    	xincrease = 0.2f;

    	if(!contains(new PointF(x, y))){
			while(!contains(new PointF(x, y))){
				x += xincrease;
				y += yincrease;
			}
			x1 = x;
			y1 = y;
		}else{
			while(contains(new PointF(x, y))){
				x -= xincrease;
				y -= yincrease;
			}
			x1 = x+xincrease;
			y1 = y+yincrease;
		}

		x=x2;
		y=y2;
		// this is for x2, y2
		if(!contains(new PointF(x, y))){
			while(!contains(new PointF(x, y))){
				x -= xincrease;
				y -= yincrease;
			}
			x2 = x;
			y2 = y;
		}else{
			while(contains(new PointF(x, y))){
				x += xincrease;
				y += yincrease;
			}
			x2 = x-xincrease;
			y2 = y-yincrease;
		}
    	float[] update = new float[]{x1, y1, x2, y2};
		return update;
    }
    
    /**
     * This method assumes that the line represented by the two points
     * intersects the fruit. If not, unpredictable results will occur.
     * Returns two new Fruits, split by the line represented by the
     * two points given.
     */
    public Fruit[] split(PointF p1, PointF p2) {
    	Region topArea = null;
    	Region bottomArea = null;

    	float x1,y1,x2,y2;
    	// let x1, y1 be the left point
    	if(p1.x < p2.x){
        	x1 = p1.x;
        	y1 = p1.y;
        	x2 = p2.x;
        	y2 = p2.y;
    	}else if(p1.x > p2.x){
        	x1 = p2.x;
        	y1 = p2.y;
        	x2 = p1.x;
        	y2 = p1.y;
    	}else if(p1.y < p2.y){ // vertical line
        	x1 = p2.x;
        	y1 = p2.y;
        	x2 = p1.x;
        	y2 = p1.y;
    	}else{
        	x1 = p1.x;
        	y1 = p1.y;
        	x2 = p2.x;
        	y2 = p2.y;
    	}
    	
    	// get the first intersections btw line and area
    	float[] update = getIntersection(x1, y1, x2, y2);
    	x1 = update[0];
    	y1 = update[1];
    	x2 = update[2];
    	y2 = update[3];
    	//PointF interPoint1 = new PointF(x1, y1);
    	//PointF interPoint2 = new PointF(x2, y2);

    	float[] interPoint1 = {x1, y1};
    	float[] interPoint2 = {x2, y2};
    	
    	double xdiff = x1-x2;
    	double ydiff = y1-y2;
    	
    	float arctan = (float)Math.atan(ydiff/xdiff);
    	double distance = Math.abs(xdiff * Math.sin(arctan));

    	Matrix original = new Matrix(transform);
    	Matrix invert = new Matrix();
    	boolean check = original.invert(invert);
    	if(!check){
    	}
    	
    	// get the vertical/horizontal distance on the intersections
    	invert.mapPoints(interPoint1);
    	invert.mapPoints(interPoint2);
    	
    	float x1d = interPoint1[0];
    	float y1d = interPoint1[1];
    	float x2d = interPoint2[0];
    	float y2d = interPoint2[1];

    	float[] origin = {0, 0};
    	transform.mapPoints(origin);
    	
    	// use original to check its current place
    	float xt = origin[0];
    	float yt = origin[1];

    	// translate back to orignal, translate some distances then do rotation to get two shapes(one above, one below)
	  	translate(-xt,-yt);

	  	// android needs to rotate at first & translate to degree
    	rotate((float)((-arctan)*180/PI));
    	
	    // check different ways to transform depending on where we cut
	  	if(x1d == x2d){ // vertical line, special case
	    	translate(0f, (float)(Math.abs(x2d*Math.sin(arctan))));
	  	}else if(contains(rightDown, interPoint1) && contains(rightDown, interPoint2)){
	        translate(0f, (float)-(hypo-distance));
	  	}else if(contains(left, interPoint1)){ // p1 on top left
	  		translate(0f, (float)-(Math.abs(y1d*Math.cos(arctan))));
	  	}else if(contains(top, interPoint1)){
	        translate(0f, (float)(Math.abs(x1d*Math.sin(arctan))));
	  	}else if(contains(rightDown, interPoint1)){ // p1 on bottom right, 2nd on top right
	        translate(0f, (float)-(Math.abs(x2d*Math.sin(arctan))));
	  	}else{
	  	}
	  	
    	// top is above x-axis & down is below
        Region top  = new Region(-100, -200, 400, 0);
        top.setPath(getTransformedPath(), new Region(-200, -400, 400, 0));
        Region down = new Region(-100, 0, 400, 200);
        down.setPath(getTransformedPath(), new Region(-200, 0, 400, 400));
        
        // check which one should be top/bottom
        if(x1 == x2){
            topArea = down;
            bottomArea = top;
        }else if(y1 < y2){
            topArea = down;
            bottomArea = top;
        }else{
            topArea = top;
            bottomArea = down;
        }
        
        Matrix inverse = new Matrix();
        transform.invert(inverse);
        
        // Rotate shape to align slice with x-axis
        // Bisect shape above/below x-axis (look at intersection methods!)
        
        // create 2 new fruits for top/down
        if (topArea != null && bottomArea != null){
        	Fruit topHalf = new Fruit(topArea, inverse, xinitial, this.xincrease, ylimit, 
        			this.yincrease, new Matrix(original), false, this.fillColor);
        	Fruit bottomHalf = new Fruit(bottomArea, inverse, xinitial, this.xincrease, ylimit, 
        			this.yincrease, original, false, this.fillColor);
        	return new Fruit[] { topHalf, bottomHalf };
        }
        return new Fruit[0];
     }/*
    	// TODO BEGIN CS349
        // calculate angle between points
        // rotate and flatten points passed in
        // rotate region
        // define region masks and use to split region into top and bottom
        // TODO END CS349
        if (topPath != null && bottomPath != null) {
           return new Fruit[] { new Fruit(topPath), new Fruit(bottomPath) };
        }
        return new Fruit[0];
    }*/
}

/**
 * Graphics2D for Android
 * Copyright (c) 2012 Jeff Avery
 */
package com.example.a4;

import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.Log;
import android.view.MotionEvent;
import java.text.DecimalFormat;

public class Graphics2D {
    private static int MATRIX_COL_SCALE_X = 0;
    private static int MATRIX_COL_SCALE_Y = 4;
    private static int MATRIX_COL_TX = 2;
    private static int MATRIX_COL_TY = 5;

    // Matrix
    public static float[] getScaleValues(Matrix matrix) {
        float[] values = new float[9];
        matrix.getValues(values);
        return new float[]{values[MATRIX_COL_SCALE_X], values[MATRIX_COL_SCALE_Y]};
    }

    public static float[] getTranslateValues(Matrix matrix) {
        float[] values = new float[9];
        matrix.getValues(values);
        return new float[]{values[MATRIX_COL_TX], values[MATRIX_COL_TY]};
    }

    public static void printMatrix(Matrix matrix) {
        float[] values = new float[9];
        matrix.getValues(values);
        DecimalFormat df = new DecimalFormat("0.00");
        Log.d("Graphics2D", "---------------");
        Log.d("Graphics2D", df.format(values[0]) + "\t" + df.format(values[1]) + "\t" + df.format(values[2]));
        Log.d("Graphics2D", df.format(values[3]) + "\t" + df.format(values[4]) + "\t" + df.format(values[5]));
        Log.d("Graphics2D", df.format(values[6]) + "\t" + df.format(values[7]) + "\t" + df.format(values[8]));
    }

    public static PointF translate(Matrix matrix, PointF point) {
        float[] points = new float[]{point.x, point.y};
        matrix.mapPoints(points);
        return new PointF(points[0], points[1]);
    }

    public static PointF invert(Matrix matrix, PointF point) {
        float[] points = new float[]{point.x, point.y};
        Matrix inverse = new Matrix();
        matrix.invert(inverse);
        inverse.mapPoints(points);
        return new PointF(points[0], points[1]);
    }

    // Vector
    // Distance between two points
    public static float distance(PointF p1, PointF p2) {
        return distance(p1.x, p1.y, p2.x, p2.y);
    }

    public static float distance(float x1, float y1, float x2, float y2) {
        float d = 0.0f;
        try {
            float dx = x2 - x1;
            float dy = y2 - y1;
            d = (float) Math.sqrt(dx * dx + dy * dy);
        } catch (Exception e) {
            Log.d("Graphics2D", "Exception calculating distance: " + e.toString());
        }
        return d;
    }

    public static float[] getNormalizedVector(PointF p1, PointF p2) {
        double vx = p2.x - p1.x;
        double vy = p2.y - p1.y;
        double d = Math.sqrt(vx * vx + vy * vy);
        return new float[]{(float) (vx / d), (float) (vy / d)};
    }

    // Angle
    public static float findAngle(PointF p1, PointF p2) {
        double dx = p1.x - p2.x;
        double dy = p1.y - p2.y;

        double angle;
        if (dx > -0.01d && dx < 0.01d) {
            angle = 90.0d;
            if (dy < 0.0d) angle *= -1;
        } else if (dy > -0.01d & dy < 0.01d) {
            angle = 0.0d;
        } else {
            angle = (float) Math.toDegrees(Math.atan(dx/dy));
            if(dx < 0 && dy > 0) angle = 90 + angle;
            if(dx > 0 && dy > 0) angle = 90 + angle;
            if(dx > 0 && dy < 0) angle = -90 + angle;
            if(dx < 0 && dy < 0) angle = -90 + angle;
        }
        return (float) angle;
    }
}


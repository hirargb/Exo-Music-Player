package com.example.olaplaystudios.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import java.util.Random;

/**
 * a music visualizer sort of animation (with random data)
 */
public class MusicVisualizerLarge extends View {

    Random random = new Random();

    Paint paint = new Paint();
    private Runnable animateView = new Runnable() {
        @Override
        public void run() {

            //run every 150 ms
            postDelayed(this, 150);

            invalidate();
        }
    };

    public MusicVisualizerLarge(Context context) {
        super(context);
        new MusicVisualizer(context, null);
    }

    public MusicVisualizerLarge(Context context, AttributeSet attrs) {
        super(context, attrs);

        //start runnable
        removeCallbacks(animateView);
        post(animateView);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //set paint style, Style.FILL will fill the color, Style.STROKE will stroke the color
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(ContextCompat.getColor(this.getContext(), android.R.color.white));
        canvas.drawRect(getDimensionInPixel(0), getHeight() - (10 + random.nextInt((int) (getHeight() / 1.5f) - 19)), getDimensionInPixel(7), getHeight(), paint);
        canvas.drawRect(getDimensionInPixel(10), getHeight() - (10 + random.nextInt((int) (getHeight() / 1.5f) - 19)), getDimensionInPixel(17), getHeight(), paint);
        canvas.drawRect(getDimensionInPixel(20), getHeight() - (10 + random.nextInt((int) (getHeight() / 1.5f) - 19)), getDimensionInPixel(27), getHeight(), paint);
        canvas.drawRect(getDimensionInPixel(30), getHeight() - (10 + random.nextInt((int) (getHeight() / 1.5f) - 19)), getDimensionInPixel(37), getHeight(), paint);
        canvas.drawRect(getDimensionInPixel(40), getHeight() - (10 + random.nextInt((int) (getHeight() / 1.5f) - 19)), getDimensionInPixel(47), getHeight(), paint);
        canvas.drawRect(getDimensionInPixel(50), getHeight() - (10 + random.nextInt((int) (getHeight() / 1.5f) - 19)), getDimensionInPixel(57), getHeight(), paint);
        canvas.drawRect(getDimensionInPixel(60), getHeight() - (10 + random.nextInt((int) (getHeight() / 1.5f) - 19)), getDimensionInPixel(67), getHeight(), paint);
        canvas.drawRect(getDimensionInPixel(70), getHeight() - (10 + random.nextInt((int) (getHeight() / 1.5f) - 19)), getDimensionInPixel(77), getHeight(), paint);
        canvas.drawRect(getDimensionInPixel(80), getHeight() - (10 + random.nextInt((int) (getHeight() / 1.5f) - 19)), getDimensionInPixel(87), getHeight(), paint);
        canvas.drawRect(getDimensionInPixel(90), getHeight() - (10 + random.nextInt((int) (getHeight() / 1.5f) - 19)), getDimensionInPixel(97), getHeight(), paint);
        canvas.drawRect(getDimensionInPixel(100), getHeight() - (10 + random.nextInt((int) (getHeight() / 1.5f) - 19)), getDimensionInPixel(107), getHeight(), paint);
        canvas.drawRect(getDimensionInPixel(110), getHeight() - (10 + random.nextInt((int) (getHeight() / 1.5f) - 19)), getDimensionInPixel(117), getHeight(), paint);
        canvas.drawRect(getDimensionInPixel(120), getHeight() - (10 + random.nextInt((int) (getHeight() / 1.5f) - 19)), getDimensionInPixel(127), getHeight(), paint);
        canvas.drawRect(getDimensionInPixel(130), getHeight() - (10 + random.nextInt((int) (getHeight() / 1.5f) - 19)), getDimensionInPixel(137), getHeight(), paint);
        canvas.drawRect(getDimensionInPixel(140), getHeight() - (10 + random.nextInt((int) (getHeight() / 1.5f) - 19)), getDimensionInPixel(147), getHeight(), paint);
        canvas.drawRect(getDimensionInPixel(150), getHeight() - (10 + random.nextInt((int) (getHeight() / 1.5f) - 19)), getDimensionInPixel(157), getHeight(), paint);
        canvas.drawRect(getDimensionInPixel(160), getHeight() - (10 + random.nextInt((int) (getHeight() / 1.5f) - 19)), getDimensionInPixel(167), getHeight(), paint);
        canvas.drawRect(getDimensionInPixel(170), getHeight() - (10 + random.nextInt((int) (getHeight() / 1.5f) - 19)), getDimensionInPixel(177), getHeight(), paint);
        canvas.drawRect(getDimensionInPixel(180), getHeight() - (10 + random.nextInt((int) (getHeight() / 1.5f) - 19)), getDimensionInPixel(187), getHeight(), paint);
        canvas.drawRect(getDimensionInPixel(190), getHeight() - (10 + random.nextInt((int) (getHeight() / 1.5f) - 19)), getDimensionInPixel(197), getHeight(), paint);
        canvas.drawRect(getDimensionInPixel(200), getHeight() - (10 + random.nextInt((int) (getHeight() / 1.5f) - 19)), getDimensionInPixel(207), getHeight(), paint);
        canvas.drawRect(getDimensionInPixel(210), getHeight() - (10 + random.nextInt((int) (getHeight() / 1.5f) - 19)), getDimensionInPixel(217), getHeight(), paint);
    }

    public void setColor(int color) {
        paint.setColor(color);
        invalidate();
    }

    //get all dimensions in dp so that views behaves properly on different screen resolutions
    private int getDimensionInPixel(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if (visibility == VISIBLE) {
            removeCallbacks(animateView);
            post(animateView);
        } else if (visibility == GONE) {
            removeCallbacks(animateView);
        }
    }
}
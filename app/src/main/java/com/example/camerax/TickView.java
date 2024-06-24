package com.example.camerax;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

public class TickView extends View {

    private Paint circlePaint;
    private Paint tickPaint;
    private Path tickPath;
    private PathMeasure pathMeasure;
    private float pathLength;
    private float animatedPathLength;
    private RectF circleBounds;
    private boolean drawTick = false;

    public TickView(Context context, AttributeSet attrs) {super(context, attrs);
        init();
    }

    private void init() {
        circlePaint = new Paint();
        circlePaint.setColor(Color.GRAY);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeWidth(10f);

        tickPaint = new Paint();
        tickPaint.setColor(Color.GREEN);
        tickPaint.setStyle(Paint.Style.STROKE);
        tickPaint.setStrokeWidth(50f);
        tickPaint.setStrokeCap(Paint.Cap.ROUND);

        tickPath = new Path();
        // Adjust tick path to fit inside the circle
        tickPath.moveTo(75, 125);
        tickPath.lineTo(125, 175);
        tickPath.lineTo(175, 75);

        pathMeasure = new PathMeasure(tickPath, false);
        pathLength = pathMeasure.getLength();

        ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
        animator.setDuration(1000); // Adjust animation duration
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                animatedPathLength = pathLength * (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        // Delay the tick animation after the circle is drawn
        animator.setStartDelay(500); // Adjust delay as needed
        animator.start();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int centerX = w / 2;
        int centerY = h / 2;
        int radius = Math.min(centerX, centerY) - 25; // Adjust circle size
        circleBounds = new RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius);
        drawTick = true; // Start drawing the tick after circle is formed
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // Draw the circle
        canvas.drawOval(circleBounds, circlePaint);
        if (drawTick) {
            Path drawnPath = new Path();
            pathMeasure.getSegment(0, animatedPathLength, drawnPath, true);
            canvas.drawPath(drawnPath, tickPaint);
        }
    }
}

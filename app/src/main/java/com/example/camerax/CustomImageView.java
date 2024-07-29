package com.example.camerax;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

public class CustomImageView extends AppCompatImageView {

    private final Paint rectanglePaint;
    private final Paint pathpaint;

    private final float squareSize = 795f;
    private final float cornerSize = 120;
    private float centerX = (float) getWidth() / 2;
    private float centerY = (float) getHeight() / 2;
    private final float gap = 16f;
    private Path path;
    private final float sub = 200;



// Top-left corner

    public CustomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        rectanglePaint = new Paint();
        rectanglePaint.setColor(Color.BLACK); // Set your desired fill color
        rectanglePaint.setStyle(Paint.Style.FILL);
        rectanglePaint.setAlpha(200);

         pathpaint = new Paint();
         pathpaint.setColor( getResources().getColor(R.color.blue));
         pathpaint.setStyle(Paint.Style.STROKE);
         pathpaint.setStrokeWidth(10f);

         path = new Path();



    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Draw filled rectangle (matching ImageView dimensions)
        //upper
        canvas.drawRect(0f, 0f,getWidth(),(getHeight()-squareSize)/2-sub, rectanglePaint);
        //lower
        canvas.drawRect(0f,(getHeight()+squareSize)/2-sub, getWidth(), getHeight(), rectanglePaint);
        //left
        canvas.drawRect(0f,(getHeight()-squareSize)/2-sub, (getWidth()-squareSize)/2, (getHeight()+squareSize)/2-sub, rectanglePaint);
        //right
        canvas.drawRect((getWidth()+squareSize)/2, (getHeight()-squareSize)/2-sub, getWidth(), (getHeight()+squareSize)/2-sub, rectanglePaint);



//Top-left corner
        path.moveTo((getWidth()-squareSize)/2-gap+cornerSize,(getHeight()-squareSize)/2-gap-sub);
        path.lineTo((getWidth()-squareSize)/2-gap, (getHeight()-squareSize)/2-gap-sub);
        path.lineTo((getWidth()-squareSize)/2-gap, (getHeight()-squareSize)/2-gap+cornerSize-sub);

// Top-right corner
        path.moveTo((getWidth()+squareSize)/2+gap-cornerSize, (getHeight()-squareSize)/2-gap-sub);
        path.lineTo((getWidth()+squareSize)/2+gap, (getHeight()-squareSize)/2-gap-sub);
        path.lineTo((getWidth()+squareSize)/2+gap, (getHeight()-squareSize)/2-gap+cornerSize-sub);

// Bottom-left corner
        path.moveTo((getWidth()-squareSize)/2-gap, (getHeight()+squareSize)/2+gap-cornerSize-sub);
        path.lineTo((getWidth()-squareSize)/2-gap, (getHeight()+squareSize)/2+gap-sub);
        path.lineTo((getWidth()-squareSize)/2-gap+cornerSize, (getHeight()+squareSize)/2+gap-sub);

// Bottom-right corner
        path.moveTo((getWidth()+squareSize)/2+gap, (getHeight()+squareSize)/2+gap-cornerSize-sub);
        path.lineTo((getWidth()+squareSize)/2+gap, (getHeight()+squareSize)/2+gap-sub);
        path.lineTo((getWidth()+squareSize)/2+gap-cornerSize, (getHeight()+squareSize)/2+gap-sub);

        canvas.drawPath(path, pathpaint);




    }
}

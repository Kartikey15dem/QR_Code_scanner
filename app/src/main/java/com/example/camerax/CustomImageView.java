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

    private float squareSize = 0;
    private final float cornerSize = 140;

    private final float gap = 19f;
    private Path path;
    private final float sub = 400f;



// Top-left corner

    public CustomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        rectanglePaint = new Paint();
        rectanglePaint.setColor(Color.BLACK);
        rectanglePaint.setStyle(Paint.Style.FILL);
        rectanglePaint.setAlpha(180);

         pathpaint = new Paint();
         pathpaint.setColor( getResources().getColor(R.color.blue));
         pathpaint.setStyle(Paint.Style.STROKE);
         pathpaint.setStrokeWidth(10f);

         path = new Path();



    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(getWidth()<getHeight()) {
            squareSize = (float) (0.7*getWidth());
        }else{squareSize = getHeight() - 300f;}

        // Draw filled rectangle (matching ImageView dimensions)
        //upper
        canvas.drawRect(0f, 0f,getWidth(), (float) getHeight() /10, rectanglePaint);
        //lower
        canvas.drawRect(0f,(float) getHeight() /10 + squareSize, getWidth(), getHeight(), rectanglePaint);
        //left
        canvas.drawRect(0f,(float) getHeight() /10, (float) (0.15*getWidth()), (float) getHeight() /10 + squareSize, rectanglePaint);
        //right
        canvas.drawRect((float) (0.85*getWidth()), (float) getHeight() /10, getWidth(), (float) getHeight() /10 + squareSize, rectanglePaint);



//Top-left corner
        path.moveTo((float) 0.15*getWidth() - gap,(float) getHeight() /10 - gap +cornerSize);
        path.lineTo((float) 0.15*getWidth() - gap ,(float) getHeight() /10 - gap);
        path.lineTo((float) 0.15*getWidth() - gap + cornerSize,(float) getHeight() /10 - gap );

// Top-right corner
        path.moveTo((float) 0.85*getWidth() + gap, (float) getHeight() /10 - gap +cornerSize);
        path.lineTo((float) 0.85*getWidth() + gap, (float) getHeight() /10 - gap);
        path.lineTo((float) 0.85*getWidth() + gap - cornerSize, (float) getHeight() /10 - gap);

// Bottom-left corner
        path.moveTo((float) 0.15*getWidth() - gap, (float) getHeight() /10 + gap + squareSize -cornerSize);
        path.lineTo((float) 0.15*getWidth() - gap, (float) getHeight() /10 + gap +squareSize);
        path.lineTo((float) 0.15*getWidth() - gap + cornerSize, (float) getHeight() /10 + gap+ squareSize);

// Bottom-right corner
        path.moveTo((float) 0.85*getWidth() + gap, (float) getHeight() /10 + squareSize + gap - cornerSize);
        path.lineTo((float) 0.85*getWidth() + gap, (float) getHeight() /10 + squareSize + gap);
        path.lineTo((float) 0.85*getWidth() + gap - cornerSize, (float) getHeight() /10 + squareSize +gap);

        canvas.drawPath(path, pathpaint);




    }
}

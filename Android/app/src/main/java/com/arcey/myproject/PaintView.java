package com.arcey.myproject;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class PaintView extends View {
    public static ArrayList<Path> pathList = new ArrayList<>();
    public static ArrayList<Float> xCord = new ArrayList<>();
    public static ArrayList<Float> yCord = new ArrayList<>();
    public static ArrayList<String> moveCords = new ArrayList<>();
    public static Path path = new Path();
    int basX;
    int basY;
    public static Paint paintBrush = new Paint();
    public ViewGroup.LayoutParams params;
    public static boolean firstTime=true;
    int tempX;
    int tempY;


    public PaintView(Context context) {
        super(context);
        init(context);
    }

    public PaintView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PaintView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        paintBrush.setAntiAlias(true);
        paintBrush.setColor(Color.BLACK);
        paintBrush.setStyle(Paint.Style.STROKE);
        paintBrush.setStrokeCap(Paint.Cap.ROUND);
        paintBrush.setStrokeJoin(Paint.Join.ROUND);
        paintBrush.setStrokeWidth(25f);
        params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int)event.getX();
        int y = (int)event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if(firstTime){
                    basX=x;
                    basY=y;
                    tempY=y;
                    tempX=x;
                    path.moveTo(x, y);
                    firstTime=false;
                    moveCords.add(x+"-"+y);
                }
                else{
                    if(x - 50 < tempX && x+50>tempX){
                        x=tempX;
                    }if(y - 50 < tempY && y+50>tempY){
                        y=tempY;
                        System.out.println(tempX);
                    }
                    tempX=x;
                    tempY = y;
                    moveCords.add(x+"-"+y);
                    path.lineTo(x, y);
                    pathList.add(path);
                    invalidate();
                    path.moveTo(x, y);
                }
                return true;
            default:
                return false;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {

        paintBrush.setColor(Color.RED);
        paintBrush.setStrokeWidth(50f);
        canvas.drawPoint(basX,basY,paintBrush);
        invalidate();
        paintBrush.setStrokeWidth(25f);
        paintBrush.setColor(Color.BLACK);
        for (int i = 0; i < pathList.size(); i++) {
            canvas.drawPath(pathList.get(i),paintBrush);
            invalidate();
        }
    }
}

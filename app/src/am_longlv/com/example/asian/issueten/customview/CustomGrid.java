package com.example.asian.issueten.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CustomGrid extends View {
    Paint mPaint = new Paint();
    private float mFirstX;
    private float mFirstY;
    private float mSecondX;
    private float mSecondY;
    private float mScale = 1;
    private float mMoveX = 0;
    private float mMoveY = 0;
    private float mTranslateX = 0;
    private float mTranslateY = 0;

    public CustomGrid(Context context) {
        super(context);
    }

    public CustomGrid(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomGrid(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        float px = Math.abs(mFirstX + mSecondX) / 2;
        float py = Math.abs(mFirstY + mSecondY) / 2;
        canvas.scale(mScale, mScale);
//        canvas.translate(mMoveX, mMoveY);
        mPaint.setStrokeWidth(2);
        for (int i = 0; i <= getWidth() / 10; i++) {
            if (i == 5) {
                mPaint.setColor(0xFFFF0000);
            } else {
                mPaint.setColor(0xFF000000);
            }
            canvas.drawLine((float) (i * getWidth()) / 10, 0, (float) (i * getWidth()) / 10, getHeight(), mPaint);
        }
        for (int i = 0; i <= getHeight() / 10; i++) {
            if (i == 5) {
                mPaint.setColor(0xFFFF0000);
            } else {
                mPaint.setColor(0xFF000000);
            }
            canvas.drawLine(0, (float) (i * getHeight()) / 10, getWidth(), (float) (i * getHeight()) / 10, mPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mFirstX = event.getX(0);
                mFirstY = event.getY(0);
                mSecondX = event.getX(0);
                mSecondY = event.getY(0);
                break;
            case MotionEvent.ACTION_MOVE:
                if (event.getPointerCount() > 1) {
                    calculatorScale(event);
                }
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                performClick();
                break;
        }
        return true;
    }

    private void calculatorScale(MotionEvent event) {
        float distance = getDistance(event.getX(0), event.getY(0), event.getX(1), event.getY(1));
        float distance1 = getDistance(mFirstX, mFirstY, mSecondX, mSecondY);
        if (mFirstX != mSecondX && mFirstY != mSecondY) {
            if (distance1 > distance) {
                if ((mScale - ((distance1 / distance) - 1)) < 1) {
                    mScale = 1;
                } else {
                    mScale -= (distance1 / distance - 1);
                }
            } else {
                if ((mScale + (distance / distance1) - 1) > 5) {
                    mScale = 5;
                } else {
                    mScale += (distance / distance1 - 1);
                    mMoveX -= (distance / distance1 - 1) * getWidth() / 2;
                }
            }
        }
        mFirstX = event.getX(0);
        mFirstY = event.getY(0);
        mSecondX = event.getX(1);
        mSecondY = event.getY(1);
    }

    private float getDistance(float x, float y, float xs, float ys) {
        float x1 = x - xs;
        float y1 = y - ys;
        return (float) Math.sqrt(x1 * x1 + y1 * y1);
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }
}

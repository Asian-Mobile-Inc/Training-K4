package com.example.asian.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.asian.R;
import com.example.asian.model.SellExpense;

import java.util.ArrayList;

public class MyChartView extends View {
    private int mColorSale;
    private int mColorExpenses;
    private ArrayList<SellExpense> mSellExpenses;
    private Paint mPaintBlack;
    private TextPaint mTextPaint;
    private TextPaint mTextCenterPaint;
    private Paint mPaintSales;
    private Paint mPaintExpenses;
    private Paint mPaintWall;
    private final ArrayList<Integer> mValues = new ArrayList<>();
    private final int mMaxSizeColumItem = 6;
    private float mScaleFactor = 1.0f;
    private int mXStart;
    private int mYStart;
    private int mSpaceItemMonth;
    private int mSpaceItemValue;
    private int mYEnd;
    private int mXEnd;
    private float mPosX;
    private float mLastTouchX;
    private float mLackSpace;
    private float mFocusX = 1f;
    private float mFocusY = 1f;
    private ScaleGestureDetector mScaleGestureDetector;

    public MyChartView(Context context) {
        super(context);
        initPaint();
    }

    public MyChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
        initPaint();
        mScaleGestureDetector = new ScaleGestureDetector(this.getContext(), new ScaleListener());
    }

    public void setSellExpenses(ArrayList<SellExpense> sellExpenses) {
        this.mSellExpenses = sellExpenses;
        initSellExpenses();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyChartView_XML);
        mColorSale = typedArray.getColor(R.styleable.MyChartView_XML_color_sales, getResources().getColor(R.color.blue_4A82BD));
        mColorExpenses = typedArray.getColor(R.styleable.MyChartView_XML_color_expenses, getResources().getColor(R.color.red_C6514A));
        typedArray.recycle();
    }

    private void initPaint() {
        mPaintBlack = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint = new TextPaint();
        mTextCenterPaint = new TextPaint();
        mPaintSales = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintExpenses = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintWall = new Paint(Paint.LINEAR_TEXT_FLAG);
        mPaintBlack.setColor(getResources().getColor(R.color.black));
        mPaintBlack.setStyle(Paint.Style.STROKE);
        mPaintBlack.setStrokeWidth(3);
        mTextPaint.setColor(getResources().getColor(R.color.black));
        mTextPaint.setTextAlign(Paint.Align.RIGHT);
        mTextPaint.setTextSize(23);
        mTextCenterPaint.setColor(getResources().getColor(R.color.black));
        mTextCenterPaint.setTextAlign(Paint.Align.CENTER);
        mTextCenterPaint.setTextSize(28);
        mPaintSales.setColor(mColorSale);
        mPaintSales.setStyle(Paint.Style.FILL);
        mPaintExpenses.setColor(mColorExpenses);
        mPaintExpenses.setStyle(Paint.Style.FILL);
        mPaintWall.setColor(getResources().getColor(R.color.white));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        if (mSellExpenses == null) {
            mSellExpenses = new ArrayList<>();
        }
        initValues();
        canvas.save();
        canvas.scale(mScaleFactor, mScaleFactor, mFocusX, mFocusY);
        canvas.translate(mPosX, 1);
        drawLineValue(canvas);
        drawChart(canvas);
        canvas.restore();
        drawWallY(canvas);
        drawMonths(canvas);
        drawWallX(canvas);
        drawAsis(canvas);
        drawValues(canvas);
        drawWallSmall(canvas);
    }

    private void initValues() {
        mXStart = 13 * getWidth() / 100;
        mYStart = 5 * getHeight() / 100;
        mSpaceItemMonth = (getWidth() - mXStart) / mMaxSizeColumItem;
        mSpaceItemValue = (getHeight() - (12 * getHeight() / 100)) / (mValues.size() - 1);
        mYEnd = (mValues.size() - 1) * mSpaceItemValue + mYStart;
        mXEnd = mSellExpenses.size() * mSpaceItemMonth + mXStart;
        mLackSpace = (mSellExpenses.size() - mMaxSizeColumItem) * mSpaceItemMonth;
    }

    private void initSellExpenses() {
        float valueMax = 0;
        for (int i = 0; i < mSellExpenses.size(); i++) {
            if (mSellExpenses.get(i).getSales() > valueMax) {
                valueMax = mSellExpenses.get(i).getSales();
            }
            if (mSellExpenses.get(i).getExpenses() > valueMax) {
                valueMax = mSellExpenses.get(i).getExpenses();
            }
        }

        int value = 0;
        mValues.add(value);
        do {
            value = value + 20000;
            mValues.add(value);
        } while (value < valueMax);
    }

    private void drawWallX(Canvas canvas) {
        canvas.drawRect(0, 0, mXStart, getHeight(), mPaintWall);
    }

    private void drawWallY(Canvas canvas) {
        canvas.drawRect(mXStart, mYEnd, getWidth(), getHeight(), mPaintWall);
        canvas.drawRect(mXStart, 0, getWidth(), mYStart, mPaintWall);
    }

    public void drawWallSmall(Canvas canvas) {
        canvas.drawRect(0, 0, mXStart, mYStart - 20, mPaintWall);
        canvas.drawRect(0, mYEnd + 20, mXStart, getHeight(), mPaintWall);
    }

    private void drawAsis(Canvas canvas) {
        canvas.drawLine(mXStart, mYStart, mXStart, mYEnd, mPaintBlack);
        canvas.drawLine(mXStart, mYEnd, mXEnd, mYEnd, mPaintBlack);
    }

    private void drawLineValue(Canvas canvas) {
        for (int i = 0; i < mValues.size(); i++) {
            canvas.drawLine(mXStart, i * mSpaceItemValue + mYStart, mXEnd, i * mSpaceItemValue + mYStart, mPaintBlack);
        }
    }

    private void drawValues(Canvas canvas) {
        canvas.save();
        canvas.scale(1, mScaleFactor, mFocusX, mFocusY);
        for (int i = 0; i < mValues.size(); i++) {
            canvas.drawText((int) (mValues.get(mValues.size() - 1 - i)) + "$", mXStart - 5, i * mSpaceItemValue + mYStart + 10, mTextPaint);
        }
        canvas.restore();
    }

    private void drawMonths(Canvas canvas) {
        canvas.save();
        canvas.scale(mScaleFactor, 1, mFocusX, mFocusY);
        canvas.translate(mPosX, 1);
        for (int i = 0; i < mSellExpenses.size(); i++) {
            canvas.drawText(mSellExpenses.get(i).getMonth(), mXStart + i * mSpaceItemMonth + ((float) mSpaceItemMonth / 2), mYEnd + 30, mTextCenterPaint);
        }
        canvas.restore();
    }

    private void drawChart(Canvas canvas) {
        int spaceColumnChart = mSpaceItemMonth / (mMaxSizeColumItem / 2);
        int spaceCenterColumn = mSpaceItemMonth / mMaxSizeColumItem;
        for (int i = 0; i < mSellExpenses.size(); i++) {
            canvas.drawRect(mXStart + i * mSpaceItemMonth + spaceCenterColumn, mYEnd - (mSellExpenses.get(i).getSales() / 20000 * mSpaceItemValue), mXStart + i * mSpaceItemMonth + spaceColumnChart + spaceCenterColumn, mYEnd, mPaintSales);
            canvas.drawRect(mXStart + i * mSpaceItemMonth + spaceColumnChart + spaceCenterColumn, mYEnd - (mSellExpenses.get(i).getExpenses() / 20000 * mSpaceItemValue), mXStart + i * mSpaceItemMonth + spaceColumnChart + spaceColumnChart + spaceCenterColumn, mYEnd, mPaintExpenses);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getPointerCount() > 1) {
            mFocusX = mScaleGestureDetector.getFocusX();
            mFocusY = mScaleGestureDetector.getFocusY();
            mScaleGestureDetector.onTouchEvent(event);
        }
        if (mSellExpenses.size() > 6) {
            final int action = event.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN: {
                    mLastTouchX = event.getX();
                    break;
                }
                case MotionEvent.ACTION_MOVE: {
                    final float x = event.getX();

                    final float dx = x - mLastTouchX;
                    float value = mPosX + dx;
                    if (value > 0) {
                        if (mScaleFactor == 1) {
                            mPosX = 0;
                        } else {
                            if (value > mXStart * (mScaleFactor - 0.75f)) {
                                mPosX = mXStart * (mScaleFactor - 0.75f);
                            } else {
                                mPosX = value;
                            }
                        }
                    } else if (value < (-mLackSpace * mScaleFactor)) {
                        mPosX = -mLackSpace * mScaleFactor;
                    } else {
                        mPosX = value;
                    }
                    mLastTouchX = x;
                    invalidate();
                    break;
                }
            }
        }
        return true;
    }

    class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(@NonNull ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor();
            mScaleFactor = Math.max(1f, Math.min(mScaleFactor, 5.0f));
            invalidate();
            return true;
        }
    }
}

package com.example.asian.issueten.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.asian.R;
import com.example.asian.issueten.model.SellExpense;

import java.util.ArrayList;
import java.util.List;

public class CustomGrid extends View {
    private final List<SellExpense> mSellExpenses = new ArrayList<>();
    private Paint mPaintSales;
    private Paint mPaintExpense;
    private final Context mContext;
    private RectF mRectFAxis;
    private float mTopAxis;
    private float mLeftAxis;
    private float mRightAxis;
    private float mBottomAxis;

    private Paint mPaint;
    private int mHeight;
    private int mWidth;
    private static final int COUNT_RATIO = 12;
    private static final int TEXT_SIZE = 35;
    private static final int COUNT_LINE_Y_AXIS = 8;
    private int mColorSales;
    private int mColorExpense;
    private float mWidthChart = 0;
    private static final int SCALE_DEFAULT = 1;
    private static final int SCALE_MAX = 5;
    private ScaleGestureDetector mScaleDetector;
    private GestureDetector mGestureDetector;
    private float mScaleFactor = 1.f;
    private float mMoveX = 0;
    private float mMoveY = 0;
    private float mXFingerFirst = 0;
    private float mYFingerFirst = 0;
    private float mScaleBefore = 1f;

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor();
            mScaleFactor = Math.max(SCALE_DEFAULT, Math.min(mScaleFactor, SCALE_MAX));
            invalidate();
            return true;
        }
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(@Nullable MotionEvent e1, @NonNull MotionEvent e2, float distanceX, float distanceY) {
            mMoveX -= distanceX;
            mMoveY += distanceY;
            float space = ((mWidth - 3 * mLeftAxis) / mSellExpenses.size());
            float minScrollWidth = (mWidth - 3f * mLeftAxis)
                    / (mScaleFactor * (mSellExpenses.size() / 6f)) - space * mSellExpenses.size();
            if (mMoveX < minScrollWidth) {
                mMoveX = minScrollWidth;
            } else if (mMoveX > 0) {
                mMoveX = 0;
            }
            if (mMoveY < -mHeight * (mScaleFactor - 1)) {
                mMoveY = -mHeight * (mScaleFactor - 1);
            } else if (mMoveY > 0) {
                mMoveY = 0;
            }
            invalidate();
            return true;
        }
    }

    public void setupDataChart(List<SellExpense> sellExpenses) {
        mSellExpenses.clear();
        mSellExpenses.addAll(sellExpenses);
        invalidate();
    }

    public CustomGrid(Context context) {
        super(context);
        mContext = context;
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        mGestureDetector = new GestureDetector(context, new GestureListener());
    }

    public CustomGrid(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setUpAttribute(context, attrs);
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        mGestureDetector = new GestureDetector(context, new GestureListener());
        mContext = context;
    }

    public CustomGrid(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    private void setUpAttribute(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.MyChartView,
                0, 0);
        mColorSales = a.getColor(R.styleable.MyChartView_mcv_sales, Color.RED);
        mColorExpense = a.getColor(R.styleable.MyChartView_mcv_expenses, Color.BLUE);
    }

    private void setupVariable() {
        mPaintSales = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintSales.setStrokeWidth(mWidthChart);
        mPaintSales.setColor(mColorSales);
        mPaintExpense = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintExpense.setStrokeWidth(mWidthChart);
        mPaintExpense.setColor(mColorExpense);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHeight = getHeight();
        mWidth = getWidth();
        mLeftAxis = (float) mWidth / COUNT_RATIO;
        mRightAxis = (float) mWidth - (float) (2 * mWidth) / COUNT_RATIO;
        mTopAxis = (float) mHeight / COUNT_RATIO;
        mBottomAxis = mHeight - mTopAxis;
        mRectFAxis = new RectF(mLeftAxis, mTopAxis, mRightAxis, mBottomAxis);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        setupVariable();
        paintAxis(canvas);
        paintChart(canvas);
        paintWall(canvas);
    }

    private void paintAxis(Canvas canvas) {
        mPaint.setColor(ContextCompat.getColor(mContext, R.color.gray_BDBDBD));
        canvas.drawRect(mRectFAxis, mPaint);
    }

    private void paintChart(Canvas canvas) {
        paintLinePrice(canvas);
        canvas.save();
        canvas.translate(mLeftAxis, mBottomAxis);
        if (mSellExpenses.size() > 6) {
            canvas.scale(mScaleFactor * (mSellExpenses.size() / 6f), mScaleFactor);
        } else {
            canvas.scale(mScaleFactor, mScaleFactor);
        }
        float space = ((mWidth - 3 * mLeftAxis) / mSellExpenses.size());
        canvas.translate(mMoveX, -mMoveY);
        for (int i = 0; i < mSellExpenses.size(); i++) {
            SellExpense sellExpense = mSellExpenses.get(i);
            long maxValue = getMaxValue();
            float axisY = mHeight - 2 * mTopAxis;
            float x = (i * space);
            float xNext = ((i + 1) * space);
            mWidthChart = space / 4;
            mPaintExpense.setStrokeWidth(mWidthChart);
            mPaintSales.setStrokeWidth(mWidthChart);
            float xDraw = x + Math.abs(x - xNext) / 2 - mWidthChart / 2;
            float ySales = -(sellExpense.getSales() * axisY / maxValue);
            float yExpense = -(sellExpense.getExpense() * axisY / maxValue);
            canvas.drawLine(xDraw, 0, xDraw, ySales, mPaintSales);
            canvas.drawLine(xDraw + mWidthChart, 0, xDraw
                    + mWidthChart, yExpense, mPaintExpense);
        }
        canvas.restore();
        paintMonth(canvas);
    }

    private void paintLinePrice(Canvas canvas) {
        canvas.save();
        canvas.translate(mLeftAxis, mBottomAxis);
        canvas.scale(mScaleFactor * 2, mScaleFactor);
        canvas.translate(mMoveX, -mMoveY);
        mPaint.setColor(ContextCompat.getColor(mContext, R.color.black));
        float axisY = mHeight - mTopAxis;
        for (int i = 1; i <= COUNT_LINE_Y_AXIS; i++) {
            canvas.drawLine(0,
                    (-(((float) (i * (mHeight - 2 * mTopAxis)) / COUNT_LINE_Y_AXIS))),
                    getWidth(),
                    (-(((float) (i * (mHeight - 2 * mTopAxis)) / COUNT_LINE_Y_AXIS))), mPaint);
        }
        canvas.restore();
    }

    private void paintMonth(Canvas canvas) {
        canvas.save();
        if (mSellExpenses.size() > 6) {
            canvas.scale(mScaleFactor * (mSellExpenses.size() / 6f), mScaleFactor);
        } else {
            canvas.scale(mScaleFactor, mScaleFactor);
        }
        canvas.translate(mMoveX, 1);
        mPaint.setColor(ContextCompat.getColor(mContext, R.color.black));
        mPaint.setTextSize(TEXT_SIZE);
        for (int i = 0; i < mSellExpenses.size(); i++) {
            float startX = (float) getWidth() / COUNT_RATIO;
            float startY = (float) getHeight() / COUNT_RATIO;
            float axisY = mHeight - 2 * startY;
            float space = ((getWidth() - 3 * startX) / mSellExpenses.size());
            float x = (i * space + startX);
            float xNext = ((i + 1) * space + startX);
            float xDraw = x + Math.abs(x - xNext) / 2 - mWidthChart / 2;
            mPaint.setStrokeWidth((float) getHeight() / COUNT_RATIO);
            mPaint.setColor(ContextCompat.getColor(mContext, R.color.white));
            canvas.drawLine(x, mHeight - startY / 2, xNext, mHeight
                    - startY / 2, mPaint);
            mPaint.setStrokeWidth(1);
            mPaint.setColor(ContextCompat.getColor(mContext, R.color.black));
            canvas.drawText(mSellExpenses.get(i).convertMonthToString(), xDraw,
                    axisY + (float) (startY * 1.5), mPaint);
            canvas.drawLine(x - 20, mHeight - startY, xNext,
                    mHeight - startY, mPaint);
            canvas.drawLine(x, mHeight - startY, x, mHeight - startY + 20, mPaint);
        }
        canvas.restore();
    }

    private void paintPrice(Canvas canvas) {
        canvas.save();
        long maxValue = getMaxValue();
        float startY = (float) getHeight() / COUNT_RATIO;
        float originX = (float) getWidth() / COUNT_RATIO;
        float axisY = mHeight - startY;
        Paint paint = new Paint();
        paint.setColor(ContextCompat.getColor(mContext, R.color.black));
        paint.setTextSize(TEXT_SIZE);
        paint.setStrokeWidth(1);
        for (int i = 0; i <= COUNT_LINE_Y_AXIS; i++) {
            if ((((axisY - (((i * (mHeight - 2 * startY)) / 8)))) >= startY)
                    && ((axisY - (((i * (mHeight - 2 * startY)) / 8))))
                    <= mHeight - startY) {
                canvas.drawLine(originX - 20,
                        (axisY - (((i * (mHeight - 2 * startY)) / 8))),
                        originX, (axisY - (((i * (mHeight - 2 * startY)) / 8)))
                        , paint);
                canvas.drawText("$" + i * maxValue / 8, 0,
                        (axisY - (((i * (mHeight - 2 * startY)) / 8))),
                        paint);
            }
        }
        canvas.restore();
    }

    private void paintWall(Canvas canvas) {
        canvas.save();
        mPaint.setColor(ContextCompat.getColor(mContext, R.color.white));
        mPaint.setStrokeWidth(((float) getWidth() / COUNT_RATIO) * 2);
        canvas.drawLine(getWidth() - (float) getWidth() / COUNT_RATIO, 0,
                getWidth() - (float) getWidth() / COUNT_RATIO, getHeight(), mPaint);
        mPaint.setStrokeWidth(((float) getWidth() / COUNT_RATIO * 2));
        canvas.drawLine(0, 0, 0, getHeight(), mPaint);
        mPaint.setStrokeWidth(((float) getHeight() / COUNT_RATIO));
        canvas.drawLine(0, mTopAxis / 2, getWidth(),
                mTopAxis / 2, mPaint);
        paintHintColor(canvas);
        paintPrice(canvas);
        canvas.restore();
    }

    private void paintHintColor(Canvas canvas) {
        Paint paint = new Paint();
        paint.setStrokeWidth(30);
        paint.setTextSize(30);
        paint.setColor(mColorSales);
        canvas.drawLine(getWidth() - (float) (getWidth() * 2 / COUNT_RATIO) + 50,
                (float) getHeight() / 2 - 30,
                getWidth() - (float) (getWidth() * 2 / COUNT_RATIO) + 80,
                (float) getHeight() / 2 - 30, paint);
        canvas.drawText(mContext.getString(R.string.sales),
                getWidth() - (float) (getWidth() * 2 / COUNT_RATIO) + 100,
                (float) getHeight() / 2 - 30, paint);
        paint.setColor(mColorExpense);
        canvas.drawLine(getWidth() - (float) (getWidth() * 2 / COUNT_RATIO) + 50,
                (float) getHeight() / 2 + 30,
                getWidth() - (float) (getWidth() * 2 / COUNT_RATIO) + 80,
                (float) getHeight() / 2 + 30, paint);
        canvas.drawText(mContext.getString(R.string.expenses),
                getWidth() - (float) (getWidth() * 2 / COUNT_RATIO) + 100,
                (float) getHeight() / 2 + 30, paint);
    }

    private long getMaxValue() {
        long max = 0;
        for (SellExpense sellExpense : mSellExpenses) {
            if (sellExpense.getSales() > max) {
                max = sellExpense.getSales();
            }
            if (sellExpense.getExpense() > max) {
                max = sellExpense.getExpense();
            }
        }
        return max;
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        performClick();
        mScaleDetector.onTouchEvent(event);
        mGestureDetector.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mXFingerFirst = event.getX();
                mYFingerFirst = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                calculateMove(event);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }

    private void calculateMove(MotionEvent event) {
        float ratioScale = mScaleFactor / mScaleBefore;
        mMoveX = mMoveX + (1 - ratioScale) * (event.getX() - mScaleDetector.getFocusX());
        mMoveY = mMoveY + (1 - ratioScale) * (event.getY() - mScaleDetector.getFocusY());
        mScaleBefore = mScaleFactor;
    }
}

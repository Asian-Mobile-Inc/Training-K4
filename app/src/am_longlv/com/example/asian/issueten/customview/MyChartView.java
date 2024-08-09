package com.example.asian.issueten.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.asian.R;
import com.example.asian.issueten.model.SellExpense;

import java.util.ArrayList;
import java.util.List;

public class MyChartView extends View {
    private final List<SellExpense> mSellExpenses = new ArrayList<>();
    private Paint mPaintSales;
    private Paint mPaintExpense;
    private final Context mContext;
    private int mHeight;
    private static final int COUNT_RATIO = 12;
    private static final int COUNT_MONTH = 12;
    private static final int TEXT_SIZE = 35;
    private static final int COUNT_LINE_Y_AXIS = 8;
    private int mColorSales;
    private int mColorExpense;
    private float mWidthChart = 0;
    private float mXFingerFirst = 0;
    private float mYFingerFirst = 0;
    private float mXFingerSecond = 0;
    private float mYFingerSecond = 0;
    private float mScale = 1;
    private float mMoveX = 0;
    private float mMoveY = 0;
    private static final int SCALE_DEFAULT = 1;
    private static final int SCALE_MAX = 5;
    private boolean mIsAnm = true;
    private float mPercentAmn = 0.1f;
    private long mTimeDown;
    private float mDownX;
    private float mAcceleration = 0f;

    public void setupDataChart(List<SellExpense> sellExpenses) {
        mSellExpenses.clear();
        mSellExpenses.addAll(sellExpenses);
        invalidate();
    }

    public MyChartView(Context context) {
        super(context);
        mContext = context;
    }

    public MyChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setUpAttribute(context, attrs);
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

    public MyChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = Math.min(heightMeasureSpec, widthMeasureSpec);
        super.onMeasure(widthMeasureSpec, height);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        initPaint();
        paintAxis(canvas);
        paintChart(canvas);
        paintWall(canvas);
        if (mIsAnm) {
            calPercentAmn();
            invalidate();
        }
        if (mAcceleration > 0.5 || mAcceleration < -0.5) {
            calMoveXAcceleration();
            invalidate();
        }
    }

    private void calMoveXAcceleration() {
        int ratioAcceleration = 100;
        if (Math.abs(mAcceleration) > 3) {
            ratioAcceleration = 50;
        }
        if (Math.abs(mAcceleration) > 6) {
            ratioAcceleration = 30;
        }
        if (mAcceleration > 0) {
            mAcceleration -= 0.1f;
            mMoveX += (float) getWidth() / ratioAcceleration;
        } else {
            mAcceleration += 0.1f;
            mMoveX -= (float) getWidth() / ratioAcceleration;
        }
        float space = ((float) (getWidth() * 2 - 6 * getWidth() / COUNT_RATIO) / COUNT_MONTH);
        float minPage;
        if (mSellExpenses.size() <= 6) {
            space = ((float) (getWidth() - 3 * getWidth() / COUNT_RATIO) / mSellExpenses.size());
            minPage = 0;
        } else {
            minPage = -space * (mSellExpenses.size() - 6);
        }
        if (mMoveX + (float) getWidth() / 100 < minPage - space * (mScale - 1) * mSellExpenses.size()) {
            mMoveX = minPage - space * (mScale - 1) * (mSellExpenses.size());
            mAcceleration = 0;
        } else if (mMoveX + (float) getWidth() / 100 > 0) {
            mMoveX = 0;
            mAcceleration = 0;
        }
    }

    private void calPercentAmn() {
        mPercentAmn += 0.015f;
        if (mPercentAmn > 1f) {
            mIsAnm = false;
        }
    }

    private void paintAxis(Canvas canvas) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(ContextCompat.getColor(mContext, R.color.gray_BDBDBD));
        paint.setStrokeWidth(getWidth());
        canvas.drawLine((float) getWidth() / 2, 0, (float) getWidth() / 2,
                getHeight(), paint);
        mHeight = getHeight();
        paint.setColor(ContextCompat.getColor(mContext, R.color.black));
        paint.setTextSize(TEXT_SIZE);
        paint.setStrokeWidth(1);
        int startY = getHeight() / COUNT_RATIO;
        int originX = getWidth() / COUNT_RATIO;
        int originY = mHeight - startY;
        int axisY = mHeight - startY;
        canvas.drawLine(originX - 20, originY, getWidth(), originY, paint);
        canvas.drawLine(originX, originY + 20, originX, startY, paint);
        int lineY = COUNT_LINE_Y_AXIS * Math.round(mScale);
        for (int i = 1; i <= lineY; i++) {
            canvas.drawLine(originX - 20,
                    (axisY - (((float) (i * (mHeight - 2 * startY)) / lineY) * mScale)) - mMoveY,
                    getWidth(), (axisY - (((float) (i * (mHeight - 2 * startY)) / lineY) * mScale))
                            - mMoveY, paint);
        }
    }

    private void paintChart(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(ContextCompat.getColor(mContext, R.color.black));
        paint.setTextSize(TEXT_SIZE);
        for (int i = 0; i < mSellExpenses.size(); i++) {
            long maxValue = getMaxValue();
            float startX = (float) getWidth() / COUNT_RATIO;
            float startY = (float) getHeight() / COUNT_RATIO;
            float axisY = mHeight - 2 * startY;
            SellExpense sellExpense = mSellExpenses.get(i);
            float space = ((getWidth() * 2 - 6 * startX) / COUNT_MONTH) * mScale;
            if (mSellExpenses.size() <= 6) {
                space = ((getWidth() - 3 * startX) / mSellExpenses.size()) * mScale;
            }
            float x = (i * space + startX + mMoveX);
            float xNext = ((i + 1) * space + startX + mMoveX);

            mWidthChart = space / 4;
            axisY = axisY * mScale;
            mPaintExpense.setStrokeWidth(mWidthChart);
            mPaintSales.setStrokeWidth(mWidthChart);
            float xDraw = x + Math.abs(x - xNext) / 2 - mWidthChart / 2;

            float ySales = (mHeight - startY
                    - (sellExpense.getSales() * axisY / maxValue)) - mMoveY;
            float yExpense = (mHeight - startY
                    - (sellExpense.getExpense() * axisY / maxValue)) - mMoveY;
            canvas.drawLine(xDraw, (mHeight - startY) - mMoveY, xDraw,
                    ySales + ((mHeight - startY) - mMoveY) * (1 - mPercentAmn), mPaintSales);
            canvas.drawLine(xDraw + mWidthChart, (mHeight - startY) - mMoveY, xDraw
                    + mWidthChart, yExpense + ((mHeight - startY) - mMoveY) * (1 - mPercentAmn), mPaintExpense);

            paint.setStrokeWidth((float) getHeight() / COUNT_RATIO);
            paint.setColor(ContextCompat.getColor(mContext, R.color.white));
            canvas.drawLine(x, mHeight - startY / 2, xNext, mHeight
                    - startY / 2, paint);
            paint.setStrokeWidth(1);
            paint.setColor(ContextCompat.getColor(mContext, R.color.black));
            canvas.drawText(mSellExpenses.get(i).convertMonthToString(), xDraw,
                    axisY / mScale + (float) (startY * 1.5), paint);
            canvas.drawLine(x - 20, mHeight - startY, xNext,
                    mHeight - startY, paint);
            canvas.drawLine(x, mHeight - startY, x, mHeight - startY + 20, paint);
        }
    }

    private void paintWall(Canvas canvas) {
        long maxValue = getMaxValue();
        float startY = (float) getHeight() / COUNT_RATIO;
        float originX = (float) getWidth() / COUNT_RATIO;
        float axisY = mHeight - startY;
        Paint paint = new Paint();
        paint.setColor(ContextCompat.getColor(mContext, R.color.white));
        paint.setStrokeWidth(((float) getWidth() / COUNT_RATIO) * 2);
        canvas.drawLine(getWidth() - (float) getWidth() / COUNT_RATIO, 0,
                getWidth() - (float) getWidth() / COUNT_RATIO, getHeight(), paint);
        paint.setStrokeWidth(((float) getWidth() / COUNT_RATIO * 2));
        canvas.drawLine(0, 0, 0, getHeight(), paint);
        paint.setStrokeWidth(((float) getHeight() / COUNT_RATIO));
        canvas.drawLine(0, startY / 2, getWidth(),
                startY / 2, paint);
        paint.setColor(ContextCompat.getColor(mContext, R.color.black));
        paint.setTextSize(TEXT_SIZE);
        paint.setStrokeWidth(1);
        paint.setTextAlign(Paint.Align.RIGHT);
        int lineY = COUNT_LINE_Y_AXIS * Math.round(mScale);
        for (int i = 0; i <= lineY; i++) {
            if ((((axisY - (((i * (mHeight - 2 * startY)) / lineY) * mScale)) - mMoveY) >= startY)
                    && ((axisY - (((i * (mHeight - 2 * startY)) / lineY) * mScale)) - mMoveY)
                    <= mHeight - startY) {
                canvas.drawLine(originX - 20,
                        (axisY - (((i * (mHeight - 2 * startY)) / lineY) * mScale)) - mMoveY,
                        originX, (axisY - (((i * (mHeight - 2 * startY)) / lineY) * mScale))
                                - mMoveY, paint);
                canvas.drawText("$" + i * maxValue / lineY, (float) getWidth() / COUNT_RATIO - 20,
                        (axisY - (((i * (mHeight - 2 * startY)) / lineY) * mScale)) - mMoveY,
                        paint);
            }
        }
        paint.setStrokeWidth(1);
        paint.setColor(ContextCompat.getColor(mContext, R.color.black));
        paint.setTextSize(20);
        canvas.drawLine((float) getWidth() / 4, (float) (getHeight() / COUNT_RATIO) / 2,
                (float) (getWidth() * 3) / 4, (float) (getHeight() / COUNT_RATIO) / 2,
                paint);
        canvas.drawCircle((float) getWidth() / 4 + ((mScale - 1) / (SCALE_MAX - 1)
                        * ((float) getWidth() / 2)), (float) (getHeight() / COUNT_RATIO) / 2,
                20, paint);
        canvas.drawText(SCALE_DEFAULT + "x", (float) getWidth() / 2 - (float) getWidth() / 4,
                (float) (getHeight() / COUNT_RATIO) / 2 - 20, paint);
        canvas.drawText(SCALE_MAX + "x", (float) getWidth() / 2 + (float) getWidth() / 4,
                (float) (getHeight() / COUNT_RATIO) / 2 - 20, paint);
        paintHintColor(canvas);
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

    private void initPaint() {
        mPaintSales = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintSales.setStrokeWidth(mWidthChart);
        mPaintSales.setColor(mColorSales);
        mPaintExpense = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintExpense.setStrokeWidth(mWidthChart);
        mPaintExpense.setColor(mColorExpense);
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        performClick();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (event.getPointerCount() == 1) {
                    mDownX = event.getX();
                    mTimeDown = System.currentTimeMillis();
                }
                mXFingerFirst = event.getX();
                mYFingerFirst = event.getY();
                mXFingerSecond = event.getX();
                mYFingerSecond = event.getY();
                if (event.getX() >= (float) getWidth() / 4 && event.getX() <=
                        (float) (getWidth() * 3) / 4 && event.getY() >= 0
                        && event.getY() <= (float) getHeight() / COUNT_RATIO) {
                    mScale = 1 + (event.getX() - (float) getWidth() / 4) / ((float) getWidth() / 2)
                            * (SCALE_MAX - 1);

                    if (mScale > SCALE_MAX) {
                        mScale = SCALE_MAX;
                    } else {
                        if (mScale < SCALE_DEFAULT) {
                            mScale = SCALE_DEFAULT;
                        }
                    }
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (event.getPointerCount() > 1) {
                    calculateScale(event);
                }
                if (event.getX() >= (float) getWidth() / 4 && event.getX() <=
                        (float) (getWidth() * 3) / 4 && event.getY() >= 0
                        && event.getY() <= (float) getHeight() / COUNT_RATIO) {
                    mScale = 1 + (event.getX() - (float) getWidth() / 4) / ((float) getWidth() / 2)
                            * (SCALE_MAX - 1);
                    if (mScale > SCALE_MAX) {
                        mScale = SCALE_MAX;
                    } else {
                        if (mScale < SCALE_DEFAULT) {
                            mScale = SCALE_DEFAULT;
                        }
                    }
                } else {
                    calculateMove(event);
                }
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                if (event.getPointerCount() == 1) {
                    long mTimeUp = System.currentTimeMillis();
                    float mDisAcc = event.getX() - mDownX;
                    mAcceleration += mDisAcc / (mTimeUp - mTimeDown);
                    invalidate();
                }
                break;
        }
        return true;
    }

    private void calculateScale(MotionEvent event) {
        if (mXFingerFirst != mXFingerSecond && mYFingerFirst != mYFingerSecond) {
            float mXCenter = (event.getX() + event.getX(1)) / 2;
            double distanceBefore = getDistance(mXFingerFirst, mYFingerFirst,
                    mXFingerSecond, mYFingerSecond);
            double distanceAfter = getDistance(event.getX(), event.getY(),
                    event.getX(1), event.getY(1));
            if (distanceAfter > distanceBefore) {
                if (mScale + (distanceAfter / distanceBefore) - 1 < SCALE_MAX) {
                    mScale += (float) (distanceAfter / distanceBefore) - 1;
                    mMoveX = -mXCenter * mScale;
                } else {
                    if (mScale + (distanceAfter / distanceBefore) - 1 > SCALE_MAX) {
                        mScale = SCALE_MAX;
                    }
                }
            }
            if (distanceAfter < distanceBefore) {
                if (mScale - ((distanceBefore / distanceAfter) - 1) > SCALE_DEFAULT) {
                    mScale -= (float) ((distanceBefore / distanceAfter) - 1);
                    mMoveX = -mXCenter * mScale;
                } else {
                    if (mScale - ((distanceBefore / distanceAfter) - 1) <= SCALE_DEFAULT) {
                        mScale = SCALE_DEFAULT;
                    }
                }
            }
        }
        mXFingerFirst = event.getX(0);
        mYFingerFirst = event.getY(0);
        mXFingerSecond = event.getX(1);
        mYFingerSecond = event.getY(1);
    }

    private void calculateMove(MotionEvent event) {
        float moveX = event.getX();
        float moveY = event.getY();
        float space = ((float) (getWidth() * 2 - 6 * getWidth() / COUNT_RATIO) / COUNT_MONTH);
        float minPage;
        if (mSellExpenses.size() <= 6) {
            space = ((float) (getWidth() - 3 * getWidth() / COUNT_RATIO) / mSellExpenses.size());
            minPage = 0;
        } else {
            minPage = -space * (mSellExpenses.size() - 6);
        }
        if (mMoveX + ((moveX - mXFingerFirst)) < minPage - space * (mScale - 1) * mSellExpenses.size()) {
            mMoveX = minPage - space * (mScale - 1) * (mSellExpenses.size());
        } else if (mMoveX + moveX - mXFingerFirst > 0) {
            mMoveX = 0;
        } else {
            mMoveX += (moveX - mXFingerFirst);
            mXFingerFirst = moveX;
        }

        if (mMoveY - moveY + mYFingerFirst < -(getHeight() * (mScale - 1))) {
            mMoveY = -(getHeight() * (mScale - 1));
        } else if (mMoveY - moveY + mYFingerFirst > 0) {
            mMoveY = 0;
        } else {
            mMoveY += (-moveY + mYFingerFirst);
            mYFingerFirst = moveY;
        }
    }

    private double getDistance(float x1, float y1, float x2, float y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }
}

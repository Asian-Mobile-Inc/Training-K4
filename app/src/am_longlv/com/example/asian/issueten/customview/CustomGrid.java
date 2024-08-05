package com.example.asian.issueten.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
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
    private float mScaleX = 1;
    private float mScaleY = 1;
    private float mMoveX = 0;
    private float mMoveY = 0;
    private static final int SCALE_DEFAULT = 1;
    private static final int SCALE_MAX = 5;

    public void setupDataChart(List<SellExpense> sellExpenses) {
        mSellExpenses.clear();
        mSellExpenses.addAll(sellExpenses);
        invalidate();
    }

    public CustomGrid(Context context) {
        super(context);
        mContext = context;
    }

    public CustomGrid(Context context, @Nullable AttributeSet attrs) {
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

    public CustomGrid(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
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
        initPaint();
        paintAxis(canvas);
        canvas.save();
        float px = (mXFingerSecond+mXFingerFirst)/2;
        float py = (mYFingerSecond+mYFingerFirst)/2;
        canvas.scale(mScaleX, mScaleY, px, py);
        canvas.translate(mMoveX, -mMoveY);
        paintChart(canvas);
        canvas.restore();
        paintWall(canvas);
    }

    private void paintAxis(Canvas canvas) {
        Paint paint = new Paint();
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
        for (int i = 1; i <= COUNT_LINE_Y_AXIS; i++) {
            canvas.drawLine(originX - 20,
                    (axisY - (((float) (i * (mHeight - 2 * startY)) / COUNT_LINE_Y_AXIS))),
                    getWidth(), (axisY - (((float) (i * (mHeight - 2 * startY))
                            / COUNT_LINE_Y_AXIS))), paint);
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
            float space = ((getWidth() * 2 - 6 * startX) / COUNT_MONTH);
            if (mSellExpenses.size() <= 6) {
                space = ((getWidth() - 3 * startX) / mSellExpenses.size());
            }
            float x = (i * space + startX);
            float xNext = ((i + 1) * space + startX);

            mWidthChart = space / 4;
            mPaintExpense.setStrokeWidth(mWidthChart);
            mPaintSales.setStrokeWidth(mWidthChart);
            float xDraw = x + Math.abs(x - xNext) / 2 - mWidthChart / 2;

            float ySales = (mHeight - startY
                    - (sellExpense.getSales() * axisY / maxValue));
            float yExpense = (mHeight - startY
                    - (sellExpense.getExpense() * axisY / maxValue));
            canvas.drawLine(xDraw, (mHeight - startY), xDraw, ySales, mPaintSales);
            canvas.drawLine(xDraw + mWidthChart, (mHeight - startY), xDraw
                    + mWidthChart, yExpense, mPaintExpense);

            paint.setStrokeWidth((float) getHeight() / COUNT_RATIO);
            paint.setColor(ContextCompat.getColor(mContext, R.color.white));
            canvas.drawLine(x, mHeight - startY / 2, xNext, mHeight
                    - startY / 2, paint);
            paint.setStrokeWidth(1);
            paint.setColor(ContextCompat.getColor(mContext, R.color.black));
            canvas.drawText(mSellExpenses.get(i).convertMonthToString(), xDraw,
                    axisY + (float) (startY * 1.5), paint);
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
        for (int i = 0; i <= COUNT_LINE_Y_AXIS; i++) {
            if ((((axisY - (((i * (mHeight - 2 * startY)) / 8))) - mMoveY) >= startY)
                    && ((axisY - (((i * (mHeight - 2 * startY)) / 8))) - mMoveY)
                    <= mHeight - startY) {
                canvas.drawLine(originX - 20,
                        (axisY - (((i * (mHeight - 2 * startY)) / 8))) - mMoveY,
                        originX, (axisY - (((i * (mHeight - 2 * startY)) / 8)))
                                - mMoveY, paint);
                canvas.drawText("$" + i * maxValue / 8, 0,
                        (axisY - (((i * (mHeight - 2 * startY)) / 8))) - mMoveY,
                        paint);
            }
        }
        paint.setStrokeWidth(1);
        paint.setColor(ContextCompat.getColor(mContext, R.color.black));
        paint.setTextSize(20);
        canvas.drawLine((float) getWidth() / 4, (float) (getHeight() / COUNT_RATIO) / 2,
                (float) (getWidth() * 3) / 4, (float) (getHeight() / COUNT_RATIO) / 2,
                paint);
        canvas.drawCircle((float) getWidth() / 4, (float) (getHeight() / COUNT_RATIO) / 2,
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
                mXFingerFirst = event.getX();
                mYFingerFirst = event.getY();
                mXFingerSecond = event.getX();
                mYFingerSecond = event.getY();
                if (event.getX() >= (float) getWidth() / 4 && event.getX() <=
                        (float) (getWidth() * 3) / 4 && event.getY() >= 0
                        && event.getY() <= (float) getHeight() / COUNT_RATIO) {
                    mScaleX = 1 + (event.getX() - (float) getWidth() / 4) / ((float) getWidth() / 2)
                            * (SCALE_MAX - 1);
                    mScaleY = 1 + (event.getX() - (float) getWidth() / 4) / ((float) getWidth() / 2)
                            * (SCALE_MAX - 1);

                    if (mScaleX > SCALE_MAX) {
                        mScaleX = SCALE_MAX;
                    } else {
                        if (mScaleX < SCALE_DEFAULT) {
                            mScaleX = SCALE_DEFAULT;
                        }
                    }
                    if (mScaleY > SCALE_MAX) {
                        mScaleY = SCALE_MAX;
                    } else {
                        if (mScaleY < SCALE_DEFAULT) {
                            mScaleY = SCALE_DEFAULT;
                        }
                    }
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                calculateMove(event);
                if (event.getPointerCount() > 1) {
                    calculateScale(event);
                }
                if (event.getX() >= (float) getWidth() / 4 && event.getX() <=
                        (float) (getWidth() * 3) / 4 && event.getY() >= 0
                        && event.getY() <= (float) getHeight() / COUNT_RATIO) {
                    mScaleX = 1 + (event.getX() - (float) getWidth() / 4) / ((float) getWidth() / 2)
                            * (SCALE_MAX - 1);
                    mScaleY = 1 + (event.getX() - (float) getWidth() / 4) / ((float) getWidth() / 2)
                            * (SCALE_MAX - 1);
                    if (mScaleX > SCALE_MAX) {
                        mScaleX = SCALE_MAX;
                    } else {
                        if (mScaleX < SCALE_DEFAULT) {
                            mScaleX = SCALE_DEFAULT;
                        }
                    }
                    if (mScaleY > SCALE_MAX) {
                        mScaleY = SCALE_MAX;
                    } else {
                        if (mScaleY < SCALE_DEFAULT) {
                            mScaleY = SCALE_DEFAULT;
                        }
                    }
                }
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }

    private void calculateScale(MotionEvent event) {
        double distanceXBefore = Math.abs(mXFingerFirst - mXFingerSecond);
        double distanceXAfter = Math.abs(event.getX(0) - event.getX(1));
        double distanceYBefore = Math.abs(mYFingerFirst - mYFingerSecond);
        double distanceYAfter = Math.abs(event.getY(0) - event.getY(1));
        if (mXFingerFirst != mXFingerSecond && mYFingerFirst != mYFingerSecond) {
            if (distanceXAfter > distanceXBefore) {
                if (mScaleX + (distanceXAfter / distanceXBefore) - 1 < SCALE_MAX) {
                    mScaleX += (float) (distanceXAfter / distanceXBefore) - 1;
                    mMoveX -= (float) ((distanceXAfter / distanceXBefore) - 1) / 2 * getWidth() * 3 / 4;
                } else {
                    if (mScaleX > SCALE_MAX) {
                        mScaleX = SCALE_MAX;
                    }
                }
            }
            if (distanceXAfter < distanceXBefore) {
                if (mScaleX - ((distanceXBefore / distanceXAfter) - 1) > 1) {
                    mScaleX -= (float) ((distanceXBefore / distanceXAfter) - 1);
                    mMoveX += (float) ((distanceXAfter / distanceXBefore) - 1) / 2 * getWidth() * 3 / 4;
                } else {
                    if (mScaleX < SCALE_DEFAULT) {
                        mScaleX = SCALE_DEFAULT;
                    }
                }
            }
            if (distanceYAfter > distanceYBefore) {
                if (mScaleY + (distanceYAfter / distanceYBefore) - 1 < SCALE_MAX) {
                    mScaleY += (float) (distanceYAfter / distanceYBefore) - 1;
                    mMoveX -= (float) ((distanceYAfter / distanceYBefore) - 1) / 2 * getWidth() * 3 / 4;
                } else {
                    if (mScaleY > SCALE_MAX) {
                        mScaleY = SCALE_MAX;
                    }
                }
            }
            if (distanceYAfter < distanceYBefore) {
                if (mScaleY - ((distanceYBefore / distanceYAfter) - 1) > 1) {
                    mScaleY -= (float) ((distanceYBefore / distanceYAfter) - 1);
                    mMoveX += (float) ((distanceYAfter / distanceYBefore) - 1) / 2 * getWidth() * 3 / 4;
                } else {
                    if (mScaleY < SCALE_DEFAULT) {
                        mScaleY = SCALE_DEFAULT;
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
        if (mMoveX + ((moveX - mXFingerFirst)) < minPage - space * (mScaleX - 1) * mSellExpenses.size()) {
            mMoveX = minPage - space * (mScaleX - 1) * (mSellExpenses.size());
        } else if (mMoveX + moveX - mXFingerFirst > 0) {
            mMoveX = 0;
        } else {
            mMoveX += (moveX - mXFingerFirst);
            mXFingerFirst = moveX;
        }

        if (mMoveY - moveY + mYFingerFirst < -(getHeight() * (mScaleY - 1))) {
            mMoveY = -(getHeight() * (mScaleY - 1));
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

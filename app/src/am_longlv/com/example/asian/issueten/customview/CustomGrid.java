package com.example.asian.issueten.customview;

import static android.view.MotionEvent.INVALID_POINTER_ID;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
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
    private int mHeight;
    private static final int COUNT_RATIO = 12;
    private static final int COUNT_MONTH = 12;
    private static final int TEXT_SIZE = 35;
    private static final int COUNT_LINE_Y_AXIS = 8;
    private int mColorSales;
    private int mColorExpense;
    private float mWidthChart = 0;
    private static final int SCALE_DEFAULT = 1;
    private static final int SCALE_MAX = 5;
    private ScaleGestureDetector mScaleDetector;
    private float mScaleFactor = 1.f;
    private float mPosX;
    private float mPosY;
    private float mLastTouchX;
    private float mLastTouchY;
    private int mActivePointerId = INVALID_POINTER_ID;

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor();
            mScaleFactor = Math.max(SCALE_DEFAULT, Math.min(mScaleFactor, SCALE_MAX));
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
    }

    public CustomGrid(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setUpAttribute(context, attrs);
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
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
        canvas.translate(mPosX, mPosY);
        canvas.scale(mScaleFactor, mScaleFactor, mScaleDetector.getFocusX(), mScaleDetector.getFocusY());
        paintLinePrice(canvas);
        paintChart(canvas);
        canvas.restore();
        canvas.save();
        canvas.translate(mPosX, 0);
        canvas.scale(mScaleFactor, SCALE_DEFAULT, mScaleDetector.getFocusX(), mScaleDetector.getFocusY());
        paintMonth(canvas);
        canvas.restore();
        canvas.save();
        paintWall(canvas);
        canvas.translate(0, mPosY);
        canvas.scale(SCALE_DEFAULT, mScaleFactor, mScaleDetector.getFocusX(), mScaleDetector.getFocusY());
        paintPrice(canvas);
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
        canvas.drawLine(originX - 20, originY, getWidth(), originY, paint);
        canvas.drawLine(originX, originY + 20, originX, startY, paint);
    }

    private void paintLinePrice(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(ContextCompat.getColor(mContext, R.color.black));
        int startY = getHeight() / COUNT_RATIO;
        int originX = getWidth() / COUNT_RATIO;
        int axisY = mHeight - startY;
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
        }
    }

    private void paintMonth(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(ContextCompat.getColor(mContext, R.color.black));
        paint.setTextSize(TEXT_SIZE);
        for (int i = 0; i < mSellExpenses.size(); i++) {
            float startX = (float) getWidth() / COUNT_RATIO;
            float startY = (float) getHeight() / COUNT_RATIO;
            float axisY = mHeight - 2 * startY;
            float space = ((getWidth() * 2 - 6 * startX) / COUNT_MONTH);
            if (mSellExpenses.size() <= 6) {
                space = ((getWidth() - 3 * startX) / mSellExpenses.size());
            }
            float x = (i * space + startX);
            float xNext = ((i + 1) * space + startX);
            float xDraw = x + Math.abs(x - xNext) / 2 - mWidthChart / 2;
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

    private void paintPrice(Canvas canvas) {
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
    }

    private void paintWall(Canvas canvas) {
        float startY = (float) getHeight() / COUNT_RATIO;
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
        paint.setColor(ContextCompat.getColor(mContext, R.color.black));
        paint.setTextSize(20);
        canvas.drawLine((float) getWidth() / 4, (float) (getHeight() / COUNT_RATIO) / 2,
                (float) (getWidth() * 3) / 4, (float) (getHeight() / COUNT_RATIO) / 2,
                paint);
        canvas.drawCircle((float) getWidth() / 4 + ((mScaleFactor - 1) / (SCALE_MAX - 1)
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
        if (event.getX() > (float) getWidth() / COUNT_RATIO && event.getX() < (float) (getWidth() * 5) / 6
                && event.getY() > (float) getHeight() / COUNT_RATIO && event.getY() < (float) (getHeight() * 11) / 12) {
            mScaleDetector.onTouchEvent(event);
        }
        final int action = event.getAction();
        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: {
                final float x = event.getX();
                final float y = event.getY();

                mLastTouchX = x;
                mLastTouchY = y;
                mActivePointerId = event.getPointerId(0);
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                final int pointerIndex = event.findPointerIndex(mActivePointerId);
                final float x = event.getX(pointerIndex);
                final float y = event.getY(pointerIndex);
                if (!mScaleDetector.isInProgress()) {
                    final float dx = x - mLastTouchX;
                    final float dy = y - mLastTouchY;

                    mPosX += dx;
                    mPosY += dy;

                    invalidate();
                }

                mLastTouchX = x;
                mLastTouchY = y;

                break;
            }

            case MotionEvent.ACTION_UP:

            case MotionEvent.ACTION_CANCEL: {
                mActivePointerId = INVALID_POINTER_ID;
                break;
            }

            case MotionEvent.ACTION_POINTER_UP: {
                final int pointerIndex = (event.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK)
                        >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
                final int pointerId = event.getPointerId(pointerIndex);
                if (pointerId == mActivePointerId) {
                    final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                    mLastTouchX = event.getX(newPointerIndex);
                    mLastTouchY = event.getY(newPointerIndex);
                    mActivePointerId = event.getPointerId(newPointerIndex);
                }
                break;
            }
        }
        return true;
    }
}

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
    private ArrayList<Integer> mValues = new ArrayList<>();

    private float mScaleFactor = 1.0f;

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
        initValues();
    }

    public void setColorSale(int colorSale) {
        this.mColorSale = colorSale;
    }

    public void setColorExpenses(int colorExpenses) {
        this.mColorExpenses = colorExpenses;
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyChartView_XML);
        mColorSale = typedArray.getColor(R.styleable.MyChartView_XML_color_sales, getResources().getColor(R.color.blue_4A82BD));
        mColorExpenses = typedArray.getColor(R.styleable.MyChartView_XML_color_expenses, getResources().getColor(R.color.red_C6514A));
    }

    private void initPaint() {
        mPaintBlack = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint = new TextPaint();
        mTextCenterPaint = new TextPaint();
        mPaintSales = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintExpenses = new Paint(Paint.ANTI_ALIAS_FLAG);
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
        drawChartFrame(canvas);
    }

    private void initValues() {
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

    private void drawChartFrame(Canvas canvas) {
        int xStart = 13 * getWidth() / 100;
        int yStart = 5 * getHeight() / 100;
        int spaceItemMonth = (getWidth() - xStart) / 6;
        int spaceItemValue = (getHeight() - (12 * getHeight() / 100)) / (mValues.size() - 1);
        int yEnd = (mValues.size() - 1) * spaceItemValue + yStart;
        int xEnd = mSellExpenses.size() * spaceItemMonth + xStart;

        int spaceColumnChart = spaceItemMonth / 3;
        int spaceCenterColumn = spaceItemMonth / 6;

        canvas.drawLine(xStart, yStart, xStart, yEnd, mPaintBlack);
        canvas.drawLine(xStart, yEnd, xEnd, yEnd, mPaintBlack);
        for (int i = 0; i < mValues.size(); i++) {
            canvas.drawLine(xStart, i * spaceItemValue + yStart, xEnd, i * spaceItemValue + yStart, mPaintBlack);
            canvas.drawText(mValues.get(mValues.size() - 1 - i) + "$", xStart - 5, i * spaceItemValue + yStart + 10, mTextPaint);
        }

        for (int i = 0; i < mSellExpenses.size(); i++) {
            canvas.drawText(mSellExpenses.get(i).getMonth().name(), xStart + i * spaceItemMonth + spaceItemMonth / 2, yEnd + 30, mTextCenterPaint);

            canvas.drawRect(xStart + i * spaceItemMonth + spaceCenterColumn, yEnd - (mSellExpenses.get(i).getSales() / 20000 * spaceItemValue), xStart + i * spaceItemMonth + spaceColumnChart + spaceCenterColumn, yEnd, mPaintSales);
            canvas.drawRect(xStart + i * spaceItemMonth + spaceColumnChart + spaceCenterColumn, yEnd - (mSellExpenses.get(i).getExpenses() / 20000 * spaceItemValue), xStart + i * spaceItemMonth + spaceColumnChart + spaceColumnChart + spaceCenterColumn, yEnd, mPaintExpenses);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mScaleGestureDetector.onTouchEvent(event);
        return true;
    }

    class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(@NonNull ScaleGestureDetector detector) {
            mScaleFactor = detector.getScaleFactor() - 1;
            mScaleFactor += detector.getScaleFactor();
//            mScaleFactor = Math.max(0.5f, Math.min(mScaleFactor, 2.5f));
            setScaleX(mScaleFactor);
            setScaleY(mScaleFactor);
            return true;
        }
    }
}

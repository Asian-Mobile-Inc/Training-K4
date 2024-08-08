package com.example.asian.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
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
    private Paint mPaintSales;
    private Paint mPaintExpenses;
    private ArrayList<Integer> mValues = new ArrayList<>();

    public MyChartView(Context context) {
        super(context);
    }

    public MyChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
        initPaint();
    }

    public void setSellExpenses(ArrayList<SellExpense> sellExpenses) {
        this.mSellExpenses = sellExpenses;
        initPaint();
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
        mPaintSales = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintExpenses = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintBlack.setColor(getResources().getColor(R.color.black));
        mPaintBlack.setStyle(Paint.Style.STROKE);
        mPaintBlack.setStrokeWidth(3);
        mTextPaint.setColor(getResources().getColor(R.color.black));
        mTextPaint.setTextAlign(Paint.Align.RIGHT);
        mTextPaint.setTextSize(28);
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
        initValues();
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
        int xStart = 200;
        int yStart = 100;
        int spaceItemMonth = 180;
        int spaceItemValue = (getHeight() - yStart) / mValues.size(); // 10value = 40dp
        int yEnd = (mValues.size() - 1) * spaceItemValue + yStart;
        int xEnd = mSellExpenses.size() * spaceItemMonth + xStart;

        int spaceColumnChart = 60;
        int spaceCenterColumn = 30;

        canvas.drawLine(xStart, yStart, xStart, yEnd, mPaintBlack);
        canvas.drawLine(xStart, yEnd, xEnd, yEnd, mPaintBlack);
        for (int i = 0; i < mValues.size(); i++) {
            canvas.drawLine(xStart, i * spaceItemValue + yStart, xEnd, i * spaceItemValue + yStart, mPaintBlack);
            canvas.drawText(mValues.get(mValues.size() - 1 - i) + "$", 190, i * spaceItemValue + yStart + 10, mTextPaint);
        }

        for (int i = 0; i < mSellExpenses.size(); i++) {
            mTextPaint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(mSellExpenses.get(i).getMonth().name(), xStart + i * spaceItemMonth + spaceItemMonth / 2, yEnd + 40, mTextPaint);

            canvas.drawRect(xStart + i * spaceItemMonth + spaceCenterColumn, yEnd - (mSellExpenses.get(i).getSales() / 10000 * (spaceItemValue / 2)), xStart + i * spaceItemMonth + spaceColumnChart + spaceCenterColumn, yEnd, mPaintSales);
            canvas.drawRect(xStart + i * spaceItemMonth + spaceColumnChart + spaceCenterColumn, yEnd - (mSellExpenses.get(i).getExpenses() / 10000 * 40), xStart + i * spaceItemMonth + spaceColumnChart + spaceColumnChart + spaceCenterColumn, yEnd, mPaintExpenses);
        }
    }
}

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
    private List<SellExpense> mSellExpenses = new ArrayList<>();
    private Paint mPaintSales;
    private Paint mPaintExpense;
    private Context mContext;
    private int mHeight;
    private int mWidth;
    private static final int COUNT_RATIO = 12;
    private static final int COUNT_MONTH = 12;
    private static final int WIDTH_CHART = 50;
    private static final int TEXT_SIZE = 35;
    private static final int COUNT_LINE_Y_AXIS = 8;
    private static final int WIDTH_HINT_COLOR = 300;
    private int mColorSales;
    private int mColorExpense;
    private float mScale = 1;
    private float mMoveX = 0;
    private float mMoveY = 0;
    private Canvas mCanvas;

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
        try {
            mColorSales = a.getColor(R.styleable.MyChartView_mcv_sales, Color.BLACK);
            mColorExpense = a.getColor(R.styleable.MyChartView_mcv_expenses, Color.BLACK);
        } finally {
            a.recycle();
        }
    }

    public MyChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    public MyChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public List<SellExpense> getmSellExpenses() {
        return mSellExpenses;
    }

    public void setmSellExpenses(List<SellExpense> mSellExpenses) {
        this.mSellExpenses = mSellExpenses;
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
        Log.d("androidruntime", "onDraw");
        mCanvas = canvas;
        setUpChart();
        initPaint();
        paintAxis(canvas);
        paintChart(canvas);
    }

    private void paintAxis(Canvas canvas) {
        Paint paint = new Paint();
        mHeight = getHeight();
        mWidth = getWidth() * 2;
        paint.setColor(ContextCompat.getColor(mContext, R.color.black));
        paint.setTextSize(TEXT_SIZE);
        long maxValue = getMaxValue();
        int startY = getHeight() / COUNT_RATIO;
        int originX = getWidth() / COUNT_RATIO;
        int originY = mHeight - startY;
        int axisY = mHeight - startY;
        canvas.drawLine(originX - 20, originY, mWidth, originY, paint);
        canvas.drawLine(originX, originY + 20, originX, startY, paint);

        canvas.drawText("$0", 0, axisY, paint);
        for (int i = 1; i <= COUNT_LINE_Y_AXIS; i++) {
            canvas.drawLine(originX - 20, axisY - (float) (i * (mHeight - 2 * startY)) / 8,
                    mWidth, mHeight - startY - (float) (i * (mHeight - 2 * startY)) / 8, paint);
            canvas.drawText("$" + i * maxValue / 8, 0,
                    axisY - (float) (i * (mHeight - 2 * startY)) / 8, paint);
        }
        for (int i = 1; i <= COUNT_MONTH; i++) {
            int space = (int) (mWidth - 2 * originX) / COUNT_MONTH;
            int x = (int) (i * space + originX);
            canvas.drawLine(x, mHeight - startY, x, mHeight - startY + 20, paint);
        }
    }

    private void paintChart(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(ContextCompat.getColor(mContext, R.color.black));
        paint.setTextSize(TEXT_SIZE);
        for (int i = 0; i < mSellExpenses.size(); i++) {
            long maxValue = getMaxValue();
            int startX = getWidth() / COUNT_RATIO;
            int startY = getHeight() / COUNT_RATIO;
            int axisY = mHeight - 2 * startY;

            SellExpense sellExpense = mSellExpenses.get(i);
            int space = (int) (mWidth - 2 * startX) / COUNT_MONTH;
            int x = (int) (i * space + startX);
            int xNext = (int) ((i + 1) * space + startX);
            int xDraw = x + Math.abs(x - xNext) / 2 - WIDTH_CHART / 2;

            float ySales = (float) (mHeight - startY
                    - (sellExpense.getmSales() * axisY / maxValue));
            float yExpense = (float) (mHeight - startY
                    - (sellExpense.getmExpense() * axisY / maxValue));
            canvas.drawLine(xDraw, mHeight - startY, xDraw, ySales, mPaintSales);
            canvas.drawLine(xDraw + WIDTH_CHART, mHeight - startY, xDraw + WIDTH_CHART,
                    yExpense, mPaintExpense);
            canvas.drawText(convertMonthToString(i + 1), xDraw, axisY + (int) (startY * 1.5), paint);
        }
    }

    private void paintHintColor(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(ContextCompat.getColor(mContext, R.color.white));
        paint.setStrokeWidth(WIDTH_HINT_COLOR);
        canvas.drawLine(getWidth() - (float) WIDTH_HINT_COLOR / 2, 0, getWidth() - (float) WIDTH_HINT_COLOR / 2, getHeight(), paint);
    }

    private long getMaxValue() {
        long max = 0;
        for (SellExpense sellExpense : mSellExpenses) {
            if (sellExpense.getmSales() > max) {
                max = sellExpense.getmSales();
            }
            if (sellExpense.getmExpense() > max) {
                max = sellExpense.getmExpense();
            }
        }
        return max;
    }

    private void initPaint() {
        mPaintSales = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintSales.setStrokeWidth(WIDTH_CHART);
        mPaintSales.setColor(mColorSales | ContextCompat.getColor(mContext, R.color.colorPrimary));
        mPaintExpense = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintExpense.setStrokeWidth(WIDTH_CHART);
        mPaintExpense.setColor(mColorExpense | ContextCompat.getColor(mContext, R.color.colorAccent));
        TypedArray a = mContext.getTheme().obtainStyledAttributes(
                R.styleable.MyChartView);
    }

    public void setUpChart() {
        mSellExpenses.add(new SellExpense(1, 100, 2000));
        mSellExpenses.add(new SellExpense(2, 200, 300));
        mSellExpenses.add(new SellExpense(3, 300, 400));
        mSellExpenses.add(new SellExpense(4, 400, 500));
        mSellExpenses.add(new SellExpense(5, 300, 200));
        mSellExpenses.add(new SellExpense(6, 600, 700));
        mSellExpenses.add(new SellExpense(7, 700, 800));
        mSellExpenses.add(new SellExpense(8, 800, 900));
        mSellExpenses.add(new SellExpense(9, 900, 1000));
        mSellExpenses.add(new SellExpense(10, 1000, 1100));
        mSellExpenses.add(new SellExpense(11, 1100, 1200));
        mSellExpenses.add(new SellExpense(12, 1200, 1300));
    }

    public String convertMonthToString(int month) {
        switch (month) {
            case 1:
                return "Jan";
            case 2:
                return "Feb";
            case 3:
                return "Mar";
            case 4:
                return "Apr";
            case 5:
                return "May";
            case 6:
                return "Jun";
            case 7:
                return "Jul";
            case 8:
                return "Aug";
            case 9:
                return "Sep";
            case 10:
                return "Oct";
            case 11:
                return "Nov";
            case 12:
                return "Dec";
            default:
                return "";
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float xo = event.getX();
        float yo = event.getY();
        Log.d("androidruntime", event.getPointerCount() + "");
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mHeight = 2 * mHeight;
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return true;
    }
    private double getDistance(int x1,int y1,int x2,int y2){
        double distance = Math.sqrt(Math.pow(x2-x1,2)+Math.pow(y2-y1,2));
        return distance;
    }
}

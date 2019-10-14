package com.stela.comics_unlimited.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.stela.comics_unlimited.R;

public class BobbleView extends View {
    private int num = 0;
    private Paint paint;
    private TextPaint textPaint;
    private float density = getResources().getDisplayMetrics().density;
    private float textSize;
    private int textColor;
    private static float CUB = 0.551915024494f;
    private Path pathCircle = new Path();
    private float padding = 10;
    private int radius;

    private enum TYPE_SIZE {
        CIRCLE1(1),
        CIRCLE2(2),
        CIRCLE3(3),
        CIRCLE4(4);
        public int value;

        TYPE_SIZE(int value) {
            this.value = value;
        }

        public static TYPE_SIZE getByValue(int value) {
            for (TYPE_SIZE code : values()) {
                if (code.value == value) {
                    return code;
                }
            }
            return null;
        }
    }

    public BobbleView(Context context) {
        super(context);
    }

    public BobbleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initLayout(context, attrs);
    }

    public BobbleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initLayout(context, attrs);
    }


    private void initLayout(Context context, AttributeSet attrs) {
        initView(context, attrs);
        initPaint();

    }

    private void initView(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.BobbleView);
        textSize = typedArray
                .getDimension(R.styleable.BobbleView_textSize, 10);
        textColor = typedArray.getColor(R.styleable.BobbleView_textColor,
                Color.WHITE);
        num = typedArray.getColor(R.styleable.BobbleView_num,
                0);
        padding = textSize / 3;
        radius = (int) textSize * 4 / 5;
        typedArray.recycle();
    }

    private void initPaint() {
        paint = new Paint();
        paint.setColor(getResources().getColor(R.color.colorAccent));
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);//设置为抗锯齿
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(3 * density);

        textPaint = new TextPaint();
        textPaint.setColor(textColor);
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(textSize);
        textPaint.setTextAlign(Paint.Align.CENTER);
//        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT_BOLD, Typeface.BOLD));

    }

    public void setNum(int num) {
        this.num = num;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 获取宽-测量规则的模式和大小
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        // 获取高-测量规则的模式和大小
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        // 设置wrap_content的默认宽 / 高值
        // 默认宽/高的设定并无固定依据,根据需要灵活设置
        // 类似TextView,ImageView等针对wrap_content均在onMeasure()对设置默认宽 / 高值有特殊处理,具体读者可以自行查看
        int mWidth = (int) (textSize * (num + "").length() + 2 * padding);
        if (num > 999) {
            mWidth = (int) (textSize * 4 + 2 * padding);
        }
        int mHeight = (int) (2 * radius + 2 * padding);

        // 当模式是AT_MOST（即wrap_content）时设置默认值
        if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(mWidth, mHeight);
            // 宽 / 高任意一个模式为AT_MOST（即wrap_content）时，都设置默认值
        } else if (widthMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(mWidth, heightSize);
        } else if (heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSize, mHeight);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBg(canvas);
    }


    private void drawBg(Canvas canvas) {
        TYPE_SIZE style = TYPE_SIZE.CIRCLE1;
        String numString = num + "";
        if (num == 0) {
//            style = TYPE_SIZE.CIRCLE1;
//            numString = "";
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, textSize / 2, paint);
            return;
        } else if (num > 0 && num <= 9) {
            style = TYPE_SIZE.CIRCLE1;
        } else if (num > 9 && num <= 99) {
            style = TYPE_SIZE.CIRCLE1;
        } else if (num > 99 && num <= 999) {
            style = TYPE_SIZE.CIRCLE2;
        } else if (num > 999) {
            style = TYPE_SIZE.CIRCLE3;
            numString = 999 + "+";
        }
        drawBgCircle(canvas, style);
        canvas.drawPath(pathCircle, paint);
        drawTextNum(canvas, numString);
    }

    private void drawTextNum(Canvas canvas, String numString) {
        Rect bounds = new Rect();
        textPaint.getTextBounds(numString, 0, numString.length(), bounds);
        Paint.FontMetricsInt fontMetrics = textPaint.getFontMetricsInt();
        //计算长宽
//        int tx = getMeasuredWidth() / 2 - bounds.width() / 2; Align Left 可以使用不是很剧中
        int tx = getMeasuredWidth() / 2;
        int ty = (getMeasuredHeight() - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
        canvas.drawText(numString, tx, ty, textPaint);
    }

    private void drawBgCircle(Canvas canvas, final TYPE_SIZE type) {
//        Log.i("lixiangyi", "drawBg: canvas.getWidth(): " + canvas.getWidth() + "canvas.getHeight:" + canvas.getHeight());
//        int radius = canvas.getHeight() * 9 / 20;

        int pathStartX = canvas.getWidth() / 2;
        int pathStartY = (int) padding;
        switch (TYPE_SIZE.getByValue(type.value)) {
            case CIRCLE1:
                pathCircle.moveTo(pathStartX, pathStartY);
                drawRightHalfCircle(radius, pathStartX, pathStartY);
                drawLeftHalfCircle(radius, pathStartX, pathStartY);
                break;
            case CIRCLE2:
                pathStartX = (int) (canvas.getWidth() / 2 - textSize / 2);
                pathCircle.moveTo(pathStartX, pathStartY);
                pathCircle.lineTo(pathStartX + textSize, pathStartY);
                drawRightHalfCircle(radius, (int) (pathStartX + textSize), pathStartY);
                pathCircle.lineTo(pathStartX, pathStartY + 2 * radius);
                drawLeftHalfCircle(radius, pathStartX, pathStartY);
                break;
            case CIRCLE3:
                pathStartX = (int) (canvas.getWidth() / 2 - textSize);
                pathCircle.moveTo(pathStartX, pathStartY);
                pathCircle.lineTo(pathStartX + 2 * textSize, pathStartY);
                drawRightHalfCircle(radius, (int) (pathStartX + 2 * textSize), pathStartY);
                pathCircle.lineTo(pathStartX, pathStartY + 2 * radius);
                drawLeftHalfCircle(radius, pathStartX, pathStartY);
                break;
            case CIRCLE4:
                pathStartX = (int) (canvas.getWidth() / 2 - textSize - "+".length() / 2);
                pathCircle.moveTo(pathStartX, pathStartY);
                pathCircle.lineTo(pathStartX + 2 * (textSize + "+".length()), pathStartY);
                drawRightHalfCircle(radius, (int) (pathStartX + 2 * textSize), pathStartY);
                pathCircle.lineTo(pathStartX, pathStartY + 2 * radius);
                drawLeftHalfCircle(radius, pathStartX, pathStartY);
                break;
            default:
                break;
        }


    }

    private void drawRightHalfCircle(int radius, int x, int y) {
        //右半圆
        pathCircle.cubicTo(x + CUB * radius, y, x + radius, y + radius * (1 - CUB), x + radius, y + radius);
        pathCircle.cubicTo(x + radius, y + radius + radius * CUB, x + CUB * radius, y + radius * 2, x, y + radius * 2);
    }

    private void drawLeftHalfCircle(int radius, int x, int y) {
        //左半圆
        pathCircle.cubicTo(x - radius * CUB, y + radius * 2, x - radius, y + radius + radius * CUB, x - radius, y + radius);
        pathCircle.cubicTo(x - radius, y + radius * (1 - CUB), x - CUB * radius, y, x, y);
    }
}

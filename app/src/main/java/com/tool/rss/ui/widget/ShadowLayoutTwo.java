package com.tool.rss.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.RelativeLayout;

import com.tool.rss.R;

/**
 * 阴影效果-修改
 */

public class ShadowLayoutTwo extends RelativeLayout {

    public static final int ALL = 0x1111;

    public static final int LEFT = 0x0001;

    public static final int TOP = 0x0010;

    public static final int RIGHT = 0x0100;

    public static final int BOTTOM = 0x1000;

    public static final int SHAPE_RECTANGLE = 0x0001;

    public static final int SHAPE_OVAL = 0x0010;

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private RectF mRectF = new RectF();

    /**
     * 阴影的颜色
     */
    private int mShadowColor = Color.TRANSPARENT;

    /**
     * 阴影的大小范围
     */
    private float mShadowRadius = 0;
    private float mShadowRadiusLeft = 0;
    private float mShadowRadiusRight = 0;
    private float mShadowRadiusTop = 0;
    private float mShadowRadiusButtom = 0;

    /**
     * 阴影 x 轴的偏移量
     */
    private float mShadowDx = 0;

    /**
     * 阴影 y 轴的偏移量
     */
    private float mShadowDy = 0;

    /**
     * 阴影显示的边界
     */
    private int mShadowSide = ALL;

    /**
     * 阴影的形状，圆形/矩形
     */
    private int mShadowShape = SHAPE_RECTANGLE;

    public ShadowLayoutTwo(Context context) {
        this(context, null);
    }

    public ShadowLayoutTwo(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShadowLayoutTwo(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        float effect = mShadowRadius;
        float effectLeft = mShadowRadiusLeft + dip2px(5);
        float effectRight = mShadowRadiusRight + dip2px(5);
        float effectTop = mShadowRadiusTop + dip2px(5);
        float effectButtom = mShadowRadiusButtom + dip2px(5);

        float rectLeft = 0;
        float rectTop = 0;
        float rectRight = this.getMeasuredWidth();
        float rectBottom = this.getMeasuredHeight();
        int paddingLeft = 0;
        int paddingTop = 0;
        int paddingRight = 0;
        int paddingBottom = 0;
        this.getWidth();
        if ((mShadowSide & LEFT) == LEFT) {
            rectLeft = effectLeft;
            paddingLeft = (int) effectLeft;
        }
        if ((mShadowSide & TOP) == TOP) {
            rectTop = effectTop;
            paddingTop = (int) effectTop;
        }
        if ((mShadowSide & RIGHT) == RIGHT) {
            rectRight = this.getMeasuredWidth() - effectRight;
            paddingRight = (int) effectRight;
        }
        if ((mShadowSide & BOTTOM) == BOTTOM) {
            rectBottom = this.getMeasuredHeight() - effectButtom;
            paddingBottom = (int) effectButtom;
        }
        if (mShadowDy != 0.0f) {
            rectBottom = rectBottom - mShadowDy;
            paddingBottom = paddingBottom + (int) mShadowDy;
        }
        if (mShadowDx != 0.0f) {
            rectRight = rectRight - mShadowDx;
            paddingRight = paddingRight + (int) mShadowDx;
        }
        mRectF.left = rectLeft;
        mRectF.top = rectTop-2;
        mRectF.right = rectRight;
        mRectF.bottom = rectBottom;
        this.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 真正绘制阴影的方法
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setUpShadowPaint();
        if (mShadowShape == SHAPE_RECTANGLE) {
            canvas.drawRect(mRectF, mPaint);
        } else if (mShadowShape == SHAPE_OVAL) {
            canvas.drawCircle(mRectF.centerX(), mRectF.centerY(), Math.min(mRectF.width(), mRectF.height()) / 2, mPaint);
        }
    }

    public void setShadowColor(int shadowColor) {
        mShadowColor = shadowColor;
        requestLayout();
        postInvalidate();
    }

    public void setShadowRadius(float shadowRadius) {
        mShadowRadius = shadowRadius;
        requestLayout();
        postInvalidate();
    }

    /**
     * 读取设置的阴影的属性
     *
     * @param attrs 从其中获取设置的值
     */
    private void init(AttributeSet attrs) {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);  // 关闭硬件加速
        this.setWillNotDraw(false);                    // 调用此方法后，才会执行 onDraw(Canvas) 方法

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ShadowLayout);
        if (typedArray != null) {
            mShadowColor = typedArray.getColor(R.styleable.ShadowLayout_shadowColor,
                    getContext().getResources().getColor(android.R.color.black));
            mShadowRadius = typedArray.getDimension(R.styleable.ShadowLayout_shadowRadius, dip2px(0));
            mShadowRadiusLeft= typedArray.getDimension(R.styleable.ShadowLayout_shadowRadiusLeft, dip2px(0));
            mShadowRadiusRight = typedArray.getDimension(R.styleable.ShadowLayout_shadowRadiusRight, dip2px(0));
            mShadowRadiusTop = typedArray.getDimension(R.styleable.ShadowLayout_shadowRadiusTop, dip2px(0));
            mShadowRadiusButtom = typedArray.getDimension(R.styleable.ShadowLayout_shadowRadiusButtom, dip2px(0));

            mShadowDx = typedArray.getDimension(R.styleable.ShadowLayout_shadowDx, dip2px(0));
            mShadowDy = typedArray.getDimension(R.styleable.ShadowLayout_shadowDy, dip2px(0));
            mShadowSide = typedArray.getInt(R.styleable.ShadowLayout_shadowSide, ALL);
            mShadowShape = typedArray.getInt(R.styleable.ShadowLayout_shadowShape, SHAPE_RECTANGLE);
            typedArray.recycle();
        }
        setUpShadowPaint();
    }

    private void setUpShadowPaint() {
        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.TRANSPARENT);
        mPaint.setShadowLayer(mShadowRadius, mShadowDx, mShadowDy, mShadowColor);
    }

    /**
     * dip2px dp 值转 px 值
     *
     * @param dpValue dp 值
     * @return px 值
     */
    private float dip2px(float dpValue) {
        DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
        float scale = dm.density;
        return (dpValue * scale + 0.5F);
    }
}

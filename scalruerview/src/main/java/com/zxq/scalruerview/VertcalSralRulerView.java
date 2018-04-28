package com.zxq.scalruerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Scroller;

import com.zxq.scalruerview.utils.DrawUtil;
import com.zxq.scalruerview.utils.TextUtil;


/**
 * Created by Administrator on 2018/4/27.
 * 垂直的 尺子选择
 */

public class VertcalSralRulerView extends View {
    private int mItemSpacing;//每列间隔
    private int mLineWidth;//刻度的线的宽度
    private int mMaxLineLength;//大刻度的刻度线长度
    private int mMiddleLineLength;//中刻度的线长度
    private int mMinLineLength;//小刻度的线长度
    private int mTextMarginRight;//刻度值离刻度的距离
    private float mTextHeight;
    private TextPaint mTextPaint; // 绘制文本的画笔
    private Paint mLinePaint;//线的画笔

    private float mValue = 50;//默认选中值
    private float mMaxValue = 100;//刻度最大值
    private float mMinValue = 0;//刻度最小值
    private int mWidth;//宽度
    private int mHeight;//高度
    private int mTotalLine;
    private int mPerSpanValue = 10;
    private float mOffset; // 默认尺起始点在屏幕中心, offset是指尺起始点的偏移值
    private int mMaxOffset;
    private VelocityTracker mVelocityTracker;
    private Scroller mScroller;
    private int mMinVelocity;
    private int mLastY, mMove;
    private OnValueChangeListener mListener;
    private int textColor=0X80222222;
    private int linColor=0X80222222;
    private float textSize;

    public VertcalSralRulerView(Context context) {
        this(context,null);
    }

    public VertcalSralRulerView(Context context,  AttributeSet attrs) {
        super(context, attrs);

        init(context,attrs);
    }

    /**
     * 设置用于接收结果的监听器
     *
     * @param listener
     */
    public void setValueChangeListener(OnValueChangeListener listener) {
        mListener = listener;
    }


    private void attresCorrection(){
        if(mItemSpacing==-1){
            mItemSpacing = DrawUtil.dip2px(14);
        }
        if(mLineWidth==-1){
            mLineWidth = DrawUtil.dip2px(1);
        }
        if(mMaxLineLength==-1){
            mMaxLineLength = DrawUtil.dip2px(42);
        }
        if(mMiddleLineLength==-1){
            mMiddleLineLength = DrawUtil.dip2px(31);
        }
        if(mMinLineLength==-1){
            mMinLineLength = DrawUtil.dip2px(17);
        }
        if(mTextMarginRight==-1){
            mTextMarginRight = DrawUtil.dip2px(11);
        }
        if(textSize==-1){
            textSize=DrawUtil.sp2px(16);
        }
    }
    /**
     * 初始化
     * @param context
     */
    private void init(Context context,AttributeSet attrs){
        //得到自定义属性
        DrawUtil.resetDensity(context.getApplicationContext());
        mScroller = new Scroller(context);
        mMinVelocity = ViewConfiguration.get(getContext()).getScaledMinimumFlingVelocity();
        if(attrs!=null){
            //获取自定义属性
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.VertcalSralRulerView);
            //获取sussic颜色
            mItemSpacing=  a.getDimensionPixelSize(R.styleable.VertcalSralRulerView_itemspacing,-1);
            mLineWidth= a.getDimensionPixelSize(R.styleable.VertcalSralRulerView_linheight,-1);
            mMaxLineLength=a.getDimensionPixelSize(R.styleable.VertcalSralRulerView_maxlinlenght,-1);
            mMiddleLineLength=a.getDimensionPixelSize(R.styleable.VertcalSralRulerView_middlinlenght,-1);
            mMinLineLength=a.getDimensionPixelSize(R.styleable.VertcalSralRulerView_minlinlenght,-1);
            mTextMarginRight=a.getDimensionPixelSize(R.styleable.VertcalSralRulerView_textmarglin,-1);
            textSize=a.getDimensionPixelSize(R.styleable.VertcalSralRulerView_textsize,-1);
            linColor=a.getColor(R.styleable.VertcalSralRulerView_lincolor,0X80222222);
            textColor=a.getColor(R.styleable.VertcalSralRulerView_textcolor,0X80222222);
            mMaxValue=a.getFloat(R.styleable.VertcalSralRulerView_maxvalue,100);
            mMinValue=a.getFloat(R.styleable.VertcalSralRulerView_minvalue,0);
            mValue=a.getFloat(R.styleable.VertcalSralRulerView_defaultvalue,1);
            //最后记得将TypedArray对象回收
            a.recycle();
            //进行参数修正
            attresCorrection();
        }else {
            mItemSpacing = DrawUtil.dip2px(14);
            mLineWidth = DrawUtil.dip2px(1);
            mMaxLineLength = DrawUtil.dip2px(42);
            mMiddleLineLength = DrawUtil.dip2px(31);
            mMinLineLength = DrawUtil.dip2px(17);
            mTextMarginRight = DrawUtil.dip2px(11);
            textSize=DrawUtil.sp2px(16);
        }

        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(textSize);
        mTextPaint.setColor(textColor);
        mTextHeight = TextUtil.getFontHeight(mTextPaint);

        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setStrokeWidth(mLineWidth);
        mLinePaint.setColor(linColor);

        //默认初始化部分参数操作
        mTotalLine = (int) (mMaxValue * 10 - mMinValue * 10) / mPerSpanValue + 1;
        mOffset = (mMinValue - mValue) / mPerSpanValue * mItemSpacing * 10;
        mMaxOffset = -(mTotalLine - 1) * mItemSpacing;
    }

    /**
     * 代码设置内部属性
     * @param itemSpacing
     * @param maxLineLength
     * @param middleLineLength
     * @param minLineLength
     * @param textMarginRight
     * @param textSize
     */
    public void setParam(int itemSpacing, int maxLineLength, int middleLineLength, int minLineLength, int textMarginRight, int textSize) {
        mItemSpacing = itemSpacing;
        mMaxLineLength = maxLineLength;
        mMiddleLineLength = middleLineLength;
        mMinLineLength = minLineLength;
        mTextMarginRight = textMarginRight;
        mTextPaint.setTextSize(textSize);
    }

    /**
     * 重新初始View
     * @param defaultValue
     * @param minValue
     * @param maxValue
     * @param spanValue
     */
    public void initViewParam(float defaultValue, float minValue, float maxValue, int spanValue) {
        this.mValue = defaultValue;
        this.mMaxValue = maxValue;
        this.mMinValue = minValue;
        this.mPerSpanValue = spanValue;
        this.mTotalLine = (int) (maxValue * 10 - minValue * 10) / spanValue + 1;
        mMaxOffset = -(mTotalLine - 1) * mItemSpacing;

        mOffset = (minValue - defaultValue) / spanValue * mItemSpacing * 10;
        invalidate();
        setVisibility(VISIBLE);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w > 0 && h > 0) {
            mWidth = w;
            mHeight = h;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //测量试图大小
        int hightMode = MeasureSpec.getMode(heightMeasureSpec);
        int hightSize = MeasureSpec.getSize(heightMeasureSpec);

        //根据模式进行大小设置
        switch (hightMode) {
            case MeasureSpec.UNSPECIFIED: //如果没有指定大小，就设置为默认大小
                mHeight = getHeight();
                break;

            case MeasureSpec.AT_MOST: //如果测量模式是最大取值为size
                //我们将大小取最大值,你也可以取其他值
                mHeight = hightSize;
                break;

            case MeasureSpec.EXACTLY: //如果是固定的大小，那就不要去改变它
                mHeight = hightSize;
                break;

        }
        //通过计算得到控件实际宽度
        //计算底部文字高度
        mWidth= (int) (mMaxLineLength+mTextMarginRight+ TextUtil.getFontlength(mTextPaint,"100"));

        //设置控件大小
        setMeasuredDimension(mWidth, mHeight);


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float top, length;
        String value;
        int alpha;
        float scale;
        int srcPointY = mHeight / 2; // 默认表尺起始点在屏幕中心

        for (int i = 0; i < mTotalLine; i++) {
            top = srcPointY + mOffset + i * mItemSpacing;

            if (top < 0 || top > mHeight) {
                continue;
            }

            if (i % 10 == 0) {
                length = mMaxLineLength;
            } else if (i % 5 == 0) {
                length = mMiddleLineLength;
            } else {
                length = mMinLineLength;
            }
            scale = 1 - Math.abs(top - srcPointY) / srcPointY;
            alpha = (int) (255 * scale * scale);
            mLinePaint.setAlpha(alpha);
            canvas.drawLine(mWidth-length, top, mWidth, top, mLinePaint);

            if (i % 10 == 0) { // 大指标,要标注文字
                value = String.valueOf((int) (mMinValue + i * mPerSpanValue / 10));
                mTextPaint.setAlpha(alpha);
                canvas.drawText(value, mWidth-length-mTextMarginRight - mTextPaint.measureText(value),
                        (top + mTextHeight/2)  - DrawUtil.dip2px(3), mTextPaint);
            }

        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        int yPosition = (int) event.getY();

        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mScroller.forceFinished(true);
                mLastY = yPosition;
                mMove = 0;
                break;
            case MotionEvent.ACTION_MOVE:
                mMove = (mLastY - yPosition);
                changeMoveAndValue();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                countMoveEnd();
                countVelocityTracker();
                return false;
            // break;
            default:
                break;
        }

        mLastY = yPosition;
        return true;
    }

    private void countVelocityTracker() {
        mVelocityTracker.computeCurrentVelocity(1000);
        float yVelocity = mVelocityTracker.getYVelocity();
        if (Math.abs(yVelocity) > mMinVelocity) {
            mScroller.fling(0, 0, 0, (int) yVelocity, Integer.MIN_VALUE, Integer.MAX_VALUE, 0, 0);
        }
    }

    private void countMoveEnd() {
        mOffset -= mMove;
        if (mOffset <= mMaxOffset) {
            mOffset = mMaxOffset;
        } else if (mOffset >= 0) {
            mOffset = 0;
        }

        mLastY = 0;
        mMove = 0;

        mValue = mMinValue + Math.round(Math.abs(mOffset) * 1.0f / mItemSpacing) * mPerSpanValue / 10.0f;
        mOffset = (mMinValue - mValue) * 10.0f / mPerSpanValue * mItemSpacing; // 矫正位置,保证不会停留在两个相邻刻度之间
        notifyValueChange();
        postInvalidate();
    }

    private void changeMoveAndValue() {
        mOffset -= mMove;
        if (mOffset <= mMaxOffset) {
            mOffset = mMaxOffset;
            mMove = 0;
            mScroller.forceFinished(true);
        } else if (mOffset >= 0) {
            mOffset = 0;
            mMove = 0;
            mScroller.forceFinished(true);
        }
        mValue = mMinValue + Math.round(Math.abs(mOffset) * 1.0f / mItemSpacing) * mPerSpanValue / 10.0f;
        notifyValueChange();
        postInvalidate();
    }

    private void notifyValueChange() {
        if (null != mListener) {
            mListener.onValueChange(mValue);
        }
    }

    public interface OnValueChangeListener {
        void onValueChange(float value);
    }
}

package com.example.circleprogress;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Author : wxz
 * Time   : 2020/07/29
 * Desc   :
 */
public class CircleProgressView extends View {
    private float mProgress;
    private float mMaxProgress;
    private float mOutsideRingWidth;
    private float mCircleWidth;
    private float backgroundStrokeWidth;

    private int mCircleColor;
    private int mBackgroundColor;
    private int mInnerCircleColor;
    private int mOutsideRingColor;

    private RectF mFirstRectF;
    private RectF mSecondRectF;
    private Paint mBackgroundPaint;
    private Paint mCirclePaint;
    private Paint mInnerCirclePaint;
    private Paint mOutsideRingPaint;
    private Interpolator mInterpolator;

    private boolean mIsTextEnabled;
    private boolean isShowOutCircle;
    private boolean isShowInnerCircle;

    private boolean isProgressGradient;//进度条颜色是否需要渐变 只有设置为true时 设置渐变颜色才生效
    private int mProgressStartColor;
    private int mProgressEndColor;


    private String mTextPrefix;
    private String mTextSuffix;

    private float mStartAngle;

    private TextView mTextView;
    private TextView mTipTextView;

    private int mTextColor;
    private int mTextSize;
    private boolean mTextBold;
    private int mTipTextColor;
    private int mTipTextSize;
    private String mTipText;

    LinearLayout mLayout;

    //外环到内环的距离
    private int mFirstFromSecond;

    private float innerCircleX, innerCircleY, innerCircleRadius;

    private ProgressAnimationListener progressAnimationListener;
    private ArgbEvaluator argbEvaluator = new ArgbEvaluator();//颜色插值器（level 11以上才可以用）

    public CircleProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mFirstRectF = new RectF();
        mSecondRectF = new RectF();

        setDefaultValues();
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CircularProgressView, 0, 0);

        try {
            mProgress = typedArray.getFloat(R.styleable.CircularProgressView_cpv_progress, mProgress);
            mMaxProgress = typedArray.getFloat(R.styleable.CircularProgressView_cpv_max_progress, mMaxProgress);
            mFirstFromSecond = typedArray.getInteger(R.styleable.CircularProgressView_cpv_first_from_second, mFirstFromSecond);
            mCircleWidth = typedArray.getDimension(R.styleable.CircularProgressView_cpv_circle_width, mCircleWidth);
            mOutsideRingWidth = typedArray.getDimension(R.styleable.CircularProgressView_cpv_outside_ring_width, mOutsideRingWidth);
            backgroundStrokeWidth = typedArray.getDimension(R.styleable.CircularProgressView_cpv_background_circle_width, backgroundStrokeWidth);
            mCircleColor = typedArray.getInt(R.styleable.CircularProgressView_cpv_circle_color, mCircleColor);
            mInnerCircleColor = typedArray.getInt(R.styleable.CircularProgressView_cpv_inner_circle_color, mInnerCircleColor);
            mOutsideRingColor = typedArray.getInt(R.styleable.CircularProgressView_cpv_outside_ring_color, mOutsideRingColor);
            mBackgroundColor = typedArray.getInt(R.styleable.CircularProgressView_cpv_background_circle_color, mBackgroundColor);
            mTextColor = typedArray.getInt(R.styleable.CircularProgressView_cpv_text_color, mTextColor);
            mTextSize = typedArray.getInt(R.styleable.CircularProgressView_cpv_text_size, mTextSize);
            mTextBold = typedArray.getBoolean(R.styleable.CircularProgressView_cpv_text_bold, mTextBold);
            mTextPrefix = typedArray.getString(R.styleable.CircularProgressView_cpv_text_prefix);
            mTextSuffix = typedArray.getString(R.styleable.CircularProgressView_cpv_text_suffix);
            mTipTextColor = typedArray.getInt(R.styleable.CircularProgressView_cpv_tip_text_color, mTipTextColor);
            mTipTextSize = typedArray.getInt(R.styleable.CircularProgressView_cpv_tip_text_size, mTipTextSize);
            mTipText = typedArray.getString(R.styleable.CircularProgressView_cpv_tip_text);
            isShowOutCircle = typedArray.getBoolean(R.styleable.CircularProgressView_cpv_isShowOutCircle, isShowOutCircle);
            isShowInnerCircle = typedArray.getBoolean(R.styleable.CircularProgressView_cpv_isShowInnerCircle, isShowInnerCircle);
            isProgressGradient = typedArray.getBoolean(R.styleable.CircularProgressView_cpv_isProgressGradient, isProgressGradient);
            mProgressStartColor = typedArray.getInt(R.styleable.CircularProgressView_cpv_progressStart, mProgressStartColor);
            mProgressEndColor = typedArray.getInt(R.styleable.CircularProgressView_cpv_progressEnd, mProgressEndColor);
        } finally {
            typedArray.recycle();
        }

        // Init Background
        mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBackgroundPaint.setColor(mBackgroundColor);
        mBackgroundPaint.setStyle(Paint.Style.STROKE);
        mBackgroundPaint.setStrokeWidth(backgroundStrokeWidth);

        // Init Circle
        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setColor(mCircleColor);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setStrokeWidth(mCircleWidth);

        // Init InnerCircle
        mInnerCirclePaint = new Paint();
        mInnerCirclePaint.setColor(mInnerCircleColor);
        mInnerCirclePaint.setAntiAlias(true);

        // Init OutsideRing
        mOutsideRingPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mOutsideRingPaint.setColor(mOutsideRingColor);
        mOutsideRingPaint.setStyle(Paint.Style.STROKE);
        mOutsideRingPaint.setStrokeWidth(mOutsideRingWidth);

        // Init TextView
        mTextView = new TextView(context);
        mTextView.setVisibility(View.VISIBLE);

        mTextView.setGravity(Gravity.CENTER_HORIZONTAL);
        mTextView.setTextSize(mTextSize);
        mTextView.setTextColor(mTextColor);

        // Init TipTextView
        mTipTextView = new TextView(context);
        mTipTextView.setVisibility(View.VISIBLE);
        mTipTextView.setGravity(Gravity.CENTER_HORIZONTAL);
        mTipTextView.setTextSize(mTipTextSize);
        mTipTextView.setTextColor(mTipTextColor);

        // Init Layout
        mLayout = new LinearLayout(context);
        mLayout.setOrientation(LinearLayout.VERTICAL);
        mLayout.setGravity(Gravity.CENTER);
        mLayout.addView(mTextView);
        mLayout.addView(mTipTextView);
        showTextView(mIsTextEnabled);
    }

    private void showTextView(boolean mIsTextEnabled) {
        mTextView.setText(getTextPrefix() +
                Math.round((mProgress / mMaxProgress) * 100) +
                getTextSuffix());
        mTextView.setVisibility(mIsTextEnabled ? View.VISIBLE : View.GONE);
        mTextView.getPaint().setFakeBoldText(mTextBold);
        mTipTextView.setText(mTipText);
        mTipTextView.setVisibility((mIsTextEnabled && !mTipTextView.getText().toString().isEmpty()) ? View.VISIBLE : View.GONE);
        invalidate();
    }

    /**
     * 设置默认状态
     */
    private void setDefaultValues() {
        mProgress = 0;
        mMaxProgress = 100;
        mCircleWidth = getResources().getDimension(R.dimen.default_circle_width);
        mOutsideRingWidth = getResources().getDimension(R.dimen.default_outside_ring_width);
        backgroundStrokeWidth = getResources().getDimension(R.dimen.default_circle_background_width);
        mCircleColor = Color.BLACK;
        mTextColor = Color.BLACK;
        mBackgroundColor = Color.GRAY;
        mInnerCircleColor = Color.TRANSPARENT;
        mOutsideRingColor = Color.WHITE;
        mFirstFromSecond = 2;
        mStartAngle = -90;
        mIsTextEnabled = true;
        mTextSize = 20;
        mTipText = "";
        mTipTextColor = Color.BLACK;
        mTipTextSize = 20;
        isShowInnerCircle = true;
        isShowOutCircle = true;
        isProgressGradient = false;
        mProgressStartColor = Color.GREEN;
        mProgressEndColor = Color.GREEN;
    }

    /**
     * 绘画圆、圆弧、文本
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //Draw OutsideRing
        if (isShowOutCircle) {
            canvas.drawOval(mFirstRectF, mOutsideRingPaint);
        }
        // Draw Background Circle
        canvas.drawOval(mSecondRectF, mBackgroundPaint);

        // Draw Circle
        float angle = 360 * mProgress / mMaxProgress;
        if (isProgressGradient) {
            for (int i = 0; i < angle; i++) {
                if (i < 180) {
                    mCirclePaint.setColor((Integer) argbEvaluator.evaluate(i / 360f, mProgressStartColor, mProgressEndColor));
                } else {
                    mCirclePaint.setColor((Integer) argbEvaluator.evaluate(i / 360f, mProgressEndColor, mProgressStartColor));
                }
                canvas.drawArc(mSecondRectF, mStartAngle + i, 1.5f, false, mCirclePaint);// 绘制圆弧 1.35f是每个色块宽度
            }
        } else {
            canvas.drawArc(mSecondRectF, mStartAngle, angle, false, mCirclePaint);
        }

        //Draw innerCircle
        if (isShowInnerCircle) {
            canvas.drawCircle(innerCircleX, innerCircleY, innerCircleRadius, mInnerCirclePaint);
        }
        // Draw TextView
        mLayout.measure(canvas.getWidth(), canvas.getHeight());
        mLayout.layout(0, 0, canvas.getWidth(), canvas.getHeight());
        canvas.translate(0,
                0);
        mLayout.draw(canvas);
    }

    /**
     * 测量宽高
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        final int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        final int min = Math.min(width, height);
        setMeasuredDimension(min, min);
        float stroke = (mCircleWidth > backgroundStrokeWidth) ? mCircleWidth : backgroundStrokeWidth;
        mFirstRectF.set(mOutsideRingWidth, mOutsideRingWidth, min - mOutsideRingWidth, min - mOutsideRingWidth);
        mSecondRectF.set(mFirstFromSecond + mOutsideRingWidth + stroke / 2, mFirstFromSecond + mOutsideRingWidth + stroke / 2,
                min - (mFirstFromSecond + mOutsideRingWidth + (stroke / 2)), min - (mFirstFromSecond + mOutsideRingWidth + (stroke / 2)));
        innerCircleX = Math.abs(width / 2);
        innerCircleY = Math.abs(height / 2);
        innerCircleRadius = Math.abs((min / 2) - (Math.max(mCircleWidth, backgroundStrokeWidth) + mFirstFromSecond + mOutsideRingWidth));
    }

    /**
     * 获取是否显示外环线
     *
     * @return
     */
    public boolean isShowOutCircle() {
        return isShowOutCircle;
    }

    /**
     * 设置是否显示外环线
     *
     * @param isShowOutCircle
     */
    public void setShowOutCircle(boolean isShowOutCircle) {
        this.isShowOutCircle = isShowOutCircle;
        invalidate();
    }

    /**
     * 获取是否显示内圆
     *
     * @return
     */
    public boolean isShowInnerCircle() {
        return isShowInnerCircle;
    }

    /**
     * 设置是否显示内圆
     *
     * @param isShowInnerCircle
     */
    public void setShowInnerCircle(boolean isShowInnerCircle) {
        this.isShowInnerCircle = isShowInnerCircle;
        invalidate();
    }

    /**
     * 获取进度条颜色是否是渐变
     *
     * @return
     */
    public boolean isProgressGradient() {
        return isProgressGradient;
    }

    /**
     * 设置进度条颜色是否渐变
     * 渐变的范围是0-180,180-360
     *
     * @param isProgressGradient
     */
    public void setProgressGradient(boolean isProgressGradient) {
        this.isProgressGradient = isProgressGradient;
        invalidate();
    }

    /**
     * 获取进度条开始的渐变颜色
     *
     * @return
     */
    public int getProgressStartColor() {
        return mProgressStartColor;
    }

    /**
     * 设置进度条开始的渐变颜色
     *
     * @param progressStartColor
     */
    public void setProgressStartColor(int progressStartColor) {
        this.mProgressStartColor = progressStartColor;
        invalidate();
    }

    /**
     * 获取进度条结束的渐变颜色
     *
     * @return
     */
    public int getProgressEndColor() {
        return mProgressEndColor;
    }

    /**
     * 设置进度条结束的渐变颜色
     *
     * @param progressEndColor
     */
    public void setProgressEndColor(int progressEndColor) {
        this.mProgressEndColor = progressEndColor;
        invalidate();
    }

    /**
     * 获取外环的宽度
     *
     * @return
     */
    public float getOutsideRingWidth() {
        return mOutsideRingWidth;
    }

    /**
     * 设置外环的宽度
     *
     * @param OutsideRingWidth
     */
    public void setOutsideRingWidth(float OutsideRingWidth) {
        this.mOutsideRingWidth = OutsideRingWidth;
        mOutsideRingPaint.setStrokeWidth(OutsideRingWidth);
        requestLayout();
        invalidate();
    }

    /**
     * 获取外环的颜色
     *
     * @return
     */
    public int getOutsideRingColor() {
        return mOutsideRingColor;
    }

    /**
     * 设置外环颜色
     *
     * @param OutsideRingColor
     */
    public void setOutsideRingColor(int OutsideRingColor) {
        this.mOutsideRingColor = OutsideRingColor;
        mOutsideRingPaint.setColor(OutsideRingColor);
        invalidate();
    }

    /**
     * 获取外环到内环的距离
     *
     * @return
     */
    public int getFirstFromSecond() {
        return mFirstFromSecond;
    }

    /**
     * 设置外环到内环的距离
     *
     * @param firstFromSecond
     */
    public void setFirstFromSecond(int firstFromSecond) {
        this.mFirstFromSecond = firstFromSecond;
        invalidate();
    }

    /**
     * 获取圆弧的宽度
     *
     * @return
     */
    public float getCircleWidth() {
        return mCircleWidth;
    }

    /**
     * 设置圆弧的宽度
     *
     * @param circleWidth
     */
    public void setCircleWidth(float circleWidth) {
        this.mCircleWidth = circleWidth;
        mCirclePaint.setStrokeWidth(circleWidth);
        requestLayout();
        invalidate();
    }

    /**
     * 获取内圆的颜色
     *
     * @return
     */
    public int getInnerCircleColor() {
        return mInnerCircleColor;
    }

    /**
     * 设置内圆的颜色
     *
     * @param innerCircleColor
     */
    public void setInnerCircleColor(int innerCircleColor) {
        this.mInnerCircleColor = innerCircleColor;
        mInnerCirclePaint.setColor(innerCircleColor);
        invalidate();
    }

    /**
     * 获取圆弧的颜色
     *
     * @return
     */
    public int getCircleColor() {
        return mCircleColor;
    }

    /**
     * 设置圆弧的颜色
     *
     * @param circleColor
     */
    public void setCircleColor(int circleColor) {
        this.mCircleColor = circleColor;
        mCirclePaint.setColor(circleColor);
        invalidate();
    }

    public Interpolator getInterpolator() {
        return mInterpolator;
    }

    public void setInterpolator(Interpolator interpolator) {
        this.mInterpolator = interpolator;
    }

    /**
     * 获取文本的前缀内容
     *
     * @return
     */
    public String getTextPrefix() {
        return mTextPrefix != null ? mTextPrefix : "";
    }

    /**
     * 设置文本的前缀内容
     *
     * @param textPrefix
     */
    public void setTextPrefix(String textPrefix) {
        this.mTextPrefix = textPrefix;
        showTextView(mIsTextEnabled);
    }

    /**
     * 获取文本的后缀内容
     *
     * @return
     */
    public String getTextSuffix() {
        return mTextSuffix != null ? mTextSuffix : "";
    }

    /**
     * 设置文本的后缀内容
     *
     * @param textSuffix
     */
    public void setTextSuffix(String textSuffix) {
        this.mTextSuffix = textSuffix;
        showTextView(mIsTextEnabled);
    }

    /**
     * 获取当前进度值的大小
     *
     * @return
     */
    public float getProgress() {
        return mProgress;
    }

    /**
     * 获取当前进度值的大小
     *
     * @return
     */
    public int getTextSize() {
        return mTextSize;
    }

    /**
     * 设置文字的大小
     *
     * @param textSize
     */
    public void setTextSize(int textSize) {
        this.mTextSize = textSize;
        mTextView.setTextSize(textSize);
        invalidate();
    }

    /**
     * 设置文字加粗
     *
     * @param textBold
     */
    public void setTextBold(boolean textBold) {
        this.mTextBold = textBold;
        mTextView.getPaint().setFakeBoldText(textBold);
        invalidate();
    }


    /**
     * 判断文本是否可用（显示）
     *
     * @return
     */
    public boolean isTextEnabled() {
        return mIsTextEnabled;
    }

    /**
     * 设置文本可用（显示）状态
     *
     * @param isTextEnabled
     */
    public void setTextEnabled(boolean isTextEnabled) {
        this.mIsTextEnabled = isTextEnabled;
        showTextView(isTextEnabled);
    }

    /**
     * 获取开始绘画圆弧的位置
     *
     * @return
     */
    public float getStartAngle() {
        return mStartAngle;
    }

    /**
     * 获取圆弧背景颜色
     *
     * @return
     */
    public int getBackgroundColor() {
        return mBackgroundColor;
    }

    /**
     * 设置圆弧背景颜色
     *
     * @param backgroundColor
     */
    public void setBackgroundColor(int backgroundColor) {
        this.mBackgroundColor = backgroundColor;
        mBackgroundPaint.setColor(backgroundColor);
        invalidate();
    }

    /**
     * 获取圆弧背景的宽度
     *
     * @return
     */
    public float getBackgroundStrokeWidth() {
        return backgroundStrokeWidth;
    }

    /**
     * 设置圆弧背景的宽度
     *
     * @param backgroundStrokeWidth
     */
    public void setBackgroundStrokeWidth(float backgroundStrokeWidth) {
        this.backgroundStrokeWidth = backgroundStrokeWidth;
        mBackgroundPaint.setStrokeWidth(backgroundStrokeWidth);
        requestLayout();
        invalidate();
    }

    /**
     * 设置开始绘画圆弧的位置
     *
     * @param startAngle
     */
    public void setStartAngle(float startAngle) {
        this.mStartAngle = startAngle;
    }

    /**
     * 获取文本字体的颜色
     *
     * @return
     */
    public int getTextColor() {
        return mTextColor;
    }

    /**
     * 设置文本字体的颜色
     *
     * @param textColor
     */
    public void setTextColor(int textColor) {
        this.mTextColor = textColor;
        mTextView.setTextColor(textColor);
        invalidate();
    }

    /**
     * 获取提示文本字体颜色
     *
     * @return
     */
    public int getTipTextColor() {
        return mTipTextColor;
    }

    /**
     * 设置提示文本字体颜色
     *
     * @param tipTextColor
     */
    public void setTipTextColor(int tipTextColor) {
        this.mTipTextColor = tipTextColor;
        mTipTextView.setTextColor(tipTextColor);
        invalidate();
    }

    /**
     * 获取提示文本字体大小
     *
     * @return
     */
    public int getTipTextSize() {
        return mTipTextSize;
    }

    /**
     * 设置提示文本字体大小
     *
     * @param tipTextSize
     */
    public void setTipTextSize(int tipTextSize) {
        this.mTipTextSize = tipTextSize;
        mTipTextView.setTextSize(tipTextSize);
        invalidate();
    }

    /**
     * 设置提示文本内容
     *
     * @param tipText
     */
    public void setTipText(String tipText) {
        this.mTipText = tipText;
        showTextView(mIsTextEnabled);
    }

    /**
     * 设置当前进度值
     *
     * @param progress
     */
    public void setProgress(float progress) {
        this.mProgress = (progress <= mMaxProgress) ? progress : mMaxProgress;
        mTextView.setText(mTextPrefix + String.valueOf(Math.round((mProgress / mMaxProgress) * 100)) + mTextSuffix);
        showTextView(mIsTextEnabled);
        invalidate();

        if (progressAnimationListener != null) {
            progressAnimationListener.onValueChanged(progress);
        }
    }

    /**
     * 获取最大进度值
     *
     * @return
     */
    public float getMaxProgress() {
        return mMaxProgress;
    }

    /**
     * 设置最大进度值
     *
     * @param maxProgress
     */
    public void setMaxProgress(float maxProgress) {
        this.mMaxProgress = maxProgress;
        invalidate();
    }

    /**
     * 设置进度动画
     *
     * @param progress 进度值
     * @param duration 动画时间，单位毫秒
     */
    public void setProgressWithAnimation(final float progress, int duration) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this, "progress", progress);
        objectAnimator.setDuration(duration);
//        objectAnimator.setInterpolator(mInterpolator != null ? mInterpolator : new DecelerateInterpolator());
        objectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mProgress = (progress <= mMaxProgress) ? progress : mMaxProgress;
                if (progressAnimationListener != null) {
                    progressAnimationListener.onAnimationEnd();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mTextView.setText(mTextPrefix +
                        String.valueOf(Math.round((((Float) animation.getAnimatedValue()) / mMaxProgress) * 100)) +
                        mTextSuffix);
            }
        });
        objectAnimator.start();

        if (progressAnimationListener != null) {
            progressAnimationListener.onValueChanged(progress);
        }
    }

    /**
     * 获取动画监听
     *
     * @return
     */
    public ProgressAnimationListener getProgressAnimationListener() {
        return progressAnimationListener;
    }

    /**
     * 添加动画监听
     *
     * @param progressAnimationListener
     */
    public void addAnimationListener(ProgressAnimationListener progressAnimationListener) {
        this.progressAnimationListener = progressAnimationListener;
    }

    private class Interpolator {
    }

    /**
     * 动画监听接口
     */
    public interface ProgressAnimationListener {

        void onValueChanged(float value);

        void onAnimationEnd();
    }
}
package com.helsong.library;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.TextView;


public class ProcessButton extends Button {

    private static final int DRAW_HORIZONTAL = 1;
    private static final int DRAW_VERTICAL = 2;

    private ButtonState mButtonState = ButtonState.NORMAL;
    private int mCurrentProgress;
    private int mDrawDirection;

    private Drawable mNormalDrawable;
    private Drawable mProcessDrawable;
    private Drawable mProcessBGDrawable;
    private Drawable mCompleteDrawable;
    private Drawable mErrorDrawable;

    private String mNormalText;
    private String mProcessText;
    private String mCompleteText;
    private String mErrorText;

    public ProcessButton(Context context) {
        super(context);
        init(null, 0);
    }

    public ProcessButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public ProcessButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.ProcessButton, defStyle, 0);

        Drawable normalDrawable = getDrawable(R.drawable.process_button_normal).mutate();
        mNormalDrawable = normalDrawable;

        Drawable progressDrawable = getDrawable(R.drawable.process_button_rect_progress).mutate();
        mProcessDrawable = progressDrawable;

        Drawable progressBGDrawable = getDrawable(R.drawable.process_button_rect_progress_bg).mutate();
        mProcessBGDrawable = progressBGDrawable;

        Drawable completeDrawable = getDrawable(R.drawable.process_button_rect_complete).mutate();
        mCompleteDrawable = completeDrawable;

        Drawable errorDrawable = getDrawable(R.drawable.process_button_error).mutate();
        mErrorDrawable = errorDrawable;

        initArrtrs(a);
        a.recycle();

        setBackgroundCompat(mNormalDrawable);
        setText(mNormalText);
    }

    private void initArrtrs(TypedArray a) {
        if (a == null) {
            return;
        }

        mNormalText = a.getString(R.styleable.ProcessButton_normalText);
        mProcessText = a.getString(R.styleable.ProcessButton_processText);
        mCompleteText = a.getString(R.styleable.ProcessButton_completeText);
        mErrorText = a.getString(R.styleable.ProcessButton_errorText);

        Drawable drawable = a.getDrawable(R.styleable.ProcessButton_normalDrawable);
        if (drawable != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mNormalDrawable = createRippleDrawable(drawable);
            } else {
                ((StateListDrawable) mNormalDrawable).addState(new int[]{android.R.attr.state_pressed},
                        drawable);
            }
        } else {
            int blue = getColor(R.color.blue_normal);
            int colorNormal = a.getColor(R.styleable.ProcessButton_normalColor, blue);
            if (colorNormal != blue) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mNormalDrawable = createRippleDrawable(new ColorDrawable(colorNormal));
                } else {
                    ((StateListDrawable) mNormalDrawable).addState(new int[]{android.R.attr.state_pressed},
                            createColorDrawable(colorNormal));
                }
            }
        }

        int purple = getColor(R.color.purple_progress);
        int colorProgress = a.getColor(R.styleable.ProcessButton_processColor, purple);
        ((GradientDrawable) mProcessDrawable).setColor(colorProgress);

        int green = getColor(R.color.green_complete);
        int colorComplete = a.getColor(R.styleable.ProcessButton_completeColor, green);
        ((GradientDrawable) mCompleteDrawable).setColor(colorComplete);

        drawable = a.getDrawable(R.styleable.ProcessButton_errorDrawable);
        if (drawable != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mErrorDrawable = createRippleDrawable(drawable);
            } else {
                ((StateListDrawable) mErrorDrawable).addState(new int[]{android.R.attr.state_pressed},
                        drawable);
            }
        } else {
            int red = getColor(R.color.red_error);
            int colorError = a.getColor(R.styleable.ProcessButton_errorColor, red);
            if (colorError != red) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mErrorDrawable = createRippleDrawable(new ColorDrawable(colorError));
                } else {
                    ((StateListDrawable) mErrorDrawable).addState(new int[]{android.R.attr.state_pressed},
                            createColorDrawable(colorError));
                }
            }
        }

        drawable = a.getDrawable(R.styleable.ProcessButton_processDrawable);
        if (drawable != null) {
            mProcessDrawable = drawable;
        }
        drawable = a.getDrawable(R.styleable.ProcessButton_processBGDrawable);
        if (drawable != null) {
            mProcessBGDrawable = drawable;
        }
        drawable = a.getDrawable(R.styleable.ProcessButton_completeDrawable);
        if (drawable != null) {
            mCompleteDrawable = drawable;
        }

        boolean state = a.getBoolean(R.styleable.ProcessButton_drawHorizontal, true);
        if (state) {
            mDrawDirection = DRAW_HORIZONTAL;
        } else {
            mDrawDirection = DRAW_VERTICAL;
        }
    }

    private Drawable createColorDrawable(int color) {
        Drawable drawable = getDrawable(R.drawable.process_button_rect_progress).mutate();
        ((GradientDrawable) drawable).setColor(color);
        return drawable;
    }

    @TargetApi(21)
    private Drawable createRippleDrawable(Drawable content) {
        int pressedColor = getColor(android.R.color.darker_gray);
        ColorStateList colorStateList = new ColorStateList(
                new int[][]
                        {
                                new int[]{android.R.attr.state_pressed},
                                new int[]{android.R.attr.state_focused},
                                new int[]{android.R.attr.state_activated},
                                new int[]{}
                        },
                new int[]
                        {
                                pressedColor,
                                pressedColor,
                                pressedColor,
                                pressedColor
                        }
        );
        return new RippleDrawable(colorStateList, content, new ColorDrawable(pressedColor));
    }

    protected Drawable getDrawable(int id) {
        return getResources().getDrawable(id);
    }

    protected int getColor(int id) {
        return getResources().getColor(id);
    }

    public void setProcess(int process) {
        mCurrentProgress = process;
        if (mButtonState == ButtonState.PROCESSING)
            invalidate();
    }

    public boolean isHorizontal() {
        if (mDrawDirection == this.DRAW_HORIZONTAL) {
            return true;
        } else {
            return false;
        }
    }

    public void setDrawHorizontal(boolean isDrawHorizontal) {
        if (isDrawHorizontal) {
            this.mDrawDirection = DRAW_HORIZONTAL;
        } else {
            this.mDrawDirection = DRAW_VERTICAL;
        }
    }

    public String getCompleteText() {
        return mCompleteText;
    }

    public void setCompleteText(String completeText) {
        this.mCompleteText = completeText;
        if (this.mButtonState == ButtonState.COMPLETE) {
            setText(mCompleteText);
        }
    }

    public String getNormalText() {
        return mNormalText;
    }

    public void setNormalText(String normalText) {
        this.mNormalText = normalText;
        if (this.mButtonState == ButtonState.NORMAL) {
            setText(mNormalText);
        }
    }

    public String getProcessText() {
        return mProcessText;
    }

    public void setProcessText(String processText) {
        this.mProcessText = processText;
        if (this.mButtonState == ButtonState.PROCESSING) {
            setText(mProcessText);
        }
    }

    public String getErrorText() {
        return mErrorText;
    }

    public void setErrorText(String errorText) {
        this.mErrorText = errorText;
        if (this.mButtonState == ButtonState.ERROR) {
            setText(mErrorText);
        }
    }

    public int getCurrentProgress() {
        return mCurrentProgress;
    }

    public void setCurrentProgress(int currentProgress) {
        this.mCurrentProgress = currentProgress;
    }

    public Drawable getNormalDrawable() {
        return mNormalDrawable;
    }

    public void setNormalDrawable(Drawable normalDrawable) {
        this.mNormalDrawable = normalDrawable;
    }

    public Drawable getProcessDrawable() {
        return mProcessDrawable;
    }

    public void setProcessDrawable(Drawable processDrawable) {
        this.mProcessDrawable = processDrawable;
    }

    public Drawable getProcessBGDrawable() {
        return mProcessBGDrawable;
    }

    public void setProcessBGDrawable(Drawable processBGDrawable) {
        this.mProcessBGDrawable = processBGDrawable;
    }

    public Drawable getCompleteDrawable() {
        return mCompleteDrawable;
    }

    public void setCompleteDrawable(Drawable completeDrawable) {
        this.mCompleteDrawable = completeDrawable;
    }

    public Drawable getErrorDrawable() {
        return mErrorDrawable;
    }

    public void setErrorDrawable(Drawable errorDrawable) {
        this.mErrorDrawable = errorDrawable;
    }

    public ButtonState getButtonState() {
        return mButtonState;
    }

    public void setButtonState(ButtonState buttonState) {
        this.mButtonState = buttonState;
        switch (buttonState) {
            case NORMAL: {
                setText(mNormalText);
                setBackgroundCompat(mNormalDrawable);
                break;
            }
            case PROCESSING: {
                setText(mProcessText);
                setBackgroundCompat(mProcessBGDrawable);
                break;
            }
            case COMPLETE: {
                setText(mCompleteText);
                setBackgroundCompat(mCompleteDrawable);
                break;
            }
            case ERROR: {
                setText(mErrorText);
                setBackgroundCompat(mErrorDrawable);
                break;
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mButtonState == ButtonState.PROCESSING && mCurrentProgress >= 0) {
            float scale = (float) mCurrentProgress / 100f;
            if (scale > 100) {
                scale = 100f;
            }
            float indicatorHeight = getMeasuredHeight();
            float indicatorWidth = getMeasuredWidth();

            if (mDrawDirection == DRAW_HORIZONTAL) {
                indicatorWidth = (float) getMeasuredWidth() * scale;
            } else {
                indicatorHeight = (float) getMeasuredHeight() * scale;
            }
            mProcessDrawable.setBounds(0, 0, (int) indicatorWidth, (int) indicatorHeight);
            mProcessDrawable.draw(canvas);
        }

        super.onDraw(canvas);
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    public void setBackgroundCompat(Drawable drawable) {
        int pL = getPaddingLeft();
        int pT = getPaddingTop();
        int pR = getPaddingRight();
        int pB = getPaddingBottom();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setBackground(drawable);
        } else {
            setBackgroundDrawable(drawable);
        }
        setPadding(pL, pT, pR, pB);
    }

    public static enum ButtonState {
        NORMAL, PROCESSING, COMPLETE, ERROR
    }

}

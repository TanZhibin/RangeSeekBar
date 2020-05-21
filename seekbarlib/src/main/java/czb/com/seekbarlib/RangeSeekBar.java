package czb.com.seekbarlib;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class RangeSeekBar extends View {
    private final String TAG = RangeSeekBar.class.getSimpleName();
    /**
     * 模式定义
     */
    private final int MODE_SINGLE_BAR = 1;
    private final int MODE_DOUBLE_BAR = 2;


    /**
     * 宽高属性
     */
    private int DEFAULT_WIDTH = 400;
    private int DEFAULT_HEIGHT = 40;
    private final int DEFAULT_RADIUS = 15;


    private int viewWidth;
    private int barWidth;//进度条长度
    private int halfWidth;
    private int viewHeight;

    private int padding = 5;//进度条左右边距

    private int max = 100;//总数
    private float percent = 0;//进度百分比，大于一半为正数，小于一半为负数
    private int progress = 0;//当前进度，默认总数一般

    private int circleX;//圆点中心坐标
    private int circleY;
    private int r = DEFAULT_RADIUS;//圆点半径
    private int thumbColor;//圆点颜色
    private Drawable thumbDrawable;//圆点图片
    private Drawable bgDrawable;


    private boolean showIndicator;//显示中心指示
    /**
     * 绘制相关
     */
    private Paint paint;
    private float strokeWidth = 5;//笔粗细
    private int mainColor = Color.CYAN;
    private int viceColor;


    /**
     * 其他
     */
    private int mode;

    public void setMax(int max) {
        this.max = max;
        invalidate();
    }

    public int getProgress() {
        return progress;
    }

    public RangeSeekBar(Context context) {
        super(context);
    }

    public RangeSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        initAttr(context, attrs);
        paint = new Paint();
        paint.setColor(Color.CYAN);
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(strokeWidth);
        paint.setStrokeCap(Paint.Cap.ROUND);


    }

    private void initAttr(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RangeSeekBar);
        mainColor = typedArray.getColor(R.styleable.RangeSeekBar_rsb_main_color, Color.CYAN);
        thumbColor = typedArray.getColor(R.styleable.RangeSeekBar_rsb_thumb_color, mainColor);
        viceColor = typedArray.getColor(R.styleable.RangeSeekBar_rsb_vice_color, Color.GRAY);
        strokeWidth = typedArray.getDimension(R.styleable.RangeSeekBar_rsb_stroke_width, 5);
        max = typedArray.getInt(R.styleable.RangeSeekBar_rsb_max, 200);
        progress = typedArray.getInt(R.styleable.RangeSeekBar_rsb_progress, 0);
        showIndicator = typedArray.getBoolean(R.styleable.RangeSeekBar_rsb_center_indicator, true);

        if (Math.abs(progress) > max / 2) {
            throw new IllegalArgumentException("The absolute value of progress could not be more than half max");
        }

        r = typedArray.getInt(R.styleable.RangeSeekBar_rsb_radius, DEFAULT_RADIUS);

        int thumbId = typedArray.getResourceId(R.styleable.RangeSeekBar_rsb_thumb, 0);
        if (thumbId != 0) {
            thumbDrawable = getResources().getDrawable(thumbId);

            if (thumbDrawable != null) {
                if (thumbDrawable.getIntrinsicWidth() > 0) {
                    r = thumbDrawable.getIntrinsicWidth() / 2;
                }

                if (thumbDrawable.getIntrinsicHeight() > 0) {
                    DEFAULT_HEIGHT = (int) (thumbDrawable.getIntrinsicHeight() + strokeWidth);
                } else {
                    DEFAULT_HEIGHT = (int) (r * 2 + strokeWidth);
                }

            }
        }

        int bgId = typedArray.getResourceId(R.styleable.RangeSeekBar_rsb_bg_res, 0);
        if (bgId != 0) {
            bgDrawable = getResources().getDrawable(bgId);
        }


        //模式
        mode = typedArray.getInt(R.styleable.RangeSeekBar_rsb_mode, MODE_SINGLE_BAR);

        typedArray.recycle();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        viewWidth = measureDismention(DEFAULT_WIDTH, widthMeasureSpec);
        viewHeight = measureDismention(DEFAULT_HEIGHT, heightMeasureSpec);
        setMeasuredDimension(viewWidth, viewHeight);

        halfWidth = viewWidth / 2;

        barWidth = viewWidth - padding * 2 - r * 2;

        if (mode == MODE_SINGLE_BAR) {
            circleX = (progress) * barWidth / max + r + padding;
        } else if (mode == MODE_DOUBLE_BAR) {
            circleX = (progress + max / 2) * barWidth / max + r + padding;
        }
        circleY = viewHeight / 2;
    }

    private int measureDismention(int defaultSize, int spec) {
        int result = defaultSize;
        int specMode = MeasureSpec.getMode(spec);
        int specSize = MeasureSpec.getSize(spec);
        //1. layout给出了确定的值，比如：100dp
        //2. layout使用的是match_parent，但父控件的size已经可以确定了，比如设置的是具体的值或者match_parent
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else if (specMode == MeasureSpec.AT_MOST) {
            //1. layout使用的是wrap_content
            //2. layout使用的是match_parent,但父控件使用的是确定的值或者wrap_content
            result = Math.min(defaultSize, specSize);
        }

        return result;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        drawBackground(canvas);
        drawProgress(canvas);
        drawThumb(canvas);
    }

    private void drawProgress(Canvas canvas) {
        paint.setColor(mainColor);
        if (mode == MODE_SINGLE_BAR) {
            canvas.drawLine(r + padding, circleY, circleX, circleY, paint);
        } else if (mode == MODE_DOUBLE_BAR) {
            canvas.drawLine(halfWidth, circleY, circleX, circleY, paint);
        }
    }

    private void drawThumb(Canvas canvas) {
        if (showIndicator && mode == MODE_DOUBLE_BAR) {
            paint.setColor(mainColor);
            canvas.drawLine(halfWidth, viewHeight / 2 + 10, halfWidth, viewHeight / 2 - 10, paint);
            paint.setColor(Color.WHITE);
            canvas.drawLine(halfWidth - strokeWidth, viewHeight / 2, halfWidth - strokeWidth - strokeWidth, viewHeight / 2, paint);
            canvas.drawLine(halfWidth + strokeWidth, viewHeight / 2, halfWidth + strokeWidth + strokeWidth, viewHeight / 2, paint);
        }
        if (thumbDrawable != null) {
            if (thumbDrawable.getIntrinsicHeight() > 0) {
                thumbDrawable.setBounds(circleX - r, circleY - thumbDrawable.getIntrinsicHeight() / 2, circleX + r, circleY + thumbDrawable.getIntrinsicHeight() / 2);
            } else {
                thumbDrawable.setBounds(circleX - r, circleY - r, circleX + r, circleY + r);
            }
            thumbDrawable.draw(canvas);
        } else {
            paint.setColor(thumbColor);
            canvas.drawCircle(circleX, circleY, r, paint);
        }

    }


    private void drawBackground(Canvas canvas) {
        if (bgDrawable != null) {
            bgDrawable.setBounds(r + padding, (int) (viewHeight / 2 - strokeWidth), viewWidth - r - padding, (int) (viewHeight / 2 + strokeWidth));
            bgDrawable.draw(canvas);
        } else {
            paint.setColor(viceColor);
            canvas.drawLine(r + padding, circleY, viewWidth - r - padding, circleY, paint);
        }
    }

    private boolean isDrag = false;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                Rect rect = new Rect(circleX - 100, circleY - 100, circleX + 100, circleY + 100);
                int x = (int) event.getX();
                int y = (int) event.getY();
                if (rect.contains(x, y)) {
                    isDrag = true;
                    if (thumbDrawable instanceof StateListDrawable) {
                        StateListDrawable stateListDrawable = (StateListDrawable) thumbDrawable;
                        stateListDrawable.setState(new int[]{android.R.attr.state_pressed});
                        invalidate();
                    }
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                if (isDrag) {
                    procMove(event);

                }
                return true;
            case MotionEvent.ACTION_UP:
                if (isDrag && onProgressListener != null) {
                    onProgressListener.onStopTrackingTouch(progress, percent);
                }
                isDrag = false;
                if (thumbDrawable instanceof StateListDrawable) {
                    StateListDrawable stateListDrawable = (StateListDrawable) thumbDrawable;
                    stateListDrawable.setState(new int[]{});
                    invalidate();
                }
                return true;
        }


        return super.onTouchEvent(event);
    }

    private void procMove(MotionEvent event) {
        int x2 = (int) event.getX();
        circleX = x2;
        if (circleX < (r + padding)) {
            circleX = r + padding;
        }
        if (circleX > viewWidth - r - padding) {
            circleX = viewWidth - r - padding;
        }
//        Log.d(TAG, "circleX:" + circleX + ",circleY:" + circleY);
        invalidate();


        if (onProgressListener != null) {
            calculateProgress(circleX);
            onProgressListener.onProgress(progress, percent);
        }


    }

    /**
     * 计算进度值和百分比
     *
     * @param x
     */
    private void calculateProgress(int x) {
        x = x - padding - r;
        if (mode == MODE_SINGLE_BAR) {
            percent = x * 1.0f / (barWidth);
            progress = (int) (max * percent);
        } else if (mode == MODE_DOUBLE_BAR) {
            percent = (x - barWidth / 2) * 1.0f / (barWidth / 2);
            progress = (int) (max / 2 * percent);
        }

    }

    private OnProgressListener onProgressListener;

    public void setOnProgressListener(OnProgressListener onProgressListener) {
        this.onProgressListener = onProgressListener;
    }

    public interface OnProgressListener {
        void onProgress(int progress, float percent);

        void onStopTrackingTouch(int progress, float percent);

        void onOrigin();
    }
}

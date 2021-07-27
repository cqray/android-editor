package cn.cqray.android.editor;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.StateListDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.Observer;

/**
 * 操作控件
 * @author Cqray
 */
public class EditorMenuView extends AppCompatImageView {

    /** 是否激活 **/
    private boolean mActivated = false;
    /** 激活的颜色，仅对部分Command生效 **/
    private int mActiveColor;
    /** 常规菜单颜色 **/
    private int mMenuColor;
    /** 背景 **/
    private Drawable mBackground;
    /** 对应指令 **/
    private EditorCommand mCommand;
    /** 对应的富文本编辑器 **/
    private RichEditor mRichEditor;
    /** 编辑器样式 **/
    private FormatStyle mCurrentStyle;

    private LifecycleOwner mLifecycleOwner;
    private LifecycleRegistry mLifecycleRegistry;

    public EditorMenuView(Context context) {
        this(context, null);
    }

    public EditorMenuView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EditorMenuView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(@NonNull final Context context, @Nullable AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.EditorMenuView);
        mCommand = EditorCommand.find(ta.getInteger(R.styleable.EditorMenuView_command,
                EditorCommand.FORMAT_CLEAR.getValue()));
        mActiveColor = ta.getColor(R.styleable.EditorMenuView_activeColor,
                ContextCompat.getColor(context, R.color.colorPrimary));
        mMenuColor = ta.getColor(R.styleable.EditorMenuView_menuColor,
                ContextCompat.getColor(context, R.color.tint));
        ta.recycle();
        setScaleType(ScaleType.FIT_CENTER);
        // 设置图像和颜色
        setImageResource(mCommand.getResId());
        setColorFilter(mActivated ? mActiveColor : mMenuColor);
        // 初始化背景
        mBackground = getBackground();
        if (mBackground == null) {
            mBackground = createRippleBackground(context);
            ViewCompat.setBackground(this, mBackground);
        }
        // 初始化生命周期相关
        mLifecycleOwner = new LifecycleOwner() {
            @NonNull
            @Override
            public Lifecycle getLifecycle() {
                return mLifecycleRegistry;
            }
        };
        mLifecycleRegistry = new LifecycleRegistry(mLifecycleOwner);
        // 设置点击事件
        super.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                execute();
            }
        });
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mLifecycleRegistry.setCurrentState(Lifecycle.State.CREATED);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mLifecycleRegistry.setCurrentState(Lifecycle.State.RESUMED);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mLifecycleRegistry.setCurrentState(Lifecycle.State.DESTROYED);
    }

    @Override
    public void setBackground(Drawable background) {
        super.setBackground(background);
        mBackground = background;
    }

    @Override
    public void setBackgroundColor(int color) {
        super.setBackgroundColor(color);
        mBackground = new ColorDrawable(color);
    }

    @Override
    public void setBackgroundDrawable(Drawable background) {
        super.setBackgroundDrawable(background);
        mBackground = background;
    }

    @Override
    public void setBackgroundResource(int resId) {
        mBackground = ContextCompat.getDrawable(getContext(), resId);
        setBackground(mBackground);
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {}

    public void setActiveColor(int color) {
        mActiveColor = color;
        setColorFilter(mActivated ? mActiveColor : mMenuColor);
    }

    public void setMenuColor(int color) {
        mMenuColor = color;
        setColorFilter(mActivated ? mActiveColor : mMenuColor);
    }

    public void setEditorCommand(@NonNull EditorCommand command) {
        mCommand = command;
        setImageResource(command.getResId());
    }

    public void setRichEditor(RichEditor richEditor) {
        mRichEditor = richEditor;
        if (mRichEditor != null) {
            EditorController controller = mRichEditor.getEditorController();
            setColorFilter(mActivated ? mActiveColor : mMenuColor);
            controller.observeStyle(mLifecycleOwner, new Observer<FormatStyle>() {
                @Override
                public void onChanged(FormatStyle style) {
                    mCurrentStyle = style;
                    onStyleChanged(mCommand);
                }
            });
        }
    }

    public void execute() {
        if (mRichEditor != null) {
            mRichEditor.getEditorController().execute(mCommand);
        }
    }

    private void onStyleChanged(@NonNull EditorCommand type) {
        switch (type) {
            case BOLD:
            case ITALIC:
            case UNDERLINE:
            case SUBSCRIPT:
            case SUPERSCRIPT:
            case STRIKETHROUGH:
            case FORMAT_PARA:
            case FORMAT_H1:
            case FORMAT_H2:
            case FORMAT_H3:
            case FORMAT_H4:
            case FORMAT_H5:
            case FORMAT_H6:
            case JUSTIFY_LEFT:
            case JUSTIFY_CENTER:
            case JUSTIFY_RIGHT:
            case JUSTIFY_FULL:
            case ORDERED:
            case UNORDERED:
                mActivated = mCurrentStyle.isActive(mCommand);
                setColorFilter(mActivated ? mActiveColor : mMenuColor);
                break;
            case FONT_COLORS:
                if (isColorsReady()) {
                    setColorFilter(Color.parseColor(mCurrentStyle.getFontForeColor()));
                    Drawable background = createFontColorDrawable(Color.parseColor(mCurrentStyle.getFontBackColor()));
                    ViewCompat.setBackground(EditorMenuView.this, background);
                }
                break;
            case FONT_FORE_COLOR:
                if (isColorsReady()) {
                    setColorFilter(Color.parseColor(mCurrentStyle.getFontForeColor()));
                }
                break;
            case FONT_BACK_COLOR:
                if (isColorsReady()) {
                    setColorFilter(Color.parseColor(mCurrentStyle.getFontBackColor()));
                }
                break;
            default:
                break;
        }
    }


    /**
     * 创建波纹背景
     */
    private Drawable createRippleBackground(@NonNull Context context) {
        TypedArray ta = context.obtainStyledAttributes(new int[] {
                android.R.attr.selectableItemBackground });
        Drawable drawable = ta.getDrawable(0);
        ta.recycle();
        return drawable;
    }

    /**
     * 专门针对FONT_COLOR的Drawable
     */
    @NonNull
    private Drawable createFontColorDrawable(int color) {
        int size = getPaddingTop() * 2;
        float radius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2.5f, getResources().getDisplayMetrics());
        // FONT_COLOR 对应的图像
        GradientDrawable fontDrawable = new GradientDrawable();
        fontDrawable.setColor(color);
        fontDrawable.setCornerRadius(radius);
        fontDrawable.setStroke(size, Color.TRANSPARENT);
        // 状态背景
        int pressed =  android.R.attr.state_pressed;
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{pressed}, new LayerDrawable(new Drawable[]{mBackground, fontDrawable}));
        stateListDrawable.addState(new int[]{0}, fontDrawable);
        return stateListDrawable;
    }

    private boolean isColorsReady() {
        return !TextUtils.isEmpty(mCurrentStyle.getFontForeColor()) &&
                !TextUtils.isEmpty(mCurrentStyle.getFontBackColor());
    }
}

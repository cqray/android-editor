package cn.cqray.android.editor;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;

import java.util.Arrays;
import java.util.List;
import java.util.Vector;

/**
 * 菜单指令栏
 * @author Cqray
 */
public class EditorMenuBar extends LinearLayout {

    private int mViewSize;
    /** 激活的颜色，仅对部分Command生效 **/
    private int mActiveColor;
    /** 常规菜单颜色 **/
    private int mMenuColor;
    private RichEditor mRichEditor;
    private LinearLayout mEditorContainer;
    private LinearLayout mFixedContainer;
    private final Vector<EditorCommand> mFixedCommands = new Vector<>();
    private final Vector<EditorCommand> mEditorCommands = new Vector<>();

    public EditorMenuBar(Context context) {
        this(context, null);
    }

    public EditorMenuBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EditorMenuBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.EditorMenuBar);
        mActiveColor = ta.getColor(R.styleable.EditorMenuBar_activeColor,
                ContextCompat.getColor(context, R.color.colorPrimary));
        mMenuColor = ta.getColor(R.styleable.EditorMenuBar_menuColor,
                ContextCompat.getColor(context, R.color.tint));
        ta.recycle();
        setMinimumHeight(EditorUtils.dp2px(40));
        initMenuBar();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        if (mode != MeasureSpec.EXACTLY) {
            mViewSize = getMeasuredHeight();
        } else {
            mViewSize = MeasureSpec.getSize(heightMeasureSpec);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Activity act = EditorUtils.getActivity(getContext());
        if (act != null) {
            KeyboardUtils.unregisterSoftInputChangedListener(act.getWindow());
        }
    }

    private void initMenuBar() {
        // 初始化布局
        LayoutInflater.from(getContext()).inflate(R.layout.__re_menu_bar, this, true);
        setGravity(Gravity.CENTER_VERTICAL);
        setOrientation(LinearLayout.HORIZONTAL);
        mFixedContainer = findViewById(R.id.__re_fixed);
        mEditorContainer = findViewById(R.id.__re_container);
        // 初始化固定指令
        setFixedCommands(EditorCommand.UNDO);
        // 初始化其他指令
        setEditorCommands(
                EditorCommand.BOLD, EditorCommand.ITALIC, EditorCommand.UNDERLINE, EditorCommand.STRIKETHROUGH,
                EditorCommand.SUBSCRIPT, EditorCommand.SUPERSCRIPT, EditorCommand.FONT_COLORS,
                //EditorCommand.FONT_FORE_COLOR, EditorCommand.FONT_BACK_COLOR,
                EditorCommand.FORMAT_CLEAR,
                EditorCommand.FONT_SIZE,  EditorCommand.LINE_HEIGHT,
                EditorCommand.FORMAT_PARA, EditorCommand.FORMAT_H1, EditorCommand.FORMAT_H2, EditorCommand.FORMAT_H3,
                EditorCommand.FORMAT_H4, EditorCommand.FORMAT_H5, EditorCommand.FORMAT_H6,
                EditorCommand.JUSTIFY_LEFT, EditorCommand.JUSTIFY_RIGHT, EditorCommand.JUSTIFY_CENTER, EditorCommand.JUSTIFY_FULL,
                EditorCommand.ORDERED, EditorCommand.UNORDERED, EditorCommand.INDENT, EditorCommand.OUTDENT,
                EditorCommand.LINK, EditorCommand.TABLE,  EditorCommand.IMAGE,  EditorCommand.DIVIDER,
                EditorCommand.BLOCK_QUOTE, EditorCommand.BLOCK_CODE
                , EditorCommand.CODE_VIEW
        );
        initKeyboardView();
    }

    public void setFixedCommands(EditorCommand... commands) {
        mFixedCommands.clear();
        if (commands != null) {
            mFixedCommands.addAll(Arrays.asList(commands));
        }
        requestFixedCommands();
    }

    public void setFixedCommands(List<EditorCommand> commands) {
        mFixedCommands.clear();
        if (commands != null) {
            mFixedCommands.addAll(commands);
        }
        requestFixedCommands();
    }

    public void setEditorCommands(EditorCommand... commands) {
        mEditorCommands.clear();
        if (commands != null) {
            mEditorCommands.addAll(Arrays.asList(commands));
        }
        requestEditorCommands();
    }

    public void setEditorCommands(List<EditorCommand> commands) {
        mEditorCommands.clear();
        if (commands != null) {
            mEditorCommands.addAll(commands);
        }
        requestEditorCommands();
    }

    public void setRichEditor(RichEditor richEditor) {
        mRichEditor = richEditor;
        requestCommands(new Observer<EditorMenuView>() {
            @Override
            public void onChanged(EditorMenuView editorMenuView) {
                editorMenuView.setRichEditor(mRichEditor);
            }
        });
    }

    public void setActiveColor(final int color) {
        mActiveColor = color;
        requestCommands(new Observer<EditorMenuView>() {
            @Override
            public void onChanged(EditorMenuView editorMenuView) {
                editorMenuView.setActiveColor(color);
            }
        });
    }

    public void setMenuColor(final int color) {
        mMenuColor = color;
        final ImageView iv = (ImageView) mFixedContainer.getChildAt(0);
        iv.setColorFilter(mMenuColor);
        requestCommands(new Observer<EditorMenuView>() {
            @Override
            public void onChanged(EditorMenuView editorMenuView) {
                editorMenuView.setMenuColor(color);
            }
        });
    }

    /**
     * 初始化键盘
     */
    private void initKeyboardView() {
        final Activity act = EditorUtils.getActivity(getContext());
        if (act == null) {
            return;
        }
        // 设置按钮键盘图片及颜色
        final ImageView iv = (ImageView) mFixedContainer.getChildAt(0);
        iv.setImageResource(R.drawable.__re_ic_keyboard_show);
        iv.setColorFilter(mMenuColor);
        //  键盘按钮点击事件
        iv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (KeyboardUtils.isSoftInputVisible(act)) {
                    KeyboardUtils.hideSoftInput(act);
                } else {
                    KeyboardUtils.showSoftInput(act);
                }
            }
        });
        // 键盘显示隐藏监听事件
        KeyboardUtils.registerSoftInputChangedListener(act.getWindow(), new KeyboardUtils.OnSoftInputChangedListener() {
            @Override
            public void onSoftInputChanged(int height) {
                iv.setImageResource(height == 0 ?
                        R.drawable.__re_ic_keyboard_show :
                        R.drawable.__re_ic_keyboard_hide);
            }
        });
    }

    /**
     * 请求固定指令
     */
    private void requestFixedCommands() {
        post(new Runnable() {
            @Override
            public void run() {
                View first = mFixedContainer.getChildAt(0);
                requestCommands(mFixedContainer, mFixedCommands);
                mFixedContainer.addView(first, 0);
                int p = mViewSize / 4;
                first.setPadding(p, p, p, p);
            }
        });
    }

    /**
     * 请求常规指令
     */
    private void requestEditorCommands() {
        post(new Runnable() {
            @Override
            public void run() {
                requestCommands(mEditorContainer, mEditorCommands);
            }
        });
    }

    /**
     * 请求指令
     * @param container 容器
     * @param commands 指令
     */
    private synchronized void requestCommands(@NonNull final LinearLayout container, @NonNull final List<EditorCommand> commands) {
        container.removeAllViews();
        for (EditorCommand item : commands) {
            int p = mViewSize / 4;
            final EditorMenuView view = new EditorMenuView(getContext());
            view.setLayoutParams(new ViewGroup.LayoutParams(mViewSize, -1));
            view.setPadding(p, p, p, p);
            view.setEditorCommand(item);
            view.setRichEditor(mRichEditor);
            view.setActiveColor(mActiveColor);
            view.setMenuColor(mMenuColor);
            container.addView(view);
        }
    }

    private synchronized void requestCommands(final Observer<EditorMenuView> observer) {
        post(new Runnable() {
            @Override
            public void run() {
                // 固定指令
                for (int i = 0; i < mFixedContainer.getChildCount(); i++) {
                    View view = mFixedContainer.getChildAt(i);
                    if (view instanceof EditorMenuView) {
                        observer.onChanged((EditorMenuView) view);
                    }
                }
                // 常规指令
                for (int i = 0; i < mEditorContainer.getChildCount(); i++) {
                    EditorMenuView view = (EditorMenuView) mEditorContainer.getChildAt(i);
                    observer.onChanged(view);
                }
            }
        });
    }

}

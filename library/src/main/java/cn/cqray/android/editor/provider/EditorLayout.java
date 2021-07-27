package cn.cqray.android.editor.provider;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 富文本编辑器基础容器
 * @author Cqray
 */
abstract class EditorLayout extends FrameLayout {

    public EditorLayout(@NonNull Context context) {
        this(context, null);
    }

    public EditorLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = onCreateView();
        addView(view);
    }

    /**
     * 初始化内部界面
     * @return 返回的界面
     */
    @NonNull
    public abstract View onCreateView();
}

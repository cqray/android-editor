package cn.cqray.android.editor.provider;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 富文本编辑器标题栏
 * @author Cqray
 */
public abstract class EditorToolbar extends EditorLayout {

    public EditorToolbar(@NonNull Context context) {
        this(context, null);
    }

    public EditorToolbar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 初始化内部界面
     * @return 返回的界面
     */
    @Override
    @NonNull
    public abstract View onCreateView();

    /**
     * 获取Nav控件
     * @return Nav控件
     */
    @NonNull
    public abstract View getNavView();

    /**
     * 获取标题控件
     * @return 标题控件
     */
    @NonNull
    public abstract TextView getTitleView();

}

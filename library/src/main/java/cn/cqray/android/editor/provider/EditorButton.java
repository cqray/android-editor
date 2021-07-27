package cn.cqray.android.editor.provider;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 富文本编辑器相关按钮
 * @author Cqray
 */
public abstract class EditorButton extends EditorLayout {

    public EditorButton(@NonNull Context context) {
        super(context);
    }

    public EditorButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 获取按钮
     * @return 按钮
     */
    public abstract TextView getButton();
}

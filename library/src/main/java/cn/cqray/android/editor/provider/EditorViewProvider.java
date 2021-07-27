package cn.cqray.android.editor.provider;

import android.content.Context;

import java.io.Serializable;

/**
 * 富文本编辑器控件提供器
 * @author Cqray
 */
public interface EditorViewProvider extends Serializable {

    /**
     * 获取富文本编辑器相关标题栏
     * @param context 上下文你
     * @return 标题栏
     */
    EditorToolbar getToolbar(Context context);

    /**
     * 获取富文本编辑器相关Button
     * @param context 上下文
     * @return button
     */
    EditorButton getButton(Context context);
}

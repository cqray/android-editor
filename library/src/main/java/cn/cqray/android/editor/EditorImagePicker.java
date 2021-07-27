package cn.cqray.android.editor;

import android.content.Context;

/**
 * 富文本图片选择器
 * @author Cqray
 */
public interface EditorImagePicker {

    /**
     * 选择图片
     * @param context 上下文
     * @param editorController 富文本控制器
     */
    void pick(Context context, EditorController editorController);
}

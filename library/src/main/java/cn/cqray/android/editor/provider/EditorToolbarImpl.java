package cn.cqray.android.editor.provider;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import cn.cqray.android.editor.R;

/**
 * 富文本编辑器标题栏实现
 * @author Cqray
 */
public class EditorToolbarImpl extends EditorToolbar {

    public EditorToolbarImpl(@NonNull Context context) {
        super(context);
    }

    public EditorToolbarImpl(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @NonNull
    @Override
    public View onCreateView() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        return inflater.inflate(R.layout.__re_toolbar_default, this, false);
    }

    @NonNull
    @Override
    public View getNavView() {
        return findViewById(R.id.re_nav);
    }

    @NonNull
    @Override
    public TextView getTitleView() {
        return findViewById(R.id.re_title);
    }
}

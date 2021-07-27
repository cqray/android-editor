package cn.cqray.android.editor.provider;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;

import cn.cqray.android.editor.R;

/**
 * 富文本编辑器相关按钮实现
 * @author Cqray
 */
public class EditorButtonImpl extends EditorButton {

    private Button mButton;

    public EditorButtonImpl(@NonNull Context context) {
        super(context, null);
        setClipChildren(false);
        setClipToPadding(false);
    }

    @Override
    public TextView getButton() {
        return mButton;
    }

    @NonNull
    @Override
    public View onCreateView() {
        GradientDrawable background = new GradientDrawable();
        background.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        background.setCornerRadius(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics()));
        mButton = new AppCompatButton(getContext());
        mButton.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        mButton.setTextColor(ContextCompat.getColor(getContext(), R.color.foreground));
        mButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.h3));
        ViewCompat.setBackground(mButton, background);
        return mButton;
    }
}

package cn.cqray.android.editor.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.core.widget.TextViewCompat;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorChangedListener;
import com.flask.colorpicker.slider.LightnessSlider;

import cn.cqray.android.editor.EditorPickStrategy;
import cn.cqray.android.editor.R;
import cn.cqray.android.editor.provider.EditorButton;

/**
 * 自定义颜色选择Fragment
 * @author Cqray
 */
public class EditorColorFragment extends BaseFragment {

    /** 是否是竖屏 **/
    private boolean mPortrait;
    /** 选择的颜色 **/
    private String mSelectColor;
    /** 颜色显示控件 **/
    private View mColorView;
    /** 输入框 **/
    private EditText mInput;
    /** 颜色选择器 **/
    private ColorPickerView mColorPicker;

    @Override
    public void onCreating(@Nullable Bundle savedInstanceState) {
        super.onCreating(savedInstanceState);
        mPortrait = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
        setContentView(mPortrait ? R.layout.__re_editor_palette_v : R.layout.__re_editor_palette_h);
        setTitleResource(R.string.__re_title_custom_color);
        checkOwner(EditorColorActivity.class);
        // 初始化选中的颜色
        Intent intent = requireActivity().getIntent();
        mSelectColor = intent.getStringExtra("color");
        mSelectColor = mSelectColor == null ? ColorUtils.getResColorStr(
                requireContext(), R.color.colorPrimary) : mSelectColor;
        // 初始化卡片间距
        EditorPickStrategy strategy = getEditorStrategy();
        int p = strategy.getSpaceSize();
        setCardPadding(p, p, p, p);

        mColorView = findViewById(R.id.__re_color_view1);
        // 初始化输入框
        initInput();
        // 初始化选择器
        initPicker();
        // 初始化提交按钮
        initSubmit();
    }

    private void initInput() {
        final String suffix = "#";
        mInput = findViewById(R.id.__re_input1);
        mInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() < 1) {
                    // 设置#不能被删除
                    mInput.setText(suffix);
                    mInput.setSelection(1);
                } else if (!s.toString().startsWith(suffix)) {
                    // 不是以#开头则重新拼接字符串
                    String[] tmp = s.toString().split(suffix);
                    mInput.setText(suffix);
                    mInput.append(tmp.length > 1 ? tmp[1] : "");
                    mInput.append(tmp[0]);
                }

                if (start == 0) {
                    // 开始位置是0，则光标跳转到最后的位置
                    mInput.setSelection(s.length());
                }
                // 获取颜色
                mSelectColor = s.length() == 7 ? s.toString() : null;
                ColorUtils.setViewBackground(mColorView, mSelectColor);
                // 更新选择器的颜色
                if (mColorPicker != null && mSelectColor != null) {
                    mColorPicker.setColor(Color.parseColor(mSelectColor), true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
        mInput.setText(mSelectColor);
        TextViewCompat.setTextAppearance(mInput, getEditorStrategy().getTextAppearance());
        // 设置背景
        int color = ContextCompat.getColor(requireContext(), R.color.colorAccent);
        int size = getResources().getDimensionPixelSize(R.dimen.divider);
        GradientDrawable background = new GradientDrawable();
        background.setStroke(size, color);
        ViewCompat.setBackground(mInput, background);
        // 设置间隔
        int margin = getEditorStrategy().getSpaceSize();
        ViewGroup.MarginLayoutParams params;
        if (mPortrait) {
            params = (ViewGroup.MarginLayoutParams) mInput.getLayoutParams();
            params.setMargins(margin, 0, 0, margin);
        } else {
            params = (ViewGroup.MarginLayoutParams) mInput.getLayoutParams();
            params.setMargins(0, margin / 2, 0, margin);
        }

    }

    /**
     * 颜色选择器
     */
    private void initPicker() {
        final EditorPickStrategy strategy = getEditorStrategy();
        final ColorPickerView picker = findViewById(R.id.__re_color_picker);
        final LightnessSlider slider = findViewById(R.id.__re_slider);
        mColorPicker = picker;
        // 颜色变化监听
        mColorPicker.addOnColorChangedListener(new OnColorChangedListener() {
            @Override
            public void onColorChanged(int selectedColor) {
                mSelectColor = ColorUtils.covertColor(selectedColor);
                mInput.setText(mSelectColor);
                ColorUtils.setViewBackground(mColorView, mSelectColor);
            }
        });
        // 设置初始化颜色
        mColorPicker.setInitialColor(Color.parseColor(mSelectColor), true);
        mColorPicker.postDelayed(new Runnable() {
            @Override
            public void run() {
                mColorPicker.setColor(Color.parseColor(mSelectColor), true);
            }
        }, 10);
        // 设置横屏相关属性
        if (!mPortrait) {
            // 设置右边控件的宽度
            ViewGroup parent = (ViewGroup) slider.getParent();
            parent.getLayoutParams().width = (int) (Resources.getSystem().getDisplayMetrics().widthPixels / 4f);
            // 设置间隔
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) picker.getLayoutParams();
            params.rightMargin = strategy.getSpaceSize();
            picker.setLayoutParams(params);
        }
    }

    /**
     * 初始化提交按钮
     */
    private void initSubmit() {
        EditorPickStrategy strategy = getEditorStrategy();
        EditorButton button = strategy.getViewProvider().getButton(requireContext());
        // 初始化按钮
        button.getButton().setText(R.string.__re_submit);
        button.getButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (EditorColorActivity.sPickCallback != null) {
                    EditorColorActivity.sPickCallback.onCall(mSelectColor == null ? null : mSelectColor.toUpperCase(), null);
                    requireActivity().finish();
                }
            }
        });
        ViewGroup root;
        if (mPortrait) {
            root = (ViewGroup) mColorPicker.getParent();
        } else {
            root = (ViewGroup) mInput.getParent();
        }
        root.addView(button);
    }
}

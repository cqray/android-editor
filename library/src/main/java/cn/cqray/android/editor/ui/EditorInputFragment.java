package cn.cqray.android.editor.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.TextViewCompat;

import java.util.Locale;

import cn.cqray.android.editor.R;
import cn.cqray.android.editor.provider.EditorButton;

/**
 * 富文本编辑器输入类Fragment
 * @author Cqray
 */
public class EditorInputFragment extends BaseFragment {

    private int mCount;
    private boolean mLink;
    private final TextView[] mCounts = new TextView[2];
    private final EditText[] mInputs = new EditText[2];

    @NonNull
    public static EditorInputFragment newInstance(boolean link) {
        Bundle arguments = new Bundle();
        arguments.putBoolean("link", link);
        EditorInputFragment fragment = new EditorInputFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreating(@Nullable Bundle savedInstanceState) {
        super.onCreating(savedInstanceState);
        setContentView(R.layout.__re_editor_input);
        checkOwner(EditorPickActivity.class);
        initViews();
        initInputs();
        initCounts();
        initSubmit();
    }

    private void initViews() {
        assert getArguments() != null;
        mLink = getArguments().getBoolean("link");
        mCount = mLink ? 125 : 2;
        mCounts[0] = findViewById(R.id.__re_count1);
        mCounts[1] = findViewById(R.id.__re_count2);
        mInputs[0] = findViewById(R.id.__re_input1);
        mInputs[1] = findViewById(R.id.__re_input2);
        // 设置标题，插入链接、插入表格
        setTitleResource(mLink ? R.string.__re_title_link : R.string.__re_title_table);
        // 设置卡片内部间隔
        int padding = getEditorStrategy().getSpaceSize();
        setCardPadding(padding, (int) (padding * 1.5f), padding, padding);
    }

    private void initInputs() {
        int[][] hintIds = new int[][]{
                {R.string.__re_link_name, R.string.__re_table_cols},
                {R.string.__re_link_url, R.string.__re_table_rows},
        };
        for (int i = 0; i < mInputs.length; i++) {
            // 设置文字样式
            TextViewCompat.setTextAppearance(mInputs[i], getEditorStrategy().getTextAppearance());
            // 设置提示
            mInputs[i].setHint(hintIds[i][mLink ? 0 : 1]);
            int p = (int) (mInputs[i].getTextSize());
            int h = p * 3;
            // 设置最大长度
            mInputs[i].setFilters(new InputFilter[]{new InputFilter.LengthFilter(mCount)});
            mInputs[i].setInputType(mLink ? EditorInfo.TYPE_CLASS_TEXT : EditorInfo.TYPE_CLASS_NUMBER);
            // 设置间隔和高度
            mInputs[i].setPadding(p, 0, p, 0);
            mInputs[i].getLayoutParams().height = h;
            mInputs[i].requestLayout();
            final int finalI = i;
            mInputs[i].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    int len = s.length();
                    mCounts[finalI].setText(String.format(Locale.getDefault(), "%d/%d", len, mCount));

                }

                @Override
                public void afterTextChanged(Editable s) {}
            });
            mInputs[i].setText("");
        }
    }


    /**
     * 初始化计数器
     */
    private void initCounts() {
        for (int i = 0; i < mCounts.length; i++) {
            // 设置文字样式
            TextViewCompat.setTextAppearance(mCounts[i], getEditorStrategy().getHintAppearance());
            int p = mInputs[i].getPaddingRight();
            mCounts[i].setPadding(0, 0, p, 0);
        }
    }


    /**
     * 初始化提交按钮
     */
    private void initSubmit() {
        ViewGroup parent = (ViewGroup) mInputs[0].getParent().getParent();
        EditorButton button = getEditorStrategy().getViewProvider().getButton(getContext());
        button.getButton().setText(R.string.__re_submit);
        button.getButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
        parent.addView(button);
    }

    /**
     * 提交输入
     */
    private void submit() {
        String v1 = mInputs[0].getText().toString();
        String v2 = mInputs[1].getText().toString();
        // 清空错误信息
        mInputs[0].setError(null);
        mInputs[1].setError(null);
        // 空字符
        String emptyStr = requireContext().getString(R.string.__re_not_empty);
        // 判空
        if (TextUtils.isEmpty(v1)) {
            mInputs[0].requestFocus();
            mInputs[0].setError(mInputs[0].getHint() + emptyStr);
            return;
        }
        // 判空
        if (TextUtils.isEmpty(v2)) {
            mInputs[1].requestFocus();
            mInputs[1].setError(mInputs[1].getHint() + emptyStr);
            return;
        }
        if (EditorPickActivity.sInnerCallback != null) {
            EditorPickActivity.sInnerCallback.onCall(v1, v2);
        }
        requireActivity().finish();
    }
}

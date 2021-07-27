package cn.cqray.android.editor.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import cn.cqray.android.editor.R;

/**
 * 富文本编辑器列表类型Fragment
 * @author Cqray
 */
public class EditorNumberFragment extends BaseFragment {

    static final float PADDING_SCALE = 1.4f;

    @NonNull
    public static EditorNumberFragment newInstance(Number cur, ArrayList<Number> data, boolean fontSize) {
        Bundle arguments = new Bundle();
        arguments.putSerializable("cur", cur);
        arguments.putSerializable("data", data);
        arguments.putBoolean("fontSize", fontSize);
        EditorNumberFragment fragment = new EditorNumberFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onCreating(@Nullable Bundle savedInstanceState) {
        super.onCreating(savedInstanceState);
        setContentView(R.layout.__re_editor_number);
        checkOwner(EditorPickActivity.class);

        // 设置卡片内部间隔
        int p = (int) (getEditorStrategy().getSpaceSize() / PADDING_SCALE);
        setCardPadding(p, p, p, p);

        // 获取传参
        assert getArguments() != null;
        Number cur = (Number) getArguments().getSerializable("cur");
        ArrayList<Number> data = (ArrayList<Number>) getArguments().getSerializable("data");
        data = data == null ? new ArrayList<Number>() : data;
        boolean fontSize = !(cur instanceof Float) && !(cur instanceof Double);
        // 设置标题
        setTitleResource(fontSize ? R.string.__re_title_font : R.string.__re_title_line_height);
        // 初始化数据
        int position = 0;
        for (int i = 0; i < data.size(); i++) {
            if (isNumberEqual(cur, data.get(i))) {
                position = i;
            }
        }
        // 初始化列表
        RecyclerView rv = findViewById(R.id.__re_recycler1);
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        EditorNumberAdapter adapter = new EditorNumberAdapter(getEditorStrategy(), data, position);
        rv.setAdapter(adapter);
        // 滚动到指定项
        rv.scrollToPosition(position);
        // 监听点击事件
        adapter.observeSelected(this, new Observer<Number>() {
            @Override
            public void onChanged(Number number) {
                if (EditorPickActivity.sInnerCallback != null) {
                    EditorPickActivity.sInnerCallback.onCall(String.valueOf(number), null);
                }
                requireActivity().finish();
            }
        });
    }

    private boolean isNumberEqual(Number n1, Number n2) {
        if (n1 instanceof Float || n2 instanceof Double) {
            float v1 = n1.floatValue();
            float v2 = n2.floatValue();
            return Math.abs(v1 - v2) <= 0.0001;
        }
        return n1.intValue() == n2.intValue();
    }
}

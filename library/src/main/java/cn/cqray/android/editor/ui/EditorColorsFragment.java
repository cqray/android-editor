package cn.cqray.android.editor.ui;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.TextViewCompat;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.List;

import cn.cqray.android.editor.EditorPickStrategy;
import cn.cqray.android.editor.R;
import cn.cqray.android.editor.provider.EditorButton;
import cn.cqray.android.editor.provider.EditorButtonImpl;

/**
 * 富文本编辑器文字颜色Fragment
 * @author Cqray
 */
public class EditorColorsFragment extends BaseFragment {

    @NonNull
    public static EditorColorsFragment newInstance(String foreColor, String backColor) {
        Bundle arguments = new Bundle();
        arguments.putString("foreColor", foreColor);
        arguments.putString("backColor", backColor);
        EditorColorsFragment fragment = new EditorColorsFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    /** 显示颜色控件宽度 **/
    static final int COLOR_VIEW_WIDTH = 80;

    private List<String> mColorList = Arrays.asList(
            "#000000", "#424242", "#636363", "#9C9C94", "#CEC6CE", "#EFEFEF", "#F7F7F7", "#FFFFFF",
            "#FF0000", "#FF9C00", "#FFFF00", "#00FF00", "#00FFFF", "#0000FF", "#9C00FF", "#FF00FF",
            "#F7C6CE", "#FFE7CE", "#FFEFC6", "#D6EFD6", "#CEDEE7", "#CEE7F7", "#D6D6E7", "#E7D6DE",
            "#E79C9C", "#FFC69C", "#FFE79C", "#B5D6A5", "#A5C6CE", "#9CC6EF", "#B5A5D6", "#D6A5BD",
            "#E76363", "#F7AD6B", "#FFD663", "#94BD7B", "#73A5AD", "#6BADDE", "#8C7BC6", "#C67BA5",
            "#CE0000", "#E79439", "#EFC631", "#6BA54A", "#4A7B8C", "#3984C6", "#634AA5", "#A54A7B",
            "#9C0000", "#B56308", "#BD9400", "#397B21", "#104A5A", "#085294", "#311873", "#731842",
            "#630000", "#7B3900", "#846300", "#295218", "#083139", "#003163", "#21104A", "#333333");

    private int mRecyclerSize;
    private boolean mPortrait;
    private LinearLayout mRoot;
    private RecyclerView mRecycler1;
    private RecyclerView mRecycler2;

    /** 色值 **/
    private final String[] mColors = new String[2];
    /** 颜色显示控件 **/
    private final View[] mColorViews = new View[2];
    /** 色值显示控件 **/
    private final TextView[] mColorTitles = new TextView[2];
    /** 标题 **/
    private final TextView[] mColorHints = new TextView[2];
    /** 适配器 **/
    private final EditorColorsAdapter[] mAdapters = new EditorColorsAdapter[2];

    @Override
    public void onCreating(@Nullable Bundle savedInstanceState) {
        super.onCreating(savedInstanceState);
        mPortrait = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
        setContentView(mPortrait ? R.layout.__re_editor_colors_v : R.layout.__re_editor_colors_h);
        setTitleResource(R.string.__re_title_font_color);
        checkOwner(EditorPickActivity.class);
        assert getArguments() != null;
        mColors[0] = getArguments().getString("foreColor");
        mColors[1] = getArguments().getString("backColor");
        mColorList.set(mColorList.size() - 1, ColorUtils.getResColorStr(requireContext(), R.color.text));
        initViews();
    }

    /**
     * 初始化控件
     */
    private void initViews() {
        // 初始化卡片间距
        EditorPickStrategy strategy = getEditorStrategy();
        int p = strategy.getSpaceSize();
        setCardPadding(p, p, p, p);
        // 初始化控件
        mRoot = findViewById(R.id.__re_root);
        mRoot.setVisibility(View.GONE);
        mRecycler1 = findViewById(R.id.__re_recycler1);
        mRecycler2 = findViewById(R.id.__re_recycler2);
        // 设置容器间隔
        ViewGroup.MarginLayoutParams params;
        params = (ViewGroup.MarginLayoutParams) ((FrameLayout) mRecycler1.getParent()).getLayoutParams();
        params.setMargins(0, 0, mPortrait ? 0 : p, 0);
        params = (ViewGroup.MarginLayoutParams) ((FrameLayout) mRecycler2.getParent()).getLayoutParams();
        params.setMargins(mPortrait ? 0 : p, mPortrait ? p : 0, mPortrait ? 0 : p, mPortrait ? p : 0);
        // 初始化其他控件
        initColorViews();
        initSubmitView();
        // 延时设置界面
        mRoot.postDelayed(new Runnable() {
            @Override
            public void run() {
                initPaletteSize();
                initFontColorAdapters();
                mRoot.setVisibility(View.VISIBLE);
                mRecycler1.setLayoutManager(new GridLayoutManager(requireContext(), 8,
                        mPortrait ? RecyclerView.HORIZONTAL : RecyclerView.VERTICAL, false));
                mRecycler1.setAdapter(mAdapters[0]);
                mRecycler2.setLayoutManager(new GridLayoutManager(requireContext(), 8,
                        mPortrait ? RecyclerView.HORIZONTAL : RecyclerView.VERTICAL, false));
                mRecycler2.setAdapter(mAdapters[1]);
            }
        }, 10);
    }

    /**
     * 初始化调色板控件大小
     */
    private void initPaletteSize() {
        EditorPickStrategy strategy = getEditorStrategy();
        DisplayMetrics dm = getResources().getDisplayMetrics();

        // 显示控件宽度
        int cvWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                COLOR_VIEW_WIDTH, getResources().getDisplayMetrics());
        if (mPortrait) {
            // 最大可用宽度 = 屏幕宽度-3个标准间隔-2个cardMargin-1个显示控件宽度
            int maxWidth = dm.widthPixels - 3 * strategy.getSpaceSize() - 2 * strategy.getCardMargin() - cvWidth;
            // 最大可用高度 = 屏幕高度-2个标准间隔-2个cardMargin-状态栏高度
            int maxHeight = dm.heightPixels - 2 * strategy.getSpaceSize() - 2 * strategy.getCardMargin() - mTitleView.getHeight();
            // 取两者最小
            mRecyclerSize = (int) Math.min(maxWidth, (maxHeight) / 2f);
        } else {
            // 最大可用宽度 = 屏幕宽度-6个标准间隔-2个cardMargin-2个显示控件宽度-屏幕2/25宽度的控件
            int maxWidth = (int) (dm.widthPixels * 0.92f - 6 * strategy.getSpaceSize() - 2 * strategy.getCardMargin() - 2 * cvWidth);
            // 最大可用高度 = 屏幕高度-2个标准间隔-2个cardMargin -状态栏高度
            int maxHeight = dm.heightPixels - 2 * strategy.getSpaceSize() - 2 * strategy.getCardMargin() - mTitleView.getHeight();
            // 取两者最小
            mRecyclerSize = (int) Math.min(maxWidth / 2f, maxHeight);
        }
    }

    private void initColorViews() {
        mColorViews[0] = findViewById(R.id.__re_color_view1);
        mColorViews[1] = findViewById(R.id.__re_color_view2);
        mColorHints[0] = findViewById(R.id.__re_color_hint1);
        mColorHints[1] = findViewById(R.id.__re_color_hint2);
        mColorTitles[0] = findViewById(R.id.__re_color_title1);
        mColorTitles[1] = findViewById(R.id.__re_color_title2);
        // 获取配置策略
        EditorPickStrategy strategy = getEditorStrategy();
        // 初始化按钮
        int buttonHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 35, getResources().getDisplayMetrics());
        // 文本样式
        int textAppearance = getEditorStrategy().getTextAppearance();

        for (int i = 0; i < mColorViews.length; i++) {
            // 设置颜色面板
            ColorUtils.setViewBackground(mColorViews[i], mColors[i]);
            // 设置颜色文字
            mColorHints[i].setText(mColors[i] == null ? "--" : mColors[i]);
            // 添加按钮
            ViewGroup parent = (ViewGroup) mColorViews[i].getParent();
            EditorButton eb = strategy.getViewProvider().getButton(requireContext());
            eb.setLayoutParams(new ViewGroup.LayoutParams(-1, buttonHeight));
            TextView btn = eb.getButton();
            btn.setText(R.string.__re_custom);
            btn.setPadding(0, 0, 0, 0);
            if (eb instanceof EditorButtonImpl) {
                btn.setTextSize(TypedValue.COMPLEX_UNIT_PX, btn.getTextSize() * 0.9f);
            }
            btn.setId(i);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditorColorsFragment.this.onClick(v);
                }
            });
            parent.addView(eb);
            // 设置文本样式
            TextViewCompat.setTextAppearance(mColorHints[i], textAppearance);
            TextViewCompat.setTextAppearance(mColorTitles[i], textAppearance);
            // 设置间隔
            int m = getEditorStrategy().getSpaceSize() / 2;
            ViewGroup.MarginLayoutParams params;
            // 右边颜色提示
            params = (ViewGroup.MarginLayoutParams) mColorViews[i].getLayoutParams();
            params.setMargins(0, m, 0, m);
            // 右边颜色文字提示
            params = (ViewGroup.MarginLayoutParams) mColorHints[i].getLayoutParams();
            params.setMargins(0, 0, 0, m);
        }
    }

    /**
     * 初始化提交控件
     */
    private void initSubmitView() {
        EditorPickStrategy strategy = getEditorStrategy();
        EditorButton eb = strategy.getViewProvider().getButton(requireContext());
        if (mPortrait) {
            int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48, getResources().getDisplayMetrics());
            eb.setLayoutParams(new ViewGroup.LayoutParams(-1, height));
        } else {
            int margin = strategy.getSpaceSize();
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, -1);
            params.weight = 1;
            params.setMargins(margin, 0, 0, 0);
            eb.setLayoutParams(params);
        }
        eb.getButton().setText(R.string.__re_submit);
        eb.getButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditorColorsFragment.this.onClick(v);
            }
        });
        mRoot.addView(eb);
    }

    /**
     * 初始化适配器
     */
    private void initFontColorAdapters() {
        int itemSize = mRecyclerSize / 8;
        assert getArguments() != null;
        mAdapters[0] = new EditorColorsAdapter(mColorList, mColors[0], itemSize);
        mAdapters[1] = new EditorColorsAdapter(mColorList, mColors[1], itemSize);
        mAdapters[1].setCancelEnable(true);

        mAdapters[0].observeSelectColor(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                mColors[0] = s;
                setColorChanged(0);
            }
        });

        mAdapters[1].observeSelectColor(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                mColors[1] = s;
                setColorChanged(1);
            }
        });
    }

    /**
     * 设置颜色变化
     */
    private void setColorChanged(int index) {
        mColorHints[index].setText(mColors[index].toUpperCase());
        ColorUtils.setViewBackground(mColorViews[index], mColors[index]);
    }

    /**
     * 开启自定义颜色界面
     */
    private void startPalette(final int index) {
        EditorColorActivity.start(requireContext(), getEditorStrategy(), mColors[index], new EditorPickCallback() {
            @Override
            public void onCall(String v1, String v2) {
                mColors[index] = v1;
                mAdapters[index].setSelectColor(mColors[index]);
            }
        });
    }

    /**
     * 点击事件
     */
    private void onClick(@NonNull View view) {
        // 获取ID
        int id = view.getId();
        if (id > View.NO_ID) {
            startPalette(id);
        } else {
            if (EditorPickActivity.sInnerCallback != null) {
                EditorPickActivity.sInnerCallback.onCall(mColors[0], mColors[1]);
            }
            requireActivity().finish();
        }
    }
}

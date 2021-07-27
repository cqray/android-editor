package cn.cqray.android.editor.ui;

import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;

import cn.cqray.android.editor.EditorPickStrategy;
import cn.cqray.android.editor.R;
import cn.cqray.android.editor.provider.EditorToolbar;

/**
 * 基础Fragment
 * @author Cqray
 */
class BaseFragment extends Fragment {

    protected TextView mTitleView;
    private View mCardView;
    private View mContentView;
    private EditorPickStrategy mStrategy;

    @Nullable
    @Override
    public final View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mStrategy = (EditorPickStrategy) requireActivity().getIntent().getSerializableExtra("strategy");
        onCreating(savedInstanceState);
        return mContentView;
    }

    public final void setContentView(@LayoutRes int resId) {
        // Activity容器
        ViewGroup container = requireActivity().findViewById(android.R.id.content);
        // 添加默认布局
        mContentView = LayoutInflater.from(requireContext()).inflate(R.layout.__re_editor_base, container, false);
        // 设置背景颜色
        mContentView.setBackgroundColor(mStrategy.getBackgroundColor());
        // 内容容器
        ViewStub stub = mContentView.findViewById(R.id.__re_stub);
        stub.setLayoutResource(resId);
        mCardView = stub.inflate();
        // 设置内容容器的背景和间隔
        GradientDrawable cardBackground = new GradientDrawable();
        cardBackground.setColor(mStrategy.getCardColor());
        cardBackground.setCornerRadius(mStrategy.getCardCorner());
        ViewCompat.setBackground(mCardView, cardBackground);
        int margin = mStrategy.getCardMargin();
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mCardView.getLayoutParams();
        params.setMargins(margin, margin, margin, margin);
        // 初始化标题
        initToolbar();
    }

    public void onCreating(@Nullable Bundle savedInstanceState) {

    }

    public EditorPickStrategy getEditorStrategy() {
        return mStrategy;
    }

    protected void setTitleResource(@StringRes int resId) {
        if (mTitleView != null) {
            mTitleView.setText(resId);
        }
    }

    protected void setCardPadding(int l, int t, int r, int b) {
        mCardView.setPadding(l, t, r, b);
    }

    protected <T extends View> T findViewById(@IdRes int resId) {
        return mContentView.findViewById(resId);
    }

    protected boolean onNavClick() {
        return false;
    }

    protected void checkOwner(Class<? extends Activity> ownerClass) {
        Activity act = requireActivity();
        Class<?> actClass = act.getClass();
        if (actClass != ownerClass) {
            throw new RuntimeException(getClass().getSimpleName() + " must be used in " + ownerClass.getSimpleName() + ".");
        }
    }

    private void initToolbar() {
        EditorToolbar toolbar = mStrategy.getViewProvider().getToolbar(requireContext());
        toolbar.getNavView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!onNavClick()) {
                    requireActivity().finish();
                }
            }
        });
        mTitleView = toolbar.getTitleView();
        FrameLayout layout = mContentView.findViewById(R.id.__re_toolbar);
        layout.addView(toolbar);
    }
}

package cn.cqray.android.editor.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.core.widget.TextViewCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import cn.cqray.android.editor.EditorPickStrategy;

/**
 * 富文本调色板文字颜色适配器
 * @author Cqray
 */
class EditorNumberAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private float mTextSize;
    private float mSelectedTextSize;
    private int mSelectedPosition;
    private List<Number> mData;
    private EditorPickStrategy mStrategy;

    private final MutableLiveData<Number> mSelected = new MutableLiveData<>();

    public EditorNumberAdapter(EditorPickStrategy strategy, List<Number> data, int selectedPosition) {
        mData = data;
        mStrategy = strategy;
        mSelectedPosition = selectedPosition;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LinearLayout root = new LinearLayout(context);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setLayoutParams(new ViewGroup.LayoutParams(-1, -2));
        // 文本控件
        TextView text = new TextView(context);
        text.setLayoutParams(new ViewGroup.LayoutParams(-1, -2));
        text.setGravity(Gravity.START);
        text.setFocusable(true);
        TextViewCompat.setTextAppearance(text, mStrategy.getItemAppearance());
        ViewCompat.setBackground(text, createItemBackground(context));
        mTextSize = text.getTextSize();
        mSelectedTextSize = mTextSize * 1.5f;
        // 分割线
        View divider = new View(context);
        divider.setLayoutParams(new ViewGroup.LayoutParams(-1, mStrategy.getDividerHeight()));
        divider.setBackgroundColor(mStrategy.getDividerColor());
        // 添加控件
        root.addView(text);
        root.addView(divider);
        return new RecyclerView.ViewHolder(root) {};
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {

        LinearLayout root = (LinearLayout) holder.itemView;
        TextView tv = (TextView) root.getChildAt(0);
        View divider = root.getChildAt(1);
        // 获取相应值
        Number value = mData.get(position);
        boolean selected = mSelectedPosition == position;
        String text;
        if (value instanceof Float || value instanceof Double) {
            text =  BigDecimal.valueOf(value.floatValue()).setScale(1, RoundingMode.HALF_UP).toPlainString();
        } else {
            text = value.intValue() + "";
        }
        tv.setText(text);
        tv.setTag(value);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectedPosition = position;
                mSelected.setValue((Number) v.getTag());
            }
        });
        // 设置是否选中
        tv.getPaint().setFakeBoldText(selected);
        // 重新设置字体大小
        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, selected ? mSelectedTextSize : mTextSize);
        // 设置上下间隔
        int tp = (int) (mTextSize / EditorNumberFragment.PADDING_SCALE);
        int offset = selected ? getOffset(text, mSelectedTextSize, mTextSize) : 0;
        tv.setPadding(tp - offset, tp, tp, tp);
        // 显示或隐藏分割线
        divider.setVisibility(position == getItemCount() - 1 ? View.GONE : View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public void observeSelected(LifecycleOwner owner, Observer<Number> observer) {
        mSelected.observe(owner, observer);
    }

    private int getOffset(String text, float size1, float size2) {
        Paint paint = new Paint();
        paint.setTextSize(size1);
        float w1 = paint.measureText(text);
        paint.setTextSize(size2);
        float w2 = paint.measureText(text);
        return (int) (Math.abs(w1 - w2) / 2);
    }

    /**
     * 创建项背景
     */
    private Drawable createItemBackground(@NonNull Context context) {
        TypedArray ta = context.obtainStyledAttributes(new int[] {
                android.R.attr.selectableItemBackground });
        Drawable drawable = ta.getDrawable(0);
        ta.recycle();
        return drawable;
    }
}

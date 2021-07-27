package cn.cqray.android.editor.ui;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * 富文本调色板文字颜色适配器
 * @author Cqray
 */
class EditorColorsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /** 是否可以取消选择 **/
    private boolean mCancelEnable;
    /** 单项大小 **/
    private int mItemSize;
    /** 数据 **/
    private List<String> mData;
    /** 选择的颜色 **/
    private final MutableLiveData<String> mSelectColor = new MutableLiveData<>();

    public EditorColorsAdapter(List<String> data, String color, int itemSize) {
        mData = data;
        mItemSize = itemSize;
        mSelectColor.setValue(color);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = new View(parent.getContext());
        view.setLayoutParams(new ViewGroup.LayoutParams(mItemSize, mItemSize));
        return new RecyclerView.ViewHolder(view) {};
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final String color = mData.get(position);
        GradientDrawable background = new GradientDrawable();
        background.setColor(Color.parseColor(color));
        if (color.equalsIgnoreCase(mSelectColor.getValue())) {
            background.setStroke(mItemSize / 20, ColorUtils.reverseColor(color));
        }
        ViewCompat.setBackground(holder.itemView, background);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean equal = color.equals(mSelectColor.getValue());
                mSelectColor.setValue(mCancelEnable && equal ? null :color);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public void setCancelEnable(boolean enable) {
        mCancelEnable = enable;
    }

    public void observeSelectColor(LifecycleOwner owner, Observer<String> observer) {
        mSelectColor.observe(owner, observer);
    }

    public void setSelectColor(String color) {
        mSelectColor.setValue(color);
        notifyDataSetChanged();
    }
}

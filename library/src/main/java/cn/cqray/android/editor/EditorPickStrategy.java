package cn.cqray.android.editor;

import android.content.Context;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;

import java.io.Serializable;

import cn.cqray.android.editor.provider.EditorButton;
import cn.cqray.android.editor.provider.EditorButtonImpl;
import cn.cqray.android.editor.provider.EditorToolbar;
import cn.cqray.android.editor.provider.EditorToolbarImpl;
import cn.cqray.android.editor.provider.EditorViewProvider;

/**
 * 富文本编辑器相关策略
 * @author Cqray
 */
public class EditorPickStrategy implements Serializable {

    private @StyleRes int mTextAppearance;
    private @StyleRes int mHintAppearance;
    private @StyleRes int mItemAppearance;

    private int mDividerColor;
    private int mDividerHeight;

    private int mBackgroundColor;

    private int mSpaceSize;
    private int mCardColor;
    private int mCardCorner;
    private int mCardMargin;

    private EditorViewProvider mViewProvider;

    private EditorPickStrategy() {}

    @NonNull
    public static EditorPickStrategy.Builder newBuilder() {
        return new EditorPickStrategy.Builder();
    }

    public int getTextAppearance() {
        return mTextAppearance;
    }

    public int getHintAppearance() {
        return mHintAppearance;
    }

    public int getItemAppearance() {
        return mItemAppearance;
    }

    public int getDividerColor() {
        return mDividerColor;
    }

    public int getDividerHeight() {
        return mDividerHeight;
    }

    public int getBackgroundColor() {
        return mBackgroundColor;
    }

    public int getCardColor() {
        return mCardColor;
    }

    public int getCardCorner() {
        return mCardCorner;
    }

    public int getCardMargin() {
        return mCardMargin;
    }

    public int getSpaceSize() {
        return mSpaceSize;
    }

    public EditorViewProvider getViewProvider() {
        return mViewProvider;
    }

    public static class Builder implements Serializable {
        private @StyleRes int mTextAppearance;
        private @StyleRes int mHintAppearance;
        private @StyleRes int mItemAppearance;

        private int mDividerColor;
        private int mDividerHeight;

        private int mBackgroundColor;

        private int mSpaceSize;
        private int mCardColor;
        private int mCardCorner;
        private int mCardMargin;
        private EditorViewProvider mViewProvider;

        Builder() {
            mTextAppearance = R.style.__TextAppearance;
            mHintAppearance = R.style.__HintAppearance;
            mItemAppearance = R.style.__TextAppearance;
            mDividerColor = Color.parseColor("#DEDEDE");
            mDividerHeight = EditorUtils.dp2px(0.5f);
            mCardColor = Color.WHITE;
            mBackgroundColor = Color.parseColor("#F0F0F0");
            mSpaceSize = EditorUtils.dp2px(16);
            mCardCorner = EditorUtils.dp2px(4);
            mCardMargin = EditorUtils.dp2px(10);
            mViewProvider = new EditorViewProvider() {
                @Override
                public EditorToolbar getToolbar(Context context) {
                    return new EditorToolbarImpl(context);
                }

                @Override
                public EditorButton getButton(Context context) {
                    return new EditorButtonImpl(context);
                }
            };
        }

        public Builder textAppearance(@StyleRes int textAppearance) {
            mTextAppearance = textAppearance;
            return this;
        }

        public Builder hintAppearance(@StyleRes int textAppearance) {
            mHintAppearance = textAppearance;
            return this;
        }

        public Builder itemAppearance(@StyleRes int textAppearance) {
            mItemAppearance = textAppearance;
            return this;
        }

        public Builder dividerColor(int color) {
            mDividerColor = color;
            return this;
        }

        public Builder dividerHeight(float height) {
            mDividerHeight = EditorUtils.dp2px(height);
            return this;
        }

        public Builder backgroundColor(int color) {
            mBackgroundColor = color;
            return this;
        }

        public Builder spaceSize(float size) {
            mSpaceSize = EditorUtils.dp2px(size);
            return this;
        }

        public Builder cardColor(int color) {
            mCardColor = color;
            return this;
        }

        public Builder cardCorner(float corner) {
            mCardCorner = EditorUtils.dp2px(corner);
            return this;
        }

        public Builder cardMargin(float margin) {
            mCardMargin = EditorUtils.dp2px(margin);
            return this;
        }

        public EditorPickStrategy build() {
            EditorPickStrategy strategy = new EditorPickStrategy();
            strategy.mTextAppearance = mTextAppearance;
            strategy.mHintAppearance = mHintAppearance;
            strategy.mItemAppearance = mItemAppearance;
            strategy.mDividerColor = mDividerColor;
            strategy.mDividerHeight = mDividerHeight;
            strategy.mBackgroundColor = mBackgroundColor;
            strategy.mSpaceSize = mSpaceSize;
            strategy.mCardColor = mCardColor;
            strategy.mCardCorner = mCardCorner;
            strategy.mCardMargin = mCardMargin;
            strategy.mViewProvider = mViewProvider;
            return strategy;
        }
    }
}

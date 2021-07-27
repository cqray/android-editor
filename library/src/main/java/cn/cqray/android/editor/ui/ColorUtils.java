package cn.cqray.android.editor.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.View;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;

import java.math.BigInteger;

import cn.cqray.android.editor.R;

/**
 * 颜色及背景工具
 * @author Cqray
 */
class ColorUtils {

    @NonNull
    static String getResColorStr(@NonNull Context context, @ColorRes int resId) {
        int color = ContextCompat.getColor(context, resId);
        return covertColor(color);
    }

    @NonNull
    public static String covertColor(int color) {
        String r, g, b;
        StringBuilder sb = new StringBuilder();
        r = Integer.toHexString(Color.red(color));
        g = Integer.toHexString(Color.green(color));
        b = Integer.toHexString(Color.blue(color));
        //判断获取到的R,G,B值的长度 如果长度等于1 给R,G,B值的前边添0
        r = r.length() == 1 ? "0" + r : r;
        g = g.length() == 1 ? "0" + g : g;
        b = b.length() == 1 ? "0" + b : b;
        sb.append("#");
        sb.append(r);
        sb.append(g);
        sb.append(b);
        return sb.toString().toUpperCase();
    }

    static int reverseColor(@NonNull String color) {
        int r = new BigInteger(color.substring(1, 3), 16).intValue();
        int g = new BigInteger(color.substring(3, 5), 16).intValue();
        int b = new BigInteger(color.substring(5, 7), 16).intValue();
        return Color.argb(255, 255 - r, 255 - g, 255 - b);
    }

    static void setViewBackground(@NonNull View view, String color) {
        int size = view.getResources().getDimensionPixelSize(R.dimen.divider);
        GradientDrawable background = new GradientDrawable();
        if (color == null) {
            int tmp = ContextCompat.getColor(view.getContext(), R.color.colorPrimary);
            background.setStroke(size, tmp);
        } else {
            int tmp = ColorUtils.reverseColor(color);
            background.setColor(Color.parseColor(color));
            background.setStroke(size, tmp);
        }
        ViewCompat.setBackground(view, background);
    }
}

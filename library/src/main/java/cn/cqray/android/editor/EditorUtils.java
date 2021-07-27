package cn.cqray.android.editor;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Resources;
import android.util.TypedValue;

import androidx.annotation.Nullable;

/**
 * 工具类
 * @author Cqray
 */
class EditorUtils {

    @Nullable
    static Activity getActivity(Context context) {
        Context act = context;
        while (!(act instanceof Activity) && act instanceof ContextWrapper) {
            act = ((ContextWrapper) act).getBaseContext();
        }
        if (act instanceof Activity) {
            return (Activity) act;
        }
        return null;
    }

    static int dp2px(float dp) {
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem().getDisplayMetrics()) + 0.5f);
    }
}

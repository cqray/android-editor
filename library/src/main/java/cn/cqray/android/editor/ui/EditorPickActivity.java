package cn.cqray.android.editor.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;

import cn.cqray.android.editor.EditorCommand;
import cn.cqray.android.editor.EditorPickStrategy;
import cn.cqray.android.editor.R;

/**
 * 富文本编辑器设置Activity
 * @author Cqray
 */
public class EditorPickActivity extends AppCompatActivity {

    /** 外部回调 **/
    static EditorPickCallback sPickCallback;
    /** 内部回调 **/
    static EditorPickCallback sInnerCallback;

    /**
     * 开启Activity
     * @param context 上下文
     * @param command 指令
     * @param strategy 策略
     * @param data 附属值
     * @param callback 回调
     */
    public static void start(Context context, EditorCommand command, EditorPickStrategy strategy, Object[] data, EditorPickCallback callback) {
        sPickCallback = callback;
        Intent intent = new Intent(context, EditorPickActivity.class);
        intent.putExtra("config", strategy);
        intent.putExtra("command", command);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (data != null) {
            if (data.length == 1) {
                intent.putExtra("data", (float) data[0]);
            } else {
                intent.putExtra("foreColor", (String) data[0]);
                intent.putExtra("backColor", (String) data[1]);
            }
        }
        context.startActivity(intent);
    }

    public static void startInput(Context context, EditorPickStrategy strategy, boolean link, EditorPickCallback callback) {
        sPickCallback = callback;
        Intent intent = new Intent(context, EditorPickActivity.class);
        intent.putExtra("strategy", strategy);
        intent.putExtra("link", link);
        intent.putExtra("type", 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void startNumber(Context context, EditorPickStrategy strategy, Number cur, EditorPickCallback callback) {
        sPickCallback = callback;
        Intent intent = new Intent(context, EditorPickActivity.class);
        intent.putExtra("strategy", strategy);
        intent.putExtra("cur", cur);
        intent.putExtra("type", 1);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void startColors(Context context, EditorPickStrategy strategy, String foreColor, String backColor, EditorPickCallback callback) {
        sPickCallback = callback;
        Intent intent = new Intent(context, EditorPickActivity.class);
        intent.putExtra("strategy", strategy);
        intent.putExtra("backColor", backColor);
        intent.putExtra("foreColor", foreColor);
        intent.putExtra("type", 2);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private String[] mValues = new String[2];

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.__re_editor_launch);

        sInnerCallback = new EditorPickCallback() {
            @Override
            public void onCall(String v1, String v2) {
                mValues[0] = v1;
                mValues[1] = v2;
            }
        };

        Intent intent = getIntent();
        int type = intent.getIntExtra("type", 0);
        switch (type) {
            case 0:
                launchFragment(EditorInputFragment.newInstance(intent.getBooleanExtra("link", true)));
                break;
            case 1:
                Number cur = (Number) intent.getSerializableExtra("cur");
                ArrayList<Number> data = new ArrayList<>();
                int dataCount = 20;
                if (cur instanceof Float || cur instanceof Double) {
                    for (int i = 0; i < dataCount; i++) {
                        data.add(0.8f + i * 0.1f);
                    }
                } else {
                    for (int i = 0; i < dataCount; i++) {
                        data.add(i + 8);
                    }
                }
                launchFragment(EditorNumberFragment.newInstance(cur, data, true));
                break;
            case 2:
                String foreColor = intent.getStringExtra("foreColor");
                String backColor = intent.getStringExtra("backColor");
                launchFragment(EditorColorsFragment.newInstance(foreColor, backColor));
                break;
            default:
                finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (sPickCallback != null) {
            sPickCallback.onCall(mValues[0], mValues[1]);
            sPickCallback = null;
        }
        sInnerCallback = null;
    }

    private void launchFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.__re_content, fragment);
        ft.commit();
    }
}

package cn.cqray.android.editor.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import cn.cqray.android.editor.EditorPickStrategy;
import cn.cqray.android.editor.R;

/**
 * 自定义颜色选择Activity
 * @author Cqray
 */
public class EditorColorActivity extends AppCompatActivity {

    static EditorPickCallback sPickCallback;

    public static void start(Context context, EditorPickStrategy strategy, String color, EditorPickCallback callback) {
        sPickCallback = callback;
        Intent intent = new Intent(context, EditorColorActivity.class);
        intent.putExtra("strategy", strategy);
        intent.putExtra("color", color);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.__re_editor_launch);
        launchFragment(new EditorColorFragment());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sPickCallback = null;
    }

    private void launchFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.__re_content, fragment);
        ft.commit();
    }
}

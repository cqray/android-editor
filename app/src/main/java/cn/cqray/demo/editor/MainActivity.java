package cn.cqray.demo.editor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import cn.cqray.android.editor.EditorMenuBar;
import cn.cqray.android.editor.KeyboardUtils;
import cn.cqray.android.editor.RichEditor;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RichEditor editor = findViewById(R.id.wv_start);
        //KeyboardUtils.showSoftInput(editor);
        EditorMenuBar richEditorActionBar = findViewById(R.id.action);
        richEditorActionBar.setRichEditor(editor);
        editor.insertText("hello world!");
    }
}
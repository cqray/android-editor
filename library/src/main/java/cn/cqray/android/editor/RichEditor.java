package cn.cqray.android.editor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;

import java.util.Locale;

/**
 * 富文本编辑器
 * @author Cqray
 */
public class RichEditor extends WebView {

    private boolean mLoadFinished;
    private EditorPickStrategy mPickStrategy;
    private EditorImagePicker mImagePicker;
    private final EditorController mEditorController = new EditorController();

    public RichEditor(Context context) {
        this(context, null, 0);
    }

    public RichEditor(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RichEditor(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initEditor();
    }

    @SuppressLint({"JavascriptInterface", "AddJavascriptInterface", "SetJavaScriptEnabled"})
    protected void initEditor() {
        mPickStrategy = EditorPickStrategy.newBuilder().build();
        mEditorController.bindEditorView(this);
        setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }
        });
        setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                mLoadFinished = newProgress >= 100;
            }
        });
        WebSettings settings = getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        addJavascriptInterface(mEditorController, "RichEditor");
        // 加载对应语言的界面
        Configuration configuration = getResources().getConfiguration();
        Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = configuration.getLocales().get(0);
        } else {
            locale = configuration.locale;
        }
        String country = "CN";
        if (locale.getCountry().equalsIgnoreCase(country)) {
            loadUrl("file:///android_asset/editor-zh.html");
        } else {
            loadUrl("file:///android_asset/editor-en.html");
        }
    }

    protected boolean isLoadFinished() {
        return mLoadFinished;
    }

    public void setPickStrategy(EditorPickStrategy strategy) {
        mPickStrategy = strategy;
    }

    public void setImagePicker(EditorImagePicker imagePicker) {
        mImagePicker = imagePicker;
    }

    public EditorController getEditorController() {
        return mEditorController;
    }

    public EditorPickStrategy getPickStrategy() {
        return mPickStrategy;
    }

    public EditorImagePicker getImagePicker() {
        return mImagePicker;
    }

    public String getHtml() {
        return mEditorController.getHtml();
    }

    public void undo() {
        mEditorController.undo();
    }

    public void redo() {
        mEditorController.redo();
    }

    public void focus() {
        mEditorController.focus();
    }

    public void disable() {
        mEditorController.disable();
    }

    public void enable() {
        mEditorController.enable();
    }

    public void bold() {
        mEditorController.bold();
    }

    public void italic() {
        mEditorController.italic();
    }

    public void underline() {
        mEditorController.underline();
    }

    public void strikethrough() {
        mEditorController.strikethrough();
    }

    public void superscript() {
        mEditorController.superscript();
    }

    public void subscript() {
        mEditorController.subscript();
    }

    public void backColor(String color) {
        mEditorController.backColor(color);
    }

    public void foreColor(String color) {
        mEditorController.foreColor(color);
    }

    public void fontName(String fontName) {
        mEditorController.fontName(fontName);
    }

    public void fontSize(float foreSize) {
        mEditorController.fontSize(foreSize);
    }

    public void lineHeight(float lineHeight) {
        mEditorController.lineHeight(lineHeight);
    }

    public void formatClear() {
        mEditorController.formatClear();
    }

    public void formatPara() {
        mEditorController.formatPara();
    }

    public void formatH1() {
        mEditorController.formatH1();
    }

    public void formatH2() {
        mEditorController.formatH2();
    }

    public void formatH3() {
        mEditorController.formatH3();
    }

    public void formatH4() {
        mEditorController.formatH4();
    }

    public void formatH5() {
        mEditorController.formatH5();
    }

    public void formatH6() {
        mEditorController.formatH6();
    }

    public void justifyLeft() {
        mEditorController.justifyLeft();
    }

    public void justifyRight() {
        mEditorController.justifyRight();
    }

    public void justifyCenter() {
        mEditorController.justifyCenter();
    }

    public void justifyFull() {
        mEditorController.justifyFull();
    }

    public void orderedList() {
        mEditorController.orderedList();
    }

    public void unorderedList() {
        mEditorController.unorderedList();
    }

    public void indent() {
        mEditorController.indent();
    }

    public void outdent() {
        mEditorController.outdent();
    }

    public void insertImageUrl(String imageUrl) {
        mEditorController.insertImageUrl(imageUrl);
    }

    public void insertImageData(@NonNull String fileName, String base64Str) {
        mEditorController.insertImageData(fileName, base64Str);
    }

    public void insertLink(String linkText, String linkUrl) {
        mEditorController.insertLink(linkText, linkUrl);
    }

    public void insertText(String text) {
        mEditorController.insertText(text);
    }

    public void unlink() {
        mEditorController.unlink();
    }

    public void insertTable(int colCount, int rowCount) {
        mEditorController.insertTable(colCount, rowCount);
    }

    public void insertDivider() {
        mEditorController.insertDivider();
    }

    public void blockQuote() {
        mEditorController.blockQuote();
    }

    public void blockCode() {
        mEditorController.blockCode();
    }

    public void codeView() {
        mEditorController.codeView();
    }

    public void insertHtml(String html) {
        mEditorController.insertHtml(html);
    }

    public void placeholder(String placeholder) {
        mEditorController.placeholder(placeholder);
    }

    public void backgroundColor(String color) {
        mEditorController.backgroundColor(color);
    }

    public void execute(EditorCommand command) {
        mEditorController.execute(command);
    }
}

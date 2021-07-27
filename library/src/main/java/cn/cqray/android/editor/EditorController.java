package cn.cqray.android.editor;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.google.gson.Gson;

import cn.cqray.android.editor.ui.EditorPickActivity;
import cn.cqray.android.editor.ui.EditorPickCallback;

/**
 * 富文本编辑器沟通桥梁
 * @author Cqray
 */
@SuppressLint("JavascriptInterface")
public class EditorController {

    private boolean mEditorInitialed;
    private boolean mKeyboardVisible;
    private RichEditor mRichEditor;
    private final Gson gson = new Gson();
    private final MutableLiveData<String> mContent = new MutableLiveData<>();
    private final MutableLiveData<FormatStyle> mStyle = new MutableLiveData<>();

    @JavascriptInterface
    public void onInit() {
        mEditorInitialed = true;
        focus();
        notifyStyleChange();
    }

    /**
     * 返回html内容
     * @param content 内容
     */
    @JavascriptInterface
    public void onContentChanged(String content) {
        mContent.postValue(content);
    }

    /**
     * 更新当前文本样式
     * @param style 样式
     */
    @JavascriptInterface
    public void onStyleChanged(String style) {
        FormatStyle formatStyle = gson.fromJson(style, FormatStyle.class);
        if (formatStyle != null) {
            mStyle.postValue(formatStyle);
        }
    }

    public void bindEditorView(RichEditor richEditor) {
        mRichEditor = richEditor;
    }

    public void observeStyle(LifecycleOwner owner, Observer<FormatStyle> observer) {
        mStyle.observe(owner, observer);
    }

    public void undo() {
        execute(EditorCommand.UNDO);
    }

    public void redo() {
        execute(EditorCommand.REDO);
    }

    public void focus() {
        execute("javascript:focus()");
    }

    public void disable() {
        execute("javascript:disable()");
    }

    public void enable() {
        execute("javascript:enable()");
    }

    public void bold() {
        execute(EditorCommand.BOLD);
    }

    public void italic() {
        execute(EditorCommand.ITALIC);
    }

    public void underline() {
        execute(EditorCommand.UNDERLINE);
    }

    public void strikethrough() {
        execute(EditorCommand.STRIKETHROUGH);
    }

    public void superscript() {
        execute(EditorCommand.SUPERSCRIPT);
    }

    public void subscript() {
        execute(EditorCommand.SUBSCRIPT);
    }

    public void fontColors(String foreColor, String backColor) {
        execute("javascript:fontColors('" + foreColor + "|" + backColor + "')");
    }

    public void foreColor(String color) {
        fontColors(color, null);
    }

    public void backColor(String color) {
        fontColors(null, color);
    }

    public void fontName(String fontName) {
        execute("javascript:fontName('" + fontName + "')");
    }

    public void fontSize(double foreSize) {
        execute("javascript:fontSize(" + foreSize + ")");
    }

    public void lineHeight(float lineHeight) {
        execute("javascript:lineHeight('" + lineHeight + "')");
    }

    public void formatClear() {
        execute(EditorCommand.FORMAT_CLEAR);
    }

    public void formatPara() {
        execute(EditorCommand.FORMAT_PARA);
    }

    public void formatH1() {
        execute(EditorCommand.FORMAT_H1);
    }

    public void formatH2() {
        execute(EditorCommand.FORMAT_H2);
    }

    public void formatH3() {
        execute(EditorCommand.FORMAT_H3);
    }

    public void formatH4() {
        execute(EditorCommand.FORMAT_H4);
    }

    public void formatH5() {
        execute(EditorCommand.FORMAT_H5);
    }

    public void formatH6() {
        execute(EditorCommand.FORMAT_H6);
    }

    public void justifyLeft() {
        execute(EditorCommand.JUSTIFY_LEFT);
    }

    public void justifyRight() {
        execute(EditorCommand.JUSTIFY_RIGHT);
    }

    public void justifyCenter() {
        execute(EditorCommand.JUSTIFY_CENTER);
    }

    public void justifyFull() {
        execute(EditorCommand.JUSTIFY_FULL);
    }

    public void orderedList() {
        execute(EditorCommand.ORDERED);
    }

    public void unorderedList() {
        execute(EditorCommand.UNORDERED);
    }

    public void indent() {
        execute(EditorCommand.INDENT);
    }

    public void outdent() {
        execute(EditorCommand.OUTDENT);
    }

    public void insertImageUrl(String imageUrl) {
        execute("javascript:insertImageUrl('" + imageUrl + "')");
    }

    public void insertImageData(@NonNull String fileName, String base64Str) {
        String imageUrl = "data:image/" + fileName.split("\\.")[1] + ";base64," + base64Str;
        execute("javascript:insertImageUrl('" + imageUrl + "')");
    }

    public void insertLink(String linkText, String linkUrl) {
        execute("javascript:insertLink('" + linkText + "','" + linkUrl + "')");
    }

    public void unlink() {
        execute("javascript:unlink()");
    }

    public void insertTable(int colCount, int rowCount) {
        execute("javascript:insertTable('" + colCount + "x" + rowCount + "')");
    }

    public void insertText(String text) {
        execute("javascript:insertText('" + text + "')");
    }

    public void insertHtml(String html) {
        execute("javascript:insertHtml('" + html + "')");
    }

    public void insertDivider() {
        execute(EditorCommand.DIVIDER);
    }

    public void blockQuote() {
        execute(EditorCommand.BLOCK_QUOTE);
    }

    public void blockCode() {
        execute(EditorCommand.BLOCK_CODE);
    }

    public void codeView() {
        execute(EditorCommand.CODE_VIEW);
    }

    public void placeholder(String placeholder) {
        execute("javascript:placeholder('" + placeholder + "')");
    }

    public void backgroundColor(String color) {
        execute("javascript:backgroundColor('" + color + "')");
    }

    public void notifyStyleChange() {
        execute("javascript:notifyStyleChange()");
    }

    /**
     * 执行对应指令
     * @param command 指令
     */
    public void execute(@NonNull EditorCommand command) {
        switch (command) {
            case UNDO:
            case REDO:
            case BOLD:
            case ITALIC:
            case UNDERLINE:
            case STRIKETHROUGH:
            case SUBSCRIPT:
            case SUPERSCRIPT:
            case FORMAT_CLEAR:
            case FORMAT_PARA:
            case FORMAT_H1:
            case FORMAT_H2:
            case FORMAT_H3:
            case FORMAT_H4:
            case FORMAT_H5:
            case FORMAT_H6:
            case JUSTIFY_LEFT:
            case JUSTIFY_RIGHT:
            case JUSTIFY_CENTER:
            case JUSTIFY_FULL:
            case ORDERED:
            case UNORDERED:
            case INDENT:
            case OUTDENT:
            case BLOCK_QUOTE:
            case BLOCK_CODE:
            case CODE_VIEW:
            case DIVIDER:
                execute("javascript:" + command.getJsMethod());
                break;
            case IMAGE:
                if (mRichEditor != null) {
                    mRichEditor.getImagePicker().pick(mRichEditor.getContext(), this);
                }
                break;
            case LINK:
            case TABLE:
            case FONT_SIZE:
            case FONT_COLORS:
            case FONT_FORE_COLOR:
            case FONT_BACK_COLOR:
            case LINE_HEIGHT:
                skip(command);
                break;
            default:
        }
    }

    public String getHtml() {
        return mContent.getValue();
    }

    /**
     * 执行js指令
     * @param jsCommand js指令
     */
    private void execute(final String jsCommand) {
        if (mRichEditor == null) {
            return;
        }
        if (mEditorInitialed && mRichEditor.isLoadFinished()) {
            loadJsCommand(jsCommand);
        } else {
            mRichEditor.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mEditorInitialed && mRichEditor.isLoadFinished()) {
                        loadJsCommand(jsCommand);
                    } else {
                        execute(jsCommand);
                    }
                }
            }, 50);
        }
    }

    /**
     * 加载Js指定
     * @param jsCommand js指令
     */
    private void loadJsCommand(final String jsCommand) {
        if (mRichEditor == null) {
            return;
        }
        mRichEditor.post(new Runnable() {
            @Override
            public void run() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    mRichEditor.evaluateJavascript(jsCommand, null);
                } else {
                    mRichEditor.loadUrl(jsCommand);
                }
            }
        });
    }

    /**
     * 检查是否能够跳转
     */
    private boolean checkSkipEnable() {
        if (mRichEditor == null) {
            return false;
        }
        final Activity act = EditorUtils.getActivity(mRichEditor.getContext());
        if (act == null) {
            return false;
        }
        mKeyboardVisible = KeyboardUtils.isSoftInputVisible(act);
        return true;
    }

    /**
     * 跳转指定界面
     */
    private void skip(final EditorCommand command) {
        if (checkSkipEnable()) {
            Context context = mRichEditor.getContext();
            EditorPickStrategy strategy = mRichEditor.getPickStrategy();
            FormatStyle es = mStyle.getValue();
            assert es != null;
            EditorPickCallback callback = new EditorPickCallback() {
                @Override
                public void onCall(String v1, String v2) {
                    // 显示键盘
                    if (mKeyboardVisible) {
                        KeyboardUtils.showSoftInput(mRichEditor);
                    }
                    // 如果没有传回值
                    if (TextUtils.isEmpty(v1)) {
                        return;
                    }
                    if (command == EditorCommand.LINK) {
                        insertLink(v1, v2);
                    } else if (command == EditorCommand.TABLE) {
                        insertTable(Integer.parseInt(v1), Integer.parseInt(v2));
                    } else if (command == EditorCommand.FONT_SIZE) {
                        fontSize(Float.parseFloat(v1));
                    } else if (command == EditorCommand.LINE_HEIGHT) {
                        lineHeight(Float.parseFloat(v1));
                    } else if (command == EditorCommand.FONT_COLORS
                            || command == EditorCommand.FONT_BACK_COLOR
                            || command == EditorCommand.FONT_FORE_COLOR) {
                        fontColors(v1, v2);
                    }
                }
            };
            if (command == EditorCommand.LINK || command == EditorCommand.TABLE) {
                EditorPickActivity.startInput(context, strategy, command == EditorCommand.LINK, callback);
            } else if (command == EditorCommand.FONT_SIZE) {
                EditorPickActivity.startNumber(context, strategy, es.getFontSize(), callback);
            } else if (command == EditorCommand.LINE_HEIGHT) {
                EditorPickActivity.startNumber(context, strategy, es.getLineHeight(), callback);
            } else if (command == EditorCommand.FONT_COLORS
                    || command == EditorCommand.FONT_BACK_COLOR
                    || command == EditorCommand.FONT_FORE_COLOR) {
                EditorPickActivity.startColors(context, strategy, es.getFontForeColor(), es.getFontBackColor(), callback);
            }
        }
    }
}

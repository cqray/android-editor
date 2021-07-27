package cn.cqray.android.editor;

import java.io.Serializable;

/**
 * 操作类型
 * @author Cqray
 */
public enum EditorCommand implements Serializable {

    /** 加粗 **/ UNDO(1, "undo()", R.drawable.__re_ic_undo),
    /** 加粗 **/ REDO(2, "redo()", R.drawable.__re_ic_redo),
    /** 加粗 **/ BOLD(3, "bold()", R.drawable.__re_ic_format_bold),
    /** 倾斜 **/ ITALIC(4, "italic()", R.drawable.__re_ic_format_italic),
    /** 下划线 **/ UNDERLINE(5, "underline()", R.drawable.__re_ic_format_underline),
    /** 删除线 **/ STRIKETHROUGH(6, "strikethrough()", R.drawable.__re_ic_format_strikethrough),
    /** 下标 **/ SUBSCRIPT(7, "subscript()", R.drawable.__re_ic_format_subscript),
    /** 上标 **/ SUPERSCRIPT(8, "superscript()", R.drawable.__re_ic_format_superscript),
    /** 字体大小 **/ FONT_SIZE(9, null, R.drawable.__re_ic_font_size),
    /** 字体相关色 **/ FONT_COLORS(10, null, R.drawable.__re_ic_font_colors),
    /** 字体前景色 **/ FONT_FORE_COLOR(11, null, R.drawable.__re_ic_font_fore_color),
    /** 字体背景色 **/ FONT_BACK_COLOR(12, null, R.drawable.__re_ic_font_back_color),
    /** 字体行距 **/ LINE_HEIGHT(13, null, R.drawable.__re_ic_line_height),
    /** 常规文本 **/ FORMAT_CLEAR(14, "formatClear()", R.drawable.__re_ic_format_clear),
    /** 常规文本 **/ FORMAT_PARA(15, "formatPara()", R.drawable.__re_ic_format_para),
    /** 标题1 **/ FORMAT_H1(16, "formatH1()", R.drawable.__re_ic_format_h1),
    /** 标题2 **/ FORMAT_H2(17, "formatH2()", R.drawable.__re_ic_format_h2),
    /** 标题3 **/ FORMAT_H3(18, "formatH3()", R.drawable.__re_ic_format_h3),
    /** 标题4 **/ FORMAT_H4(19, "formatH4()", R.drawable.__re_ic_format_h4),
    /** 标题5 **/ FORMAT_H5(20, "formatH5()", R.drawable.__re_ic_format_h5),
    /** 标题6 **/ FORMAT_H6(21, "formatH6()", R.drawable.__re_ic_format_h6),
    /** 左对齐 **/ JUSTIFY_LEFT(22, "justifyLeft()", R.drawable.__re_ic_align_left),
    /** 居中 **/ JUSTIFY_CENTER(23, "justifyCenter()", R.drawable.__re_ic_align_center),
    /** 右对齐 **/ JUSTIFY_RIGHT(24, "justifyRight()", R.drawable.__re_ic_align_right),
    /** 左右对齐 **/ JUSTIFY_FULL(25, "justifyFull()", R.drawable.__re_ic_align_justify),
    /** 有序排序 **/ ORDERED(26, "ordered()", R.drawable.__re_ic_list_ordered),
    /** 无序排序 **/ UNORDERED(27, "unordered()", R.drawable.__re_ic_list_unordered),
    /** 添加缩进 **/ INDENT(28, "indent()", R.drawable.__re_ic_format_indent),
    /** 减少缩进 **/ OUTDENT(29, "outdent()", R.drawable.__re_ic_format_outdent),
    /** 插入图片 **/ IMAGE(30, null, R.drawable.__re_ic_insert_image),
    /** 插入链接 **/ LINK(31, null, R.drawable.__re_ic_insert_link),
    /** 插入表格 **/ TABLE(32, null, R.drawable.__re_ic_insert_table),
    /** 插入分割线 **/ DIVIDER(33, "insertDivider()", R.drawable.__re_ic_divider),
    /** 引用块 **/ BLOCK_QUOTE(34, "blockQuote()", R.drawable.__re_ic_bock_quote),
    /** 代码块 **/ BLOCK_CODE(35, "blockCode()", R.drawable.__re_ic_block_code),
    /** 代码模式 **/ CODE_VIEW(36, "codeView()", R.drawable.__re_ic_code_review);

    private final int mValue;
    private final int mResId;
    private final String mJsMethod;

    EditorCommand(int value, String jsMethod, int resId) {
        mJsMethod = jsMethod;
        mValue = value;
        mResId = resId;
    }

    int getValue() {
        return mValue;
    }

    int getResId() {
        return mResId;
    }

    String getJsMethod() {
        return mJsMethod;
    }

    static EditorCommand find(int value) {
        for (EditorCommand item : EditorCommand.values()) {
            if (item.mValue == value) {
                return item;
            }
        }
        return FORMAT_CLEAR;
    }
}

package cn.cqray.android.editor;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

/**
 * 格式化样式
 * @author Cqray
 */
class FormatStyle {

    /**
     * font-family : "Helvetica Neue", Helvetica, Arial, sans-serif
     * font-size : 36
     * text-align : start
     * list-style-type : disc
     * line-height : 1.1
     * font-bold : normal
     * font-italic : normal
     * font-underline : normal
     * font-subscript : normal
     * font-superscript : normal
     * font-strikethrough : normal
     * list-style : none
     * anchor : false
     * ancestors : [{},{}]
     * range : {"sc":{},"so":4,"ec":{},"eo":4}
     */

    // h1\h2\h3\h4\h5\h6  36 30 24 18 14 12
    @SerializedName("font-size") private int fontSize;
    @SerializedName("font-backColor") private String fontBackColor;
    @SerializedName("font-foreColor") private String fontForeColor;
    @SerializedName("text-align") private String textAlign;
    @SerializedName("line-height") private float lineHeight;
    @SerializedName("font-bold") private String fontBold;
    @SerializedName("font-italic") private String fontItalic;
    @SerializedName("font-underline") private String fontUnderline;
    @SerializedName("font-subscript") private String fontSubscript;
    @SerializedName("font-superscript") private String fontSuperscript;
    @SerializedName("font-strikethrough") private String fontStrikethrough;
    @SerializedName("list-style") private String listStyle;

    public int getFontSize() {
        return fontSize;
    }

    public float getLineHeight() {
        return lineHeight;
    }

    public String getFontBackColor() {
        return fontBackColor;
    }

    public String getFontForeColor() {
        return fontForeColor;
    }

    public boolean isActive(@NonNull EditorCommand editorCommand) {
        switch (editorCommand) {
            case BOLD: return "bold".equalsIgnoreCase(fontBold);
            case ITALIC: return "italic".equalsIgnoreCase(fontItalic);
            case UNDERLINE: return "underline".equalsIgnoreCase(fontUnderline);
            case SUBSCRIPT: return "subscript".equalsIgnoreCase(fontSubscript);
            case SUPERSCRIPT: return "superscript".equalsIgnoreCase(fontSuperscript);
            case STRIKETHROUGH: return "strikethrough".equalsIgnoreCase(fontStrikethrough);
            case JUSTIFY_LEFT: return "left".equalsIgnoreCase(textAlign) || "start".equalsIgnoreCase(textAlign);
            case JUSTIFY_CENTER: return "center".equalsIgnoreCase(textAlign);
            case JUSTIFY_RIGHT: return "right".equalsIgnoreCase(textAlign) || "end".equalsIgnoreCase(textAlign);
            case JUSTIFY_FULL: return "justify".equalsIgnoreCase(textAlign);
            case ORDERED: return "ordered".equalsIgnoreCase(listStyle);
            case UNORDERED: return "unordered".equalsIgnoreCase(listStyle);
            default: break;
        }
        return false;
    }
}

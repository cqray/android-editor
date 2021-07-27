// 加粗
var bold = function() {
    $('#summernote').summernote('bold');
    notifyStyleChange();
};

// 斜体
var italic = function() {
    $('#summernote').summernote('italic');
    notifyStyleChange();
};

// 下划线
var underline = function() {
    $('#summernote').summernote('underline');
    notifyStyleChange();
};

// 删除线
var strikethrough = function() {
    $('#summernote').summernote('strikethrough');
    notifyStyleChange();
};

// 上标
var superscript = function() {
    $('#summernote').summernote('superscript');
    notifyStyleChange();
};

// 下标
var subscript = function() {
    $('#summernote').summernote('subscript');
    notifyStyleChange();
};

// 文字相关颜色
var fontColors = function(colors) {
    var tmp = colors.split("|");
    if (tmp[0] && tmp[0] != 'null') {
        $('#summernote').summernote('foreColor', tmp[0]);
    }
    if (tmp[1] && tmp[1] != 'null') {
        $('#summernote').summernote('backColor', tmp[1]);
    }
    notifyStyleChange();
}

// 字体
var fontName = function(fontName) {
    $('#summernote').summernote('fontName', fontName);
    notifyStyleChange();
};

// 文字大小
var fontSize = function(fontSize) {
    $('#summernote').summernote('fontSize', fontSize);
    notifyStyleChange();
};

// 行高
var lineHeight = function(lineHeight) {
    $('#summernote').summernote('lineHeight', lineHeight);
    notifyStyleChange();
};

// 格式清除
var formatClear = function() {
    $('#summernote').summernote('removeFormat');
    notifyStyleChange();
};

// 正文
var formatPara = function() {
    $('#summernote').summernote('formatPara');
};

// 标题一
var formatH1 = function() {
    $('#summernote').summernote('formatH1');
};

// 标题二
var formatH2 = function() {
    $('#summernote').summernote('formatH2');
};

// 标题三
var formatH3 = function() {
    $('#summernote').summernote('formatH3');
};

// 标题四
var formatH4 = function() {
    $('#summernote').summernote('formatH4');
};

// 标题五
var formatH5 = function() {
    $('#summernote').summernote('formatH5');
};

// 标题六
var formatH6 = function() {
    $('#summernote').summernote('formatH6');
};

// 居左
var justifyLeft = function() {
    $('#summernote').summernote('justifyLeft');
    notifyStyleChange();
};

// 居右
var justifyRight = function() {
    $('#summernote').summernote('justifyRight');
    notifyStyleChange();
};

// 居中
var justifyCenter = function() {
    $('#summernote').summernote('justifyCenter');
    notifyStyleChange();
};

// 充满
var justifyFull = function() {
    $('#summernote').summernote('justifyFull');
    notifyStyleChange();
};

// 有序列表
var ordered = function() {
    $('#summernote').summernote('insertOrderedList');
    notifyStyleChange();
};

// 无序列表
var unordered = function() {
    $('#summernote').summernote('insertUnorderedList');
    notifyStyleChange();
};

var indent = function() {
    $('#summernote').summernote('indent');
};

var outdent = function() {
    $('#summernote').summernote('outdent');
};

// 通知样式变化
var notifyStyleChange = function() {
    var context = $('#summernote').first().data('summernote');
    var style = context.module('editor').currentStyle();
    style['font-foreColor'] = colorHex(document.queryCommandValue('foreColor'));
    style['font-backColor'] = colorHex(document.queryCommandValue('backColor'));
    console.log(JSON.stringify(style));
    RichEditor.onStyleChanged(JSON.stringify(style));
}

// 获取对应的色值
function colorHex(color) {
    // RGB颜色值的正则
    var reg = /^(rgb|RGB)/;
    if (reg.test(color)) {
        var strHex = "#";
        // 把RGB的3个数值变成数组
        var colorArr = color.replace(/(?:\(|\)|rgb|RGB)*/g, "").split(",");
        // 转成16进制
        for (var i = 0; i < colorArr.length; i++) {
            var hex = Number(colorArr[i]).toString(16);
            if (hex === "0") {
                hex += hex;
            }
　　　　　　　hex = hex.length == 1?'0'+hex:hex;
            strHex += hex;
        }
        return strHex;
    } else {
        return String(color);
    }
}
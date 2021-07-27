// 撤销
var undo = function() {
    $('#summernote').summernote('undo');
};

// 重做
var redo = function() {
    $('#summernote').summernote('redo');
};

// 不可用
var disable = function() {
    $('#summernote').summernote('disable');
};

// 可用
var enable = function() {
    $('#summernote').summernote('enable');
};

// 获取焦点
var focus = function(){
    $('#summernote').summernote('focus');
};

// 占位符
var placeholder = function(text) {
    var context = $('#summernote').first().data('summernote');
    context.options['placeholder'] = text;
    $(".note-placeholder").html(text);
    $("#summernote").summernote("placeholder.update");
};

// 引用块
var blockQuote = function() {
    $('#summernote').summernote('formatBlock', 'blockquote');
}

// 代码块
var blockCode = function() {
    $('#summernote').summernote('formatBlock', 'pre');
}

// 代码界面
var codeView = function() {
    $('#summernote').summernote('codeview.toggle');
};

// 背景颜色
var backgroundColor = function(color) {
    $(".note-editable").css("background", color);
    $(".note-status-output").css("background", color);
}
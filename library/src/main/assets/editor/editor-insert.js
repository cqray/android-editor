// 插入图片
var insertImageUrl = function(imageUrl) {
    $('#summernote').summernote('insertImage', imageUrl, null);
};

// 插入链接
var insertLink = function(linkText, linkUrl) {
    $('#summernote').summernote('createLink', {
      text: linkText,
      url: linkUrl,
      isNewWindow: false
    });
};

// 取消链接
var unlink = function() {
    $('#summernote').summernote('unlink');
};

// 插入表格
var insertTable = function(dim) {
    $('#summernote').summernote('insertTable', dim);
}

// 插入分割线
var insertDivider = function() {
    $('#summernote').summernote('insertHorizontalRule');
}

// 插入文本
var insertText = function(text) {
    $('#summernote').summernote('insertText', text);
};

// 插入Html文本
var insertHtml = function(html) {
    $('#summernote').summernote('pasteHTML', html);
}

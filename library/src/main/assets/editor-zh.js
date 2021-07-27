addScript('editor/editor-format.js');
addScript('editor/editor-insert.js');
addScript('editor/editor-other.js');

// 内容长度，内容变短则通知样式样式改变
var contentLength = 0;
// 初始化富文本编辑器
var initSummernote = function(){
    $('#summernote').summernote({
        toolbar: [],
        placeholder: " ",
        callbacks: {
            onInit: function(e) {
                $("#summernote").summernote("fullscreen.toggle");
                // 初始化成功
                RichEditor.onInit();
            },
            onChange: function(contents, $editable) {
                RichEditor.onContentChanged(contents);
                // 内容变短则通知样式样式改变
                if (contents.length < contentLength) {
                    notifyStyleChange();
                }
                contentLength = contents.length;
            }
        },
        lang: 'zh-CN' // default: 'en-US'
    });
    // 鼠标事件，主要用于更新当前文字样式
    $('#summernote').on('summernote.mouseup', function() {
        notifyStyleChange();
    });
}

function addScript(url){
    var script = document.createElement('script');
    script.setAttribute('type','text/javascript');
    script.setAttribute('src',url);
    document.getElementsByTagName('head')[0].appendChild(script);
}


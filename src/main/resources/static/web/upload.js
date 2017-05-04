
function uploadFile(obj) {
    var strs = $(obj).val().split('.');
    var suffix = strs [strs .length - 1];

    if (suffix != 'xml') {
        alert("你选择的不是XML文件，请重新选择");
        obj.outerHTML = obj.outerHTML; //这样清空，在IE8下也能执行成功
    } else {
        $("#generate-item").html('<i class="fa fa-spinner"></i> 项目生成中……');
        $("#generate-item").css("color", "#F00");
        /*$("#generate-item").click(function() {
            showDialog('<i class="fa fa-spinner"></i> <p>项目正在努力生成中，请稍候点击下载！</p><p>大约需要1分钟。</p>', "<i class='fa fa-info-circle'></i> 系统提示");
        });*/
        var formData = new FormData();
        formData.append("file", $(obj)[0].files[0]);
        $.ajax({
            url: '/uploadXml',
            type: 'POST',
            data: formData,
            processData : false,
            // 告诉jQuery不要去设置Content-Type请求头
            contentType : false,
            success: function(res) {
                console.log(res);
                $("#generate-item").html('<i class="fa fa-download"></i> 生成完成，点击下载');
                $("#generate-item").css("color", "#00F");

                $("#generate-item").click(function() {
                    window.location.href = res;
                });
            },
            error: function(res) {
                alert("提交出错:"+res);
            }
        });
    }
}
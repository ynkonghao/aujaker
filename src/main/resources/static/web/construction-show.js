$(function() {
    $(".save-item").click(function() {
        saveItem($(this));
    });
});

function saveItem(obj) {
    var canBuild = $(obj).attr("remove");
    if(canBuild!='1') {
        var hasMaven = $("input[name='hasMaven']").val();
        var hasDb = $("input[name='hasDb']").val();
        var modelCount = parseInt($(".modelSize").html());
        //alert(hasMaven+"=="+hasDb+"=="+modelCount);
        if(hasMaven!='1' || hasDb!='1' || modelCount<=0) {
            showDialog("数据不全，无法创建项目……", '<i class="fa fa-info-circle"></i> 系统提示');
        } else {
            $(obj).attr("disabled", "disabled");
            $(obj).html("<i class='fa fa-spinner'></i> 项目生成中……");
            $.post('/construction/build',{}, function(res) {
//                console.log(res.msg);
                if(res.code=='1') {
                    $(obj).html('<i class="fa fa-download"></i> 生成完成，点击下载');
                    $(obj).removeAttr("disabled");
                    $(obj).attr("remove","1");

                    $(obj).click(function() {
                        window.location.href = res.msg;
                    });
                } else {
                    alert("出现错误："+res.msg);
                }
            }, "json");
        }
    }
}
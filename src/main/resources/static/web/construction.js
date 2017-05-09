$(function() {
    $(".save-btn").click(function() {
        var step = $(this).attr("step");
        if(step=='1') {
            saveMaven();
        } else if(step=='2') {
            saveDb();
        }
    });

    $("input[name='type']").change(function() {
        checkDbType();
    });
    checkDbType();

    $(".add-class-btn").click(function() {
        addClass();
    });
});

function checkDbType() {
    var val = $("input[name='type']:checked").val();
    if(val!='mysql') {
        $(".mysql-need").css("display", "none");
        $(".db-part").html(sqliteHtml());
    } else {
        $(".mysql-need").css("display", "block");
        $(".db-part").html(mysqlHtml());
    }
}

function saveMaven() {
    var groupId = $("input[name='groupId']").val();
    var artifactId = $("input[name='artifactId']").val();
    if($.trim(groupId)=='' || $.trim(artifactId)=='') {
        showDialog("GroupId和ArtifactId均不能为空", "<i class='fa fa-info-circle'></i> 系统提示");
    } else {
        $.post("/construction/save", {step:'1', groupId : groupId, artifactId : artifactId}, function(res) {
            if(res=='0') {
                showDialog('参数异常，请修改后重新提交', "<i class='fa fa-info-circle'></i> 系统提示");
            } else {
                showDialog('暂存成功', "<i class='fa fa-check'></i> 系统提示");
            }
        }, "json");
    }
}

function saveDb() {
    var type = $("input[name='type']:checked").val();
    var url = $('input[name="url"]').val();
    var port = $('input[name="port"]').val();
    var database = $('input[name="database"]').val();
    var username = $('input[name="username"]').val();
    var password = $('input[name="password"]').val();

    var checkRes = checkDb(type, url, port, database, username, password);
    if(checkRes) {
        $.post("/construction/save", {step:'2', type:type,url:url,port:port,database:database,username:username,password:password}, function(res) {
            if(res=='-1') {
                showDialog('端口号必须是整数', "<i class='fa fa-info-circle'></i> 系统提示");
            } else if(res=='-2') {
                showDialog('参数异常，请修改后重新提交', "<i class='fa fa-info-circle'></i> 系统提示");
            } else {
                showDialog('暂存成功', "<i class='fa fa-check'></i> 系统提示");
            }
        }, "json");
    } else {
        showDialog('参数异常，请修改后重新提交', "<i class='fa fa-info-circle'></i> 系统提示");
    }
}

function checkDb(type, url, port, database, username, password) {
    if($.trim(database)=='') {return false;}
    if('mysql'==type) {
        if($.trim(url)=='' || $.trim(port)=='' || $.trim(username)=='' || $.trim(password)=='') {
            return false;
        }
    }
    return true;
}

function mysqlHtml() {
    var url = $("input[name='url-hide']").val();
    var port = $("input[name='port-hide']").val();
    var db = $("input[name='db-hide']").val();
    var html = '<div class="input-group-addon">数据库：</div>'+
               '<input name="url" type="text" class="form-control" value="'+url+'" placeholder="如：localhost" />'+
                '<div class="input-group-addon">:</div>'+
                '<input name="port" type="text" class="form-control" value="'+port+'" placeholder="如：3306" />'+
                '<div class="input-group-addon">/</div>'+
                '<input name="database" type="text" class="form-control" value="'+db+'" placeholder="如：aujaker" />';

    return html;
}

function sqliteHtml() {
    var db = $("input[name='db-hide']").val();
    var html = '<div class="input-group-addon">数据库：</div>'+
                    '<input name="database" type="text" class="form-control" value="'+db+'" placeholder="如：aujaker" />';
    return html;
}

function addClass() {
    var html = '<div class="form-group ">'+
                    '<div class="input-group">'+
                        '<div class="input-group-addon">类名：</div>'+
                        '<input name="className" type="text" class="form-control" placeholder="类名（英文），如：User" />'+
                    '</div>'+
                '</div>'+
                '<div class="form-group ">'+
                    '<div class="input-group">'+
                        '<div class="input-group-addon">表名：</div>'+
                        '<input name="tableName" type="text" class="form-control" placeholder="表名（英文），如：t_user" />'+
                    '</div>'+
                '</div>'+
                '<div class="form-group ">'+
                    '<div class="input-group">'+
                        '<div class="input-group-addon">说明：</div>'+
                        '<input name="classShowName" type="text" class="form-control" placeholder="说明，如：用户" />'+
                    '</div>'+
                '</div>'+
                '<div class="form-group ">'+
                    '<div class="input-group">'+
                        '<div class="input-group-addon">作者：</div>'+
                        '<input name="author" type="text" class="form-control" placeholder="作者（可空）" />'+
                    '</div>'+
                '</div>';
    var thisDialog = confirmDialog(html, '<i class="fa fa-plus"></i> 添加实体', function() {
        checkClass(thisDialog);
    });
}

function checkClass(obj) {
    var className = $("input[name='className']").val();
    var tableName = $("input[name='tableName']").val();
    var classShowName = $("input[name='classShowName']").val();
    var author = $("input[name='author']").val();

    if($.trim(className)=='' || $.trim(tableName)=='' || $.trim(classShowName)=='') {
        showDialog('参数异常，请修改后重新提交', "<i class='fa fa-info-circle'></i> 系统提示");
    } else {
        addSingleClass(classShowName, className, tableName, author);
        $(obj).remove();
    }
}

function addSingleClass(showName, className, tableName, author) {
    $.post("/construction/save", {step:'3', type:'cls', className:className, tableName:tableName, author:author, showName:showName}, function(res) {
        if(res=='1') {
            //alert("保存成功");
            var html = buildSingleClass(showName, className, tableName, author);
            $(".class-list").append(html);
            window.location.reload();
        }
    }, "json");

}

function buildSingleClass(showName, className, tableName, author) {
    if(author) {author='作者：'+author;}
    else {author = '';}
    var html = '<div class="col-md-4"><div class="panel panel-danger single-class" className="'+className+'">'+
                    '<div class="panel-heading" title="'+author+'">'+showName+'（'+className+'，表名：'+tableName+'）<a href="javascript:void(0)" onclick="removeCls(this)" className="'+className+'" title="删除实体类" style="float:right;"><i class="fa fa-remove"></i></a></div>'+
                    '<div class="panel-body"></div>'+
                    '<div class="panel-footer"><button class="btn btn-primary" onclick="addProperty(this)"><i class="fa fa-plus"></i> 添加属性</button></div>'+
                '</div></div>';

    return html;
}

function removeCls(obj) {
    var clsName = $(obj).attr("className");
    var html = '确定删除实体类：'+clsName+" 吗？";
    var thisDialog = confirmDialog(html, '<i class="fa fa-question-circle"></i> 删除提示', function() {
        $.post("/construction/remove", {type:'cls', className:clsName}, function(res) {
            if(res=='1') {
                $(".single-class[className='"+clsName+"']").parents(".col-md-4").remove();
            }
            $(thisDialog).remove();
        }, "json");
    });
}

function addProperty(obj) {
    var html = '<div class="form-group ">'+
                    '<div class="input-group">'+
                        '<div class="input-group-addon">属性名称：</div>'+
                        '<input name="pro-name" type="text" class="form-control" placeholder="属性名称（英文），如：id" />'+
                    '</div>'+
                '</div>'+

                '<div class="form-group ">'+
                    '<div class="input-group">'+
                        '<div class="input-group-addon">字段名称：</div>'+
                        '<input name="pro-column-name" type="text" class="form-control" placeholder="字段名称（英文），如：id" />'+
                    '</div>'+
                '</div>'+

                '<div class="form-group">'+
                    '<div class="input-group">'+
                        '<div class="input-group-addon">类型</div>'+
                        '<div class="form-control">'+
                            '<input type="radio" name="pro-type" value="String" id="type_1" /><label for="type_1">&nbsp;String</label>'+
                            '<input type="radio" name="pro-type" value="Integer" id="type_2" /><label for="type_2">&nbsp;Integer</label>'+
                            '<input type="radio" name="pro-type" value="Double" id="type_3" /><label for="type_3">&nbsp;Double</label>'+
                            '<input type="radio" name="pro-type" value="java.util.Date" id="type_4" /><label for="type_4">&nbsp;Date</label>'+
                        '</div>'+
                    '</div>'+
                '</div>'+

                '<div class="form-group ">'+
                    '<div class="input-group">'+
                        '<div class="input-group-addon">说明：</div>'+
                        '<input name="pro-comment" type="text" class="form-control" placeholder="说明（中文），如：主键" />'+
                    '</div>'+
                '</div>'+
                '<div class="form-group">'+
                    '<div class="input-group">'+
                        '<div class="input-group-addon">是否主键</div>'+
                        '<div class="form-control">'+
                            '<input type="radio" name="pro-pk" value="1" id="pk_1" /><label for="pk_1">&nbsp;是主键</label>'+
                            '<input type="radio" name="pro-pk" value="0" id="pk_0" checked="checked"/><label for="pk_0">&nbsp;非主键</label>'+
                            '&nbsp;&nbsp;(每个对象有且只有一个主键)'+
                        '</div>'+
                    '</div>'+
                '</div>'+
                '<div class="form-group">'+
                    '<div class="input-group">'+
                        '<div class="input-group-addon">是否长文本</div>'+
                        '<div class="form-control">'+
                            '<input type="radio" name="pro-lob" value="1" id="lob_1" /><label for="lob_1">&nbsp;是长文本</label>'+
                            '<input type="radio" name="pro-lob" value="0" id="lob_0" checked="checked"/><label for="lob_0">&nbsp;非长文本</label>'+
                        '</div>'+
                    '</div>'+
                '</div>';
    var thisDialog = confirmDialog(html, '<i class="fa fa-plus"></i> 添加对象属性', function() {
        checkProperty(obj, thisDialog);
    });
}

function checkProperty(clsObj, obj) {
    var name = $("input[name='pro-name']").val();
    var columnName = $("input[name='pro-column-name']").val();
    var type = $("input[name='pro-type']:checked").val();
    var comment = $("input[name='pro-comment']").val();
    var pk = $("input[name='pro-pk']:checked").val();
    var lob = $("input[name='pro-lob']:checked").val();
    var className = $(clsObj).parents(".single-class").attr("className");

    if($.trim(name)=='' || $.trim(columnName)=='' || $.trim(type)=='' || $.trim(comment)=='' || $.trim(pk)=='' || $.trim(lob)=='') {
        showDialog('参数异常，请修改后重新提交', "<i class='fa fa-info-circle'></i> 系统提示");
    } else {
        addSinglePro(name, columnName, type, comment, pk, lob, className);
        $(obj).remove();
    }
}

function addSinglePro(name, columnName, type, comment, pk, lob, className) {
//alert(name+"=="+columnName+"=="+type+"=="+comment+"==");
    $.post("/construction/save", {step:'3', type:'pro', columnName:columnName, name:name, fieldType:type, comment:comment, pk:pk, lob:lob, className:className}, function(res) {
        if(res=='1') {
            //alert("保存成功");
            var html = buildSinglePro(name, columnName, type, comment, pk, lob, className);
            $(".single-class[className='"+className+"']").find(".panel-body").append(html);
            window.location.reload();
        }
    }, "json");
}

function buildSinglePro(name, columnName, type, comment, pk, lob, className) {
    var html = '<div class="alert alert-info" className="'+className+'">'+
                   '<a href="javascript:void(0)" onclick="removePro(this)" className="'+className+'" proName="'+name+'" style="float:right;" title="删除属性"><i class="fa fa-remove"></i></a>'+
                   '<p>'+type+' '+name+'（'+columnName+'）</p>'+
                   '<p>'+comment+'，'+(pk=='1'?'主键':'非主键')+'，'+(lob=='1'?'长文本':'非长文本')+'</p>'+
               '</div>';

    return html;
}

function removePro(obj) {
    var clsName = $(obj).attr("className");
    var name = $(obj).attr("proName");
//alert("=="+clsName+"=="+name);
    var html = '确定删除实体类：'+clsName+" 的属性 "+name+" 吗？";
    var thisDialog = confirmDialog(html, '<i class="fa fa-question-circle"></i> 删除提示', function() {
        $.post("/construction/remove", {type:"pro", className:clsName, name:name}, function(res) {
            if(res=='1') {
                $(obj).parents(".alert[className='"+clsName+"']").remove();
            }
            $(thisDialog).remove();
        }, "json");
    });
}
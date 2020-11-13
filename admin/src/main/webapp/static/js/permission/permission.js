/**
 * 权限管理
 */
var pageCurr;
var form;
$(function() {
    layui.use('table', function(){
        var table = layui.table;
        form = layui.form;

        tableIns=table.render({
            id:'id',
            elem: '#permissionList',
            url:'/permission/getPermitList',
            method: 'post', //默认：get请求
            cellMinWidth: 80,
            page: true,
            even: true,
            request: {
                pageName: 'pageNum', //页码的参数名称，默认：pageNum
                limitName: 'pageSize' //每页数据量的参数名，默认：pageSize
            },
            response:{
                statusName: 'code', //数据状态的字段名称，默认：code
                statusCode: 200, //成功的状态码，默认：0
                countName: 'total', //数据总数的字段名称，默认：count
                dataName: 'list' //数据列表的字段名称，默认：data
            },
            cols: [[
                {type:'numbers'/*,width:"5%"*/}
                /*,{field:'id', title:'id'}
                ,{field:'parentId', title:'parentId'}*/
                ,{field:'permCode', title:'权限编码',align:'center'/*,width:"10%"*/}
                ,{field:'permName', title:'权限名称',align:'center'/*,width:"10%"*/}
                ,{field:'url', title:'菜单url',align:'center'/*,width:"15%"*/}
                ,{field:'parentName', title:'父菜单名称',align:'center'/*,width:"10%"*/}
                ,{fixed:'right',title:'操作',align:'center', toolbar:'#optBar'/*,width:"25%"*/}
            ]],
            done: function(res, curr, count){
                $("[data-field='parentName']").children().each(function(){
                    if($(this).text() == ''){
                        $(this).text("根目录");
                    }else {
                        $(this).text($(this).text());
                    }
                });
                pageCurr=curr;

            }
        });


        //监听工具条
        table.on('tool(permissionTable)', function(obj){
            var data = obj.data;
            if(obj.event === 'del'){
                //删除
                del(data,data.id);
            } else if(obj.event === 'edit'){
                //编辑
                edit(data);
            }
        });

        //监听提交
        form.on('submit(permissionSubmit)', function(data){
            formSubmit(data);
            return false;
        });

    });

});

//提交表单
function formSubmit(obj){
    $.ajax({
        type: "post",
        data: $("#permissionForm").serialize(),
        url: "/permission/saveOrUpdatePermit",
        success: function (data) {
            if (data.code == 200) {
                layer.alert(data.msg,function(){
                    layer.closeAll();
                    load(obj);
                });
            } else {
                layer.alert(data.msg);
            }
        },
        error: function () {
            layer.alert("操作请求错误，请您稍后再试",function(){
                layer.closeAll();
                load(obj);
            });
        }
    });
}
//新增
function add() {
    edit(null,"新增");
}
//打开编辑框
function edit(data,title){
    var parentId = null;
    if(data == null){
        $("#id").val("");
    }else{
        //回显数据
        $("#id").val(data.id);
        $("#permCode").val(data.permCode);
        $("#permName").val(data.permName);
        $("#url").val(data.url);
        $("#remark").val(data.remark);
        parentId = data.pid;
    }
    var pageNum = $(".layui-laypage-skip").find("input").val();
    $("#pageNum").val(pageNum);
    $.ajax({
        url:'/permission/getParentPermitList',
        dataType:'json',
        async: true,
        success:function(data){
            /*if(!data.code){

            }*/

            $("#pid").html("<option value='0'>根目录</option>");
            $.each(data,function(index,item){
                if(!parentId){
                    var option = new Option(item.permName,item.id);
                }else {
                    var option = new Option(item.permName,item.id);
                    // 如果是之前的parentId则设置选中
                    if(item.id == parentId) {
                        option.setAttribute("selected",'true');
                    }
                }
                $('#pid').append(option);//往下拉菜单里添加元素
                form.render('select'); //这个很重要
            })
        }
    });

    layer.open({
        type:1,
        title: title,
        fixed:false,
        resize :false,
        shadeClose: true,
        area: ['550px'],
        content:$('#setPermission'),
        end:function(){
            cleanPermission();
        }
    });
}

function cleanPermission() {
    $("#permCode").val("");
    $("#permName").val("");
    $("#url").val("");
    $("#remark").val("");
}

//重新加载table
function load(obj){
    tableIns.reload({
        where: obj.field
        , page: {
            curr: 1 //从第一页开始
        }
    });
}

//删除
function del(obj,id) {
    if(null!=id){
        layer.confirm('您确定要删除吗？', {
            btn: ['确认','返回'] //按钮
        }, function(){
            $.post("/permission/delPermit",{"id":id},function(data){
                if (data.code == 200) {
                    layer.alert(data.msg,function(){
                        layer.closeAll();
                        load(obj);
                    });
                } else {
                    layer.alert(data.msg);
                }
            });
        }, function(){
            layer.closeAll();
        });
    }
}
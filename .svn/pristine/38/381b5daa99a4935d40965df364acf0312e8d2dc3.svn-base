/**
 * 角色管理
 */
//'formSelects.render('permissions');
var pageCurr;
var form;

$(function() {

    layui.use('table', function(){
        var table = layui.table;
        form = layui.form;

        tableIns=table.render({
            elem: '#roleList',
            url:'/role/getRoleList',
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
                {type:'numbers'}
                ,{field:'roleCode', title:'角色编码',align:'center'}
                ,{field:'roleName', title:'角色名称',align:'center'}
                ,{field:'permissions', title:'权限',align:'center'}//TODO 后期使用templet做成下拉框
                ,{field:'admin', title:'是否管理员',align:'center'}
                ,{fixed:'right',title:'操作',align:'center', toolbar:'#optBar'}
            ]],
            done: function(res, curr, count){
                $("[data-field='admin']").children().each(function(){
                    if($(this).text()=='1'){
                        $(this).text("是")
                    }else if($(this).text()=='0'){
                        $(this).text("否")
                    }
                });
                pageCurr=curr;

            }
        });


        //监听工具条
        table.on('tool(roleTable)', function(obj){
            var data = obj.data;
            if(obj.event === 'del'){
                //删除
                delRole(data,data.id);
            } else if(obj.event === 'edit'){
                //编辑
                edit(data,"编辑");
            }
        });

        //监听提交
        form.on('submit(roleSubmit)', function(data){
            formSubmit(data);
            return false;
        });

    });

});

//提交表单
function formSubmit(obj){
    $.ajax({
        type: "post",
        data: $("#roleForm").serialize(),
        url: "/role/saveOrUpdateRole",
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
    edit(null,"新增角色");
}
//打开编辑框
function edit(data,title){
    var pid = [];
    if(data == null){
        $("#id").val("");
    }else{
        //回显数据
        $("#id").val(data.id);
        $("#roleCode").val(data.roleCode);
        $("#roleName").val(data.roleName);
        $.post("/permission/getChildPermitsForRole", {"roleId": data.id}, function (result) {
            if (!result.code) {
                $.each(result, function (index, item) {
                    pid.push(item.id);

                })
            } else {
                layer.alert(result.msg);
            }
        });

    }

    formSelects.data('permissions', 'server', {
        url: '/permission/getAllChildPermits',
        keyName: 'permName',
        keyVal: 'id',
        success: function(id, url, searchVal, result){      //使用远程方式的success回调

            if(pid.length != 0){
                formSelects.value('permissions', pid);
            }

        },
        error: function(id, url, searchVal, err){           //使用远程方式的error回调
                                                            //同上
            console.log(err);   //err对象
        },
    });


    var pageNum = $(".layui-laypage-skip").find("input").val();
    $("#pageNum").val(pageNum);

    layer.open({
        type:1,
        title: title,
        fixed:false,
        resize :true,
        shadeClose: true,
        area: ['550px','550px'],
        content:$('#setRole'),
        end:function(){
            cleanRole();
        }
    });

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
function delRole(obj,id) {
    if(null!=id){
        layer.confirm('您确定要删除吗？', {
            btn: ['确认','返回'] //按钮
        }, function(){
            $.post("/role/delRole",{"id":id},function(data){
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

function cleanRole() {
    $("#roleCode").val("");
    $("#roleName").val("");
    $("#permissions").val("");
}


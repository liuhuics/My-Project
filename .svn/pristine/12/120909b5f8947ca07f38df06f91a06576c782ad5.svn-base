/**
 * 修改用户密码
 * */
$(function(){
    layui.use(['form' ,'layer'], function() {
        var form = layui.form;
        //确认修改密码
        form.on("submit(updatePwd)",function () {
            updatePwd();
            return false;
        });
    })
})
function updatePwd(){
    var pwd=$("#pwd").val();
    var rePwd=$("#rePwd").val();
    $.post("/sysUser/updatePwd",{"pwd":pwd,"rePwd":rePwd},function(data){
        console.log("data:"+data);
        if(data.code==200){
            layer.alert("操作成功",function () {
                layer.closeAll();
                window.location.href="/logout";
            });
        }else{
            layer.alert(data.message,function () {
                layer.closeAll();
            });
        }
    });
}

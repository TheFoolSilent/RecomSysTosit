
function signin() {
    if ($('#username').val() != '' && $('#password').val() != '') {
        $.ajax(
            {
                contentType: "application/json",
                type: "POST",
                datatype: "json",
                url: "/login",
                data: JSON.stringify({"username": $('#username').val(), "password": $('#password').val()}),
                success: function (result) {
                    // console.log(result);
                    var data = eval("(" + result + ")");
                    if (data.msg == "1") {
                        alert("登录成功！");
                        $('#productModal').modal("hide");
                        // window.location.replace('http://localhost:8080/index');
                        // TODO
                    }
                    else if(data.msg == "0")
                    {
                        window.location.replace('http://localhost:8080/administer');
                    }
                    else
                        alert("输入错误！");

                },
                error: function (result) {
                    var data = eval("(" + result + ")");
                    alert(data.description);
                }
            });
    } else
        alert("账号密码不可为空！");
}

function signup() {
    window.location.replace('http://localhost:8080/register');
}

function Post()
{
    if($('#r_password').val() == $('#password').val()) {
        $.ajax(
            {
                contentType:"application/json",
                type: "POST",
                datatype: "json",
                url: "/registerraise",
                data: JSON.stringify({"username": $('#name').val(),"password": $('#password').val()}),
                success: function (result) {
                    // console.log(result);
                    var data = eval("("+result+")");
                    if (data.msg== "1")
                    {
                        alert("注册成功！");
                        window.location.replace("/register");
                    }
                },
                error: function (result) {
                    var data = eval("("+result+")");
                    alert(data.description);
                }
            });
    }else{
        alert("两次密码不一致，请重新输入！");
    }
}

$("#conform").click(function () {
    var formData = new FormData();
    formData.append("file",$("#fileupload")[0].files[0]);
    formData.append("service",'App.Passion.UploadFile');
    // formData.append("token",token);
    $.ajax({
        url:'/uploadfile', /*接口域名地址*/
        type:'post',
        data: formData,
        contentType: false,
        processData: false,
        success:function(res){
            console.log(res);
            if(res["msg"]=="success"){
                alert('成功');
            }else if(res["msg"]=="error"){
                alert('失败');
            }else{
                console.log(res);
            }
        }
    })
})



function f() {
    for(i){
        var str = `<li>?      </li>`.format(i)
    }

    $("#filelist").html5();
}
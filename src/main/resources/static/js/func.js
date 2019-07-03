// Login

function signin() {

    if ($('#username').val() != '' && $('#password').val() != '') {

//         // if ($("#rememberme").is(":checked")) {
//             //存储一个带7天期限的cookie
//
//         //     $.cookie("username", $("#username").val(), { path: "/", expires: 7 });
//         //     $.cookie("password", $("#password").val(), { path: "/", expires: 7 });
//         //
//         // }
//         $('#login_url').submit();

        // else {
        //     $.cookie("username", "", { path: "/", expires: -1 });
        //     $.cookie("password", "", { path: "/", expires: -1 });
        // }

        $.ajax(
            {
                contentType: "application/json",
                type: "POST",
                datatype: "json",
                url: "/login",
                data: JSON.stringify({"username": $('#username').val(), "password": $('#password').val()}),
                success: function (result) {
                    // console.log(result);
                    var datajson = eval("(" + result + ")");
                    if (datajson.msg == "1") { // user
                        alert("登录成功！");
                        // $('#productModal').modal("hide");
                        window.location.reload();
                        // $.cookie("username", datajson.username, { expires: 7 });
                        // $.cookie("password", datajson.password, { expires: 7 });

                        // $('#loginpos').html("<label>" + datajson.username + "</label>");

                    }
                    else if(datajson.msg == "0") {  // root

                        // $.cookie("username", datajson.username, { expires: 7 });
                        // $.cookie("password", datajson.password, { expires: 7 });

                        window.location.replace('http://localhost:8080/administer');
                    }
                    else
                        alert("输入错误！");

                },
                error: function (result) {
                    var datajson = eval("(" + result + ")");
                    alert(datajson.description);
                }
            });

    } else
        alert("账号密码不可为空！");
}

function signup() {
    window.location.replace('http://localhost:8080/register');
}


// Register
function Post()
{
    if($('#r_password').val() == $('#password').val()) {
        // $("#register_url").submit();
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
                        window.location.replace("/");
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


// upload file
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
            var data = eval("("+result+")");
            if(data.msg == "success"){
                alert('成功');
            }else if(data.msg== "error"){
                alert('失败');
            }else{
                console.log(res);
            }
        }
    })
})



// function f() {
//     for(i){
//         var str = `<li>?      </li>`.format(i)
//     }
//
//     $("#filelist").html5();
// }
// function addRow(){
//     //行的长度
//     var rowlength=document.getElementById("order").rows.length;
//     //得到整个表格对象
//     var order = document.getElementById("order").insertRow(rowlength-1);
//     order.id=rowlength-1;
//     //插入列
//     var cel1=order.insertCell(0).innerHTML="file1";
//
//     var cel4=order.insertCell(1).innerHTML="<input type=\"button\"value=\"Delete\" onclick=\"delRow('"+(rowlength-1)+"')\"/>"+ "<input type=\"checkbox\"\"/>"
// }

// function delRow(qwe){
//     var ewq=document.getElementById(qwe).rowIndex;
//     document.getElementById("order").deleteRow(ewq);
// }


function signin() {
    if ($('#username').val() != '' && $('#password').val() != '') {

        if ($("#rememberme").is(":checked")) {
            //存储一个带7天期限的cookie
            $.cookie("username", $('#username').val(), {expires: 7});
            $.cookie("password", $('#password').val(), {expires: 7});
            // }else {
            //     $.cookie("yhm", "", { expires: -1 });
            //     $.cookie("mm", "", { expires: -1 });
            // }
        }
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


// <script>
function KeyDown()
{
    if (event.keyCode == 13)
    {
        signin();
    }
}
// </script>



function signout() {
        $.ajax(
            {
                // contentType: "application/json",
                type: "GET",
                datatype: "json",
                url: "/logout?state=1",
               // data: JSON.stringify({"username": $('#username').val(), "password": $('#password').val()}),
                success: function (result) {
                    // console.log(result);
                    var datajson = eval("(" + result + ")");
                    if (datajson.msg == "OK") { // user
                        alert("退出成功！");
                        // $('#productModal').modal("hide");
                        window.location.reload();
                    }
                    else alert("失败！");
                },
                error:function (result) {
                    alert("!!!");

                }


            })
}
$("#logout").click(function () {
    signout();
})
$("#adout").click(function(){

    $.ajax(
        {
            // contentType: "application/json",
            type: "GET",
            datatype: "json",
            url: "/logout?state=1",
            // data: JSON.stringify({"username": $('#username').val(), "password": $('#password').val()}),
            success: function (result) {
                // console.log(result);
                var datajson = eval("(" + result + ")");
                if (datajson.msg == "OK") { // user
                    alert("退出成功！");
                    // $('#productModal').modal("hide");
                    window.location.replace('http://localhost:8080/');
                }
                else alert("失败！");
            },
            error:function (result) {
                alert("!!!");

            }


        })
})











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


showLoading = function (loadText) {
    if (!loadText) {
        $("#loadText").html(loadText)
    }
    $('#loadingModal').modal({backdrop: 'static', keyboard: false});
}
hideLoading = function () {
    $('#loadingModal').modal('hide');
}


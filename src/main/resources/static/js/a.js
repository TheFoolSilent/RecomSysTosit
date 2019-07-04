$(function() {
    reder()
})//DOM加载完毕后执行函数且只执行一次

function reder() {
    var option = {
        url: "js/data.json",
        searfunc: searfunc

    }

    function searfunc(arr) {
        //arr为查询按钮之后输出的搜索条件
        console.log(arr);
        var data = [];
        for(item in arr)
        {
            console.log(arr[item])
            console.log(arr[item]['val'])
            for (item_one in arr[item]['val']){
                data.push(arr[item]['val'][item_one]);
            }
        }
        console.log(data);
        $.ajax(
            {
                contentType:"application/json",
                type: "POST",
                datatype: "json",
                url: "/findjob",
                data: JSON.stringify({"state": "1","wantjob": "", skillset:data}),
                success: function (result) {
                    // console.log(result);
                    var data = eval("("+result+")");
                    if (data.msg== "success")
                    {
                        alert("注册成功！");
                        window.location.replace("/");
                    }
                },
                error: function (result) {
                    var data = eval("("+result+")");
                    alert(data.description);
                }
            })
    }

    $("#searchcon").SEarch(option);
}
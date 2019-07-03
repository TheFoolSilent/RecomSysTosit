$(function() {
    reder()
})

function reder() {
    var option = {
        url: "js/data.json",
        searfunc: searfunc

    }

    function searfunc(arr) {
        //arr为查询按钮之后输出的搜索条件
        console.log(arr);
    }

    $("#searchcon").SEarch(option);
}
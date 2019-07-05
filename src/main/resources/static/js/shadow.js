/*全局遮罩层*/
var globalmodal = function (element, action) {
    var mod = $("#myModal");//全局遮罩层
    if (action == true) {
        /*打开遮罩层*/
        element.spinModal();
        mod.attr("style", "display:block");
    }
    else {
        /*关闭遮罩层*/
        element.spinModal(false);
        mod.attr("style", "display:none");
    }
    /*遮罩层样式及位置*/
    mod.height(element.height() + 10);//遮罩层高度
    mod.width(element.width());//设置遮罩层宽度
    mod.offset(element.offset());//根据遮罩对象来进行定位
}
$(function () {
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
        for (item in arr) {
            console.log(arr[item])
            console.log(arr[item]['val'])
            for (item_one in arr[item]['val']) {

                var temp = arr[item]['val'][item_one].toLowerCase();

                data.push(temp);
            }
        }
        console.log(data);
        showLoading();

        $.ajax(
            {
                contentType: "application/json",
                type: "POST",
                datatype: "json",
                url: "/findjob",
                data: JSON.stringify({"state": "1", "wantjob": "", "skillset": data}),
                success: function (result) {
                    console.log(typeof (result));
                    var datajson = eval("(" + result + ")");
                    if (datajson['msg'] == "success") {
                        console.log("OK");
                        var Frame = "";


                        for (i  in datajson['job_list']) {

                            // Frame += "<div class='single-job-post fix'>" +
                            //     "<div class='job-title col-4 pl-30'>" +
                            //     "<span class='pull-left block mtb-17'>" +
                            //     "<a href=''#'><img src='images/company-logo/1.png'></a>" +
                            //     "</span>" +
                            //     "<div class='fix pl-30 mt-29'>" +
                            //     " <h4 class='mb-5'>" + datajson['job_list'][i] + "</h4>" +
                            //     "<h5><a href='#'>Devitems</a></h5>" +
                            //     "</div>" +
                            //     "</div>" +
                            //     "<div class='address col-4 pl-50'>" +
                            //     " <span class='mtb-30 block'>2020 Willshire Glen,<br>" +
                            //     "Alpharetta, GA-30009</span>" +
                            //     "</div>" +
                            //     "<div class='time-payment col-2 pl-60 text-center pt-22'>" +
                            //     "<span class='block mb-6'>€ 200.00</span>" +
                            //     "<a href='#' class='button button-red'>Full Time</a>" +
                            //     "</div>" +
                            //     "</div>";

                            Frame+= "<div class='single-job-post fix'>"+
                                "<div class='job-title col-4 pl-30'>"+
                                "<span class='pull-left block mtb-17'>"+
                                "<a href=''#'><img src='te1.png'></a>"+
                                "</span>"+
                                "<div class='fix pl-30 mt-29'>"+
                                " <h4 class='mb-5'>"+datajson['job_list'][i]+"</h4>"+
                                "<h5><a href='#'></a></h5>"+
                                "</div>"+
                                "</div>"+
                                "<div class='address col-4 pl-50'>"+
                                " <span class='mtb-30 block'></span>"+
                                "</div>"+
                                "<div class='time-payment col-2 pl-60 text-center pt-22'>"+
                                "<span class='block mb-6'></span>"+
                                "<a href='https://baike.baidu.com/item/"+datajson['job_list'][i]+"' target='_blank' class='button button-red'>Details</a>"+
                                "</div>"+
                                "</div>";

                        }
                        console.log(Frame);
                        $("#joblist").html(Frame);
                        hideLoading();
                    }
                },
                error: function (result) {
                    var data = eval("(" + result + ")");
                    alert(data.description);
                }
            })
    }

    $("#searchcon").SEarch(option);
}
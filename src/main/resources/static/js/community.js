function post() {
    var questionId = $("#question_id").val();
    var content = $("#comment_content").val();
    if (!content) {
        alert("不能回复空内容~~");
        return;
    }
    $.ajax({
        type: "POST",
        url: "/comment",
        contentType: 'application/json',
        data: JSON.stringify({
            "parentId": questionId,
            "content": content,
            "type": 1
        }),
        success: function (response) {
            //如果成功隐藏对话框
            if (response.code == 200) {
                window.location.reload();
                $("#comment_section").hide();
            } else {
                if (response.code == 2003) {
                    var confirm1 = confirm(response.message);
                    if (confirm1) {
                        window.open("https://github.com/login/oauth/authorize?client_id=6f67a01d2d93cff1d8ad&redirect_uri=http://localhost:8832/callback&scope=user&state=1")
                        window.localStorage.setItem("closable", true);
                    }
                } else {
                    alert(response.message);
                }
            }
        },
        dataType: "json"
    });
}

/*
*展开二级评论
 */
function collapseComments(e) {
    var id = e.getAttribute("data-id");

    console.log(id);
}
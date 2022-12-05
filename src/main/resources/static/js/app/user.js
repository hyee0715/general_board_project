var main = {
    init : function () {
        var _this = this;
        $('#btn-save').on('click', function () {
            _this.save();
        });
    },
    save : function () {
        var data = {
            username: $('#username').val(),
            nickname: $('#nickname').val(),
            password: $('#password').val(),
            email: $('#email').val()
        };

        $.ajax({
            type: 'POST',
            url: '/user/signUp',
            dataType: 'json',
            contentType:'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function() {
            alert('회원이 정상적으로 등록되었습니다.');
            window.location.href = '/';
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    }
};

main.init();
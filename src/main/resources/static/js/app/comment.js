var main = {
    init : function () {
        var _this = this;

        $('#btn-comment-save').on('click', function () {
            _this.commentSave();
        });

        document.querySelectorAll('#btn-comment-update').forEach(
            function (item) {
                item.addEventListener('click', function() { //버튼 클릭 했을 때
                    const form = this.closest('form'); //버튼과 가장 가까운 조상의 form을 반환
                     _this.commentUpdate(form); //해당 form으로 commentUpdate 함수 실행
                });
            }
        );
    },
    commentSave : function () {
        var data = {
            boardId: $('#boardId').val(),
            content: $('#content').val(),
        };

        if (!data.content || data.content.trim() === "") { // 공백 및 빈 문자열 체크
            alert("댓글 내용을 입력해주세요.");
            return false;
        } else {
            $.ajax({
                type: 'POST',
                url: '/api/board/detail/' + data.boardId + '/comments',
                dataType: 'text',
                contentType:'application/json; charset=utf-8',
                data: JSON.stringify(data)
            }).done(function() {
                alert('댓글이 등록되었습니다.');
                window.location.reload();
            }).fail(function (error) {
                alert(JSON.stringify(error));
            });
        }
    },
    commentUpdate : function (form) {
        const data = {
            id: form.querySelector('#commentId').value,
            boardId: form.querySelector('#boardIdForComment').value,
            content: form.querySelector('#comment-content').value,
        };

        if (!data.content || data.content.trim() === "") {
            alert("공백 또는 입력하지 않은 부분이 있습니다.");
            return false;
        }

        const con_check = confirm("수정하시겠습니까?");
        if (con_check === true) {
            $.ajax({
                type: 'PUT',
                url: '/api/board/detail/' + data.boardId + '/comments/' + data.id,
                dataType: 'JSON',
                contentType: 'application/json; charset=utf-8',
                data: JSON.stringify(data)
            }).done(function () {
                window.location.reload();
            }).fail(function (error) {
                alert(JSON.stringify(error));
            });
        }
    },
    commentDelete : function(boardId, commentId) {
        const con_check = confirm("삭제하시겠습니까?");
        if (con_check === true) {
            $.ajax( {
                type: 'DELETE',
                url: '/api/board/detail/' + boardId + '/comments/' + commentId,
                dataType: 'JSON',
            }).done(function () {
                alert('댓글이 삭제되었습니다.');
                window.location.reload();
            }).fail(function (error) {
                alert(JSON.stringify(error));
            });
        }
    }
};

main.init();
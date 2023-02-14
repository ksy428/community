"use strict"

$(document).ready(function(e){

    connectWs();
	getSubscribeInfo();
    getRecentBoard();

    $(".body").on("click",".visit-link", function(e){
    	e.preventDefault();

    	let boardType = $(this).attr("data-target");

    	$(this).closest("li").remove();

        deleteRecentBoard(boardType);
    });
});

function connectWs(){
    	let socket = new SockJS("/echo");
        let stompClient = Stomp.over(socket);
        stompClient.connect({}, function(frame){
            stompClient.subscribe('/topic/main', function(response){
                let post = JSON.parse(response.body);
                let toast = "";

                toast +=    '<div id="t_'+ post.postId +'" class="toast hide" role="alert" aria-live="assertive" aria-atomic="true" data-bs-delay="10000"> \n'+
                                '<div class="toast-header"> \n'+
                                    '<i class="bi-star-fill"></i> \n'+
                                    '<strong class="me-auto">실시간베스트</strong> \n'+
                                    '<small>'+ post.createdDate+'</small> \n'+
                                    '<button type="button" class="btn-close" data-bs-dismiss="toast" aria-label="Close"></button> \n'+
                                '</div> \n'+
                                '<div class="toast-body"> \n'+
                                    '<a href="/board/'+ post.boardType +'/'+ post.postId +'">' + post.title +'</a> \n'+
                                '</div> \n'+
                            '</div> \n';

                $('.toast-container').append(toast);

                let myToast = $('#t_'+post.postId);

                myToast[0].addEventListener('hidden.bs.toast', function(){
                    $(this).remove();
                });

                myToast.toast('show');

            });
        });
}

function getSubscribeInfo(){

	$.ajax({
			url: '/member/info/subscribe',
			type: 'get',
			dataType: 'json',
			success: function(result){
				//alert(JSON.stringify(result));
				let itemList ="";

				if(result.length){
				$.each(result, function(index, item) {
					itemList += '<a class="dropdown-item" href="/board/'+ item.boardType +'">'+ item.boardName +'</a> \n';
				});
				}
				else{
					itemList += '<a class="dropdown-item" href="#"> 즐겨찾기가 없습니다 </a> \n';
				}

				$('.dropdown-menu[data-subscribe]').append(itemList);
			},
			error : function(result){
				alert(result.responseText);
			}
	});
}

function getRecentBoard(){

	$.ajax({
			url: '/redis/recent-board',
			type: 'get',
			dataType: 'json',
			success: function(result){ //List<BoardInfoDto> 가져옴

			let recentBoardList ="";
            $.each(result, function(index, board) {
            	recentBoardList += '<li> \n'+
            	                        '<a href="/board/'+ board.boardType +'">' + board.boardName +'</a> \n' +
            	                        '<a href="#" class="visit-link" data-target="'+ board.boardType+'"> \n' +
            	                            '<span> <i class="bi-x"> </i> </span> \n' +
            	                        '</a> \n' +
            	                   '</li> \n';
            });

            $('.board-visit-history__list').append(recentBoardList);

			},
			error : function(result){
				alert(result.responseText);
			}
	});
}

function deleteRecentBoard(boardType){
    $.ajax({
    		url: '/redis/recent-board/delete/'+ boardType,
    		type: 'get',
    		success: function(result){
    	    },
    	    error : function(result){
    			alert(result.responseText);
    		}
    });
}

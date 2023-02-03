"use strict"

$(document).ready(function(e){

    connectWs();
	getSubscribeInfo();

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
			success: function(result){ //commentPagingDto 가져옴
				//alert(JSON.stringify(result));
				let itemList ="";

				if(result.length){
				$.each(result, function(index, item) {
					itemList += '<a class="dropdown-item" href="/board/'+ item.boardType +'">'+ item.boardName +'</a> \n'
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


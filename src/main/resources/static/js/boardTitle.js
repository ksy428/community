"use strict"

$(document).ready(function(e){
	
	
	getBoardInfo();
	
	$(".body").on("click", ".subscribe", function(e) {

		e.preventDefault();

		let form = $(this).closest("form");

		form.submit();
	});

	// 게시글 삭제
	$(".body").on("click", ".unsubcribe", function(e) {

		e.preventDefault();
		let form = $(this).closest("form");

		form.submit();

	});

});

function getBoardInfo(){
	
	$.ajax({
			url: '/boards/'+ boardType,
			type: 'get',
			dataType: 'json',
			success: function(result){			
				
				let boardTitle = "";
				boardTitle += '<a class="title" href="/board/'+ boardType +'"> <span>'+ result.boardName + '  </span></a> \n';
				if(!result.subscribe){
					boardTitle += '<form class="subscribeForm" \n'+
									'action="/board/'+ boardType +'/subscribe" method="post" > \n'+
									'<a class="subscribe" href="#" title="즐겨찾기"><i class="bi-star"></i></a> \n'+
								  '</form> \n';
				}
				else{							  
					boardTitle += '<form class="subscribeForm" \n'+
									'action="/board/'+ boardType +'/subscribe" method="post" > \n'+
									'<input type="hidden" name="_method" value="delete"> \n'+
									'<a class="unsubcribe" href="#" title="즐겨찾기취소"><i class="bi-star-fill"></i></a> \n'+
								  '</form> \n';
				}							  
				$('.head[data-title]').append(boardTitle);
							  
			},
			error : function(result){
				alert(result.responseText);
			} 
	});
}
"use strict"

$(document).ready(function(e){
		
	getSubscribeInfo();
	
});

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
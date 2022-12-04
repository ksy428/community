"use strict"

$(document).ready(function(e){
	
	getCommentList();
	
	$(".reply-form.write").attr('action','/comment/'+ postId);
	
	$("#edit-btn").on("click", function(e){
		e.preventDefault();
		
		document.location.href = window.location.href+'/edit';
	});
	
	
	//대댓글 form 생성
	$(".body").on("click",".reply-link", function(){
			
		let commentId = $(this).attr("data-target");
		
		//답글을 눌렀을때 이미 열린 답글form이 있다면 삭제, 뒤에 조건은 기본 댓글 form은 제외하는 조건 
		if($('.reply-form').length && $('.reply-form').attr("id") != 'commentForm'){
			
			//답글을 누르고 열린 답글form이 이미 존재하고, 같은 답글을 또 누르면 삭제만 하고 return
			if($('.reply-form').attr("id") == 'f_'+commentId){
				
				return $('.reply-form').remove();
			}
			$('.reply-form').remove();			
		}

		//답글 form 생성	
		let form = '<form class="reply-form" id="'+'f_'.concat(commentId,'" ')+'action="/comment/'.concat(postId,'/',commentId,'" ') +'method="post">' +
						'<div class="reply-form__container">'+
							'<div class="reply-form-textarea-wrapper">'+
								'<textarea class="reply-form-textarea" name="content"></textarea>'+
							'</div>'+
						'</div>'+
						'<div class="reply-form__submit-button-wrapper">'+
							'<button class="reply-form__submit-button" id="write-btn" type="submit">작성</button>'+
						'</div>'+
					'</form>';
		//let commentItem = $('#'+'c_'.concat(commentId));
		//commentItem.after(form);
		$('#'+'c_'.concat(commentId)).after(form);							
	});
	
	//댓글 write
	$(".body").on("click",".reply-form__submit-button",function(e){
		
		e.preventDefault();
		
		let form = $(this).closest("form");
	
		$.ajax({
			url: form.attr('action'),
			data: form.serialize(),
			type: 'post',
			success : function(result){		
			//여기에 댓글추가 html에
									
			let list_area = getCommentList();
			
			$('.list_area').append(list_area);
			//$('.article-body').append(list_area);
			}
		});
	});
});
// 댓글리스트 가져오기
function getCommentList(){

	let url = "/comment/"+ postId;
	let urlParams = new URLSearchParams(location.search);
	
	let data = {
		page: urlParams.get("page"),
		cp: urlParams.get("cp")		
	}

	$.ajax({
		url: url,
		type: 'get',
		data: data,
		success: function(result){ //commentPagingDto 가져옴
		
		$('.list-area').children().remove();
		
		//페이징으로 인해서 대댓글이 첫번째에 올 경우 grouping을 위한 변수
		let groupId = 0;
		let isFirst = true;
		
		$.each(result.commentList, function(index, comment) {
		
			let isParent = (comment.parentId  == null) ? true : false;
			let isDeleted = (comment.isDeleted) ? true : false;
		
			let commentList ="";
			let hierarchy = isParent ? '"parentcomment-wrapper"':'"recomment-wrapper"';
			let content = isDeleted ? '<span style="color: #999;"> 삭제된 댓글입니다.</span>' : comment.content;

			commentList += '<div class='+hierarchy+'> \n'+ 
								'<div class="comment-item" id=c_' + comment.commentId +'> \n'+
									'<div class="content"> \n';
			if(!isDeleted){	//삭제되지않은 댓글만 추가내용		
				commentList += 			'<div class="info-row clearfix"> \n'+
											'<span class="user-info">'+ comment.writerNickname + '</span> \n'+
											'<div class="right"> \n'+ 
												'<span>'+ comment.createdDate +'</span> \n';
				if(isParent){ //부모댓글만 답글있음
					commentList += 				'<span class="sep"></span> \n'+
												'<a href="#" class="reply-link" data-target='+ comment.commentId +'>답글</a> \n';
				}
				commentList +=				'</div> \n'+
										'</div>\n';
			}			
				commentList += 			'<div class="message"> \n'+
											'<div class="text">'+ content +'</div> \n'+
										'</div>\n'+
									'</div>\n'+
								'</div>\n'+
							'</div>\n';
			
			//첫번째 오는것이 댓글인지 대댓글인지 판단
			if(isParent){
				isFirst = true;
			}	
			// group의 루트로 설정
			// isFirst 초기화가 true이기 때문에 대댓글이 처음으로 오는 경우 , 댓글인 경우
			if(isFirst){
				$('.list-area').append(commentList);
				groupId = comment.commentId;
				isFirst = false;
			}
			else{
				$('#c_'+groupId).parent().append(commentList);
			}
		});		
		//---댓글페이징---	
		let pageList = "";
		let queryString = "";
	
		if(result.hasPrev){
			queryString = '?'.concat(createQueryString(1),'#comment');
			pageList += '<li class="page-item"> \n'+
							'<a class="page-link" href='+ queryString +' aria-label="First"> \n'+
								'<span aria-hidden="true">&laquo;</span> \n'+
							'</a> \n'+
						'</li> \n';
			queryString = '?'.concat(createQueryString(result.startPageNum - 1),'#comment');			
			pageList +='<li class="page-item"> \n'+
							'<a class="page-link" href='+ queryString +' aria-label="Previous"> \n'+
								'<span aria-hidden="true">&lt;</span> \n'+
							'</a> \n'+
						'</li>';
		}
		//페이지번호
		let index = result.startPageNum;	
		for(index; index <= result.endPageNum; index++){
					let active = result.currentPageNum == index ? 'class="page-item active"' : '';

			queryString = '?'.concat(createQueryString(index),'#comment');
			pageList += '<li '+ active + '>'+
							'<a class="page-link" href='+ queryString +' >'+ index +' </a>'+
						'</li>';
		}			
		if(result.hasNext){
			queryString = '?'.concat(createQueryString(result.endPageNum + 1),'#comment');
			pageList += '<li class="page-item"> \n'+
							'<a class="page-link" href='+ queryString +' aria-label="Next"> \n'+
								'<span aria-hidden="true">&gt;</span> \n'+
							'</a> \n'+
						'</li> \n';
			queryString = '?'.concat(createQueryString(result.totalPageCount),'#comment');			
			pageList += '<li class="page-item"> \n'+
							'<a class="page-link" href='+ queryString +' aria-label="Last"> \n'+
								'<span aria-hidden="true">&raquo;</span> \n'+
							'</a> \n'+
						'</li>';
		}		
		$('#comment-paging').html(pageList);			
		}	
	});
	return $('.list-area');
}

function writeComment(){

}

function createQueryString(cp){
	
	let queryString = {
		//page: page,
		cp: cp		
	}
	return new URLSearchParams(queryString).toString();	
}
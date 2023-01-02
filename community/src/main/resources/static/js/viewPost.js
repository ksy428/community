"use strict"

$(document).ready(function(e){

	let urlParams = new URLSearchParams(location.search);
	
	getCommentList(urlParams.get('cp')).then(pagingDto => {
		
		let result = pagingDto;
		
		// 처음 게시글에 들어왔을때 마지막 댓글페이지  => 쿼리스트링에서 댓글페이지cp가 체크 후 null이면 마지막페이지로 댓글리스트를 다시가져온다
		if(!urlParams.get('cp')){
			getCommentList(pagingDto.totalPageCount).then(pagingDto => {	
				result = pagingDto;
				viewCommentList(result);
			});
		}
		else{
			viewCommentList(result);
		}	
	});
		
	getPostList();
	
	// 게시글 수정
	$(".body").on("click",".post-edit", function(e){
		
		e.preventDefault();
		
		let form = $(this).closest("form");
		form.submit();
	});
	
	// 게시글 삭제
	$(".body").on("click",".post-delete", function(e){
		
		e.preventDefault();
		let form = $(this).closest("form");
		
		if (confirm("게시글을 삭제하시겠습니까?")) {
			form.submit();
		}
	});
	
	//대댓글쓰기 form 생성
	$(".body").on("click",".reply-link", function(e){
		
		e.preventDefault();
			
		let commentId = $(this).attr("data-target");
		
		//답글을 눌렀을때 이미 열린 답글form이 있다면 삭제
		if($('.reply-form[data-reply]').length){

			//답글을 누르고 열린 답글form이 이미 존재하고, 같은 답글을 또 누르면 삭제만 하고 return
			if($('.reply-form[data-reply]').attr("id") == 'f_'+commentId){
				return $('.reply-form[data-reply]').remove();
			}
			$('.reply-form[data-reply]').remove();		
		}

		//답글 form 생성	
		let form = '<form class="reply-form" id="'+'f_'.concat(commentId,'" ')+' data-reply action="/comment/'.concat(postId,'/',commentId,'" ') +'method="post">' +
						'<div class="reply-form__container">'+
							'<div class="reply-form__info">' +
								'<div class="reply-form__user-info">'+
									'<span class="replay-form-title">답글 쓰기</span>'+
								'</div>'+
								'<div class="reply-form__submit-button-wrapper">'+
									'<button class="reply-form__submit-button" id="write-btn" type="submit">작성</button>'+
								'</div>'+
							'</div>'+															
							'<div class="reply-form-textarea-wrapper">'+
								'<textarea class="reply-form-textarea" name="content"></textarea>'+
							'</div>'+
						'</div>'+				
					'</form>';

		$('#c_'+ commentId).after(form);
		
		let offset = $('#c_'+commentId).offset();
		$('.window').animate({scrollTop: (offset.top - $('window').height() / 2)}, 500);
		//$('html, body').animate({scrollTop: $('#c_'+ commentId).offset.top}, 400);						
	});
	
	//댓글 수정 form 생성	
	$(".body").on("click",".edit-link", function(e){
		
		e.preventDefault();
		
		let commentId = $(this).attr("data-target");
		

		let content = $(this).closest('.comment-item').find('.text').text();
		
		//답글을 눌렀을때 이미 열린 답글form이 있다면 삭제
		if($('.reply-form[data-reply]').length){

			//답글을 누르고 열린 답글form이 이미 존재하고, 같은 답글을 또 누르면 삭제만 하고 return
			if($('.reply-form[data-reply]').attr("id") == 'f_'+commentId){
				return $('.reply-form[data-reply]').remove();
			}
			$('.reply-form[data-reply]').remove();		
		}
		
		let form = '<form class="reply-form edit" id="'+'f_'.concat(commentId,'" ')+' data-reply action="/comment/'.concat(commentId,'" ') +'method="put">' +
						'<div class="reply-form__container">'+
							'<div class="reply-form__info">' +
								'<div class="reply-form__user-info">'+
									'<span class="replay-form-title">댓글 수정</span>'+
								'</div>'+
								'<div class="reply-form__submit-button-wrapper">'+
									'<button class="reply-form__submit-button" id="edit-btn" type="submit">수정</button>'+
								'</div>'+
							'</div>'+															
							'<div class="reply-form-textarea-wrapper">'+
								'<textarea class="reply-form-textarea" name="content">'+ content+'</textarea>'+
							'</div>'+
						'</div>'+				
					'</form>';
					
		$('#c_'+ commentId).after(form);
	});
	
				
	//댓글 write
	$(".body").on("click","#write-btn",function(e){		
		
		e.preventDefault();
		
		let form = $(this).closest("form");
		let writeCommentId;
		
		writeComment(form).then(commentId => {
			
			writeCommentId = commentId;		
		
			let urlParams = new URLSearchParams(location.search);
		
			getCommentList(urlParams.get('cp')).then(pagingDto => {
										
				//현재페이지에서 방금 작성 한 댓글 있는지 찾기			
				//못찾았으면 어느페이지에 있는지 찾기
				if(existsComment(pagingDto.commentList, writeCommentId)){				
					viewCommentList(pagingDto);

					$('#comment_count').html(pagingDto.totalElementCount);

					let url = "?".concat(createQueryString(urlParams.get('p'), pagingDto.currentPageNum, urlParams.get('target'), urlParams.get('keyword')), '#c_', writeCommentId);
					location.href = url;	
				}
				else{													
					for(let index = pagingDto.totalPageCount ; index > 0 ; index--){
						getCommentList(index).then(pagingDto => {	
							
							if(existsComment(pagingDto.commentList, writeCommentId)){
								viewCommentList(pagingDto);

								$('#comment_count').html(pagingDto.totalElementCount);

								let url = "?".concat(createQueryString(urlParams.get('p'), pagingDto.currentPageNum, urlParams.get('target'), urlParams.get('keyword')), '#c_', writeCommentId);
								location.href = url;
							}					
						});			
					}		
				}											
			});
			
			$('.reply-form-textarea').val('');
		});
		
	});
	
	//댓글 edit
	$(".body").on("click","#edit-btn",function(e){		
		
		e.preventDefault();
	
		let form = $(this).closest("form");
	
		editComment(form).then(() =>{
			
			let urlParams = new URLSearchParams(location.search);
			
			getCommentList(urlParams.get('cp')).then(pagingDto => {
				
				viewCommentList(pagingDto);													
			});
		});
	});
	
	//댓글 delete
	$(".body").on("click",".delete-link", function(e){
		
		e.preventDefault();
	
		let commentId = $(this).attr("data-target");
		
		if (confirm("댓글을 삭제하시겠습니까?")) {
		deleteComment(commentId).then(() =>{
			
			let urlParams = new URLSearchParams(location.search);
			
			getCommentList(urlParams.get('cp')).then(pagingDto => {
				
				viewCommentList(pagingDto);							

				$('#comment_count').html(pagingDto.totalElementCount);	
				
			});
		});
		}
	});

	//추천
	$('#recommend-btn').on("click", function(e){
		e.preventDefault();
		
		$.ajax({
			url: '/recommend/'+postId,
			type: 'post',
			dataType: 'json',
			success : function(result){				
				$('#recommend').html(result);	
			},
			error : function(result){
				alert(result.responseText);
			}
		});
	});
	
});


//댓글쓰기 ajax
function writeComment(form){
	return new Promise(function(resolve, reject){
		$.ajax({
			url: form.attr('action'),
			data: form.serialize(),
			type: 'post',
			dataType: 'json',
			success : function(result){				
				resolve(result);
			},
			error : function(result){
				alert(result.responseText);
			}
		});		
	});
}

//댓글수정 ajax
function editComment(form){
	return new Promise(function(resolve, reject){
		
		$.ajax({
			url: form.attr('action'),
			data: form.serialize(),
			type: 'put',
			dataType: 'json',
			success : function(result){				
				resolve(result);
			},
			error : function(result){
				alert(result.responseText);
			}
		});		
	});
}

//댓글삭제 ajax
function deleteComment(commentId){
	return new Promise(function(resolve, reject){
		$.ajax({
			url: '/comment/' + commentId,
			type: 'delete',
			dataType: 'json',
			success : function(result){
				resolve(result);
			},
			error : function(result){
				alert(result.responseText);
			} 
		});
	});
}

// 댓글리스트 가져오기 ajax
function getCommentList(cp){
	return new Promise(function(resolve, reject){
		
		$.ajax({
			url: '/comment/'+ postId,
			type: 'get',
			data: {
				cp : cp
				},
			dataType: 'json',
			success: function(result){ //commentPagingDto 가져옴				
				resolve(result);		
			},
			error : function(result){
				alert(result.responseText);
			} 
		})	
	});
}

// 게시글리스트 가져오기 ajax
function getPostList(){

	$('.list-table').children('a').remove();
	let urlParams = new URLSearchParams(location.search);
	
	let pathName = location.pathname.split('/');
	let boardType = pathName[2];
	
	let postSearch = {
		target: urlParams.get('target'),
		keyword: urlParams.get('keyword') ?? '',
		p: urlParams.get('p')
	}

	$.ajax({
		url: '/board/'+ boardType +'/list',
		type: 'get',
		data: postSearch,
		dataType: 'json',
		success: function(result){
			$.each(result.postList, function(index, post) {
				
				let postList="";
				let queryString = '?'.concat(createQueryString(urlParams.get('p'), null, urlParams.get('target'), urlParams.get('keyword'),'#comment'));
				let active = (postId == post.postId) ? 'active' : '';
				postList += '<a class="vrow column ' + active + '" href= /board/'+ boardType +'/'+ post.postId +''+ queryString + '> \n'+ 
								'<div class="vrow-inner"> \n'+
									'<div class ="vrow-top"> \n'+
										'<span class="vcol col-id">'+ post.postId + '</span> \n'+
										'<span class="vcol col-title">'+ post.title + '</span> \n'+
									'</div> \n'+
									'<div class ="vrow-bottom"> \n'+
										'<span class="vcol col-author">'+ post.writerNickname + '</span> \n'+
										'<span class="vcol col-time">'+ post.createdDate + '</span> \n'+
										'<span class="vcol col-view">'+ post.hit + '</span> \n'+
										'<span class="vcol col-rate">'+ post.recommend + '</span> \n'+
									'</div> \n'+
								'</div> \n'+
							'</a> \n';
				
				$('.list-table').append(postList);	
			});
			
		/////////////////////////////////////////////////////////	
			//---게시글페이징---	
		let pageList = "";
		let queryString = "";
		//let urlParams = new URLSearchParams(location.search);
	
		if(result.hasPrev){
			queryString = '?'.concat(createQueryString(1, null, urlParams.get('target'), urlParams.get('keyword')));
			pageList += '<li class="page-item"> \n'+
							'<a class="page-link" href="/board/'+ boardType +''+ queryString +'" aria-label="First"> \n'+
								'<span aria-hidden="true">&laquo;</span> \n'+
							'</a> \n'+
						'</li> \n';
			queryString = '?'.concat(createQueryString(result.startPageNum - 1, null, urlParams.get('target'), urlParams.get('keyword')));			
			pageList +='<li class="page-item"> \n'+
							'<a class="page-link" href="/board/'+ boardType +''+ queryString +'" aria-label="Previous"> \n'+
								'<span aria-hidden="true">&lt;</span> \n'+
							'</a> \n'+
						'</li>';
		}
		//페이지번호
		for(let index = result.startPageNum; index <= result.endPageNum; index++){
					let active = result.currentPageNum == index ? 'class="page-item active"' : 'class="page-item"';

			queryString = '?'.concat(createQueryString(index, null, urlParams.get('target'), urlParams.get('keyword')));
			pageList += '<li '+ active + '>'+
							'<a class="page-link" href="/board/'+ boardType +''+ queryString +'">'+ index +' </a>'+
						'</li>';
		}			
		if(result.hasNext){
			queryString = '?'.concat(createQueryString(result.endPageNum + 1, null, urlParams.get('target'), urlParams.get('keyword')));
			pageList += '<li class="page-item"> \n'+
							'<a class="page-link" href="/board/'+ boardType +''+ queryString +'" aria-label="Next"> \n'+
								'<span aria-hidden="true">&gt;</span> \n'+
							'</a> \n'+
						'</li> \n';
			queryString = '?'.concat(createQueryString(result.totalPageCount, null, urlParams.get('target'), urlParams.get('keyword')));			
			pageList += '<li class="page-item"> \n'+
							'<a class="page-link" href="/board/'+ boardType +''+ queryString +'" aria-label="Last"> \n'+
								'<span aria-hidden="true">&raquo;</span> \n'+
							'</a> \n'+
						'</li>';
		}		
		$('#post-paging').html(pageList);
		
		//////////////////////////////////////////////////////			
		},
		error : function(result){
				alert(result.responseText);
		} 
	});
}
//댓글리스트 뷰에 추가
function viewCommentList(result){
	
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
				if(writerId == authId){//작성자만 수정,삭제 있음																	
					commentList +=				'<span class="sep"></span> \n'+
												'<a href="#" class="edit-link" data-target='+ comment.commentId +'>수정</a> \n'+
												'<span class="sep"></span> \n'+
												'<a href="#" class="delete-link" data-target='+ comment.commentId +'>삭제</a> \n';		
				}								
				if(isParent && authRole != '[ROLE_ANONYMOUS]'){ //부모댓글&&로그인해야만 답글있음
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
		let urlParams = new URLSearchParams(location.search);
	
		if(result.hasPrev){
			queryString = '?'.concat(createQueryString(urlParams.get('p'), 1, urlParams.get('target'), urlParams.get('keyword'),'#comment'));
			pageList += '<li class="page-item"> \n'+
							'<a class="page-link" href='+ queryString +' aria-label="First"> \n'+
								'<span aria-hidden="true">&laquo;</span> \n'+
							'</a> \n'+
						'</li> \n';
			queryString = '?'.concat(createQueryString(urlParams.get('p'), result.startPageNum - 1, urlParams.get('target'), urlParams.get('keyword')),'#comment');			
			pageList +='<li class="page-item"> \n'+
							'<a class="page-link" href='+ queryString +' aria-label="Previous"> \n'+
								'<span aria-hidden="true">&lt;</span> \n'+
							'</a> \n'+
						'</li>';
		}
		//페이지번호
		for(let index = result.startPageNum; index <= result.endPageNum; index++){
					let active = result.currentPageNum == index ? 'class="page-item active"' : 'class="page-item"';

			queryString = '?'.concat(createQueryString(urlParams.get('p'), index, urlParams.get('target'), urlParams.get('keyword')),'#comment');
			pageList += '<li '+ active + '>'+
							'<a class="page-link" href='+ queryString +' >'+ index +' </a>'+
						'</li>';
		}			
		if(result.hasNext){
			queryString = '?'.concat(createQueryString(urlParams.get('p'), result.endPageNum + 1, urlParams.get('target'), urlParams.get('keyword')),'#comment');
			pageList += '<li class="page-item"> \n'+
							'<a class="page-link" href='+ queryString +' aria-label="Next"> \n'+
								'<span aria-hidden="true">&gt;</span> \n'+
							'</a> \n'+
						'</li> \n';
			queryString = '?'.concat(createQueryString(urlParams.get('p'), result.totalPageCount, urlParams.get('target'), urlParams.get('keyword')),'#comment');			
			pageList += '<li class="page-item"> \n'+
							'<a class="page-link" href='+ queryString +' aria-label="Last"> \n'+
								'<span aria-hidden="true">&raquo;</span> \n'+
							'</a> \n'+
						'</li>';
		}		
		$('#comment-paging').html(pageList);	
		
	return $('.list-area');	
}

function createQueryString(p, cp, target, keyword){
	
	let urlParams = new URLSearchParams();
	
	if(target){
		urlParams.set('target',target);
	}
	if(keyword){
		urlParams.set('keyword',keyword);
	}
	
	if(p){
		urlParams.set('p',p);
	}
	else{
		urlParams.set('p',1);
	}
	if(cp){
		urlParams.set('cp',cp);
	}
	
	
	return urlParams;	
}

function existsComment(commentList, findId) {

	let isFind = false;

	$.each(commentList, function(index, comment) {
		if (comment.commentId == findId) {			
			isFind = true;
			return false;
		}
	});
	return isFind;
}

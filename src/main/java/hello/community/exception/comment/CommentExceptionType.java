package hello.community.exception.comment;

import org.springframework.http.HttpStatus;

import hello.community.exception.BaseExceptionType;

public enum CommentExceptionType implements BaseExceptionType {

	NOT_FOUND_COMMENT(404, HttpStatus.NOT_FOUND, "존재하지 않은 댓글입니다"),
	NOT_AUTHORIZATION_COMMENT(403, HttpStatus.FORBIDDEN,"권한이 없습니다");
	
	private int errorCode;
	private HttpStatus httpStatus;
	private String message;
	
	private CommentExceptionType(int errorCode, HttpStatus httpStatus, String message) {
		this.errorCode = errorCode;
		this.httpStatus = httpStatus;
		this.message = message;
	}
	
	@Override
	public int getErrorCode() {
		return this.errorCode;
	}

	@Override
	public HttpStatus getHttpStatus() {
		return this.httpStatus;
	}

	@Override
	public String getErrorMessage() {
		return this.message;
	}

}

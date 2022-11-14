package hello.community.exception.post;

import org.springframework.http.HttpStatus;

import hello.community.exception.BaseExceptionType;

public enum PostExceptionType implements BaseExceptionType {

	NOT_FOUND_POST(404, HttpStatus.NOT_FOUND, "존재하지 않은 게시글입니다");
	
	private int errorCode;
	private HttpStatus httpStatus;
	private String message;
	
	private PostExceptionType(int errorCode, HttpStatus httpStatus, String message) {
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

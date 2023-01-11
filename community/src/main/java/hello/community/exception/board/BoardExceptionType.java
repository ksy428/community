package hello.community.exception.board;

import org.springframework.http.HttpStatus;

import hello.community.exception.BaseExceptionType;

public enum BoardExceptionType implements BaseExceptionType {

	NOT_FOUND_BOARD(404, HttpStatus.NOT_FOUND, "존재하지 않은 게시판입니다"),
	NOT_SUBSCRIBE_BOARD(404, HttpStatus.NOT_FOUND,"구독중인 게시판이 아닙니다"),
	OVERLAP_SUBSCRIBE_BOARD(409, HttpStatus.CONFLICT, "이미 구독중인 게시판입니다");
	
	
	private int errorCode;
	private HttpStatus httpStatus;
	private String message;
	
	private BoardExceptionType(int errorCode, HttpStatus httpStatus, String message) {
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

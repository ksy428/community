package hello.community.exception.member;

import org.springframework.http.HttpStatus;

import hello.community.exception.BaseExceptionType;

public enum MemberExceptionType implements BaseExceptionType {

	NOT_FOUND_MEMBER(404, HttpStatus.NOT_FOUND, "존재하지 않은 회원입니다"), 
	NOT_LOGIN(401, HttpStatus.UNAUTHORIZED, "비로그인 상태입니다"),
	WRONG_PASSWORD(400, HttpStatus.BAD_REQUEST, "비밀번호가 올바르지 않습니다");
	
	private int errorCode;
	private HttpStatus httpStatus;
	private String message;
	
	private MemberExceptionType(int errorCode, HttpStatus httpStatus, String message) {
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

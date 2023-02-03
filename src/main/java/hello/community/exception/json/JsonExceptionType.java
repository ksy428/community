package hello.community.exception.json;

import hello.community.exception.BaseExceptionType;
import org.springframework.http.HttpStatus;

public enum JsonExceptionType implements BaseExceptionType {
	
	FAIL_OBJECT_TO_JSON(400, HttpStatus.BAD_REQUEST, "JSON 변환에 실패했습니다");

	private int errorCode;
	private HttpStatus httpStatus;
	private String message;

	private JsonExceptionType(int errorCode, HttpStatus httpStatus, String message) {
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

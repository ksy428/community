package hello.community.exception.file;

import org.springframework.http.HttpStatus;

import hello.community.exception.BaseExceptionType;

public enum FileExceptionType implements BaseExceptionType {
	
	FAIL_SAVE_FILE(400, HttpStatus.BAD_REQUEST, "파일 저장에 실패했습니다"),
	FAIL_DELETE_FILE(400, HttpStatus.BAD_REQUEST,"파일 삭제에 실패했습니다");
	
	private int errorCode;
	private HttpStatus httpStatus;
	private String message;

	private FileExceptionType(int errorCode, HttpStatus httpStatus, String message) {
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

package hello.community.exception.json;

import hello.community.exception.BaseException;
import hello.community.exception.BaseExceptionType;

public class JsonException extends BaseException{
	
	private JsonExceptionType exceptionType;

	public JsonException(JsonExceptionType exceptionType) {
		this.exceptionType = exceptionType;
	}

	@Override
	public BaseExceptionType getExceptionType() {
		return this.exceptionType;
	}

}

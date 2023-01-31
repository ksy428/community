package hello.community.exception;

public abstract class BaseException extends RuntimeException{
	
	public abstract BaseExceptionType getExceptionType();
}

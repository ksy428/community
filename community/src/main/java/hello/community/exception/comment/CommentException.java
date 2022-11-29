package hello.community.exception.comment;

import hello.community.exception.BaseException;
import hello.community.exception.BaseExceptionType;

public class CommentException extends BaseException{

	private CommentExceptionType excetpionType;

	public CommentException(CommentExceptionType excetpionType) {
		this.excetpionType = excetpionType;
	}

	@Override
	public BaseExceptionType getExceptionType() {
		return this.excetpionType;
	}
	
}

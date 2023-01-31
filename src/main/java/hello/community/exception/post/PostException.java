package hello.community.exception.post;

import hello.community.exception.BaseException;
import hello.community.exception.BaseExceptionType;

public class PostException extends BaseException{

	private PostExceptionType excetpionType;

	public PostException(PostExceptionType excetpionType) {
		this.excetpionType = excetpionType;
	}

	@Override
	public BaseExceptionType getExceptionType() {
		return this.excetpionType;
	}
	
}

package hello.community.exception.board;

import hello.community.exception.BaseException;
import hello.community.exception.BaseExceptionType;

public class BoardException extends BaseException{

	private BoardExceptionType excetpionType;

	public BoardException(BoardExceptionType excetpionType) {
		this.excetpionType = excetpionType;
	}

	@Override
	public BaseExceptionType getExceptionType() {
		return this.excetpionType;
	}
	
}

package hello.community.exception.member;

import hello.community.exception.BaseException;
import hello.community.exception.BaseExceptionType;

public class MemberException extends BaseException{

	private MemberExceptionType excetpionType;

	public MemberException(MemberExceptionType excetpionType) {
		this.excetpionType = excetpionType;
	}

	@Override
	public BaseExceptionType getExceptionType() {
		return this.excetpionType;
	}
	
}

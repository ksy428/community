package hello.community.exception.file;

import hello.community.exception.BaseException;
import hello.community.exception.BaseExceptionType;

public class FileException extends BaseException{
	
	private FileExceptionType exceptionType;

	public FileException(FileExceptionType exceptionType) {
		this.exceptionType = exceptionType;
	}

	@Override
	public BaseExceptionType getExceptionType() {
		return this.exceptionType;
	}

}

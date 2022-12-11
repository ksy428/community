package hello.community.exception;

import java.net.http.HttpRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@ControllerAdvice
public class ExControllerAdvice {

	/*@ExceptionHandler(BaseException.class)
	public ResponseEntity memberExHandler(BaseException e) {
		
		log.info("exeption message: {}", e.getExceptionType().getErrorMessage());
		return new ResponseEntity<>(e.getExceptionType().getHttpStatus());
	}*/
	
	@ExceptionHandler(BaseException.class)
	public Object memberExHandler(BaseException e, Model model, HttpServletRequest request) {
		log.info("url: {}", request.getRequestURI());
		log.info("exeption message: {}", e.getExceptionType().getErrorMessage());
		log.info("헤더: {}", request.getHeader("X-Requested-With")); // XMLHttpRequest
		
		//ajax요청에서 예외발생
		if(request.getHeader("X-Requested-With") != null) {
			
			return new ResponseEntity<>(e.getExceptionType().getErrorMessage(), e.getExceptionType().getHttpStatus());
		}
		else {	
			model.addAttribute("message", e.getExceptionType().getErrorMessage());
			model.addAttribute("uri", request.getRequestURI());

			return "errorPage";
		}
		
	}
}

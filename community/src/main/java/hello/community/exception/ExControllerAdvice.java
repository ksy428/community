package hello.community.exception;

import java.net.http.HttpRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
	public String memberExHandler(BaseException e, Model model, HttpServletRequest request) {
		log.info("url: {}", request.getRequestURI());
		log.info("exeption message: {}", e.getExceptionType().getErrorMessage());
		model.addAttribute("message", e.getExceptionType().getErrorMessage());
		model.addAttribute("uri", request.getRequestURI());
		return "errorPage";
	}
	
}

package com.siemens.nextwork.tag.exception;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * Global controller to over ride the ["/error"] url for the application to
 * provide proper response.
 *
 * @author Z004RD2N
 * @since Feb 8, 2023
 */
@RestController
@Slf4j
public class ApplicationErrorController implements ErrorController {

	public ApplicationErrorController() {
		log.info("ApplicationErrorController: Initialized...[OK]");
	}

	@GetMapping(value = "/error")
	public ResponseEntity<ExceptionResponse> handleError(HttpServletRequest request) {
		Integer statusCode = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
		Throwable throwable = (Throwable) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
		String uri = (String) request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);
		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), "Error occured at " + uri,
				throwable == null ? "N/A" : throwable.getMessage());
		return new ResponseEntity<>(exceptionResponse, HttpStatus.valueOf(statusCode));
	}

}

package com.siemens.nextwork.tag.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.siemens.nextwork.tag.constants.NextworkConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * {@linkplain RestController} for mapping error url and respond with
 * {@linkplain ExceptionResponse}
 *
 * @author Z004RD2N
 * @since Feb 8, 2023
 */
@ControllerAdvice
@Slf4j
public class RestAPIExceptionHandler extends ResponseEntityExceptionHandler {

    public RestAPIExceptionHandler() {
        log.info("RestAPIExceptionHandler: Initialized...[OK]");
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ExceptionResponse> handleAllExceptions(Exception ex, WebRequest request) {
    	var exceptionResponse = new ExceptionResponse(new Date(), NextworkConstants.METHOD_NOT_SUPPORTED,
                NextworkConstants.METHOD_NOT_SUPPORTED);
        return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ResponseBody
    @ExceptionHandler(RestClientResponseException.class)
    public final ResponseEntity<ExceptionResponse> handleRestClientResponseException(RestClientResponseException ex,
            WebRequest request) {
    	var exceptionResponse = new ExceptionResponse(new Date(), "Rest client response exception",
                ex.getResponseBodyAsString());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.valueOf(ex.getRawStatusCode()));
    }

    @ResponseBody
    @ExceptionHandler(RestBadRequestException.class)
    public final ResponseEntity<ExceptionResponse> handleRestBadRequestException(RestBadRequestException ex,
            WebRequest request) {
    	var exceptionResponse = new ExceptionResponse(new Date(), "Bad Request", ex.getMessage());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ResponseBody
    @ExceptionHandler(ResourceNotFoundException.class)
    public final ResponseEntity<ExceptionResponse> handleResourceNotFoundException(Exception ex, WebRequest request) {
    	var exceptionResponse = new ExceptionResponse(new Date(), "Resource not found", ex.getMessage());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    @ResponseBody
    @ExceptionHandler(ResourceUpdateException.class)
    public final ResponseEntity<ExceptionResponse> handleResourceUploadException(Exception ex, WebRequest request) {
    	var exceptionResponse = new ExceptionResponse(new Date(), "Resource not Updated", ex.getMessage());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.CONFLICT);
    }
    
    @ResponseBody
    @ExceptionHandler(RestForbiddenException.class)
    public final ResponseEntity<ExceptionResponse> handleRestForbiddenException(Exception ex, WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), "Resource Forbidden", ex.getMessage());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.FORBIDDEN);
    }

    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpStatus status, WebRequest request) {
        BindingResult result = ex.getBindingResult();
        Map<String, String> fieldErrorMap = result.getFieldErrors().stream().collect(
                Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage, (s, s2) -> s + "," + s2));
        var mapper = new ObjectMapper();
        String builder = "";
        try {
            builder = mapper.writeValueAsString(fieldErrorMap);
        } catch (JsonProcessingException e) {
            log.error("", e);
        }
        var exceptionResponse = new ExceptionResponse(new Date(), "Validation Failed", builder);
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }


    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,HttpStatus status, WebRequest request) {
    	var exceptionResponse = new ExceptionResponse(new Date(), "Method not supported",
                ex.getMessage());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }


    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,HttpStatus status, WebRequest request) {
    	var exceptionResponse = new ExceptionResponse(new Date(), "Media Type not supported",
                ex.getMessage());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }


    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex,
             HttpStatus status, WebRequest request) {
    	var exceptionResponse = new ExceptionResponse(new Date(), "Media Type not accepted",
                ex.getMessage());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }


    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers,
             WebRequest request) {
    	var exceptionResponse = new ExceptionResponse(new Date(), "Missing path variable",
    			"Missing path variable");
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }


    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
             HttpStatus status, WebRequest request) {
    	var exceptionResponse = new ExceptionResponse(new Date(), "Missing request parameter",
                ex.getMessage());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }


    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
             HttpStatus status, WebRequest request) {
    	var exceptionResponse = new ExceptionResponse(new Date(), "Message Not Readable",
                "Bad Request Input");
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException ex,
             HttpStatus status, WebRequest request) {
    	var exceptionResponse = new ExceptionResponse(new Date(), "Message Not Writable",
                ex.getMessage());
		return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
	}

}

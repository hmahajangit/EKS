package com.siemens.nextwork.tag.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Common exception related to data model.
 *
 * @author Z004RD2N
 * @since Feb 8, 2023
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class RestResponseException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5262921081706498128L;

	public RestResponseException() {
		super();
	}

	public RestResponseException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public RestResponseException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public RestResponseException(String arg0) {
		super(arg0);
	}

	public RestResponseException(Throwable arg0) {
		super(arg0);
	}

}

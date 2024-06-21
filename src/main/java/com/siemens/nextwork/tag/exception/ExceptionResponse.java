package com.siemens.nextwork.tag.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * Response object to be returned for exception.
 *
 * @author Z004RD2N
 * @since Feb 8, 2023
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionResponse {

    private Date timestamp;
    private String message;
    private String details;
}

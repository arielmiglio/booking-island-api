package com.island.bookingapi.exception;

import lombok.Getter;
import lombok.Setter;


/**
 * 
 * @author miglioa
 * Exception thrown when arrival or departure date is null 
 *
 */
@SuppressWarnings("serial")
@Getter
@Setter
public abstract class ValidationDatesException extends RuntimeException {

    private int errorCode;
    
    private String message;
    
    public ValidationDatesException(String message) {
	this.message = message;
    }
    
}

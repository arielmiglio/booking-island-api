package com.island.bookingapi.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author miglioa
 * Exception used to expose that an operation is being executed in a restricted status of a booking object  
 */
@Getter
@Setter
public class DeniedBookingOperationException extends RuntimeException {
	
	private int errorCode = ErrorCodes.ERROR_1001_DENIED_OPERATION;
	
	private String status;

	public DeniedBookingOperationException(String string) {
		this.status = string;
	}
}

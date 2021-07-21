package com.island.bookingapi.exception;

import lombok.Getter;
import lombok.Setter;


/**
 * 
 * @author miglioa
 * Exception used when an cancel operations is being executed over a Booking already cancelled.  
 */

@Getter
@Setter
public class AlreadyCancelledBookingException extends RuntimeException {
	
	private int errorCode = ErrorCodes.ERROR_1000_BOOKING_ALREADY_CANCELLED;
	

}

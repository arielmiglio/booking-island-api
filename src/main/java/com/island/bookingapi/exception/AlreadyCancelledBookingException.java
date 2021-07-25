package com.island.bookingapi.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author miglioa 
 * 
 * Exception thrown when an cancel operations is being executed over a Booking already cancelled.
 */

@Getter
@Setter
@SuppressWarnings("serial")
public class AlreadyCancelledBookingException extends RuntimeException {

    private long bookingId;
    
    public AlreadyCancelledBookingException(Long id) {
	this.bookingId = id;
    }


    private int errorCode = ErrorCodes.ERROR_1000_BOOKING_ALREADY_CANCELLED;

}

package com.island.bookingapi.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author miglioa
 * Exception thrown when not exist a persistent booking with a given ID. 
 *
 */
@Getter
@Setter
@SuppressWarnings("serial")
public class BookingNotFoundException extends RuntimeException {
    
    private int errorCode = ErrorCodes.ERROR_1002_BOOKING_NOT_FOUND;
    
    private long bookingId;
    
    public BookingNotFoundException(long bookingId) {
	this.bookingId = bookingId;
    }

}

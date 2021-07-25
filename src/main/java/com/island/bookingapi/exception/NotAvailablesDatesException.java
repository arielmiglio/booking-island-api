package com.island.bookingapi.exception;

import lombok.Getter;

/**
 * 
 * @author miglioa
 * Exception thrown when in the requested dates for a booking some dates are NOT available 
 */
@Getter
@SuppressWarnings("serial")
public class NotAvailablesDatesException extends RuntimeException {
    
    private int errorCode = ErrorCodes.ERROR_1003_NON_AVAILABLE_DATES;

}

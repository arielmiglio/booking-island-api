package com.island.bookingapi.exception;

@SuppressWarnings("serial")
public class DatesMustNotBeNullException extends ValidationDatesException {

    
    public DatesMustNotBeNullException(String message) {
	super(message);
	this.setErrorCode(ErrorCodes.ERROR_1004_NOT_NULL_DATES);
    }

}

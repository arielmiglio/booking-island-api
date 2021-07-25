package com.island.bookingapi.exception;

@SuppressWarnings("serial")
public class SameDaysNotAllowedException extends ValidationDatesException {

    public SameDaysNotAllowedException(String message) {
	super(message);
	this.setErrorCode(ErrorCodes.ERROR_1005_NOT_ALLOWED_DATES);
    }

}

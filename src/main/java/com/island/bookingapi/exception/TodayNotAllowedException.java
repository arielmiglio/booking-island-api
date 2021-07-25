package com.island.bookingapi.exception;

@SuppressWarnings("serial")
public class TodayNotAllowedException extends ValidationDatesException {

    public TodayNotAllowedException(String message) {
	super(message);
	this.setErrorCode(ErrorCodes.ERROR_1007_TODAY_NOT_ALLOWED);
    }

}

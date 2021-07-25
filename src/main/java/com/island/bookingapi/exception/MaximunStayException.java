package com.island.bookingapi.exception;

@SuppressWarnings("serial")
public class MaximunStayException extends ValidationDatesException {

    public MaximunStayException(String message) {
	super(message);
	this.setErrorCode(ErrorCodes.ERROR_1006_MAXIMUN_STAY);
    }

}

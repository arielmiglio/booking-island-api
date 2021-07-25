package com.island.bookingapi.exception;

@SuppressWarnings("serial")
public class MaximunArrivalDateExceededException extends ValidationDatesException {

    public MaximunArrivalDateExceededException(String message) {
	super(message);
	this.setErrorCode(ErrorCodes.ERROR_1008_ARRAIVAL_EXCEEDED);
    }

}

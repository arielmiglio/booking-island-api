package com.island.bookingapi.exception;

public class BookingNotFoundException extends RuntimeException {

	private int errorCode = ErrorCodes.ERROR_1002_BOOKING_NOT_FOUND;
	
}

package com.island.bookingapi.service;

import java.time.LocalDate;
import java.util.List;

import javax.validation.constraints.Future;

public interface AvailableDatesServices {

	/**
	 * Return the available dates between from and to parameters
     *
     * @param from Arrival day
     * @param to   Departure day
     * @return List<LocalDate> the available dates
     */
	List<LocalDate> getAvailableDates(@Future LocalDate from, @Future LocalDate to);
	
	

}

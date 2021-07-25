package com.island.bookingapi.service;

import java.time.LocalDate;

import javax.validation.Valid;

import com.island.bookingapi.exception.AlreadyCancelledBookingException;
import com.island.bookingapi.exception.BookingNotFoundException;
import com.island.bookingapi.exception.DeniedBookingOperationException;
import com.island.bookingapi.model.Booking;
import com.island.bookingapi.request.BookingControllerRequest;

public interface BookingService {

    /**
     * Looks for the booking with the given id in the DB
     * 
     * @param bookingId the booking ID to be retrieved
     * @return the {@link Booking} with the given id
     * @throws BookingNotFoundException if the id is not found in the DB
     */

    Booking getBooking(long bookingId);

    /**
     * Create a booking and a collection of BookedDates and persist both. The method
     * fail when already exist at least one bookedDate in the data base.
     * 
     * @param request
     * @return The created {@link Booking}
     * @throws NotAvailablesDatesException when dates are not available to booking
     */
    Booking createBooking(@Valid BookingControllerRequest request);
    Booking createBooking(String fullName, String userEmail, LocalDate arrivalDate, LocalDate departureDate) ;

    /**
     * Cancel a booking changing its status to CANCELLED
     * 
     * @param bookingId the booking ID to be deleted
     * @return The deleted {@link Booking} with the given id
     * @throws BookingNotFoundException when the id is not found in the DB
     * @throws AlreadyCancelledBookingException when the Booking was canceled previously
     */
    Booking cancelBooking(Long bookingId);

    /**
     * Update a booking changing its dates checking availability for the change
     * 
     * @param request a Booking Request with some changes
     * @param id      Booking id to be modified
     * @return The modified {@link Booking}
     * @throws BookingNotFoundException if the id is not found in the DB
     * @throws DeniedBookingOperationException if the status doesn't let the update 
     */
    Booking updateBooking(@Valid BookingControllerRequest request, long id);
    Booking updateBooking(String fullName, String userEmail, LocalDate arrivalDate, LocalDate departureDate, long bo) ;

}

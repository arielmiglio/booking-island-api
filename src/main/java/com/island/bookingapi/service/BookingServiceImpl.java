package com.island.bookingapi.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.island.bookingapi.exception.BookingNotFoundException;
import com.island.bookingapi.exception.DeniedBookingOperationException;
import com.island.bookingapi.exception.NotAvailablesDatesException;
import com.island.bookingapi.model.Booking;
import com.island.bookingapi.model.BookingStatus;
import com.island.bookingapi.model.DateBooked;
import com.island.bookingapi.repository.BookingRepository;
import com.island.bookingapi.repository.DateBookedRepository;
import com.island.bookingapi.request.BookingControllerRequest;

import lombok.extern.slf4j.Slf4j;

/**
 * Service layer.
 */
@Service
@Slf4j
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private DateBookedRepository dateBookedRepository;

    @Override
    public Booking getBooking(long bookingId) {
	log.info("Fetching booking by Id {}", bookingId);
	return this.bookingRepository.findById(bookingId).orElseThrow(() -> new BookingNotFoundException(bookingId));
    }

    @Override
    @Transactional
    public Booking createBooking(@Valid BookingControllerRequest request) {
	log.info("Starting creation of a new Booking for the user {}", request.getFullName());
	Booking booking = new Booking(request.getFullName(), request.getUserEmail(), request.getArrivalDate(), request.getDepartureDate());
	this.saveBooking(booking);
	log.info("Successfull Creation of a new Booking for the user {}", request.getFullName());
	return booking;
    }
    
    @Override
    @Transactional
    public Booking createBooking(String fullName, String userEmail, LocalDate arrivalDate, LocalDate departureDate) {
	log.info("Starting creation of a new Booking for the user {}", fullName);
	Booking booking = new Booking(fullName, userEmail, arrivalDate, departureDate);
	this.saveBooking(booking);
	log.info("Successfull Creation of a new Booking for the user {}", fullName);
	return booking;
    }


    /**
     * Create a collection of dates between arrival and departure dates to persist as BookedDate. 
     * Persist all BookedDates collected. 
     * Persist the booking received as parameter
     * 
     * @param booking
     */
    private void saveBooking(Booking booking) {
	List<LocalDate> bookingDates = booking.getArrivalDate().datesUntil(booking.getDepartureDate()).collect(Collectors.toList());
	List<DateBooked> datesToBooked = bookingDates.stream().map(DateBooked::new).collect(Collectors.toList());
	try {
	    this.dateBookedRepository.saveAll(datesToBooked);
	}catch (DataIntegrityViolationException e) {
	    log.info("There are some booking dates overlaping existing ones");
	    throw new NotAvailablesDatesException();
	}

	this.bookingRepository.save(booking);
    }

    @Override
    @Transactional
    public Booking cancelBooking(Long bookingId) {
	Booking booking = this.bookingRepository.findById(bookingId).orElseThrow(() -> new BookingNotFoundException(bookingId));
	List<LocalDate> bookingDates = booking.getArrivalDate().datesUntil(booking.getDepartureDate()).collect(Collectors.toList());

	booking.cancel();
	this.dateBookedRepository.deleteByDayIn(bookingDates);
	this.bookingRepository.save(booking);

	return booking;
    }

    @Override
    @Transactional
    public Booking updateBooking(@Valid BookingControllerRequest request, long id) {
	Booking booking = this.bookingRepository.findById(id).orElseThrow(() -> new BookingNotFoundException(id));

	if (BookingStatus.ACTIVE != booking.getBookingStatus()) {
	    throw new DeniedBookingOperationException(booking.getBookingStatus().toString());
	}

	
	try {
	    this.workWithDates(booking, request.getArrivalDate(), request.getDepartureDate());
	}catch (DataIntegrityViolationException e) {
	    //catch the exception to throw another more specific 
	    log.info("There are some booking dates overlaping existing ones");
	    throw new NotAvailablesDatesException();
	}
	booking.setFullName(request.getFullName());
	booking.setUserEmail(request.getUserEmail());

	this.bookingRepository.save(booking);
	return booking;
    }

    /**
     * Calculate the difference between the persisted dates and new ones.
     * 
     * The intersection is used to calculate new dates to insert and old ones to
     * delete from the database.
     * Save the new ones and delete the old ones
     * 
     * @param booking
     * @param arrivalDate
     * @param departureDate
     */

    private void workWithDates(Booking booking, LocalDate newArrivalDate, LocalDate newDepartureDate) {

	if (booking.getArrivalDate().isEqual(newArrivalDate) && booking.getDepartureDate().isEqual(newDepartureDate)) {
	    return; // If the dates didn't change, is nothing to do
	}

	List<LocalDate> previousBookedDates = booking.getArrivalDate().datesUntil(booking.getDepartureDate()).collect(Collectors.toList());
	List<LocalDate> newBookingDates = newArrivalDate.datesUntil(newDepartureDate).collect(Collectors.toList());

	// Non existing dates in the original booking
	List<LocalDate> toSave = newBookingDates.stream().filter(x -> !previousBookedDates.contains(x)).collect(Collectors.toList());
	List<DateBooked> datesToSave = toSave.stream().map(DateBooked::new).collect(Collectors.toList());
	this.dateBookedRepository.saveAll(datesToSave);

	// Dates previously saved that I have to delete
	List<LocalDate> toDelete = previousBookedDates.stream().filter(x -> !newBookingDates.contains(x))
		.collect(Collectors.toList());
	this.dateBookedRepository.deleteByDayIn(toDelete);

	booking.setArrivalDate(newArrivalDate);
	booking.setDepartureDate(newDepartureDate);
    }

    @Override
    public Booking updateBooking(String fullName, String userEmail, LocalDate arrivalDate, LocalDate departureDate, long bookingId) {
	Booking booking = this.bookingRepository.findById(bookingId).orElseThrow(() -> new BookingNotFoundException(bookingId));

	if (BookingStatus.ACTIVE != booking.getBookingStatus()) {
	    throw new DeniedBookingOperationException(booking.getBookingStatus().toString());
	}

	
	try {
	    this.workWithDates(booking, arrivalDate, departureDate);
	}catch (DataIntegrityViolationException e) {
	    //catch the exception to throw another more specific 
	    log.info("There are some booking dates overlaping existing ones");
	    throw new NotAvailablesDatesException();
	}
	booking.setFullName(fullName);
	booking.setUserEmail(userEmail);

	this.bookingRepository.save(booking);
	return booking;
    }

}

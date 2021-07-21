package com.island.bookingapi.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.island.bookingapi.exception.BookingNotFoundException;
import com.island.bookingapi.model.Booking;
import com.island.bookingapi.model.BookingStatus;
import com.island.bookingapi.model.DateBooked;
import com.island.bookingapi.repository.BookingRepository;
import com.island.bookingapi.repository.DateBookedRepository;
import com.island.bookingapi.request.BookingControllerRequest;
import com.island.bookingapi.response.BookingResponse;

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
		return this.bookingRepository.findById(bookingId).orElseThrow(BookingNotFoundException::new);
	}


	@Override
	@Transactional
	public Booking create(@Valid BookingControllerRequest request) {
		log.info("Starting creation of a new Booking for the user {}", request.getFullName());		
		Booking booking = new Booking(request.getFullName(), request.getUserEmail(), request.getArrivalDate(), request.getDepartureDate());
		saveBooking(booking);
		 
		log.info("Successfull Creation of a new Booking for the user {}", request.getFullName());
		return booking;
	}
	
	

	/**
	 * Create a collection of dates between arrival and departure dates to persist as BookedDate.
	 * Persist all BookedDates collected.
	 * Persist the booking received as parameter
	 * @param booking
	 */
	private void saveBooking(Booking booking) {
        List<LocalDate> bookingDates = booking.getArrivalDate().datesUntil(booking.getDepartureDate()).collect(Collectors.toList());
        List<DateBooked> datesToBooked = bookingDates.stream().map(DateBooked::new).collect(Collectors.toList());
        this.dateBookedRepository.saveAll(datesToBooked);
        //TODO VER SI CAPTURA LA CONSTRAINT EXCEPTION PARA ASEGURAR WRAPPEARLA
        
        this.bookingRepository.save(booking);
    }


	@Override
	@Transactional
	public Booking cancelBooking(Long bookingId) {
		Booking booking = this.bookingRepository.getById(bookingId);
		List<LocalDate> bookingDates = booking.getArrivalDate().datesUntil(booking.getDepartureDate()).collect(Collectors.toList());
        
        booking.cancel();
        this.dateBookedRepository.deleteByDayIn(bookingDates);
        this.bookingRepository.save(booking);
        
		return booking;
	}


	@Override
	@Transactional
	public Booking updateBooking(@Valid BookingControllerRequest request, long id) {
		Booking booking = this.bookingRepository.getById(id);
		
		return null;
	}
	
	

	
	
    
    

}

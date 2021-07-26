package com.island.bookingapi.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.island.bookingapi.exception.AlreadyCancelledBookingException;
import com.island.bookingapi.exception.DatesMustNotBeNullException;
import com.island.bookingapi.exception.MaximunArrivalDateExceededException;
import com.island.bookingapi.exception.MaximunStayException;
import com.island.bookingapi.exception.SameDaysNotAllowedException;
import com.island.bookingapi.exception.TodayNotAllowedException;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 
 * @author miglioa
 *	This class modeled a booking. It has the business logic associated with rules to let make a booking    
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Booking {

    public static final Integer MAX_STAY = 3;
    public static final Integer BOOKING_ANTICIPATION_MONTHS = 1;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(length = 300)
    private String fullName;

    @Email
    @NotBlank
    private String userEmail;

    private LocalDate arrivalDate;

    private LocalDate departureDate;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private BookingStatus bookingStatus;
    
    

    public Booking(String name, String email, LocalDate arrivalDate, LocalDate departureDate) {
	this.validateBooking(name, email, arrivalDate, departureDate);
	this.fullName = name;
	this.userEmail = email;
	this.arrivalDate = arrivalDate;
	this.departureDate = departureDate;
	this.bookingStatus = BookingStatus.ACTIVE;
    }

    public void cancel() {
	if (BookingStatus.CANCELLED == this.bookingStatus) {
	    throw new AlreadyCancelledBookingException(this.id);
	}
	this.bookingStatus = BookingStatus.CANCELLED;
    }
    
    /**
     * Checks all the constraints associated with the creation of a Booking
     * @param name
     * @param email
     * @param arrival
     * @param departure
     */
    private void validateBooking(String name, String email, LocalDate arrival, LocalDate departure) {
	if (arrival == null || departure == null) {
	    throw new DatesMustNotBeNullException("It must be specified arribal and departure dates");
	}

	if (arrival.isAfter(departure) || arrival.isEqual(departure)) {
	    throw new SameDaysNotAllowedException("Arraival and departure can not be the same");
	}

	if (ChronoUnit.DAYS.between(arrival, departure) > MAX_STAY) {
	    throw new MaximunStayException("The maximum stay is"+ MAX_STAY +"days");
	}
	
	LocalDate now = LocalDate.now();
	if (now.compareTo(arrival) == 0) {
	    throw new TodayNotAllowedException("Arrival date must be greater today");
	}
	
	LocalDate limitLocalDate = now.plusMonths(BOOKING_ANTICIPATION_MONTHS);
	if (limitLocalDate.compareTo(arrival) < 0) {
	    throw new MaximunArrivalDateExceededException(String.format("Arrival date must be less than %s", limitLocalDate.toString()));
	}

    }

}

package com.island.bookingapi.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.island.bookingapi.model.Booking;
import com.island.bookingapi.model.BookingStatus;

import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class BookingResponse {

    private Long bookingId;

    private String fullName;

    private String userEmail;

    private LocalDate arrivalDate;

    private LocalDate departureDate;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private BookingStatus bookingStatus;

    /**
     * Creates a {@link BookingResponse} from a {@link Booking} instance
     * 
     * @param newGame
     */
    public BookingResponse(Booking booking) {
	this.bookingId = booking.getId();
	this.fullName = booking.getFullName();
	this.userEmail = booking.getUserEmail();
	this.arrivalDate = booking.getArrivalDate();
	this.departureDate = booking.getDepartureDate();
	this.createdAt = booking.getCreatedAt();
	this.updatedAt = booking.getUpdatedAt();
	this.bookingStatus = booking.getBookingStatus();

    }

}

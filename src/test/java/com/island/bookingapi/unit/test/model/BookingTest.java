package com.island.bookingapi.unit.test.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.island.bookingapi.exception.AlreadyCancelledBookingException;
import com.island.bookingapi.exception.DatesMustNotBeNullException;
import com.island.bookingapi.exception.MaximunArrivalDateExceededException;
import com.island.bookingapi.exception.MaximunStayException;
import com.island.bookingapi.exception.SameDaysNotAllowedException;
import com.island.bookingapi.exception.TodayNotAllowedException;
import com.island.bookingapi.model.Booking;
import com.island.bookingapi.model.BookingStatus;

public class BookingTest {
    
    private Booking booking;
    
    @BeforeEach
    public  void setup() {
        this.booking = new Booking("Da Vinci Leonardo", "davincil@gmail.com", LocalDate.now().plusDays(1), LocalDate.now().plusDays(3));
    }
    
    @Test
    public void cancelAnActiveBookingShouldBeCancelled() {
	this.booking.cancel();
	assertEquals(this.booking.getBookingStatus(), BookingStatus.CANCELLED);
    }
    
    
    @Test
    public void cancelAnCancelledBookingShouldThrowAlreadyCancelledBookingException() {
	this.booking.setId(1l); //Sets the id because only can be modified persistent objects
	this.booking.cancel();
	assertThrows(AlreadyCancelledBookingException.class, () -> this.booking.cancel());
    }
    
    @Test
    public void creationBookingWithNullDatesShouldThrownDatesMustNotBeNullException() {
	assertThrows(DatesMustNotBeNullException.class, () -> new Booking("Da vinci Leonardo", "davinciq@gmail.com", null, null));
    }
    
    
    @Test
    public void creationBookingInSameArrivalDeparturShouldThrownSameDaysNotAllowedException() {
	LocalDate date = LocalDate.now().plusDays(1);
	assertThrows(SameDaysNotAllowedException.class, () -> new Booking("Da vinci Leonardo", "davinciq@gmail.com", date, date));
    }
    
    @Test
    public void creationBookingExcedingMaximunShouldThrownMaximunStayException() {
	LocalDate arrival = LocalDate.now().plusDays(1);
	LocalDate departure = arrival.plusDays(Booking.MAX_STAY + 1);
	assertThrows(MaximunStayException.class, () -> new Booking("Da vinci Leonardo", "davinciq@gmail.com", arrival, departure));
    }
    
    @Test
    public void creationBookingInCurrentDayShouldThrownTodayNotAllowedException() {
	LocalDate arrival = LocalDate.now();
	LocalDate departure = arrival.plusDays(1);
	assertThrows(TodayNotAllowedException.class, () -> new Booking("Da vinci Leonardo", "davinciq@gmail.com", arrival, departure));
    }

    @Test
    public void creationBookingMoreThanMonthLaterShouldThrownMaximunArrivalDateExceededException() {
	LocalDate arrival = LocalDate.now().plusMonths(Booking.BOOKING_ANTICIPATION_MONTHS).plusDays(1);
	LocalDate departure = arrival.plusDays(1);
	assertThrows(MaximunArrivalDateExceededException.class, () -> new Booking("Da vinci Leonardo", "davinciq@gmail.com", arrival, departure));
    }


}

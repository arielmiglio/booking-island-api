package com.island.bookingapi.integration.test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.island.bookingapi.exception.AlreadyCancelledBookingException;
import com.island.bookingapi.exception.NotAvailablesDatesException;
import com.island.bookingapi.model.Booking;
import com.island.bookingapi.model.BookingStatus;
import com.island.bookingapi.request.BookingControllerRequest;
import com.island.bookingapi.service.BookingService;
import com.island.bookingapi.service.DatesBookedService;

/**
 * 
 * @author miglioa
 * Integration Test with embedded H2
 * 
 * 
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
public class BookingIntegrationTest {
    
    @Autowired
    private BookingService bookingService;
    
    @Autowired
    private DatesBookedService datesBookedService;
    
    @Test
    public void createAndGetBooking() {
	List<LocalDate> availableDates =  datesBookedService.getAvailableDates(LocalDate.now(), LocalDate.now().plusMonths(1));
	int availableDateBefore = availableDates.size();
	
	BookingControllerRequest reqAna = new BookingControllerRequest("Ana", "ana@gmail.com", LocalDate.now().plusDays(1), LocalDate.now().plusDays(4));
	Booking bookingAna = bookingService.createBooking(reqAna);
	assertAll(
		()-> assertEquals(bookingAna.getFullName(), reqAna.getFullName()),
		()-> assertEquals(bookingAna.getUserEmail() , reqAna.getUserEmail() ),
		()-> assertEquals(bookingAna.getArrivalDate() , reqAna.getArrivalDate() ),
		()-> assertEquals(bookingAna.getDepartureDate() , reqAna.getDepartureDate() ),
		()-> assertEquals(bookingAna.getBookingStatus() , BookingStatus.ACTIVE),
		()-> assertNotNull(bookingAna.getCreatedAt()),
		()-> assertNotNull(bookingAna.getUpdatedAt())
		);
	
	
	Booking bookingAnaRetrieved = bookingService.getBooking(bookingAna.getId());
	assertNotNull(bookingAnaRetrieved);
	
	List<LocalDate> availableDatesAfter =  datesBookedService.getAvailableDates(LocalDate.now(), LocalDate.now().plusMonths(1));
	
	
	assertEquals(availableDateBefore-3, availableDatesAfter.size());
    }  
    
    
    @Test
    public void createAndCancelBooking() {
	List<LocalDate> availableDates =  datesBookedService.getAvailableDates(LocalDate.now(), LocalDate.now().plusMonths(1));
	int availableDateBefore = availableDates.size();
	
	BookingControllerRequest reqPaz = new BookingControllerRequest("Paz", "paz@gmail.com", LocalDate.now().plusDays(4), LocalDate.now().plusDays(6));
	Booking bookingPaz = bookingService.createBooking(reqPaz);
	
	List<LocalDate> availableDatesAfter =  datesBookedService.getAvailableDates(LocalDate.now(), LocalDate.now().plusMonths(1));
	assertEquals(availableDateBefore-2, availableDatesAfter.size());
	
	Booking bookingPazCancelled = bookingService.cancelBooking(bookingPaz.getId());
	
	assertEquals(bookingPazCancelled.getBookingStatus() , BookingStatus.CANCELLED);
		
	List<LocalDate> datesAfterCancel =  datesBookedService.getAvailableDates(LocalDate.now(), LocalDate.now().plusMonths(1));
	assertEquals(availableDates.size(), datesAfterCancel.size());
	
	assertThrows(AlreadyCancelledBookingException.class, ()->bookingService.cancelBooking(bookingPaz.getId()));
    }
    
    @Test
    public void updateBooking() {
	List<LocalDate> availableDates =  datesBookedService.getAvailableDates(LocalDate.now(), LocalDate.now().plusMonths(1));
	int availableDateBefore = availableDates.size();
	
	BookingControllerRequest reqRosi = new BookingControllerRequest("Rosi", "rosi@gmail.com", LocalDate.now().plusDays(6), LocalDate.now().plusDays(8));
	Booking bookingRosi = bookingService.createBooking(reqRosi);
	
	BookingControllerRequest reqSilvia = new BookingControllerRequest("Silvia", "silvia@gmail.com", LocalDate.now().plusDays(8), LocalDate.now().plusDays(10));
	Booking bookingSilvia = bookingService.createBooking(reqSilvia);
	
	BookingControllerRequest reqRosi2 = new BookingControllerRequest("Rosi2", "rosi2@gmail.com", LocalDate.now().plusDays(10), LocalDate.now().plusDays(12));
	bookingService.updateBooking(reqRosi2, bookingRosi.getId());
	
	Booking bookingRosiUpdated = bookingService.getBooking(bookingRosi.getId());
	
	assertAll(
		()-> assertEquals(bookingRosiUpdated.getFullName(), "Rosi2"),
		()-> assertEquals(bookingRosiUpdated.getUserEmail() , "rosi2@gmail.com" ),
		()-> assertEquals(bookingRosiUpdated.getArrivalDate() , LocalDate.now().plusDays(10)),
		()-> assertEquals(bookingRosiUpdated.getDepartureDate() , LocalDate.now().plusDays(12))
		);
	
	BookingControllerRequest reqSilvia2 = new BookingControllerRequest("Silvia", "silvia@gmail.com", LocalDate.now().plusDays(11), LocalDate.now().plusDays(13));
	assertThrows(NotAvailablesDatesException.class, ()-> bookingService.updateBooking(reqSilvia2, bookingSilvia.getId()));
	
	List<LocalDate> availableDatesAfter =  datesBookedService.getAvailableDates(LocalDate.now(), LocalDate.now().plusMonths(1));
	assertEquals(availableDateBefore - 4, availableDatesAfter.size());
	
    }
    
}

package com.island.bookingapi.unit.test.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.island.bookingapi.controller.BookingController;
import com.island.bookingapi.exception.AlreadyCancelledBookingException;
import com.island.bookingapi.exception.BookingNotFoundException;
import com.island.bookingapi.exception.DeniedBookingOperationException;
import com.island.bookingapi.exception.NotAvailablesDatesException;
import com.island.bookingapi.model.Booking;
import com.island.bookingapi.model.BookingStatus;
import com.island.bookingapi.request.BookingControllerRequest;
import com.island.bookingapi.service.BookingService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = BookingController.class)
@AutoConfigureMockMvc
public class BookingControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private BookingService bookingServiceMock;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    private Booking booking;
    
    @BeforeEach
    public  void setup() {
        this.booking = new Booking("Da Vinci Leonardo", "davincil@gmail.com", LocalDate.now().plusDays(1), LocalDate.now().plusDays(3));
        this.booking.setId(1l);
    }

    @Test
    public void givenAnExistingBookingIdReturn200() throws Exception {
	long bookingId = 1;
	when(bookingServiceMock.getBooking(1)).thenReturn(booking);
	mockMvc.perform(get("/booking/{id}", bookingId)
				.accept(MediaType.APPLICATION_JSON))
                		.andExpect(status().isOk());
    }
    

    @Test
    public void givenAnNonExistingBookingIdThrows() throws Exception {
	long bookingId = 1;
	when(bookingServiceMock.getBooking(1)).thenThrow(new BookingNotFoundException(bookingId));
	mockMvc.perform(get("/booking/{id}", bookingId)
				.accept(MediaType.APPLICATION_JSON))
                		.andExpect(status().isNotFound());
    }
    
    @Test
    public void createBookingWithWrongMail() throws Exception {
	BookingControllerRequest request = new BookingControllerRequest("Da Vinci Leonardo", "davincil", LocalDate.now().plusDays(1), LocalDate.now().plusDays(3));
	
	mockMvc.perform(post("/booking/")
	                .contentType("application/json")
	                .content(objectMapper.writeValueAsString(request)))
	                .andExpect(status().isBadRequest());
    }
    
    //CREATION BOOKING TEST CASES
    @Test
    public void createBookingWithEmptyFullName() throws Exception {
	BookingControllerRequest request = new BookingControllerRequest("", "davincil", LocalDate.now().plusDays(1), LocalDate.now().plusDays(3));
	
	mockMvc.perform(post("/booking/")
	                .contentType("application/json")
	                .content(objectMapper.writeValueAsString(request)))
	                .andExpect(status().isBadRequest());
    }

    
    @Test
    public void createBookingInAlreadyBookedDates() throws Exception {
	BookingControllerRequest request = new BookingControllerRequest("Da Vinci Leonardo", "davincil@gmail.com", LocalDate.now().plusDays(1), LocalDate.now().plusDays(3));
	when(bookingServiceMock.createBooking(request)).thenThrow(new NotAvailablesDatesException());
	
	mockMvc.perform(post("/booking/")
	                .contentType("application/json")
	                .content(objectMapper.writeValueAsString(request)))
	                .andExpect(status().isBadRequest());
    }
    
    @Test
    public void createBookingSuccessfully() throws Exception {
	BookingControllerRequest request = new BookingControllerRequest("Da Vinci Leonardo", "davincil@gmail.com", LocalDate.now().plusDays(1), LocalDate.now().plusDays(3));
	
	when(bookingServiceMock.createBooking(request)).thenReturn(booking);
	mockMvc.perform(post("/booking/")
        		.contentType("application/json")
        		.content(objectMapper.writeValueAsString(request)))
        		.andExpect(status().isCreated());
    }
    
    
    //CANCELATION BOOKING TEST CASES
    @Test
    public void cancelBookingWithWrongId_ShouldFail() throws Exception {
	long bookingId = 1;
	when(bookingServiceMock.cancelBooking(bookingId)).thenThrow(new BookingNotFoundException(bookingId));
	mockMvc.perform(delete("/booking/{id}", bookingId)
        		.contentType("application/json"))
        		.andExpect(status().isNotFound());
    }
    
        
    @Test
    public void cancelNonActiveBooking_ShouldFail() throws Exception {
	long bookingId = 1;
	when(bookingServiceMock.cancelBooking(bookingId)).thenThrow(new AlreadyCancelledBookingException(bookingId));
	mockMvc.perform(delete("/booking/{id}", bookingId)
        		.contentType("application/json"))
        		.andExpect(status().isBadRequest());
    }
    
    @Test
    public void cancelNonActiveBooking_Successfuly() throws Exception {
	long bookingId = 1;
	when(bookingServiceMock.cancelBooking(bookingId)).thenReturn(booking);
	mockMvc.perform(delete("/booking/{id}", bookingId)
        		.contentType("application/json"))
        		.andExpect(status().isOk());
    }
    
    //UPDATE BOOKING TEST
    @Test
    public void updateBookingSuccessfully() throws Exception {
	BookingControllerRequest request = new BookingControllerRequest("Da Vinci Leonardo", "davincil@gmail.com", LocalDate.now().plusDays(1), LocalDate.now().plusDays(3));
	Long bookingId = 1l;
	when(bookingServiceMock.updateBooking(request, bookingId)).thenReturn(booking);
	mockMvc.perform(patch("/booking/" + bookingId)
        		.contentType("application/json")
        		.content(objectMapper.writeValueAsString(request)))
        		.andExpect(status().isOk());
    }
    
    
    @Test
    public void updateBookingOnUnavailableDate_ShouldFail() throws Exception {
	BookingControllerRequest request = new BookingControllerRequest("Da Vinci Leonardo", "davincil@gmail.com", LocalDate.now().plusDays(1), LocalDate.now().plusDays(3));
	
	Long bookingId = 1l;
	when(bookingServiceMock.updateBooking(request, bookingId)).thenThrow(new NotAvailablesDatesException());
	mockMvc.perform(patch("/booking/{id}", bookingId)
        		.contentType("application/json")
        		.content(objectMapper.writeValueAsString(request)))
        		.andExpect(status().isBadRequest());
    }
    
    
    @Test
    public void updateBookingWrongId_ShouldFail() throws Exception {
	BookingControllerRequest request = new BookingControllerRequest("Da Vinci Leonardo", "davincil@gmail.com", LocalDate.now().plusDays(1), LocalDate.now().plusDays(3));
	long bookingId = 1l;
	
	when(bookingServiceMock.updateBooking(request, bookingId)).thenThrow(new BookingNotFoundException(bookingId));

	mockMvc.perform(patch("/booking/{id}", bookingId)
        		.contentType("application/json")
        		.content(objectMapper.writeValueAsString(request)))
        		.andExpect(status().isNotFound());
    }
    
    
    @Test
    public void updateNonActiveBooking_ShouldFail() throws Exception {
	BookingControllerRequest request = new BookingControllerRequest("Da Vinci Leonardo", "davincil@gmail.com", LocalDate.now().plusDays(1), LocalDate.now().plusDays(3));
	long bookingId = 1l;

	when(bookingServiceMock.updateBooking(request, bookingId)).thenThrow(new DeniedBookingOperationException(BookingStatus.CANCELLED.toString()));
	mockMvc.perform(patch("/booking/{id}", bookingId)
        		.contentType("application/json")
        		.content(objectMapper.writeValueAsString(request)))
        		.andExpect(status().isMethodNotAllowed());
    }


}

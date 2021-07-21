package com.island.bookingapi.controller;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.island.bookingapi.model.Booking;
import com.island.bookingapi.request.BookingControllerRequest;
import com.island.bookingapi.response.BookingResponse;
import com.island.bookingapi.service.BookingService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;

/**
 * REST Controller layer. It basically implements the RESTFul API
 */
@RestController()
@RequestMapping("/booking")
@Slf4j
public class BookingController {

	@Autowired
	private BookingService bookingService;

	@Operation(summary = "Retrieve a whole booking")
	@ApiResponses(value = { @ApiResponse(responseCode = "404", description = "id not found") })
	@GetMapping(value = "/booking/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody	
	public ResponseEntity<BookingResponse> getBooking(@PathVariable @NotNull long id) {
       
		Booking booking = this.bookingService.getBooking(id);
        log.info("Booking for user {}", booking.getFullName());
        return ResponseEntity.status(HttpStatus.OK).body(new BookingResponse(booking));
    }

	
	
	
	@Operation(summary = "Create a new Booking with the provided details")
	@ApiResponses(value = { @ApiResponse(responseCode = "400", description = "When booking dates are already booked") })
    @PostMapping(value = "/booking", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<BookingResponse> createBooking(@RequestBody @Valid BookingControllerRequest request) {
        Booking newBooking = bookingService.create(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(new BookingResponse(newBooking));
    }
	
	
	@Operation(summary = "Cancel the Booking related with the recibed id")
	@ApiResponses(value = { @ApiResponse(responseCode = "404", description = "bookingId not found"),
							@ApiResponse(responseCode = "400", description = "Invalid booking status (other than ACTIVE")})
	@DeleteMapping(value = "/{bookingId}")
    public ResponseEntity<BookingResponse> cancelBooking(@PathVariable Long bookingId) {
        Booking booking = this.bookingService.cancelBooking(bookingId);
        return ResponseEntity.status(HttpStatus.OK).body(new BookingResponse(booking));
    }

	@Operation(summary = "Updates a Booking with the provided id")
	@ApiResponses(value = { @ApiResponse(responseCode = "404", description = "bookingId not found"),
							@ApiResponse(responseCode = "400", description = "Invalid booking status (other than ACTIVE)"),
							@ApiResponse(responseCode = "400", description = "If the date range has unavailable dates"),})
    @PatchMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BookingResponse> updateBooking(@RequestBody @Valid BookingControllerRequest request, @PathVariable long id){
		Booking booking = this.bookingService.updateBooking(request, id);
		
		return ResponseEntity.status(HttpStatus.OK).body(new BookingResponse(booking));
	}
	

}

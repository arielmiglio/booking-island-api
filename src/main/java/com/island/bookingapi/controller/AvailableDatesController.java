package com.island.bookingapi.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.validation.constraints.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.island.bookingapi.response.AvailabilityResponse;
import com.island.bookingapi.service.AvailableDatesServices;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;

/**
 * REST Controller layer. It basically implements the RESTFul API
 */
@RestController()
@RequestMapping("/availability")
@Slf4j
public class AvailableDatesController {
	
	@Autowired
	private AvailableDatesServices availableDatesServices;
	
	@Operation(summary = "Search available dates between the sended dates")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "When find dates") })
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody	
	public ResponseEntity<AvailabilityResponse> getAvailableDates(
            @Future @RequestParam(value = "from", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @Future @RequestParam(value = "to", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        from = Optional.ofNullable(from).orElse(LocalDate.now().plusDays(1));
        to = Optional.ofNullable(to).orElse(from.plusMonths(1));
        List<LocalDate> availableDates = this.availableDatesServices.getAvailableDates(from, to);
        return ResponseEntity.status(HttpStatus.OK).body(AvailabilityResponse.builder().from(from).to(to).availableDates(availableDates).build());
	}

}

package com.island.bookingapi.response;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AvailabilityResponse {

    private LocalDate from;
    private LocalDate to;
    private List<LocalDate> availableDates;
}

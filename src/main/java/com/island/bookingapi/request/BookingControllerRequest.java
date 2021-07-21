package com.island.bookingapi.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.island.bookingapi.validation.ValidateDateRange;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@RequiredArgsConstructor
@ValidateDateRange(arrivalDate = "arrivalDate", departureDate = "departureDate")
public class BookingControllerRequest {

    @NotBlank
    private final String fullName;
    @Email
    @NotBlank
    private final String userEmail;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private final LocalDate arrivalDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private final LocalDate departureDate;


}

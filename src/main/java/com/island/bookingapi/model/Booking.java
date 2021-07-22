package com.island.bookingapi.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
import com.island.bookingapi.exception.DeniedBookingOperationException;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Booking {

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

    public Booking(String name, String email, LocalDate arribalDate, LocalDate departureDate) {
	this.fullName = name;
	this.userEmail = email;
	this.arrivalDate = arribalDate;
	this.departureDate = departureDate;
	this.bookingStatus = BookingStatus.ACTIVE;
    }

    public void cancel() {
	if (BookingStatus.ACTIVE != this.bookingStatus) {
	    throw new AlreadyCancelledBookingException();
	}
	this.bookingStatus = BookingStatus.CANCELLED;
    }

}

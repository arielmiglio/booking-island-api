package com.island.bookingapi.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 
 * @author miglioa
 *
 *         This class is used to persist each date that has a concreted booking.
 *         Being the column day annotated with unique = true, avoid insert duplicated bookings days.
 *         Is used to resolve concurrency request 
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class DateBooked {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private LocalDate day;

    public DateBooked(LocalDate date) {
	this.day = date;
    }

}

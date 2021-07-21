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
 * This class is used to instance each date that has concrete bookings.
 * Being the ID the only attribute cannot be able to persist two instances with in the same day
 * It become the class a constraint to avoid insert duplicated bookings days.
 *  
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

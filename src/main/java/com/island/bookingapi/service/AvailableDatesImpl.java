package com.island.bookingapi.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.Future;

import org.springframework.stereotype.Service;

import com.island.bookingapi.repository.DateBookedRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AvailableDatesImpl implements AvailableDatesServices {

    private DateBookedRepository dateBookedRepository;

    @Override
    public List<LocalDate> getAvailableDates(@Future LocalDate from, @Future LocalDate to) {
	log.info("Getting availability for range of days from: {} to: {}", from, to);
	List<LocalDate> bookedDays = this.dateBookedRepository.getOrderedBookedDates(from, to);
	List<LocalDate> periodDays = from.datesUntil(to.plusDays(1)).collect(Collectors.toList());

	return periodDays.stream().filter(x -> !bookedDays.contains(x)).collect(Collectors.toList());
    }

}

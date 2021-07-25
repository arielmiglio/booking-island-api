package com.island.bookingapi.unit.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.island.bookingapi.repository.DateBookedRepository;
import com.island.bookingapi.service.DatesBookedService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(DatesBookedService.class)
public class DatesBookedServiceTest {
    
    @MockBean
    private DateBookedRepository dateBookedRepository;
    
    @Autowired
    private DatesBookedService datesBookedService;
    
    @Test
    public void allAvailableDates() {
	LocalDate from = LocalDate.now();
	LocalDate to = LocalDate.now().plusDays(3);
	
	List<LocalDate> emptyList = new LinkedList<LocalDate>();
	when(dateBookedRepository.getOrderedBookedDates(from, to)).thenReturn(emptyList);
	List<LocalDate> availableDates = this.datesBookedService.getAvailableDates(from, to);
	assertEquals(availableDates.size(), 4);
    }
    
    @Test
    public void nonAvailableDates() {
	LocalDate from = LocalDate.now();
	LocalDate to = LocalDate.now().plusDays(1);
	
	List<LocalDate> twoElementList = new LinkedList<LocalDate>();
	twoElementList.add(from);
	twoElementList.add(to);
	when(dateBookedRepository.getOrderedBookedDates(from, to)).thenReturn(twoElementList);
	List<LocalDate> availableDates = this.datesBookedService.getAvailableDates(from, to);
	assertEquals(availableDates.size(), 0);
    }
    
    @Test
    public void parcialAvailableDates() {
	LocalDate from = LocalDate.now();
	LocalDate to = LocalDate.now().plusDays(1);
	
	List<LocalDate> oneElementList = new LinkedList<LocalDate>();
	oneElementList.add(from);
	
	when(dateBookedRepository.getOrderedBookedDates(from, to)).thenReturn(oneElementList);
	List<LocalDate> availableDates = this.datesBookedService.getAvailableDates(from, to);
	assertEquals(availableDates.size(), 1);
	assertEquals(availableDates.get(0), to);
	
    }
    

}

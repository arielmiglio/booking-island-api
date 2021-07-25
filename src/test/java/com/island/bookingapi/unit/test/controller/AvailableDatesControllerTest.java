package com.island.bookingapi.unit.test.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.island.bookingapi.controller.AvailableDatesController;
import com.island.bookingapi.service.DatesBookedService;


@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = AvailableDatesController.class)
public class AvailableDatesControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private DatesBookedService availableDatesServiceMock;
    
    
    @Test
    public void givenExplicitValuesOfFromAndToInputReturn200() throws Exception {
	LocalDate from = LocalDate.now().plusDays(1);
	LocalDate to = LocalDate.now().plusDays(15);
	List<LocalDate> periodDays = from.datesUntil(to.plusDays(1)).collect(Collectors.toList());
	when(availableDatesServiceMock.getAvailableDates(from, to)).thenReturn(periodDays);
	mockMvc.perform(get("/availability")
				.param("from", from.toString())
				.param("to", to.toString())
                		.contentType("application/json"))
                		.andExpect(status().isOk());
    }
    
    @Test
    public void givenNullValuesOfFromAndToInputReturn200() throws Exception {
	LocalDate from = null;
	LocalDate to = null;
	List<LocalDate> periodDays = LocalDate.now().plusDays(1).datesUntil(LocalDate.now().plusMonths(1)).collect(Collectors.toList());
	when(availableDatesServiceMock.getAvailableDates(from, to)).thenReturn(periodDays);
	mockMvc.perform(get("/availability")
                			.contentType("application/json"))
                			.andExpect(status().isOk());
    }
    
}

package com.island.bookingapi.unit.test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.island.bookingapi.repository.BookingRepository;
import com.island.bookingapi.repository.DateBookedRepository;
import com.island.bookingapi.service.DatesBookedService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(DatesBookedService.class)
public class BookingServiceTest {
    
    @MockBean
    private BookingRepository bookingRepository;
    
    @MockBean
    private DateBookedRepository dateBookedRepository;

}

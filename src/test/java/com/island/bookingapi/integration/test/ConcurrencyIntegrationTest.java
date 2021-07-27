package com.island.bookingapi.integration.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.island.bookingapi.repository.BookingRepository;
import com.island.bookingapi.request.BookingControllerRequest;
import com.island.bookingapi.service.BookingService;
import com.island.bookingapi.service.DatesBookedService;

import lombok.experimental.PackagePrivate;
import lombok.extern.slf4j.Slf4j;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
@AutoConfigureMockMvc
public class ConcurrencyIntegrationTest {

    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;


    @Autowired
    private DatesBookedService datesBookedService;
    
    @Autowired
    private BookingRepository bookingRepository;
    
  
    @Test
    @Transactional
    public void whenBookSameBookingSeveralTimes_bookOnlyOnce() throws JsonProcessingException, Exception {
	List<LocalDate> availableDates =  datesBookedService.getAvailableDates(LocalDate.now(), LocalDate.now().plusMonths(1));
	long countBeforeRun = bookingRepository.count();
	
	int availableDateBefore = availableDates.size();
	
        Runnable runableCreationBooking = this.getRunnable("Paz Miglio", "pazmiglio@gmail.com", LocalDate.now().plusDays(15), LocalDate.now().plusDays(17));
        List<Thread> threadList = new ArrayList<Thread>();
        for (int i = 0; i < 10; i++) {
            threadList.add(new Thread(runableCreationBooking));
        }
        this.runThreads(threadList);

        assertEquals(availableDateBefore -2, datesBookedService.getAvailableDates(LocalDate.now(), LocalDate.now().plusMonths(1)).size());
        assertEquals(countBeforeRun+1, bookingRepository.count());
    }
    
    @Test
    @Transactional
    public void whenBookDifferentBookingSeveralTimes_bookAll() throws JsonProcessingException, Exception {
	List<LocalDate> availableDates =  datesBookedService.getAvailableDates(LocalDate.now(), LocalDate.now().plusMonths(1));
	long countBeforeRun = bookingRepository.count();
	log.debug("Booking count before runing: "  + countBeforeRun);
	
	int availableDateBefore = availableDates.size();
	
        Runnable runablePazBooking = this.getRunnable("Paz Miglio", "pazmiglio@gmail.com", LocalDate.now().plusDays(17), LocalDate.now().plusDays(18));
        Runnable runableAnaBooking = this.getRunnable("Ana Miglio", "anamiglio@gmail.com", LocalDate.now().plusDays(18), LocalDate.now().plusDays(19));
        Runnable runableMariBooking = this.getRunnable("Mari Miglio", "marimiglio@gmail.com", LocalDate.now().plusDays(19), LocalDate.now().plusDays(20));
        Runnable runableGiBooking = this.getRunnable("Gi Miglio", "gimiglio@gmail.com", LocalDate.now().plusDays(21), LocalDate.now().plusDays(23));
        
        List<Thread> threadList = new ArrayList<Thread>();
        threadList.add(new Thread(runablePazBooking));
        threadList.add(new Thread(runableAnaBooking));
        threadList.add(new Thread(runableMariBooking));
        threadList.add(new Thread(runableGiBooking));
        
        this.runThreads(threadList);

        assertEquals(availableDateBefore -5, datesBookedService.getAvailableDates(LocalDate.now(), LocalDate.now().plusMonths(1)).size());
        assertEquals(countBeforeRun+4, bookingRepository.count());
    }
    


    private void runThreads(List<Thread> threadList) throws InterruptedException {
	for (Thread t : threadList) {
            t.start();
        }
	for (Thread t : threadList) {
            t.join();
        }
	log.debug("ALL TREADS ALREADY FINISH");
    }


    private Runnable getRunnable(String fullName, String email, LocalDate arraivalDate, LocalDate departureDate) {
        return new Runnable() {
            @Override
            public void run() {
                try {
                    ConcurrencyIntegrationTest.this.restInvocationBooking(fullName, email, arraivalDate, departureDate);
                } catch (Exception e) {
                }
            }
        };
    }


    protected void restInvocationBooking(String fullName, String email, LocalDate arraivalDate, LocalDate departureDate) throws JsonProcessingException, Exception {
	BookingControllerRequest request = new BookingControllerRequest(fullName, email, arraivalDate, departureDate);
	log.info("CREATING A NEW BOOKING");
	mockMvc.perform(post("/booking/")
        		.contentType("application/json")
        		.content(objectMapper.writeValueAsString(request)));
	
    }


    
}

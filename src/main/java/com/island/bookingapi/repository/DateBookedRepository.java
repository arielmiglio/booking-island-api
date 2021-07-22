package com.island.bookingapi.repository;


import com.island.bookingapi.model.DateBooked;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface DateBookedRepository extends JpaRepository<DateBooked, Long> {

    @Query("SELECT d.day from DateBooked d where d.day between :initialDate and :endDate order by d.day asc")
    List<LocalDate> getOrderedBookedDates(@Param("initialDate") LocalDate initialDate, @Param("endDate") LocalDate endDate);


//    @Query("SELECT d.day from DateBooked d where d.day between :initialDate and :endDate")
//    List<LocalDate> getDatesBooked(@Param("initialDate") LocalDate initialDate, @Param("endDate") LocalDate endDate);

    void findByDayBetween(LocalDate initialDate, LocalDate endDate);
        
    void deleteByDayIn(List<LocalDate> dates);
    
    
}

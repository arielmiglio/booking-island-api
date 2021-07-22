package com.island.bookingapi.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.island.bookingapi.model.DateBooked;

public interface DateBookedRepository extends JpaRepository<DateBooked, Long> {

    @Query("SELECT d.day from DateBooked d where d.day between :initialDate and :endDate order by d.day asc")
    List<LocalDate> getOrderedBookedDates(@Param("initialDate") LocalDate initialDate,
	    @Param("endDate") LocalDate endDate);

    void findByDayBetween(LocalDate initialDate, LocalDate endDate);

    void deleteByDayIn(List<LocalDate> dates);

}

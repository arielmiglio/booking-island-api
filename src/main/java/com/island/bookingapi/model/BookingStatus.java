package com.island.bookingapi.model;

public enum BookingStatus {
    ACTIVE(1),
    CANCELLED(0);

    Integer id;

    BookingStatus(Integer id) {
        this.id = id;
    }

}

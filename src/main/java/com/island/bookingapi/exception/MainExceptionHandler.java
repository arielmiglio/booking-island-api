package com.island.bookingapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * This class represents the main exception handler for the controller layer.
 * All the non caught runtime exceptions thrown will be handled here and provide a proper HTTP response
 */
@ControllerAdvice
public class MainExceptionHandler extends ResponseEntityExceptionHandler{
    

  
    @ExceptionHandler(value = BookingNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponse handleBookingNotFound(BookingNotFoundException e) {
        return ErrorResponse.builder().
                message("Booking ID: " + e.getBookingId() + " was not found").
                errorCode(e.getErrorCode()).
                build();
    }

    @ExceptionHandler(value = AlreadyCancelledBookingException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleAlreadyCancelledBooking(AlreadyCancelledBookingException e) {
        return ErrorResponse.builder().
                message("The booing with Booking ID: " + e.getBookingId() + " was already cancelled").
                errorCode(e.getErrorCode()).
                build();
    }


    @ExceptionHandler(value = DeniedBookingOperationException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ResponseBody
    public ErrorResponse handleDeniedBookingOperation(DeniedBookingOperationException e) {
        return ErrorResponse.builder().
                message("A booking in state: " + e.getStatus() + " can't be modified").
                errorCode(e.getErrorCode()).
                build();
    }

    
    @ExceptionHandler(value = NotAvailablesDatesException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleNonAvailablesDates(NotAvailablesDatesException e) {
        return ErrorResponse.builder().
                message("Some days are not available").
                errorCode(e.getErrorCode()).
                build();
    }
    
    
    @ExceptionHandler(value = ValidationDatesException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleEmptyBookingDatesNotAllowed(ValidationDatesException e) {
        return ErrorResponse.builder().
                message(e.getMessage()).
                errorCode(e.getErrorCode()).
                build();
    }
    

    
    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException e, WebRequest request) {
        String error = e.getName() + " should be of type " + e.getRequiredType().getName();
        return ErrorResponse.builder().
        		message(error).
        		errorCode(0).build();
    }


    /**
     * Handle previously unhandled exceptions to keep response format
     *
     * @param ex      Exception
     * @param request
     * @return ResponseEntity with HTTP status 500
     */
    @ExceptionHandler({Exception.class})
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Object handleMethodException(Exception ex, WebRequest request) {
        
        ErrorResponse response = ErrorResponse.builder().message(ex.getMessage()).build();
        return response;
    }

}

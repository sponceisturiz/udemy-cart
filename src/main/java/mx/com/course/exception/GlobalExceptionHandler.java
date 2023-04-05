package mx.com.course.exception;

import java.net.ConnectException;
import java.util.Date;

import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.extern.slf4j.Slf4j;
import mx.com.course.exception.custom.CartIsEmptyException;
import mx.com.course.exception.custom.CourseAlreadyInCartException;
import mx.com.course.exception.custom.CourseNotInUserCartException;
import mx.com.course.exception.custom.CustomErrorDetails;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    
    @ExceptionHandler(value = {CartIsEmptyException.class})
    public ResponseEntity<?> cartIsEmptyException(CartIsEmptyException exception, WebRequest request) {
        return new ResponseEntity<>(generateErrorDetails(
                exception.getMessage() != null ? exception.getMessage() : "cart is empty" , request.getDescription(false)), HttpStatus.OK);
    }

    @ExceptionHandler(value = {CourseNotInUserCartException.class})
    public ResponseEntity<?> courseNotInUserCartException(CourseNotInUserCartException exception, WebRequest request) {
        return new ResponseEntity<>(generateErrorDetails(
                exception.getMessage() != null ? exception.getMessage() : "course is not in the user cart" , request.getDescription(false)), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {CourseAlreadyInCartException.class})
    public ResponseEntity<?> courseAlreadyInCartException(CourseAlreadyInCartException exception, WebRequest request) {
        return new ResponseEntity<>(generateErrorDetails(
                exception.getMessage() != null ? exception.getMessage() : "course is already in user cart" , request.getDescription(false)), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {CannotCreateTransactionException.class})
    public ResponseEntity<?> cannotCreateTransactionException(CannotCreateTransactionException exception, WebRequest request) {
        if (exception.contains(ConnectException.class)) {
            log.error("Unable to reach Database:  {}", exception.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //ConstraintViolationException
    @ExceptionHandler(ConstraintViolationException.class)
    public final ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex, WebRequest request){
        return new ResponseEntity<>(generateErrorDetails(ex.getMessage(), request.getDescription(false)), HttpStatus.BAD_REQUEST); 
    }

    //MethodArgumentNotValid
    @Override
    protected  ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return new ResponseEntity<>(generateErrorDetails(
            "MethodArgumentNotValid Exception", ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    //HttpRequestMethodNotSupported
    @Override
    protected  ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return new ResponseEntity<>(
            generateErrorDetails("HttpRequestMethodNotSupportedException - Method Not allowed", ex.getMessage()), HttpStatus.METHOD_NOT_ALLOWED);
    }

    //NoHandlerFoundException - commonly routes that does not exists
    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
            return new ResponseEntity<>(
                generateErrorDetails("NoHandlerFoundException - Route does not exist", ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    private CustomErrorDetails generateErrorDetails(String message, String errorDetails){
        return new CustomErrorDetails(new Date(), message, errorDetails);
    }
}

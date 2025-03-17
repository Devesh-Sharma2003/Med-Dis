package in.kpmg.medicaldisbursement.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;

import in.kpmg.medicaldisbursement.dtos.ApiResponse2;
import in.kpmg.medicaldisbursement.dtos.ApiResponseStatus;
import in.kpmg.medicaldisbursement.dtos.ResponseException;
import in.kpmg.medicaldisbursement.exceptions.BadRequestException;
import in.kpmg.medicaldisbursement.exceptions.InternalServerErrorException;
import in.kpmg.medicaldisbursement.exceptions.NoRecordFoundException;
import in.kpmg.medicaldisbursement.exceptions.NotFoundException;
import in.kpmg.medicaldisbursement.exceptions.UnauthorizedException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

//    @ExceptionHandler(value = {NoRecordFoundException.class})
//    public ResponseEntity<ResponseException> handleNoCustomerFoundException(NoRecordFoundException ncfe) {
//    	return new ResponseEntity<>(new ApiResponse2<>(false, "Unexpected Error Occurred!", null, HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
//    }
    
    @ExceptionHandler(NoRecordFoundException.class)
    public ResponseEntity<ApiResponse2<?>> handleNoRecordsException(NoRecordFoundException e) {
    	e.printStackTrace();
        return new ResponseEntity<>(new ApiResponse2<>(false, e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolationException(Exception ex, WebRequest request) {
        List<String> details = new ArrayList<String>();
        details.add(ex.getMessage());
        ResponseException err = new ResponseException();
        err.setMsg(details.toString());
        return new ResponseEntity<ResponseException>(err, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleInvalidArgument(MethodArgumentNotValidException ex) {

        Map<String, String> errorMap = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errorMap.put(error.getField(), error.getDefaultMessage());
        });
        ResponseException err = new ResponseException();
        err.setMsg(errorMap.toString());

        return new ResponseEntity<>(new ApiResponse2<>(false, ApiResponseStatus.badRequest, errorMap, HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiResponse2<?>> handleEntityNotFoundException(EntityNotFoundException e) {
        return new ResponseEntity<>(new ApiResponse2<>(false, e.getMessage(), null, HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse2<?>> handleBadRequestException(BadRequestException e) {
        return new ResponseEntity<>(new ApiResponse2<>(false, e.getMessage(), null, HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
    }

//    @ExceptionHandler(ConstraintViolationException.class)
//    public ResponseEntity<ApiResponse2<?>> handleConstraintViolationException(ConstraintViolationException e) {
//        return new ResponseEntity<>(new ApiResponse2<>(false, e.getMessage(), null, HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
//    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiResponse2<?>> handleUnauthorisedException(UnauthorizedException e) {
        return new ResponseEntity<>(new ApiResponse2<>(false, e.getMessage(), null, HttpStatus.UNAUTHORIZED.value()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiResponse2<?>> handleNotFoundException(NotFoundException e) {
        return new ResponseEntity<>(new ApiResponse2<>(false, e.getMessage(), null, HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse2<?>> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        return new ResponseEntity<>(new ApiResponse2<>(false, e.getMessage(), null, HttpStatus.FORBIDDEN.value()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<ApiResponse2<?>> handleException(InternalServerErrorException e) {
        return new ResponseEntity<>(new ApiResponse2<>(false, e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse2<?>> handleBaseExceptop(Exception e) {
        e.printStackTrace();
        return new ResponseEntity<>(new ApiResponse2<>(false, e.getMessage(), e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
    }




//    @ExceptionHandler(Exception.class)
//    public ApiResponse2<Object> exception(Exception ex) {
//        return new ApiResponse2<>(false, ex.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR.value());
//    }


}
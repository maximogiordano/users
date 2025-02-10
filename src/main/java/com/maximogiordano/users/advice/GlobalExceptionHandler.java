package com.maximogiordano.users.advice;

import com.maximogiordano.users.dto.ErrorDto;
import com.maximogiordano.users.dto.ErrorItemDto;
import com.maximogiordano.users.exception.ConflictException;
import com.maximogiordano.users.exception.ResourceNotFoundException;
import com.maximogiordano.users.utils.DateTimeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    public static final int CONSTRAINT_VIOLATION_ERROR_CODE = 1;
    public static final int CONFLICT_ERROR_CODE = 2;
    public static final int RESOURCE_NOT_FOUND_ERROR_CODE = 3;

    private final DateTimeUtils dateTimeUtils;

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorDto> handleConstraintViolationException(ConstraintViolationException e) {
        var errorDto = new ErrorDto();
        var errors = e.getConstraintViolations()
                .stream()
                .map(this::toErrorItem)
                .collect(Collectors.toList());

        errorDto.setErrors(errors);

        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    private ErrorItemDto toErrorItem(ConstraintViolation<?> constraintViolation) {
        var errorItemDto = new ErrorItemDto();

        errorItemDto.setTimestamp(dateTimeUtils.currentOffsetDateTime());
        errorItemDto.setCode(CONSTRAINT_VIOLATION_ERROR_CODE);
        errorItemDto.setDetail(constraintViolation.toString());

        return errorItemDto;
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorDto> handleConflictException(ConflictException e) {
        return handleException(e, CONFLICT_ERROR_CODE, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDto> handleResourceNotFoundException(ResourceNotFoundException e) {
        return handleException(e, RESOURCE_NOT_FOUND_ERROR_CODE, HttpStatus.NOT_FOUND);
    }

    private ResponseEntity<ErrorDto> handleException(Exception e, int code, HttpStatus httpStatus) {
        var errorItemDto = new ErrorItemDto();

        errorItemDto.setTimestamp(dateTimeUtils.currentOffsetDateTime());
        errorItemDto.setCode(code);
        errorItemDto.setDetail(e.getMessage());

        var errorDto = new ErrorDto();

        errorDto.setErrors(List.of(errorItemDto));

        return new ResponseEntity<>(errorDto, httpStatus);
    }
}

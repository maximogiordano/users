package com.maximogiordano.users.advice;

import com.maximogiordano.users.dto.ErrorDto;
import com.maximogiordano.users.dto.ErrorItemDto;
import com.maximogiordano.users.exception.ConflictException;
import com.maximogiordano.users.exception.ResourceNotFoundException;
import com.maximogiordano.users.utils.DateTimeUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {
    @InjectMocks
    GlobalExceptionHandler globalExceptionHandler; // system under test

    @Mock
    DateTimeUtils dateTimeUtils; // dependency

    @Mock
    ConstraintViolation<?> constraintViolation; // internal mock

    @Test
    void handleConstraintViolationException() {
        // given an input Exception
        ConstraintViolationException inputException = new ConstraintViolationException(Set.of(constraintViolation));

        // and the current date and time with time offset
        OffsetDateTime now = OffsetDateTime.of(2024, 2, 6, 10, 15, 30, 0, ZoneOffset.UTC);

        // and the expected output
        ErrorItemDto errorItemDto = new ErrorItemDto();

        errorItemDto.setTimestamp(now);
        errorItemDto.setCode(GlobalExceptionHandler.CONSTRAINT_VIOLATION_ERROR_CODE);
        errorItemDto.setDetail("some error detail");

        ErrorDto expectedOutput = new ErrorDto();

        expectedOutput.setErrors(List.of(errorItemDto));

        // and the mocks behavior
        when(dateTimeUtils.currentOffsetDateTime()).thenReturn(now);
        when(constraintViolation.toString()).thenReturn("some error detail");

        // when the given input is handled
        ResponseEntity<ErrorDto> result = globalExceptionHandler.handleConstraintViolationException(inputException);

        // then the corresponding methods are called
        verify(dateTimeUtils).currentOffsetDateTime();

        // and the expected output is obtained
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals(expectedOutput, result.getBody());
    }

    @Test
    void handleConflictException() {
        // given an input Exception
        ConflictException inputException = new ConflictException("some message");

        // and the current date and time with time offset
        OffsetDateTime now = OffsetDateTime.of(2024, 2, 6, 10, 15, 30, 0, ZoneOffset.UTC);

        // and the expected output
        ErrorItemDto errorItemDto = new ErrorItemDto();

        errorItemDto.setTimestamp(now);
        errorItemDto.setCode(GlobalExceptionHandler.CONFLICT_ERROR_CODE);
        errorItemDto.setDetail("some message");

        ErrorDto expectedOutput = new ErrorDto();

        expectedOutput.setErrors(List.of(errorItemDto));

        // and the mocks behavior
        when(dateTimeUtils.currentOffsetDateTime()).thenReturn(now);

        // when the given input is handled
        ResponseEntity<ErrorDto> result = globalExceptionHandler.handleConflictException(inputException);

        // then the corresponding methods are called
        verify(dateTimeUtils).currentOffsetDateTime();

        // and the expected output is obtained
        assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
        assertEquals(expectedOutput, result.getBody());
    }

    @Test
    void handleResourceNotFoundException() {
        // given an input Exception
        ResourceNotFoundException inputException = new ResourceNotFoundException("some message");

        // and the current date and time with time offset
        OffsetDateTime now = OffsetDateTime.of(2024, 2, 6, 10, 15, 30, 0, ZoneOffset.UTC);

        // and the expected output
        ErrorItemDto errorItemDto = new ErrorItemDto();

        errorItemDto.setTimestamp(now);
        errorItemDto.setCode(GlobalExceptionHandler.RESOURCE_NOT_FOUND_ERROR_CODE);
        errorItemDto.setDetail("some message");

        ErrorDto expectedOutput = new ErrorDto();

        expectedOutput.setErrors(List.of(errorItemDto));

        // and the mocks behavior
        when(dateTimeUtils.currentOffsetDateTime()).thenReturn(now);

        // when the given input is handled
        ResponseEntity<ErrorDto> result = globalExceptionHandler.handleResourceNotFoundException(inputException);

        // then the corresponding methods are called
        verify(dateTimeUtils).currentOffsetDateTime();

        // and the expected output is obtained
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertEquals(expectedOutput, result.getBody());
    }
}

package com.example.distance.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.client.HttpClientErrorException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler exceptionHandler;

    @Test
    void handleBadRequest() {
        // Arrange
        HttpClientErrorException exception = new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Bad request");

        // Act
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleBadRequest(exception);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("400 Bad request", response.getBody().getMessage());
    }

    @Test
    void handleMissingServletRequestParameterException() {
        // Arrange
        MissingServletRequestParameterException exception = new MissingServletRequestParameterException("param", "type");

        // Act
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleMissingServletRequestParameterException(exception);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Required request parameter 'param' for method parameter type type is not present", response.getBody().getMessage());
    }

    @Test
    void handleInternalServerError() {
        // Arrange
        Exception exception = new Exception("Internal server error");

        // Act
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleInternalServerError(exception);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Internal Server Error", response.getBody().getMessage());
    }
}

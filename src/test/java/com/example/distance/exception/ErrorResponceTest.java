package com.example.distance.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ErrorResponseTest {

    @Test
    void testConstructorAndGetters() {
        // Arrange
        int status = 404;
        String message = "Not Found";

        // Act
        ErrorResponse errorResponse = new ErrorResponse(status, message);

        // Assert
        assertEquals(status, errorResponse.getStatus());
        assertEquals(message, errorResponse.getMessage());
    }

    @Test
    void testSetters() {
        // Arrange
        ErrorResponse errorResponse = new ErrorResponse(0, "");

        // Act
        errorResponse.setStatus(500);
        errorResponse.setMessage("Internal Server Error");

        // Assert
        assertEquals(500, errorResponse.getStatus());
        assertEquals("Internal Server Error", errorResponse.getMessage());
    }
}

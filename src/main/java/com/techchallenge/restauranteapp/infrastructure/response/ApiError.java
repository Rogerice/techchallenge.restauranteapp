// infrastructure/response/ApiError.java
package com.techchallenge.restauranteapp.infrastructure.response;

import java.time.OffsetDateTime;
import java.util.List;

public record ApiError(
        int status,
        String error,
        String message,
        String path,
        OffsetDateTime timestamp,
        List<FieldError> fieldErrors
) {
    public record FieldError(String field, String message) {}
}

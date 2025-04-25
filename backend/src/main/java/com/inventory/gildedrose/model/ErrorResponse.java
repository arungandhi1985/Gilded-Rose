package com.inventory.gildedrose.model;

import java.time.LocalDateTime;

public record ErrorResponse(
        String message,
        String details,
        int status,
        LocalDateTime timestamp
) {
    public ErrorResponse(String message, String details, int status) {
        this(message, details, status, LocalDateTime.now());
    }
}
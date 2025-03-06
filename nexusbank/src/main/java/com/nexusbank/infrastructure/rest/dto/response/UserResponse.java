package com.nexusbank.infrastructure.rest.dto.response;

import java.time.LocalDateTime;

/**
 * DTO for user information.
 */
public record UserResponse(
    String id,
    String name,
    String email,
    boolean active,
    LocalDateTime createdAt) {
}
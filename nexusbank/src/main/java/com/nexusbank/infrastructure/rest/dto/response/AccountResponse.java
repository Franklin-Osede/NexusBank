package com.nexusbank.infrastructure.rest.dto.response;

import com.nexusbank.domain.model.Account;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Datos de una cuenta bancaria")
public record AccountResponse(
    @Schema(description = "ID de la cuenta", example = "acc-123") String id,
    @Schema(description = "ID del usuario propietario", example = "user-123") String userId,
    @Schema(description = "Saldo de la cuenta", example = "1000.00") BigDecimal balance,
    @Schema(description = "Moneda de la cuenta", example = "USD") String currency,
    @Schema(description = "Estado de la cuenta", example = "true") boolean active,
    @Schema(description = "Fecha de creación", example = "2025-03-06T10:15:30") LocalDateTime createdAt,
    @Schema(description = "Fecha de última actualización", example = "2025-03-06T10:15:30") LocalDateTime updatedAt) {
  public static AccountResponse fromDomain(Account account) {
    return new AccountResponse(
        account.getId(),
        account.getUserId(),
        // Suponiendo que account.getBalance() devuelve un objeto Money
        // y que Money tiene los métodos getAmount() y getCurrency()
        account.getBalance().getAmount(),
        account.getBalance().getCurrency(),
        account.isActive(),
        account.getCreatedAt(),
        account.getUpdatedAt());
  }
}

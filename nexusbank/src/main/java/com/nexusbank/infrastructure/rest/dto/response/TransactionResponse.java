package com.nexusbank.infrastructure.rest.dto.response;

import com.nexusbank.domain.model.Transaction;
import com.nexusbank.domain.model.TransactionStatus;
import com.nexusbank.domain.model.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Datos de una transacción bancaria")
public record TransactionResponse(
        @Schema(description = "ID de la transacción", example = "tx-123") String id,
        @Schema(description = "ID de la cuenta asociada", example = "acc-123") String accountId,
        @Schema(description = "ID de la cuenta destino (opcional)", example = "acc-456") String targetAccountId,
        @Schema(description = "Datos del monto de la transacción") MoneyResponse amount,
        @Schema(description = "Tipo de transacción", example = "DEPOSIT") TransactionType type,
        @Schema(description = "Estado de la transacción", example = "COMPLETED") TransactionStatus status,
        @Schema(description = "Descripción de la transacción", example = "Depósito realizado") String description,
        @Schema(description = "Fecha de creación", example = "2025-03-06T10:15:30") LocalDateTime createdAt,
        @Schema(description = "Fecha de última actualización", example = "2025-03-06T10:15:30") LocalDateTime updatedAt) {

    public static TransactionResponse fromDomain(Transaction transaction) {
        return new TransactionResponse(
                transaction.getId(),
                transaction.getAccountId(),
                transaction.getTargetAccountId(),
                new MoneyResponse(transaction.getAmount().getAmount(), transaction.getAmount().getCurrency()),
                transaction.getType(),
                transaction.getStatus(),
                transaction.getDescription(),
                transaction.getCreatedAt(),
                transaction.getUpdatedAt());
    }

    @Schema(description = "Datos del monto de la transacción")
    public record MoneyResponse(
            @Schema(description = "Monto de la transacción", example = "150.00") BigDecimal amount,
            @Schema(description = "Moneda de la transacción", example = "USD") String currency) {
    }
}

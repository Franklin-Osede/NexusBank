package com.nexusbank.infrastructure.rest.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "Saldo de la cuenta bancaria")
public record BalanceResponse(
                @Schema(description = "Saldo actual", example = "1000.00") BigDecimal balance,
                @Schema(description = "Moneda", example = "USD") String currency) {
}

package com.pagamento.api.dto;

import com.pagamento.api.enums.StatusPagamento;
import jakarta.validation.constraints.NotNull;

public record AtualizarPagamentoDTO(
        @NotNull
        Integer codigoPagamento,
        @NotNull
        StatusPagamento statusPagamento
) {
}

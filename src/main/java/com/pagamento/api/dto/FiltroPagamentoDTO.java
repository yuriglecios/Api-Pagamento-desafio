package com.pagamento.api.dto;

import com.pagamento.api.enums.StatusPagamento;
import com.pagamento.api.enums.TipoPagamento;

public record FiltroPagamentoDTO(
        Integer codigoPagamento,
        Integer documento,
        Double valorPagamento,
        TipoPagamento tipoPagamento,
        Integer numeroCartao,
        StatusPagamento statusPagamento
) {
}

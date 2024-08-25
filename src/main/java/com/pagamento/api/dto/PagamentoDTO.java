package com.pagamento.api.dto;

import com.pagamento.api.entity.Pagamento;
import com.pagamento.api.enums.TipoPagamento;
import jakarta.validation.constraints.NotNull;

public record PagamentoDTO(
        @NotNull
        Integer documento,
        @NotNull
        Double valorPagamento,
        @NotNull
        TipoPagamento tipoPagamento,
        Integer numeroCartao
) {
    public PagamentoDTO(Pagamento pagamentoAnterior) {
        this(pagamentoAnterior.getDocumento(), pagamentoAnterior.getValorPagamento(), pagamentoAnterior.getTipoPagamento(), pagamentoAnterior.getNumeroCartao());
    }
}

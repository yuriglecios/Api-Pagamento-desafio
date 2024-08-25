package com.pagamento.api.dto;

import com.pagamento.api.entity.Pagamento;
import com.pagamento.api.enums.StatusPagamento;
import com.pagamento.api.enums.TipoPagamento;

public record DetalhamentoPagamentoDTO(
        Integer codigoPagamento,
        Integer documento,
        Double valorPagamento,
        TipoPagamento tipoPagamento,
        Integer numeroCartao,
        StatusPagamento statusPagamento,
        Boolean ativo
) {
    public DetalhamentoPagamentoDTO(Pagamento pagamento) {
        this(pagamento.getCodigoPagamento(), pagamento.getDocumento(), pagamento.getValorPagamento(), pagamento.getTipoPagamento(), pagamento.getNumeroCartao(), pagamento.getStatusPagamento(), pagamento.getAtivo());
    }

}

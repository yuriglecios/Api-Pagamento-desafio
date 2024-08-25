package com.pagamento.api.entity;

import com.pagamento.api.dto.PagamentoDTO;
import com.pagamento.api.enums.StatusPagamento;
import com.pagamento.api.enums.TipoPagamento;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Pagamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer codigoPagamento;

    private Integer documento;

    private Double valorPagamento;

    @Enumerated(EnumType.STRING)
    private TipoPagamento tipoPagamento;

    private Integer numeroCartao;

    @Enumerated(EnumType.STRING)
    private StatusPagamento statusPagamento;

    private Boolean ativo;

    public Pagamento(PagamentoDTO pagamentoDTO) {
        this.documento = pagamentoDTO.documento();
        this.tipoPagamento = pagamentoDTO.tipoPagamento();
        this.valorPagamento = pagamentoDTO.valorPagamento();
        this.numeroCartao = pagamentoDTO.numeroCartao();
        this.statusPagamento = StatusPagamento.PENDENTE_PROCESSAMENTO;
        this.ativo = Boolean.TRUE;
    }


    public void atualizarStatusPagamento(StatusPagamento statusPagamento) {
        this.statusPagamento = statusPagamento;
    }
}

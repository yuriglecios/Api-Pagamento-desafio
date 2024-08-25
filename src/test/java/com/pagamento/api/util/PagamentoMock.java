package com.pagamento.api.util;

import com.pagamento.api.dto.AtualizarPagamentoDTO;
import com.pagamento.api.dto.PagamentoDTO;
import com.pagamento.api.enums.StatusPagamento;
import com.pagamento.api.enums.TipoPagamento;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PagamentoMock {

    public static PagamentoDTO getPagDTOMock_Sucesso() {
        return new PagamentoDTO(
                123,
                123.0,
                TipoPagamento.BOLETO,
                null
        );
    }

    public static PagamentoDTO getPagDTOMock_Falha_Pix() {
        return new PagamentoDTO(
                123,
                123.0,
                TipoPagamento.PIX,
                123
        );
    }

    public static PagamentoDTO getPagDTOMock_Falha_Boleto() {
        return new PagamentoDTO(
                123,
                123.0,
                TipoPagamento.BOLETO,
                123
        );
    }

    public static PagamentoDTO getPagDTOMock_Falha_Cartao_Credito() {
        return new PagamentoDTO(
                123,
                123.0,
                TipoPagamento.CARTAO_CREDITO,
                null
        );
    }

    public static PagamentoDTO getPagDTOMock_Falha_Cartao_Debito() {
        return new PagamentoDTO(
                123,
                123.0,
                TipoPagamento.CARTAO_DEBITO,
                null
        );
    }

    public static AtualizarPagamentoDTO getAtualizarPagDTOMock_Sucesso() {
        return new AtualizarPagamentoDTO(
                1,
                StatusPagamento.PROCESSADO_SUCESSO
        );
    }

    public static AtualizarPagamentoDTO getAtualizarPagDTOMock_Falha_Caso1() {
        return new AtualizarPagamentoDTO(
                1,
                StatusPagamento.PROCESSADO_FALHA
        );
    }

}

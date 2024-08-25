package com.pagamento.api.service;

import com.pagamento.api.dto.DetalhamentoPagamentoDTO;
import com.pagamento.api.dto.PagamentoDTO;
import com.pagamento.api.entity.Pagamento;
import com.pagamento.api.enums.StatusPagamento;
import com.pagamento.api.enums.TipoPagamento;
import com.pagamento.api.repository.PagamentoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;

import static com.pagamento.api.util.PagamentoMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class PagamentoServiceTest {
    @Mock
    PagamentoRepository pagamentoRepository;

    @InjectMocks
    PagamentoService pagamentoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSalvarPagamento_Sucesso() {

        ResponseEntity<DetalhamentoPagamentoDTO> result = pagamentoService.salvarPagamento(getPagDTOMock_Sucesso(), UriComponentsBuilder.newInstance());

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
    }

    @Test
    void testSalvarPagamento_Falha_Pix() {

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            pagamentoService.salvarPagamento(getPagDTOMock_Falha_Pix(), UriComponentsBuilder.newInstance());
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void testSalvarPagamento_Falha_Boleto() {

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            pagamentoService.salvarPagamento(getPagDTOMock_Falha_Boleto(), UriComponentsBuilder.newInstance());
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void testSalvarPagamento_Falha_Cartao_Credito() {

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            pagamentoService.salvarPagamento(getPagDTOMock_Falha_Cartao_Credito(), UriComponentsBuilder.newInstance());
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void testSalvarPagamento_Falha_Cartao_Debito() {

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            pagamentoService.salvarPagamento(getPagDTOMock_Falha_Cartao_Debito(), UriComponentsBuilder.newInstance());
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void testAtualizarStatusPagamento_Sucesso() {
        Pagamento pagamentoAnterior = new Pagamento();
        pagamentoAnterior.setCodigoPagamento(1);
        pagamentoAnterior.setDocumento(123);
        pagamentoAnterior.setValorPagamento(100.00);
        pagamentoAnterior.setTipoPagamento(TipoPagamento.BOLETO);
        pagamentoAnterior.setNumeroCartao(null);
        pagamentoAnterior.setAtivo(true);
        pagamentoAnterior.setStatusPagamento(StatusPagamento.PENDENTE_PROCESSAMENTO);

        when(pagamentoRepository.save(any(Pagamento.class))).thenReturn(pagamentoAnterior);
        when(pagamentoRepository.getReferenceById(getAtualizarPagDTOMock_Sucesso().codigoPagamento())).thenReturn(pagamentoAnterior);

        ResponseEntity<PagamentoDTO> result = pagamentoService.atualizarStatusPagamento(getAtualizarPagDTOMock_Sucesso());
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void testAtualizarStatusPagamento_Falha_caso1() {
        Pagamento pagamentoAnterior = new Pagamento();
        pagamentoAnterior.setCodigoPagamento(1);
        pagamentoAnterior.setDocumento(123);
        pagamentoAnterior.setValorPagamento(100.00);
        pagamentoAnterior.setTipoPagamento(TipoPagamento.BOLETO);
        pagamentoAnterior.setNumeroCartao(null);
        pagamentoAnterior.setAtivo(true);
        pagamentoAnterior.setStatusPagamento(StatusPagamento.PROCESSADO_SUCESSO);

        when(pagamentoRepository.save(any(Pagamento.class))).thenReturn(pagamentoAnterior);
        when(pagamentoRepository.getReferenceById(getAtualizarPagDTOMock_Falha_Caso1().codigoPagamento())).thenReturn(pagamentoAnterior);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            pagamentoService.atualizarStatusPagamento(getAtualizarPagDTOMock_Sucesso());
        });
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void testAtualizarStatusPagamento_Falha_caso2() {
        Pagamento pagamentoAnterior = new Pagamento();
        pagamentoAnterior.setCodigoPagamento(1);
        pagamentoAnterior.setDocumento(123);
        pagamentoAnterior.setValorPagamento(100.00);
        pagamentoAnterior.setTipoPagamento(TipoPagamento.BOLETO);
        pagamentoAnterior.setNumeroCartao(null);
        pagamentoAnterior.setAtivo(true);
        pagamentoAnterior.setStatusPagamento(StatusPagamento.PROCESSADO_FALHA);

        when(pagamentoRepository.save(any(Pagamento.class))).thenReturn(pagamentoAnterior);
        when(pagamentoRepository.getReferenceById(getAtualizarPagDTOMock_Sucesso().codigoPagamento())).thenReturn(pagamentoAnterior);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            pagamentoService.atualizarStatusPagamento(getAtualizarPagDTOMock_Sucesso());
        });
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }


    @Test
    void testFiltrarPagamento() {
        when(pagamentoRepository.findAll()).thenReturn(List.of(new Pagamento()));

        ResponseEntity<List<DetalhamentoPagamentoDTO>> result = pagamentoService.filtrarPagamento(null, null, null);
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void testExcluirPagamento_Sucesso() {
        Pagamento pagamentoAnterior = new Pagamento();
        pagamentoAnterior.setCodigoPagamento(1);
        pagamentoAnterior.setDocumento(123);
        pagamentoAnterior.setValorPagamento(100.00);
        pagamentoAnterior.setTipoPagamento(TipoPagamento.BOLETO);
        pagamentoAnterior.setNumeroCartao(null);
        pagamentoAnterior.setAtivo(true);
        pagamentoAnterior.setStatusPagamento(StatusPagamento.PENDENTE_PROCESSAMENTO);

        when(pagamentoRepository.save(any(Pagamento.class))).thenReturn(pagamentoAnterior);
        when(pagamentoRepository.getReferenceById(pagamentoAnterior.getCodigoPagamento())).thenReturn(pagamentoAnterior);

        ResponseEntity<DetalhamentoPagamentoDTO> result = pagamentoService.excluirPagamento(1);
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
    }

    @Test
    void testExcluirPagamento_Falha() {
        Pagamento pagamentoAnterior = new Pagamento();
        pagamentoAnterior.setCodigoPagamento(1);
        pagamentoAnterior.setDocumento(123);
        pagamentoAnterior.setValorPagamento(100.00);
        pagamentoAnterior.setTipoPagamento(TipoPagamento.BOLETO);
        pagamentoAnterior.setNumeroCartao(null);
        pagamentoAnterior.setAtivo(true);
        pagamentoAnterior.setStatusPagamento(StatusPagamento.PROCESSADO_SUCESSO);

        when(pagamentoRepository.save(any(Pagamento.class))).thenReturn(pagamentoAnterior);
        when(pagamentoRepository.getReferenceById(pagamentoAnterior.getCodigoPagamento())).thenReturn(pagamentoAnterior);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            pagamentoService.excluirPagamento(1);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void testDetalharPagamento() {
        Pagamento pagamentoAnterior = new Pagamento();
        pagamentoAnterior.setCodigoPagamento(1);
        pagamentoAnterior.setDocumento(123);
        pagamentoAnterior.setValorPagamento(100.00);
        pagamentoAnterior.setTipoPagamento(TipoPagamento.BOLETO);
        pagamentoAnterior.setNumeroCartao(null);
        pagamentoAnterior.setAtivo(true);
        pagamentoAnterior.setStatusPagamento(StatusPagamento.PROCESSADO_SUCESSO);

        when(pagamentoRepository.save(any(Pagamento.class))).thenReturn(pagamentoAnterior);
        when(pagamentoRepository.getReferenceById(pagamentoAnterior.getCodigoPagamento())).thenReturn(pagamentoAnterior);

        ResponseEntity<DetalhamentoPagamentoDTO> result = pagamentoService.detalharPagamento(1);
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }
}

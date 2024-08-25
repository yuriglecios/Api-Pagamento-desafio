package com.pagamento.api.service;

import com.pagamento.api.dto.*;
import com.pagamento.api.entity.Pagamento;
import com.pagamento.api.enums.StatusPagamento;
import com.pagamento.api.enums.TipoPagamento;
import com.pagamento.api.repository.PagamentoRepository;
import com.pagamento.api.validation.ValidaPagamento;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.stream.Stream;

@Service
public class PagamentoService {

    private final PagamentoRepository pagamentoRepository;

    public PagamentoService(PagamentoRepository pagamentoRepository) {
        this.pagamentoRepository = pagamentoRepository;
    }

    public ResponseEntity<DetalhamentoPagamentoDTO> salvarPagamento(PagamentoDTO pagamentoDTO, UriComponentsBuilder uriComponentsBuilder){
        var pagamento = new Pagamento(pagamentoDTO);
        List<String> erros = Stream.of(
                        new ValidaPagamento(pagamento.getTipoPagamento() == TipoPagamento.PIX && pagamentoDTO.numeroCartao() != null,
                                "Pagamento via PIX não necessita de número de cartão"),
                        new ValidaPagamento(pagamento.getTipoPagamento() == TipoPagamento.BOLETO && pagamentoDTO.numeroCartao() != null,
                                "Pagamento via BOLETO não necessita de número de cartão"),
                        new ValidaPagamento((pagamento.getTipoPagamento() == TipoPagamento.CARTAO_CREDITO ||
                                pagamento.getTipoPagamento() == TipoPagamento.CARTAO_DEBITO) && pagamentoDTO.numeroCartao() == null,
                                "Pagamento usando cartão necessita do número do cartão de crédito ou débito"),
                        new ValidaPagamento(pagamento.getValorPagamento() == null || pagamentoDTO.valorPagamento() <= 0,
                                "Não é possivel fazer um pagamento de R$0,00 Reais")
                )
                .filter(ValidaPagamento::isInvalida)
                .map(ValidaPagamento::getMensagemErro)
                .toList();

        if (!erros.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.join(", ", erros));
        }

        pagamentoRepository.save(pagamento);

        var uri = uriComponentsBuilder.path("/pagamento/detalharPagamento/{id}").buildAndExpand(pagamento.getCodigoPagamento()).toUri();

        return ResponseEntity.created(uri).body(new DetalhamentoPagamentoDTO(pagamento));
    }

    public ResponseEntity<PagamentoDTO>  atualizarStatusPagamento(AtualizarPagamentoDTO atualizarPagamentoDTO){

       var pagamentoAnterior = pagamentoRepository.getReferenceById(atualizarPagamentoDTO.codigoPagamento());

       List<String> erros = Stream.of(
                       new ValidaPagamento(pagamentoAnterior.getStatusPagamento().equals(StatusPagamento.PENDENTE_PROCESSAMENTO)
                               && atualizarPagamentoDTO.statusPagamento().equals(StatusPagamento.PENDENTE_PROCESSAMENTO),
                               "Não é possível atualizar o pagamento para o mesmo status"),

                       new ValidaPagamento(pagamentoAnterior.getStatusPagamento().equals(StatusPagamento.PROCESSADO_SUCESSO),
                               "Não é possível atualizar um pagamento que está processado com sucesso"),

                       new ValidaPagamento(pagamentoAnterior.getStatusPagamento().equals(StatusPagamento.PROCESSADO_FALHA)
                               && !atualizarPagamentoDTO.statusPagamento().equals(StatusPagamento.PENDENTE_PROCESSAMENTO),
                               "Só é possível atualizar um pagamento com falha para o status pendente de processamento")
               )
               .filter(ValidaPagamento::isInvalida)
               .map(ValidaPagamento::getMensagemErro)
               .toList();

       if (!erros.isEmpty()) {
           throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.join(", ", erros));
       }

       pagamentoAnterior.atualizarStatusPagamento(atualizarPagamentoDTO.statusPagamento());

       return ResponseEntity.ok(new PagamentoDTO(pagamentoAnterior));
   }

    public ResponseEntity<List<DetalhamentoPagamentoDTO>> filtrarPagamento(Integer codigoPagamento, Integer documento, StatusPagamento statusPagamento){
        var pagamentoFiltrado = pagamentoRepository.findAll().stream()
                .filter(pagamento -> codigoPagamento == null || pagamento.getCodigoPagamento().equals(codigoPagamento))
                .filter(pagamento -> documento == null || pagamento.getDocumento().equals(documento))
                .filter(pagamento -> statusPagamento == null || pagamento.getStatusPagamento().equals(statusPagamento))
                .map(DetalhamentoPagamentoDTO::new)
                .toList();
        return ResponseEntity.ok(pagamentoFiltrado);
   }

    public ResponseEntity<DetalhamentoPagamentoDTO> excluirPagamento(Integer codigoPagamento){

        var pagamento = pagamentoRepository.getReferenceById(codigoPagamento);

        var inativaPagamento = Stream.of(
                new ValidaPagamento(!pagamento.getStatusPagamento().equals(StatusPagamento.PENDENTE_PROCESSAMENTO),
                        "Não é possivel excluir um pagamento que não está pendente de processamento")
        )
        .filter(ValidaPagamento::isInvalida)
        .map(ValidaPagamento::getMensagemErro)
        .toList();

        if (!inativaPagamento.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.join(", ", inativaPagamento));
        }

        pagamento.setAtivo(false);

        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<DetalhamentoPagamentoDTO> detalharPagamento(Integer codigoPagamento){
        var pagamento = pagamentoRepository.getReferenceById(codigoPagamento);
        return ResponseEntity.ok(new DetalhamentoPagamentoDTO(pagamento));
    }
}

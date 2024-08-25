package com.pagamento.api.controller;

import com.pagamento.api.dto.AtualizarPagamentoDTO;
import com.pagamento.api.dto.DetalhamentoPagamentoDTO;
import com.pagamento.api.dto.PagamentoDTO;
import com.pagamento.api.enums.StatusPagamento;
import com.pagamento.api.service.PagamentoService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/pagamento" )
public class PagamentoController {

    private final PagamentoService pagamentoService;

    public PagamentoController(PagamentoService pagamentoService) {
        this.pagamentoService = pagamentoService;
    }

    @PostMapping("/novoPagamento")
    @Transactional
    @Operation(summary = "Recebe um novo pagamento", description = "O numero do cartão só deverá ser informado se o pagamento for pelo método de cartão de crédito ou débito")
    public ResponseEntity<DetalhamentoPagamentoDTO> novoPagamento(@RequestBody @Valid PagamentoDTO pagamentoDTO, UriComponentsBuilder uriComponentsBuilder){
        return pagamentoService.salvarPagamento(pagamentoDTO, uriComponentsBuilder);
    }

    @PutMapping("/atualizarPagamento")
    @Transactional
    @Operation(summary = "Atualiza o status de um pagamento")
    public ResponseEntity<PagamentoDTO> atualizarPagamento(@RequestBody @Valid AtualizarPagamentoDTO atualizarPagamentoDTO){
        return pagamentoService.atualizarStatusPagamento(atualizarPagamentoDTO);
    }

    @GetMapping("/filtroPagamento")
    @Operation(summary = "Filtra os pagamentos por parâmetros")
    public ResponseEntity<List<DetalhamentoPagamentoDTO>> pagamentosFiltrados(
            @RequestParam(required = false) Integer codigoPagamento,
            @RequestParam(required = false) Integer documento,
            @RequestParam(required = false) StatusPagamento statusPagamento
    ){
        return pagamentoService.filtrarPagamento(codigoPagamento, documento, statusPagamento);
    }

    @DeleteMapping("/inativarPagamento")
    @Transactional
    @Operation(summary = "Inativa um pagamento")
    public ResponseEntity<DetalhamentoPagamentoDTO> inativarPagamento(@RequestParam Integer codigoPagamento){
        return pagamentoService.excluirPagamento(codigoPagamento);
    }

    @GetMapping("/detalharPagamento")
    @Operation(summary = "Detalha um pagamento")
    public ResponseEntity<DetalhamentoPagamentoDTO> detalharPagamento(@RequestParam Integer codigoPagamento){
        return pagamentoService.detalharPagamento(codigoPagamento);
    }

}

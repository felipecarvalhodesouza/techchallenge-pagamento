package br.com.postech.pagamento.infraestrutura.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.postech.pagamento.application.usecases.PagamentoInteractor;
import br.com.postech.pagamento.domain.Pagamento;
import br.com.postech.pagamento.domain.exception.PagamentoInexistenteException;
import br.com.postech.pagamento.domain.exception.StatusPagamentoInvalidoException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/pagamentos")
@Tag(name = "API de Pagamentos", description = "API responsável pelo controle de pagamento")
public class PagamentoController {
	
	PagamentoInteractor pagamentoInteractor;
	
	@Autowired
	public PagamentoController(PagamentoInteractor pagamentoInteractor) {
		this.pagamentoInteractor = pagamentoInteractor;
	}
	
	@Operation(summary = "Consultar o status do pagamento")
	@GetMapping(path = "/{pedidoId}/statusPagamento")
	@ApiResponse(responseCode = "200")
	public String getStatusPagamentoPedido(@PathVariable String pedidoId) throws PagamentoInexistenteException {
		return pagamentoInteractor.getStatusPagamentoPedido(pedidoId);
	}
	
	@Operation(summary = "Incluir o registrro de um pagamento")
	@PostMapping
	@ApiResponse(responseCode = "201")
	public ResponseEntity<Pagamento> inserirPagamento(@RequestBody Pagamento pagamento) {
		Pagamento pagamentoInserido = pagamentoInteractor.inserirPagamento(pagamento);

		String uri = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(pagamento.getId())
				.toUriString();

		return ResponseEntity.created(URI.create(uri)).body(pagamentoInserido);
	}

	@Operation(summary = "Aprovar pagamento")
	@PutMapping(path = "/{pedidoId}/aprovar")
	@ApiResponse(responseCode = "200")
	public void aprovarPagamento(@PathVariable String pedidoId) throws StatusPagamentoInvalidoException, NumberFormatException, PagamentoInexistenteException {
		pagamentoInteractor.aprovarPagamento(pedidoId);
	}

	@Operation(summary = "Recusar pagamento")
	@PutMapping(path = "/{pedidoId}/recusar")
	@ApiResponse(responseCode = "200")
	public void recusarPagamento(@PathVariable String pedidoId) throws StatusPagamentoInvalidoException, NumberFormatException, PagamentoInexistenteException {
		pagamentoInteractor.recusarPagamento(pedidoId);
	}
}

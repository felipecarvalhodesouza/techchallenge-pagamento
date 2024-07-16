package br.com.postech.pagamento.application.usecases;


import java.io.IOException;

import br.com.postech.pagamento.application.gateway.PagamentoGateway;
import br.com.postech.pagamento.domain.Pagamento;
import br.com.postech.pagamento.domain.enumeration.StatusPagamento;
import br.com.postech.pagamento.domain.exception.PagamentoInexistenteException;
import br.com.postech.pagamento.domain.exception.StatusPagamentoInvalidoException;
import br.com.postech.pagamento.infraestrutura.helper.HttpHelper;

public class PagamentoInteractor{

	private final PagamentoGateway pagamentoGateway;
	private final HttpHelper httpHelper;
	
	public PagamentoInteractor(PagamentoGateway pagamentoGateway, HttpHelper httpHelper) {
		this.pagamentoGateway = pagamentoGateway;
		this.httpHelper = httpHelper;
	}
	
	public Pagamento getPagamentoPor(String pagamentoId) throws PagamentoInexistenteException {
		return pagamentoGateway.getPagamentoPor(pagamentoId);
	}

	public String getStatusPagamentoPedido(String pedidoId) throws PagamentoInexistenteException {
		return pagamentoGateway.getStatusPagamentoPedido(pedidoId);
	}
	
	public Pagamento inserirPagamento(Pagamento pagamento) {
		return pagamentoGateway.inserirPagamento(pagamento);
	}

	public void aprovarPagamento(String pagamentoId) throws StatusPagamentoInvalidoException, PagamentoInexistenteException, IOException {
		Pagamento pagamento = pagamentoGateway.getPagamentoPor(pagamentoId);
		
		if(pagamento == null) {
			throw new PagamentoInexistenteException();
		}
		
		validarStatusPagamento(pagamento);
		pagamentoGateway.aprovarPagamento(String.valueOf(pagamento.getId()));
		
		httpHelper.sendPostRequest(String.format("{\"id\": \"%s\"}", String.valueOf(pagamento.getId())));
	}
	
	public void recusarPagamento(String pagamentoId) throws StatusPagamentoInvalidoException, PagamentoInexistenteException {
		Pagamento pagamento = pagamentoGateway.getPagamentoPor(pagamentoId);
		
		if(pagamento == null) {
			throw new PagamentoInexistenteException();
		}

		validarStatusPagamento(pagamento);
		pagamentoGateway.recusarPagamento(String.valueOf(pagamento.getId()));
	}

	private void validarStatusPagamento(Pagamento pagamento) throws StatusPagamentoInvalidoException {
		if(pagamento.getStatusPagamento() != StatusPagamento.PENDENTE) {
			throw new StatusPagamentoInvalidoException();
		}
	}

}

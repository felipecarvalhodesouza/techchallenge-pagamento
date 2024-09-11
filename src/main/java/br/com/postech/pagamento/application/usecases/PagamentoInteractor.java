package br.com.postech.pagamento.application.usecases;


import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.springframework.transaction.annotation.Transactional;

import br.com.postech.pagamento.application.gateway.PagamentoGateway;
import br.com.postech.pagamento.domain.Pagamento;
import br.com.postech.pagamento.domain.enumeration.StatusPagamento;
import br.com.postech.pagamento.domain.exception.PagamentoInexistenteException;
import br.com.postech.pagamento.domain.exception.StatusPagamentoInvalidoException;
import br.com.postech.pagamento.domain.repository.IPagamentoQueueAdapter;

public class PagamentoInteractor{

	private final PagamentoGateway pagamentoGateway;
	private final IPagamentoQueueAdapter pagamentoQueueAdapter;
	
	public PagamentoInteractor(PagamentoGateway pagamentoGateway, IPagamentoQueueAdapter pagamentoQueueAdapter) {
		this.pagamentoGateway = pagamentoGateway;
		this.pagamentoQueueAdapter = pagamentoQueueAdapter;
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

	@Transactional
	public void aprovarPagamento(String pagamentoId) throws StatusPagamentoInvalidoException, PagamentoInexistenteException, IOException {
		Pagamento pagamento = pagamentoGateway.getPagamentoPor(pagamentoId);
		
		if(pagamento == null) {
			throw new PagamentoInexistenteException();
		}
		
		validarStatusPagamento(pagamento);
		pagamentoGateway.aprovarPagamento(String.valueOf(pagamento.getId()));
		
		try {
			pagamentoQueueAdapter.publicarAprovacaoPagamento(String.format("{\"id\": \"%s\"}", String.valueOf(pagamento.getId())));
		} catch (IOException | TimeoutException e) {
			throw new RuntimeException("Erro ao aprovar pagamento");
		}
	}
	
	@Transactional
	public void recusarPagamento(String pagamentoId) throws StatusPagamentoInvalidoException, PagamentoInexistenteException {
		Pagamento pagamento = pagamentoGateway.getPagamentoPor(pagamentoId);
		
		if(pagamento == null) {
			throw new PagamentoInexistenteException();
		}

		validarStatusPagamento(pagamento);
		pagamentoGateway.recusarPagamento(String.valueOf(pagamento.getId()));
		
		try {
			pagamentoQueueAdapter.publicarErroPagamento(String.format("{\"id\": \"%s\"}", String.valueOf(pagamento.getId())));
		} catch (IOException | TimeoutException e) {
			throw new RuntimeException("Erro ao aprovar pagamento");
		}
	}

	private void validarStatusPagamento(Pagamento pagamento) throws StatusPagamentoInvalidoException {
		if(pagamento.getStatusPagamento() != StatusPagamento.PENDENTE) {
			throw new StatusPagamentoInvalidoException();
		}
	}

}

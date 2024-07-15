package br.com.postech.pagamento.application.usecases;

import br.com.postech.pagamento.application.gateway.PagamentoGateway;
import br.com.postech.pagamento.domain.Pagamento;
import br.com.postech.pagamento.domain.enumeration.StatusPagamento;
import br.com.postech.pagamento.domain.exception.PagamentoInexistenteException;
import br.com.postech.pagamento.domain.exception.StatusPagamentoInvalidoException;
import br.com.postech.pagamento.infraestrutura.helper.SQSHelper;

public class PagamentoInteractor{
	
	private final PagamentoGateway pagamentoGateway;
	private final SQSHelper sqsHelper;
	
	public PagamentoInteractor(PagamentoGateway pagamentoGateway, SQSHelper sqsHelper) {
		this.pagamentoGateway = pagamentoGateway;
		this.sqsHelper = sqsHelper;
	}
	
	public Pagamento getPagamentoPor(String pagamentoId) throws PagamentoInexistenteException {
		return pagamentoGateway.getPagamentoPor(pagamentoId);
	}

	public String getStatusPagamentoPedido(String pedidoId) throws PagamentoInexistenteException {
		return pagamentoGateway.getStatusPagamentoPedido(pedidoId);
	}
	
	public Pagamento inserirPagamento(Pagamento pagamento) {
		Pagamento pagamentoInserido = pagamentoGateway.inserirPagamento(pagamento);
		
		//Enviar mensagem para serviço de preparo
		sqsHelper.enviarMensagem(pagamentoInserido);

		return pagamentoInserido;
	}

	public void aprovarPagamento(String pagamentoId) throws StatusPagamentoInvalidoException, PagamentoInexistenteException {
		Pagamento pagamento = pagamentoGateway.getPagamentoPor(pagamentoId);
		
		if(pagamento == null) {
			throw new PagamentoInexistenteException();
		}
		
		validarStatusPagamento(pagamento);
		pagamentoGateway.aprovarPagamento(String.valueOf(pagamento.getId()));
		
		// enviar mensagem para a fila de preparo TODO
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

package br.com.postech.pagamento.application.gateway;

import br.com.postech.pagamento.domain.Pagamento;
import br.com.postech.pagamento.domain.exception.PagamentoInexistenteException;
import br.com.postech.pagamento.domain.exception.StatusPagamentoInvalidoException;

public interface PagamentoGateway {

	Pagamento getPagamentoPor(String pagamentoId) throws PagamentoInexistenteException;

	String getStatusPagamentoPedido(String pagamentoId) throws PagamentoInexistenteException;

	Pagamento inserirPagamento(Pagamento pagamento);

	void aprovarPagamento(String pagamentoId) throws StatusPagamentoInvalidoException, PagamentoInexistenteException;

	void recusarPagamento(String pagamentoId) throws StatusPagamentoInvalidoException, PagamentoInexistenteException;

}

package br.com.postech.pagamento.domain.repository;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public interface IPagamentoQueueAdapter {

	public void publicarAprovacaoPagamento(String pagamentoJson) throws IOException, TimeoutException;
	
	public void publicarErroPagamento(String pagamentoJson) throws IOException, TimeoutException;
}

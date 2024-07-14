package br.com.postech.pagamento.domain;

import br.com.postech.pagamento.domain.enumeration.StatusPagamento;

public class Pagamento {

	private Long id;
	private double valorTotal;
	private StatusPagamento statusPagamento = StatusPagamento.PENDENTE;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public double getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(double valorTotal) {
		this.valorTotal = valorTotal;
	}

	public StatusPagamento getStatusPagamento() {
		return statusPagamento;
	}

	public void setStatusPagamento(StatusPagamento statusPagamento) {
		this.statusPagamento = statusPagamento;
	}
}

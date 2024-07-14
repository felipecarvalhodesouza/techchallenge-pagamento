package br.com.postech.pagamento.infraestrutura.persistence;

import br.com.postech.pagamento.domain.enumeration.StatusPagamento;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;

@Entity(name = "pagamento")
public class PagamentoEntity {

	@Id
	private Long id;

	@Column(name = "qt_valor_total")
	private double valorTotal;

	@Enumerated(EnumType.ORDINAL)
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

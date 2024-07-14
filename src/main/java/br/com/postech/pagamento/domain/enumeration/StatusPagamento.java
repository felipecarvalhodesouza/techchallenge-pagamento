package br.com.postech.pagamento.domain.enumeration;

public enum StatusPagamento {

	PENDENTE(0, "Pendente"), APROVADO(1, "Aprovado"), RECUSADO(2, "Recusado");

	private int codigo;
	private String descricao;

	StatusPagamento(int codigo, String descricao) {
		this.codigo = codigo;
		this.descricao = descricao;
	}

	public int getCodigo() {
		return codigo;
	}

	public String getDescricao() {
		return descricao;
	}
}

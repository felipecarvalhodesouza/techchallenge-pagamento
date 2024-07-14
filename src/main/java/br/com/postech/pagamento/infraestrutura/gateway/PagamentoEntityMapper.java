package br.com.postech.pagamento.infraestrutura.gateway;

import org.springframework.beans.BeanUtils;

import br.com.postech.pagamento.domain.Pagamento;
import br.com.postech.pagamento.infraestrutura.persistence.PagamentoEntity;

public class PagamentoEntityMapper {

	public PagamentoEntity toEntity(Pagamento pagamento) {
		PagamentoEntity entity = new PagamentoEntity();
		BeanUtils.copyProperties(pagamento, entity);
		return entity;
	}

	public Pagamento toDomainObject(PagamentoEntity entity) {
		Pagamento pagamento = new Pagamento();
		BeanUtils.copyProperties(entity, pagamento);
		return pagamento;
	}
}

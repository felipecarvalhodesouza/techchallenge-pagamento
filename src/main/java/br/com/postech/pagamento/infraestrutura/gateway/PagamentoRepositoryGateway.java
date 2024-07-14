package br.com.postech.pagamento.infraestrutura.gateway;

import org.springframework.transaction.annotation.Transactional;

import br.com.postech.pagamento.application.gateway.PagamentoGateway;
import br.com.postech.pagamento.domain.Pagamento;
import br.com.postech.pagamento.domain.enumeration.StatusPagamento;
import br.com.postech.pagamento.domain.exception.PagamentoInexistenteException;
import br.com.postech.pagamento.infraestrutura.persistence.PagamentoEntity;
import br.com.postech.pagamento.infraestrutura.persistence.PagamentoRepository;

public class PagamentoRepositoryGateway implements PagamentoGateway{

	private final PagamentoRepository pagamentoRepository;
	private final PagamentoEntityMapper mapper;

	public PagamentoRepositoryGateway(PagamentoRepository pagamentoRepository, PagamentoEntityMapper mapper) {
		this.pagamentoRepository = pagamentoRepository;
		this.mapper = mapper;
	}
	
	public Pagamento getPagamentoPor(String pagamentoId) throws PagamentoInexistenteException {
		return mapper.toDomainObject(pagamentoRepository.findById(Long.valueOf(pagamentoId)).orElseThrow(() -> new PagamentoInexistenteException()));
	}
	
	@Transactional
	public String getStatusPagamentoPedido(String pagamentoId) throws PagamentoInexistenteException {
		Pagamento pagamento = getPagamentoPor(pagamentoId);
		return pagamento.getStatusPagamento().getDescricao();
	}
	
	public Pagamento inserirPagamento(Pagamento pagamento) {
		PagamentoEntity entity = mapper.toEntity(pagamento);
		entity.setStatusPagamento(StatusPagamento.PENDENTE);
		return mapper.toDomainObject(pagamentoRepository.save(entity));
	}
	
	public void aprovarPagamento(String pagamentoId) throws PagamentoInexistenteException {
		PagamentoEntity pagamento = pagamentoRepository.findById(Long.valueOf(pagamentoId)).orElseThrow(() -> new PagamentoInexistenteException());
		pagamento.setStatusPagamento(StatusPagamento.APROVADO);
		pagamentoRepository.save(pagamento);
	}

	public void recusarPagamento(String pagamentoId) throws PagamentoInexistenteException {
		PagamentoEntity pagamento = pagamentoRepository.findById(Long.valueOf(pagamentoId)).orElseThrow(() -> new PagamentoInexistenteException());
		pagamento.setStatusPagamento(StatusPagamento.RECUSADO);
		pagamentoRepository.save(pagamento);
	}
}

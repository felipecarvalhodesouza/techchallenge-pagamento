package br.com.postech.pagamento.main.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.postech.pagamento.application.gateway.PagamentoGateway;
import br.com.postech.pagamento.application.usecases.PagamentoInteractor;
import br.com.postech.pagamento.domain.repository.IPagamentoQueueAdapter;
import br.com.postech.pagamento.infraestrutura.gateway.PagamentoEntityMapper;
import br.com.postech.pagamento.infraestrutura.gateway.PagamentoRepositoryGateway;
import br.com.postech.pagamento.infraestrutura.persistence.PagamentoRepository;
import br.com.postech.pagamento.infraestrutura.queue.PagamentoQueueAdapter;

@Configuration
public class PagamentoConfig {

	@Bean
	PagamentoInteractor createPagamentoInteractor(PagamentoGateway pagamentoGateway, IPagamentoQueueAdapter pagamentoQueueAdapter) {
		return new PagamentoInteractor(pagamentoGateway, pagamentoQueueAdapter);
	}

	@Bean
	PagamentoGateway createPagamentoGateway(PagamentoRepository pagamentoRepository, PagamentoEntityMapper mapper) {
		return new PagamentoRepositoryGateway(pagamentoRepository, mapper);
	}

	@Bean
	PagamentoEntityMapper createPagamentoEntityMapper() {
		return new PagamentoEntityMapper();
	}
	
	@Bean
	IPagamentoQueueAdapter pagamentoQueueAdapter() {
		return new PagamentoQueueAdapter();
	}
}

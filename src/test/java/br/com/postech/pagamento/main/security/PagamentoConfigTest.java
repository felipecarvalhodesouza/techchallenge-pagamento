package br.com.postech.pagamento.main.security;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;

import br.com.postech.pagamento.application.gateway.PagamentoGateway;
import br.com.postech.pagamento.application.usecases.PagamentoInteractor;
import br.com.postech.pagamento.infraestrutura.gateway.PagamentoEntityMapper;

@SpringBootTest
@ActiveProfiles("test")
class PagamentoConfigTest {

	@Autowired
	private ApplicationContext context;

	@Test
	void contextLoads() {
		assertThat(context.getBean(PagamentoInteractor.class)).isNotNull();
		assertThat(context.getBean(PagamentoGateway.class)).isNotNull();
		assertThat(context.getBean(PagamentoEntityMapper.class)).isNotNull();
	}

}

package br.com.postech.pagamento.infraestrutura.controller;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.standaloneSetup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import br.com.postech.pagamento.application.usecases.PagamentoInteractor;
import br.com.postech.pagamento.domain.Pagamento;
import br.com.postech.pagamento.domain.exception.PagamentoInexistenteException;
import br.com.postech.pagamento.domain.exception.StatusPagamentoInvalidoException;

class PagamentoControllerTest {

	@Mock
	private PagamentoInteractor pagamentoInteractor;

	@InjectMocks
	private PagamentoController pagamentoController;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		standaloneSetup(pagamentoController);
	}

	@Test
	void deveConsultarStatusPagamentoPedido() throws PagamentoInexistenteException {
		String pedidoId = "123456789";

		when(pagamentoInteractor.getStatusPagamentoPedido(pedidoId)).thenReturn("Aprovado");

		String status = 
				given()
				.when()
					.get("/pagamentos/{pedidoId}/statusPagamento", pedidoId)
				.then()
					.statusCode(HttpStatus.OK.value())
					.extract()
					.asString();

		assertThat(status).isEqualTo("Aprovado");
		verify(pagamentoInteractor).getStatusPagamentoPedido(pedidoId);
	}

	@Test
	void deveInserirPagamento() {
		Pagamento pagamento = new Pagamento();
		pagamento.setId(Long.valueOf("123"));
		when(pagamentoInteractor.inserirPagamento(any(Pagamento.class))).thenReturn(pagamento);

		Pagamento response = 
				given()
					.contentType(MediaType.APPLICATION_JSON_VALUE)
					.body(pagamento)
				.when()
					.post("/pagamentos")
				.then()
					.statusCode(HttpStatus.CREATED.value())
					.extract()
					.as(Pagamento.class);

		assertThat(response).isNotNull();
		assertThat(response.getId()).isEqualTo(pagamento.getId());
		verify(pagamentoInteractor).inserirPagamento(any(Pagamento.class));
	}

	@Test
	void deveAprovarPagamento() throws StatusPagamentoInvalidoException, PagamentoInexistenteException {
		String pedidoId = "123456789";

		given()
		.when()
			.put("/pagamentos/{pedidoId}/aprovar", pedidoId)
		.then()
			.statusCode(HttpStatus.OK.value());

		verify(pagamentoInteractor).aprovarPagamento(pedidoId);
	}

	@Test
	void deveRecusarPagamento() throws StatusPagamentoInvalidoException, PagamentoInexistenteException {
		String pedidoId = "123456789";

		given()
		.when()
			.put("/pagamentos/{pedidoId}/recusar", pedidoId)
		.then()
			.statusCode(HttpStatus.OK.value());

		verify(pagamentoInteractor).recusarPagamento(pedidoId);
	}
}

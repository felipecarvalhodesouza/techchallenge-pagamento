package br.com.postech.pagamento.infraestrutura.queue;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.postech.pagamento.application.gateway.PagamentoGateway;
import br.com.postech.pagamento.domain.Pagamento;
import br.com.postech.pagamento.main.config.RabbitMQConfig;

@Service
public class PedidoQueueAdapter {

	private final ObjectMapper objectMapper = new ObjectMapper();
	private final PagamentoGateway pagamentoGateway;
	
	public PedidoQueueAdapter(PagamentoGateway pagamentoGateway) {
		this.pagamentoGateway = pagamentoGateway;
	}

	@RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
	public void receiveMessage(String message) {
		
		try {
			System.out.println("Mensagem recebida: " + message);
			pagamentoGateway.inserirPagamento(objectMapper.readValue(message, Pagamento.class));
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
}
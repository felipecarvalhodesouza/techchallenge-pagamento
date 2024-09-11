package br.com.postech.pagamento.infraestrutura.queue;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.springframework.beans.factory.annotation.Value;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import br.com.postech.pagamento.domain.repository.IPagamentoQueueAdapter;


public class PagamentoQueueAdapter implements IPagamentoQueueAdapter {

	private final static String QUEUE_APROVADO = "mensageria_pagamento_aprovado";
	private final static String QUEUE_REPROVADO = "mensageria_erro_pagamento";
	
    @Value("${rabbitmq-host}")
    private String rabbitmqhost;
    
    @Value("${rabbitmq-port}")
    private String rabbitmqport;
	
	@Override
	public void publicarAprovacaoPagamento(String pagamentoJson) throws IOException, TimeoutException {
		publish(pagamentoJson, QUEUE_APROVADO);
    }

	@Override
	public void publicarErroPagamento(String pagamentoJson) throws IOException, TimeoutException {
		publish(pagamentoJson, QUEUE_REPROVADO);
	}
	
	private void publish(String json, String queue) throws IOException, TimeoutException {
		ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(rabbitmqhost);
        factory.setPort(Integer.valueOf(rabbitmqport));

        try (Connection connection = factory.newConnection(); 
             Channel channel = connection.createChannel()) {
             
            channel.queueDeclare(queue, false, false, false, null);

            channel.basicPublish("", queue, 
                                 MessageProperties.PERSISTENT_TEXT_PLAIN, 
                                 json.getBytes());
        }
	}

}

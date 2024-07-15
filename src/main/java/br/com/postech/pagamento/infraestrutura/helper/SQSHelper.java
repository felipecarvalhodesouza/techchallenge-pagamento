package br.com.postech.pagamento.infraestrutura.helper;

import java.util.logging.Logger;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;

import br.com.postech.pagamento.domain.Pagamento;

public class SQSHelper {
	
	private static final Logger logger = Logger.getLogger(SQSHelper.class.getName());

	public void enviarMensagem(Pagamento pagamento) {

		String json = String.format("{\"id\": \"%s\"}", String.valueOf(pagamento.getId()));
		logger.info(json);
		
        try {
			AmazonSQS sqs = AmazonSQSClientBuilder.standard()
			        .withCredentials(new DefaultAWSCredentialsProviderChain())
			        .withEndpointConfiguration(new EndpointConfiguration("https://sqs.us-east-1.amazonaws.com", "us-east-1"))
			        .build();

			String queueUrl = "https://sqs.us-east-1.amazonaws.com/615687076434/techchallenge-preparo-sqs";


			SendMessageRequest sendMsgRequest = new SendMessageRequest()
			        .withQueueUrl(queueUrl)
			        .withMessageBody(json);

			SendMessageResult sendMessage = sqs.sendMessage(sendMsgRequest);
			logger.warning(sendMessage.getMessageId());
			logger.severe(sendMessage.getMessageId());
			logger.info(sendMessage.getMessageId());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

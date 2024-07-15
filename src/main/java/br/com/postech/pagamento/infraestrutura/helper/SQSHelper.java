package br.com.postech.pagamento.infraestrutura.helper;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;

import br.com.postech.pagamento.domain.Pagamento;

public class SQSHelper {

	public void enviarMensagem(Pagamento pagamento) {
        AmazonSQS sqs = AmazonSQSClientBuilder.standard()
                .withCredentials(new DefaultAWSCredentialsProviderChain())
                .withEndpointConfiguration(new EndpointConfiguration("https://sqs.us-east-1.amazonaws.com", "us-east-1"))
                .build();

        String queueUrl = "https://sqs.us-east-1.amazonaws.com/615687076434/techchallenge-preparo-sqs";

		String json = String.format("{\"id\": \"%s\"}", String.valueOf(pagamento.getId()));

        SendMessageRequest sendMsgRequest = new SendMessageRequest()
                .withQueueUrl(queueUrl)
                .withMessageBody(json);

        sqs.sendMessage(sendMsgRequest);
	}
}

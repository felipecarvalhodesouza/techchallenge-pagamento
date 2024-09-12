# Esse projeto é um dos artefatos do último tech challenge da Pós Tech da FIAP para o curso de Software Archtecture #

Os principais desafios dessa fase foram a determinação da estratégia de orquestração do SAGA Pattern, Desenvolvimento Seguro e LGDP.

No final da 4ª fase, o desenho da solução era o seguinte:

![image](https://github.com/user-attachments/assets/fbc2b0e2-dba3-41d5-9c55-adde6b8f9668)
Na verdade, a primeira ideia foi utilizar o SQS, realizando trigger de lambdas que faziam requsições HTTP para os microserviços.
Porém, no final não foi possível configurar corretamente as credenciais da AWS e os Pods não tinham acesso ao envio de mensagens.
Dessa forma, foi entregue totalmente síncrono, com todos os microserviços se comunicando diretamente.

Para a 5ª fase, houve uma completa refatoração da estrutura de mensageria:

![microserviços](https://github.com/user-attachments/assets/5968f92f-29ec-47a2-afc4-dabe4aa0691e)
No contexto atual, foi implementado Pod do RabbitMQ para ser o Message Broker da solução, e definidas três filas de comunicação entre os serviços.
Ao incluir um pedido, é enviada mensagem para a fila, que está sendo escutada pelo microserviço de pagamentos.

Ao realizar o pagamento, é enviada mensagem para a fila de pagamentos aprovados, que está sendo escutada pelo serviço de preparo/cozinha.
No caso de erros no pagamento, ou pagamento recusado, é enviado mensagem para a fila de erros de pagamento, que é escutada pelo próprio serviço 'core', de pedidos, que notifica o cliente.

Na arquitetura da solução da AWS, temos três bancos RDS e um banco MongoDB Atlas, todos os serviços estão compartilhando um EKS, e o acesso aos serviços é feito através de load balancers.

Além disso, existe o serviço de autenticação no Cognito, que cadastra o usuário em um pool do identity provider.

Sobre a decisão de utilizar a coreografia, veio do fato da solução inicial já ter ido por esse caminho.
Alterar para orquestração envolveria alterar completamente todos os fluxos que já faziam a comunicação respectiva de suas responsabilidades.
A coreografia de SAGA em microsserviços promove maior descentralização, permitindo que cada serviço se auto-organize em resposta a eventos, o que aumenta a flexibilidade e escalabilidade. Reduz a dependência de um coordenador central, melhorando a autonomia dos serviços. Além disso, facilita a adaptação a mudanças no fluxo de trabalho distribuído.
Por fim, a coreografia evita um microserviço muito grande que, para fazer a orquestração, pode gerar um ponto único de falhas.

-------

Para execução do projeto localmente, é necessário executar os seguintes comandos [local dos arquivos foi omitido para simplificar]:

	* kubectl apply -f mysql-pod.yaml
	* kubectl apply -f mongodb-pod.yaml
	* kubectl apply -f rabbitmq-pod.yaml 
	* export DATABASE_URL=jdbc:mysql://mysql-service:3306/core
	* export DATABASE_PASSWORD=adminpassword
	* envsubst < goodburguer-core-keys.yaml | kubectl apply -f -
	* kubectl apply -f goodburguer-core-sv.yaml
	* export RABBITMQ_URL=rabbitmq-service
	* export RABBITMQ_PORT=5672
	* envsubst < goodburguer-core.yaml | kubectl apply -f -
	* export DATABASE_URL=jdbc:mysql://mysql-service:3306/pagamento
	* export DATABASE_PASSWORD=adminpassword
	* envsubst < goodburguer-pagamento-keys.yaml | kubectl apply -f -
	* kubectl apply -f goodburguer-pagamento-sv.yaml
	* kubectl apply -f service-account.yaml
	* envsubst < goodburguer-pagamento.yaml | kubectl apply -f -
	* export DATABASE_URL=jdbc:mysql://mysql-service:3306/preparo
	* envsubst < goodburguer-preparo-keys.yaml | kubectl apply -f -
	* kubectl apply -f goodburguer-preparo-sv.yaml
	* envsubst < goodburguer-preparo.yaml | kubectl apply -f -
	* export DATABASE_URL=mongodb://root:rootpassword@mongodb-service:27017/produtos?authSource=admin
	* envsubst < goodburguer-produto-keys.yaml | kubectl apply -f -
	* kubectl apply -f goodburguer-produto-sv.yaml
	* kubectl apply -f goodburguer-produto.yaml
 
* Video com a apresentação da solução:
   * https://youtu.be/hRooFquBibk
* RELATÓRIO DE IMPACTO À PROTEÇÃO DE DADOS PESSOAIS
   * https://drive.google.com/file/d/1eRxZcdEcjqMEGOGnW1UOC-xphmFuf6w6/view?usp=drive_link
* Relatório ZAP
  * https://drive.google.com/file/d/1eOvoZX5jAdNgJw8QhDAsaIUs9OxXpjVW/view?usp=drive_link

# Voting Service

## Introdução

Esse projeto é uma prova, parte de um processo seletivo.
A solicitação está descrita em: [Avaliação técnica Backend V1](https://github.com/SoftdesignBrasil/avalicao-tecnica-backend-v1/issues/1).

## Solução proposta

A solução proposta consiste numa aplicação RESTful criada com Java e SpringBoot, pronta pra deploy em produção e rodar na nuvem.
Arquitetura reativa usando Spring Webflux. 
Micro-serviços com API bem definida seguindo boas práticas, documentada e integrada com Swagger. 
Banco de dados NoSQL com MongoBD.

A escolha da linguagem Java se dá pelo fato do candidato trabalhar por praticamente por toda sua carreira. 
 
Pelo tempo destinado ao desenvolvimento do exercício e por todos os recursos plugáveis que o Spring provê, 
se optou por utilizar o SpringBoot e assim agilizar o desenvolvimento já criando a aplicação pronta para deploy.

Programação reativa é um assunto que está em alta e a opção por usar neste projeto é justamente aproveitar o paradigma desde o início do desenvolvimento, preparando a aplicação para performar e escalar utilizando melhor os recursos de hardware.  

O modelo de dados do exercício é bastante simples e ainda que tivesse alguma relação entre entidades, acredito que a opção foi utilizar banco NoSQL, por um olhar de trabalhar mais com dados consolidados e visando melhor desempenho com volumes maiores. 

### Tecnologias

* Java 8
* Spring 5
* Swagger
* MongoDB
* Junit
* AssertJ

### API

A API pode ser acessada através da URL http://localhost:8080/swagger-ui.html onde é possível também fazer simulações e testes.

### Banco de dados

Os dados são gravados no MongoDB, em suas respectivas *collections* no *database* chamado **coop**.

### Clone

Para clonar o repositório, execute:

```bash
git clone https://github.com/eurafa/voting-service.git
``` 

Lembrando que se você usa chave SSH, pode clonar executando:

```bash
git clone git@github.com:eurafa/voting-service.git
```  

### Build

Para compilar o projeto e gerar o artefato JAR, execute:

```bash
gradlew build
```  

### Testes

Os testes são executados também no build.
Para executar apenas os testes, rode o comando:

```bash
gradlew test
```  

Se quiser gerar os relatórios de cobertura do JaCoCo, execute:

```bash
gradlew test jacocoTestReport
```  

O arquivo do relatório irá aparecer no console.

### Executando a aplicação

#### Configurando o ambiente

Lembrando que é pré-requisito que se tenha instalado previamente a JDK.

O banco de dados MongoDB e o gerenciador de filas RabbitMQ, foram disponibilizado através do Docker.

Para baixar as imagens, execute:

```bash
docker pull mongo
```  

e

```bash
docker pull rabbitmq:3-management
```

No diretório do projeto, está configurado o arquivo docker-composer.yml.

Execute o comando abaixo para a montagem do ambiente:

```bash
docker-compose up
```

#### Inicializando a aplicação

Com os recursos do ambiente disponíveis, é possível subir a aplicação do backend.
Contando que você já tenha gerado o artefato JAR, basta executar agora: 

```bash
java -jar build/libs/voting-service-0.0.1-SNAPSHOT.jar
```

## Tarefas bônus

* Tarefa Bônus 1 - Integração com sistemas externos

A integração com o serviço externo foi implementado no branch feat/bonus-1.
Foi utilizado RestTemplate na ocasião.

Link p/ o PR: https://github.com/eurafa/voting-service/pull/9

* Tarefa Bônus 2 - Mensageria e filas

A integração com o serviço de mensageria foi implementado no branch feat/bonus-2.

Link p/ o PR: https://github.com/eurafa/voting-service/pull/10

* Tarefa Bônus 3 - Performance



* Tarefa Bônus 4 - Versionamento da API



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

### Build

### Testes

### Executando a aplicação

## Tarefas bônus

* Tarefa Bônus 1 - Integração com sistemas externos
* Tarefa Bônus 2 - Mensageria e filas
* Tarefa Bônus 3 - Performance
* Tarefa Bônus 4 - Versionamento da API

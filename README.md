# Spring Boot - Debezium

## Prova de conceito

Migrar dados entre databases SQL Server com CDC, utilizando Debezium.

- Java
- Spring Boot
- Debezium
- SQL Server

## Requisitos

- Java JDK 17
- Apache Maven 3.8.6
- Docker 20+
- Docker Compose 1.25

## Como executar

1. Rodar `cd docker | docker compose up -d` para subir containers Docker com os databases
2. Executar aplicação `mvn spring-boot:run`

Pronto! Pode inserir, alterar ou deletar registros na tabela OLD_DATABASE.Estudante, deve migrar para as tabelas NEW_DATABASE.usuario e NEW_DATABASE.usuario_email.

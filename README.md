![Logo Compasso](https://compasso.com.br/wp-content/uploads/2020/07/LogoCompasso-Negativo.png)

# Catálogo De Produtos

## Requisitos Projeto

Para acessar os requisitos do projeto clique em [COMPASSO-README.md](COMPASSO-README.md)

## Requisitos Para Rodar Projeto Localmente

- Java 8
- Maven

## Para Rodar Testes Automatizados

Entre na pasta do projeto e execute o comando:

```shell
mvn clean test -P test
```

## Para Rodar Projeto Na Porta 9999

### Rodando Aplicação E Banco De Dados Com Docker

Requisitos: `docker` e `docker-compose`.

Com docker rodando, execute o comando na pasta raíz do projeto:

```shell
mvn clean package -DskipTests && docker-compose up -d --build
```

### Acompanhando Logs De App Em Docker

```shell
docker-compose logs backend -f
 ```

### Rodando Aplicação Em Banco Em Memória

Entre na pasta do projeto e execute o comando:

```shell
mvn clean spring-boot:run
```

### Rodando Aplicação Com Banco Em Docker.

Inicie o banco de dados com o seguinte comando

```shell
docker-compose up -d database
```

após inicialização do banco de dados execute

```shell
 mvn clean spring-boot:run -Dspring-boot.run.profiles=mysql
 ```

ou execute o projeto em sua IDE de preferência com o perfil spring boot `mysql`

##### Desligando Aplicação

Para desligar o(s) container(s) execute o comando

```shell
docker-compose down
```

Para desligar o(s) container(s) e remover todos os dados salvos em volume/imagem/containers use:

```shell
docker-compose down -v --remove-orphans 
```

### Para Acessar Documentação Swagger

após iniciar projeto entre no link:
http://localhost:9999, após isso será direcionado para a documentação Swagger.

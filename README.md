![Logo Compasso](https://compasso.com.br/wp-content/uploads/2020/07/LogoCompasso-Negativo.png)

# Catálogo de produtos

## Requisitos Projeto

Para acessar os requisitos do projeto clique em [COMPASSO-README.md](COMPASSO-README.md)

## Requisitos para rodar projeto localmente

- Java 8
- Maven

## Comandos Maven

### Para rodar testes automatizados

Entre na pasta do projeto e execute o comando:

```shell
mvn clean test
```

### Para rodar projeto na porta 9999

#### Em Banco em memória

Entre na pasta do projeto e execute o comando:

```shell
mvn clean spring-boot:run
```

#### Em Banco mysql

##### Configurando Banco

Com docker rodando, execute o comando, para iniciar o container com banco de dados:

```shell
docker-compose -f stack.yml up -d
```

##### Iniciando aplicação.

após inicialização do banco de dados execute

```shell
 mvn clean spring-boot:run -Dspring-boot.run.profiles=mysql
 ```

ou execute o projeto em sua IDE de preferência com o perfil spring boot `mysql`

##### Desligando aplicação

Para desligar o banco execute o comando

```shell
docker-compose -f stack.yml down
```

Para desligar o banco e remover todos os dados salvos em volume use:

```shell
docker-compose -f stack.yml down -v --remove-orphans
```

### Para acessar documentação swagger

após iniciar projeto entre no link:
http://localhost:9999, após isso será direcionado para a documentação Swagger.

![Logo Compasso](http://www.compasso.com.br/wp-content/uploads/2020/02/LogoCompasso-preto.png)

# Catálogo de produtos

Sua tarefa é implementar um catálogo de produtos com Java e Spring Boot.

## product-ms

Neste microserviço deve ser possível criar, alterar, visualizar e excluir um determinado produto, além de visualizar a lista de produtos atuais disponíveis. Também deve ser possível realizar a busca de produtos filtrando por *name, description e prices*.

### Formato

O formato esperado de um produto é o seguinte:

```javascript
  {
    "id": "string",
    "name": "string",
    "description": "string",
    "prices": [
      {
        "currency": "BRL",
        "amount": 59.99
      },
      {
        "currency": "USD",
        "amount": 9.99
      }
    ]
  }
```
Durante a criação e alteração, os campos *name, description e prices* são obrigatórios. Em relação ao campo *prices* é necessário ter ao menos um preço para moeda BRL, e no máximo dois preços (BRL e USD), o campo *amount* deve ser positivo.

### Endpoints

Devem ser disponibilizados os seguintes endpoints para operação do catálogo de produtos:


| Verbo HTTP  |  Resource path    |           Descrição           |
|-------------|:-----------------:|------------------------------:|
| POST        |  /products        |   Criação de um produto       |
| PUT         |  /products/{id}   |   Atualização de um produto   |
| GET         |  /products/{id}   |   Busca de um produto por ID  |
| GET         |  /products        |   Lista de produtos           |
| GET         |  /products/search |   Lista de produtos filtrados |

#### POST /products e PUT /products/{id}

Em caso de algum erro de validação, a API deve retornar um HTTP 400 indicando uma requisição inválida. O JSON retornado nesse caso deve seguir o seguinte formato:

```javascript
  {
    "status_code": -1,
    "message": "string",
    "details": [
      {
        "item": "string",
        "description": "string"
      }
    ]
  }
```
Nos campo *message* deverá vir uma mensagem genérica indicando o erro ocorrido, no campo *details* deve vir o número de itens necessários, onde cada um representa o que deu erro (*item*) e a descrição do erro ocorrido (*description*)

#### GET /products/{id}

Em caso de não localização do produto, a API deve retornar um HTTP 404 indicando que o recurso não foi localizado. Não há necessidade de retornar um JSON (response body) nesse caso.

#### GET /products

Nesse endpoint a API deve retornar a lista atual de todos os produtos.

#### GET /products/search

Nesse endpoint a API deve retornar a lista atual de todos os produtos filtrados de acordo com query parameters passados na URL.

Os query parameters aceitos serão: q, currency, min_price e max_price.

**Importante: nenhum deles deverá ser obrigatório na requisição**

Onde:

| Query param |  Ação de filtro     
|-------------|:---------------------------------------------------------------:|
| q           |  deverá bater o valor contra os campos *name* e *description*   |
| currency    |  deverá bater o valor filtrando por *prices.currency*           |
| min_price   | deverá bater o valor ">=" contra o campo *prices.amount*        |
| max_price   | deverá bater o valor "<=" contra o campo *prices.amount*        |

**Importante: apenas os objetos *price* que baterem no(s) filtro(s) devem ser mantidos na resposta no array de prices**


## Validação

A validação dos endpoints e sua correta funcionalidade será através de script automatizado. Logo, importante que você defina a porta do serviço como sendo 9999, ficando a base url então: http://localhost:9999

Também ocorrerá avaliação técnica do código-fonte produzido.

## Entrega do código

Você é responsável por entregar o código da forma como achar mais adequado, bem como eventuais documentações necessárias para a execução do seu microserviço.

### Bom desafio \m/
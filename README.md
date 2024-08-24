# API de Pagamento - Desafio Técnico Para Desenvolvedor Java

Objetivo do teste é a criação de um a api capaz de efetuar operações CRUD (Create, Read, Update e Delete) e  possibilitar o recebimento de pagamentos de pessoas físicas e jurídicas.

## Tecnologias Usadas

Para o desenvolvimento dessa api foi utilizado as seguintes tecnologias:

- Spring Boot
- Java 17
- Spring Data
- Lombook
- H2
- Swagger

---

## Requisitos do Desafio

1. A API deve ser capaz de receber um pagamento.

   Um pagamento possui os seguintes campos:

   - Código do débito ( valor Integer )
   - CPF ou CNJP do pagador
   - Metodo de pagamento ( BOLETO, PIX, CARTAO_DEBITO, CARTAO_CREDITO )
   - Número do cartão

   > *O campo **Numero do cartão** só deve ser enviado caso o método de pagamento seja CARTAO_CREDITO ou CARTAO_DEBITO*

   - Valor do pagamento
2. A API deve ser capaz de atualizar o status de um pagamento.
   - A atualização do status de um pagamento sempre irá conter o ID do Pagamento e o

      novo status.

   - Quando o pagamento está Pendente de Processamento, ele pode ser alterado para

      Processado com Sucesso ou Processado com Falha.

   - Quando o pagamento está Processado com Sucesso, ele não pode ter seu status

      alterado.

   - Quando o pagamento está Processado com Falha, ele só pode ter seu status alterado

      para Pendente de Processamento.

3. A API deve ser capaz de listar todos os pagamentos recebidos e oferecer filtros de busca

   para o cliente.

   Os filtros de busca devem ser:

   - Por código do débito
   - Por CPF/CNPJ do pagador
   - Por status do pagamento
4. A API deve ser capaz de realizar uma exclusão logica, mantendo o pagamento, porem com

status inativo, desde que este ainda esteja com status Pendente de Processamento.


#  Growe Backend | Security-First API
> Infraestrutura robusta de autenticação e gestão de dados, construída com Spring Boot 3 e arquitetura orientada a serviços.

Este é o core da plataforma **Growe**, uma API robusta focada em segurança, utilizando o padrão **OAuth2 Resource Server** com chaves assimétricas (RSA) e persistência em PostgreSQL.

##  Stack Tecnológica
* **Java 21** (LTS)
* **Spring Boot 3.x**
* **Spring Security & OAuth2 Resource Server**
* **Nimbus JOSE + JWT** (Criptografia e assinatura de tokens)
* **PostgreSQL 16** (Base de dados)
* **Flyway** (Gestão de Migrations)
* **Docker & Docker Compose** (Contentorização)

---

##  Segurança e Chaves RSA
A API utiliza um par de chaves RSA de 2048 bits para garantir a integridade dos tokens. A chave **Privada** assina o token e a chave **Pública** valida a assinatura.

###  Gerar as chaves de Desenvolvimento
Na raiz do projeto, execute os seguintes comandos para criar a pasta de certificados e as chaves no formato **PKCS#8** (padrão exigido pelo Java):

```bash
mkdir certs

# 1. Gerar chave privada original
openssl genrsa -out certs/private.pem 2048

# 2. Converter para PKCS#8 (Obrigatório para o Spring ler o ficheiro)
openssl pkcs8 -topk8 -inform PEM -outform PEM -nocrypt -in certs/private.pem -out certs/private_pkcs8.pem

# 3. Extrair a chave pública
openssl rsa -in certs/private.pem -pubout -out certs/public.pem

```

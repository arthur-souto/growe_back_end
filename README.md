# Growe Platform

Growe is a professional platform designed to empower users with secure, efficient tools for personal and professional growth. Visit our landing page at [usegrowe.com.br](https://usegrowe.com.br) for more information.

<p align="center">
  <img src="docs/page.png" alt="Growe Landing Page" style="max-width: 100%; border-radius: 12px; box-shadow: 0 2px 8px rgba(0,0,0,0.08);" />
</p>

# Growe Backend | Security-First API
> Robust authentication and data management infrastructure, built with Spring Boot 3 and service-oriented architecture.

This is the core of the **Growe** platform, a robust API focused on security, using the **OAuth2 Resource Server** standard with asymmetric keys (RSA) and persistence in PostgreSQL.

## Technological Stack
* **Java 21** (LTS)
* **Spring Boot 3.x**
* **Spring Security & OAuth2 Resource Server**
* **Nimbus JOSE + JWT** (Cryptography and token signing)
* **PostgreSQL 16** (Database)
* **Flyway** (Migration Management)
* **Docker & Docker Compose** (Containerization)

---

## Security and RSA Keys
The API uses a pair of 2048-bit RSA keys to ensure token integrity. The **Private** key signs the token and the **Public** key validates the signature.

### Generate Development Keys
In the project root, run the following commands to create the certificates folder and the keys in **PKCS#8** format (required standard for Java):

```bash
mkdir certs

# 1. Generate original private key
openssl genrsa -out certs/private.pem 2048

# 2. Convert to PKCS#8 (Required for Spring to read the file)
openssl pkcs8 -topk8 -inform PEM -outform PEM -nocrypt -in certs/private.pem -out certs/private_pkcs8.pem

# 3. Extract the public key
openssl rsa -in certs/private.pem -pubout -out certs/public.pem

```

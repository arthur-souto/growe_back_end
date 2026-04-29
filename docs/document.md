# Growe Backend — Project Documentation

## Overview

**Growe Backend** is a RESTful API built with Spring Boot 3.5 and Java 21, serving as the backend for the Growe platform — a multi-tenant SaaS application where users can create and manage companies with role-based access control.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 3.5.13 |
| Security | Spring Security + OAuth2 Resource Server (JWT / RS256) |
| ORM | Spring Data JPA (Hibernate) |
| Database | PostgreSQL 16 |
| Migrations | Flyway |
| Validation | Jakarta Validation (Bean Validation) |
| Docs | SpringDoc OpenAPI (Swagger UI) |
| Metrics | Spring Actuator + Micrometer |
| Build | Maven (Maven Wrapper) |
| Boilerplate | Lombok |
| Containerization | Docker / Docker Compose |

---

## Project Structure

```
growe-backend/
├── certs/                          # RSA key pair for JWT signing
│   ├── private.pem
│   ├── private_pkcs8.pem
│   └── public.pem
├── docker-compose.yml              # PostgreSQL service
├── Dockerfile                      # Application image (commented out in compose)
├── pom.xml
└── src/
    └── main/
        ├── java/br/com/growe/growe_backend/
        │   ├── GroweBackendApplication.java
        │   ├── config/
        │   │   ├── JpaConfig.java              # Enables JPA auditing
        │   │   ├── SwaggerConfig.java
        │   │   └── security/
        │   │       ├── SecurityConfig.java         # Main security filter chain
        │   │       ├── JwtToPrincipalConverter.java
        │   │       ├── UserPrincipal.java          # Authenticated user record
        │   │       └── UserAuthenticationToken.java
        │   ├── controller/
        │   │   ├── AuthController.java         # POST /sign-in, POST /logout
        │   │   ├── UsersController.java        # POST /sign-up, GET /me
        │   │   └── CompanyController.java      # CRUD /companies
        │   ├── domain/
        │   │   ├── User.java
        │   │   ├── Company.java
        │   │   └── CompanyMember.java
        │   ├── dtos/
        │   │   ├── request/
        │   │   │   ├── SignInRequest.java
        │   │   │   ├── SignUpRequest.java
        │   │   │   ├── CreateCompanyRequest.java
        │   │   │   └── UpdateCompanyRequest.java
        │   │   └── response/
        │   │       ├── SignInResponse.java
        │   │       ├── SignUpResponse.java
        │   │       ├── UserDetailsResponse.java
        │   │       ├── CreateCompanyResponse.java
        │   │       ├── ResumeCompanyResponse.java
        │   │       ├── CompanyDetailsResponse.java
        │   │       ├── ResumeMemberResponse.java
        │   │       └── IdResponse.java
        │   ├── exceptions/
        │   │   ├── BusinessException.java
        │   │   ├── ConflictException.java
        │   │   ├── ResourceNotFoundException.java
        │   │   ├── ValidationException.java
        │   │   └── response/
        │   │       └── ErrorResponse.java
        │   ├── handler/
        │   │   └── GlobalExceptionHandler.java  # @ControllerAdvice
        │   ├── repository/
        │   │   ├── UserRepository.java
        │   │   ├── CompanyRepository.java
        │   │   └── CompanyMembersRepository.java
        │   ├── rules/
        │   │   ├── Role.java           # System-wide user roles
        │   │   ├── CompanyRole.java    # Per-company member roles
        │   │   ├── Plan.java           # Subscription plans
        │   │   └── SizeRange.java      # Company size buckets
        │   ├── service/
        │   │   ├── AuthService.java
        │   │   ├── UserService.java
        │   │   ├── CompanyService.java
        │   │   ├── TokenService.java       # JWT generation (RS256)
        │   │   ├── CookieService.java      # httpOnly cookie management
        │   │   ├── CustomUserDetails.java  # UserDetailsService impl
        │   │   └── PermissionsService.java
        │   └── utils/
        │       └── SizeRangeConverter.java
        └── resources/
            ├── application.yaml
            └── db/migration/
                ├── V1__initial_auth.schema.sql       # users table
                ├── V2__insert_admin_user.sql          # seed admin
                └── V3__initial_company_setup.schema.sql  # companies + company_members
```

---

## Domain Model

### User

Table: `users`

| Column | Type | Notes |
|---|---|---|
| id | UUID | PK, auto-generated |
| fullName | VARCHAR(100) | |
| email | VARCHAR(255) | UNIQUE |
| password | VARCHAR(255) | BCrypt encoded |
| role | VARCHAR(20) | Enum: ADMIN, EMPLOYEE, RH, MANAGER, GUEST |
| active | BOOLEAN | Default true |
| profileImage | VARCHAR(500) | Nullable |
| lastLoginAt | TIMESTAMPTZ | Updated on sign-in |
| createdAt | TIMESTAMPTZ | Auto (JPA Auditing) |
| updatedAt | TIMESTAMPTZ | Auto (JPA Auditing) |

### Company

Table: `companies`

| Column | Type | Notes |
|---|---|---|
| id | UUID | PK |
| name | VARCHAR(200) | |
| slug | VARCHAR(100) | UNIQUE, auto-generated from name |
| cnpj | VARCHAR(14) | UNIQUE, Brazilian company registration |
| sizeRange | VARCHAR(30) | Enum: 1-10, 11-50, 51-200, 201+ |
| plan | VARCHAR(20) | Enum: FREE, STARTER, GROWTH, ENTERPRISE |
| companyImage | VARCHAR(500) | Nullable |
| trialEndsAt | TIMESTAMPTZ | Set to +3 months on creation |
| isActive | BOOLEAN | Default true |
| createdAt / updatedAt | TIMESTAMPTZ | Auto (JPA Auditing) |

### CompanyMember

Table: `company_members`

Join table between users and companies with a per-company role.

| Column | Type | Notes |
|---|---|---|
| id | UUID | PK |
| company_id | UUID | FK → companies, CASCADE DELETE |
| user_id | UUID | FK → users, CASCADE DELETE |
| role | VARCHAR(30) | Enum: OWNER, ADMIN, MANAGER, EMPLOYEE |
| createdAt | TIMESTAMPTZ | Auto |

Constraint: `UNIQUE(company_id, user_id)` — one membership per user per company.

---

## Enums

### `Role` (system-wide, stored on `users`)
- `ADMIN` — full platform access
- `RH` — HR role
- `MANAGER`
- `EMPLOYEE`
- `GUEST`

### `CompanyRole` (per-company membership)
- `OWNER` — creator of the company, full control
- `ADMIN`
- `MANAGER`
- `EMPLOYEE`

### `Plan` (company subscription)
- `FREE`, `STARTER`, `GROWTH`, `ENTERPRISE`

### `SizeRange` (company headcount)
- `1-10`, `11-50`, `51-200`, `201+`

---

## API Endpoints

Base path: `/api/v1`

### Auth — `/auth`

| Method | Path | Auth | Description |
|---|---|---|---|
| POST | `/auth/sign-in` | Public | Authenticates user, sets httpOnly JWT cookie |
| POST | `/auth/logout` | Public | Clears the access_token cookie |

### Users — `/users`

| Method | Path | Auth | Description |
|---|---|---|---|
| POST | `/users/sign-up` | Public | Creates a new user account, sets cookie |
| GET | `/users/me` | Required | Returns authenticated user's details |

### Companies — `/companies`

| Method | Path | Auth | Description |
|---|---|---|---|
| POST | `/companies/create-company` | Required | Creates a company; creator becomes OWNER |
| GET | `/companies/my-companies` | Required | Paginated list of companies where user is OWNER |
| GET | `/companies/{slug}` | Required | Company details (member access check) |
| PUT | `/companies/{slug}` | Required | Update company (OWNER only) |
| DELETE | `/companies/{slug}` | Required | Delete company (OWNER only) |

### Role-based route prefixes

| Prefix | Allowed roles |
|---|---|
| `/api/v1/admin/**` | ADMIN |
| `/api/v1/rh/**` | ADMIN, RH |
| `/api/v1/employees/**` | ADMIN, RH, EMPLOYEE |

### Public routes (no auth)
- `POST /api/v1/auth/sign-in`
- `POST /api/v1/users/sign-up`
- `GET /swagger-ui/**`, `/v3/api-docs/**`

---

## Security

### JWT Authentication (RS256)

- Token signed with an RSA private key (`certs/private_pkcs8.pem`) and verified with the public key (`certs/public.pem`).
- Issuer claim validated: `growe-backend`.
- Expiration: **3600 seconds (1 hour)**.
- JWT claims include: `sub` (userId), `role`, `email`, `fullName`, `profileImage`, `lastLoginAt`.

### Token Transport

The JWT is stored in an **httpOnly cookie** (`access_token`) to prevent XSS access. The `SecurityConfig` custom `bearerTokenResolver` reads the token from:
1. The `access_token` cookie (primary)
2. The `Authorization: Bearer <token>` header (fallback)

### CSRF

CSRF protection is enabled via `CookieCsrfTokenRepository` (token readable by JS as `XSRF-TOKEN` cookie). Sign-in, sign-up, and logout endpoints are CSRF-exempt.

### CORS

Allowed origins in development:
- `http://localhost:3000`
- `http://localhost:5173`

Allowed methods: GET, POST, PUT, DELETE, OPTIONS. Credentials are included.

### Password Encoding

BCrypt via `BCryptPasswordEncoder`.

---

## Exception Handling

`GlobalExceptionHandler` (`@ControllerAdvice`) handles all exceptions and returns a structured `ErrorResponse`:

```json
{
  "timestamp": "...",
  "status": 400,
  "error": "VALIDATION_ERROR",
  "message": "Request validation failed",
  "path": "/api/v1/...",
  "traceId": "abc123...",
  "validationErrors": { "field": ["message"] }
}
```

Each error increments a Micrometer counter (`api_errors_total`) tagged with `error_code` and `status_code`.

| Exception | HTTP Status | Error Code |
|---|---|---|
| `BadCredentialsException` | 401 | AUTHENTICATION_FAILED |
| `BusinessException` | varies | custom |
| `ResourceNotFoundException` | 404 | RESOURCE_NOT_FOUND |
| `ValidationException` | 400 | VALIDATION_ERROR |
| `MethodArgumentNotValidException` | 400 | VALIDATION_ERROR |
| `ConstraintViolationException` | 400 | VALIDATION_ERROR |
| `HttpMessageNotReadableException` | 400 | MALFORMED_REQUEST |
| `MissingServletRequestParameterException` | 400 | MISSING_PARAMETER |
| `MethodArgumentTypeMismatchException` | 400 | TYPE_MISMATCH |
| `HttpRequestMethodNotSupportedException` | 405 | METHOD_NOT_ALLOWED |
| `HttpMediaTypeNotSupportedException` | 415 | UNSUPPORTED_MEDIA_TYPE |
| `NoHandlerFoundException` | 404 | ENDPOINT_NOT_FOUND |
| `Exception` (catch-all) | 500 | INTERNAL_ERROR |

Stack traces in error responses are controlled by `app.include-error-details` (default `false`; enable in dev/staging).

---

## Database Migrations (Flyway)

| Version | File | Description |
|---|---|---|
| V1 | `V1__initial_auth.schema.sql` | Creates `users` table with index on email |
| V2 | `V2__insert_admin_user.sql` | Seeds the default admin user |
| V3 | `V3__initial_company_setup.schema.sql` | Creates `companies` and `company_members` tables |

`ddl-auto: validate` — Hibernate validates schema against migrations, never auto-creates or alters.

---

## Configuration

### `application.yaml` key properties

| Property | Default | Description |
|---|---|---|
| `spring.datasource.url` | `jdbc:postgresql://localhost:5432/growe_db` | Override with `DB_URL` |
| `spring.datasource.username` | `growe_dev` | Override with `DB_USERNAME` |
| `spring.datasource.password` | `1234` | Override with `DB_PASSWORD` |
| `jwt.public.key` | `file:certs/public.pem` | Override with `JWT_PUBLIC_KEY` |
| `jwt.private.key` | `file:certs/private_pkcs8.pem` | Override with `JWT_PRIVATE_KEY` |
| `app.include-error-details` | `false` | Override with `INCLUDE_ERROR_DETAILS` |
| `jpa.show-sql` | `false` | Override with `JPA_SHOW_SQL` |
| `springdoc.swagger-ui.path` | `/swagger-ui.html` | Swagger UI |
| `springdoc.api-docs.path` | `/v3/api-docs` | OpenAPI spec |

---

## Local Development

### Prerequisites
- Java 21
- Maven (or use `./mvnw`)
- Docker

### Run database

```bash
docker compose up -d
```

### Run application

```bash
./mvnw spring-boot:run
```

### Access Swagger UI

```
http://localhost:8080/swagger-ui.html
```

### Environment variables (`.env`)

```env
DB_URL=jdbc:postgresql://localhost:5432/growe_db
DB_USERNAME=growe_dev
DB_PASSWORD=1234
JWT_PUBLIC_KEY=file:certs/public.pem
JWT_PRIVATE_KEY=file:certs/private_pkcs8.pem
INCLUDE_ERROR_DETAILS=true
```

---

## Business Rules

- **Company creation**: On creation, the authenticated user is automatically set as `OWNER` in `company_members`. The slug is auto-generated from the company name (normalized, lowercase, hyphenated). A 3-month trial period is applied.
- **CNPJ uniqueness**: Enforced at the service layer before persisting.
- **Company access**: All mutating operations (update, delete, get details) verify that the authenticated user is a member of the target company via `PermissionsService.checkMemberPermissionForCompany`.
- **My companies**: Lists only companies where the user holds the `OWNER` role and `isActive = true`, with pagination.
- **Slug**: Used as the public identifier for companies in URL paths.

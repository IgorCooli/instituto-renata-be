# Plano de implementação (backend)

Plano incremental para **`instituto-renata-be`** em **Java**, **Spring Boot**, **PostgreSQL** e organização em camadas alinhada a **Clean Architecture** (ver `docs/SPEC.md` §2). O **arranque Spring** (`@SpringBootApplication`) e classes `@Configuration` concentram a **composição** e a **injeção de dependências por feature** (auth, CRM, vendas, estoque, …).

## Visão das fases

```mermaid
flowchart LR
  subgraph base [Base]
    P0[Fase 0 — Docs]
    P1[Fase 1 — Spring Boot + PostgreSQL]
    P2[Fase 2 — Auth + contrato FE]
    P3[Fase 3 — Tenant e features]
  end
  subgraph mod [Domínio]
    P4[CRM]
    P5[Vendas]
    P6[Estoque]
  end
  subgraph end [Encerramento]
    P7[Qualidade e hardening]
  end
  base --> mod
  mod --> end
```

---

## Fase 0 — Documentação e convenções

**Objetivo:** alinhar repositório ao spec e às mesmas diretrizes de changelog/README do frontend.

**Passos:**

1. Manter `docs/SPEC.md` e `docs/PLAN.md` atualizados.
2. Manter **`docs/ENTITIES.md`** alinhado ao domínio: sempre que decisões de produto ou implementação **alterem entidades, relações ou diagramas**, actualizar este ficheiro **na mesma alteração** que `SPEC`/código/migrações (regra espelhada no `docs/PROMPT.md` e no `docs/SPEC.md` §3.1 e §10).
3. `README.md` na raiz: stack (Java, Spring Boot, PostgreSQL), como correr (quando existir), links para docs; secção **produção** como no `instituto-renata-fe`.
4. `CHANGELOG.md` na raiz (Keep a Changelog / semver, espírito idêntico ao frontend).

**Saída:** processo claro antes do primeiro build Gradle útil; modelo de entidades documentado e com regra de manutenção explícita.

---

## Fase 1 — Projeto Spring Boot executável + PostgreSQL

**Objetivo:** aplicação sobe, conecta ao Postgres, migrações aplicadas, health check HTTP.

**Passos:**

1. Criar projecto **Spring Boot 4.x** (via [Spring Initializr](https://start.spring.io/) ou equivalente): **Java 25**, dependências mínimas — **Spring Web**, **Spring Data JPA** (ou JDBC + escolha documentada), **PostgreSQL driver**, **Flyway** ou **Liquibase** (fixar no repo e no SPEC).
2. **Gradle** (Kotlin DSL) — `build.gradle.kts` / `settings.gradle.kts` (`com.institutorenata:instituto-renata-api`); pacote base `com.institutorenata.api`.
3. Configurar **`ENV`** e perfis Spring (`application.yml`, `application-local.yml`, …) para datasource por ambiente — ver `docs/SPEC.md` §7.2; ficheiro **`.env.example`** (sem segredos).
4. **Docker Compose** com **PostgreSQL** como forma padrão de BD em desenvolvimento local (§7.1 do SPEC).
5. Primeira **migração** (schema mínimo ou tabela de metadados).
6. Expor **`GET /api/v1/health`** (liveness; pode incluir verificação de ligação à BD conforme convenção do projecto).

**Saída:** API responde em desenvolvimento com Postgres (preferencialmente via Docker local) e configuração seleccionada por **`ENV`** / perfil.

---

## Fase 2 — Autenticação e contrato de sessão

**Objetivo:** substituir o mock de login do frontend.

**Passos:**

1. Tabelas de utilizador (email, hash de password, `role`, vínculo a tenant quando existir).
2. `POST /api/v1/auth/login`, logout conforme SPEC; payload com `email`, `role`, `enabledFeatures`.
3. **Spring Security** com JWT (ou sessão) e `GET /api/v1/me` se necessário.
4. **Composição:** beans de **auth** (serviços + controllers) registados na configuração da aplicação, por pacote **feature** `auth`.

**Saída:** frontend pode apontar o client HTTP para o backend mantendo o contrato documentado no SPEC.

---

## Fase 3 — Tenant e features no PostgreSQL

**Objetivo:** `enabledFeatures` persistido por tenant, não hardcoded.

**Passos:**

1. Modelo tenant + associação user ↔ tenant.
2. Tabela ou JSON de features habilitadas; seed de desenvolvimento com todas as features para QA.
3. Guardas nos casos de uso: verificar feature antes de aceder a CRM/vendas/estoque.

**Saída:** pacotes por cliente refletidos na BD.

---

## Fase 4 — Feature CRM (`crm`)

**Objetivo:** API alinhada ao §4.3 do spec do produto (frontend).

**Passos:** entidades e repositórios; serviços de aplicação; controllers REST; testes nos casos de uso com dependências mockadas.

**Composição:** registar beans do módulo **CRM** (persistência + web) na aplicação.

---

## Fase 5 — Feature Vendas (`vendas`)

**Objetivo:** orçamentos/oportunidades conforme §4.4 do spec do produto.

**Passos:** idem estrutura CRM; transações onde necessário (`@Transactional`).

---

## Fase 6 — Feature Estoque (`estoque`)

**Objetivo:** itens e movimentações conforme §4.5 do spec do produto.

**Passos:** consistência de stock em transação; auditoria mínima.

---

## Fase 7 — Qualidade, observabilidade e API estável

**Objetivo:** preparar integração contínua com o frontend e deploy.

**Passos:** testes de integração com Postgres de teste (Testcontainers opcional); revisão de segurança; documentação OpenAPI opcional (**springdoc-openapi** ou equivalente); logs estruturados e métricas conforme SPEC §9.

---

## Bootstrap e composição (injeção por feature)

- A classe de arranque **`@SpringBootApplication`** e pacotes **`@Configuration`** devem compor a aplicação de forma legível: abrir **DataSource** / **EntityManager** via Spring Boot, instanciar **adaptadores** dos repositórios, **serviços de aplicação** e **controllers** **por feature** (auth, CRM, vendas, estoque).
- Preferir construtores e **injeção por construtor**; evitar “service locator” global opaco.

---

## Ordem resumida

| Ordem | Foco |
|-------|------|
| 0 | Docs, README, changelog |
| 1 | Spring Boot + PostgreSQL + health |
| 2 | Auth + contrato FE |
| 3 | Tenant e features na BD |
| 4 | CRM |
| 5 | Vendas |
| 6 | Estoque |
| 7 | Hardening |

---

## Documentação de domínio (`docs/ENTITIES.md`)

- Durante **todas as fases**, quando o trabalho introduzir ou mudar **tabelas, entidades JPA, regras de negócio multi-tenant, CRM, vendas ou estoque** de forma que altere o **modelo conceptual**, actualizar **`docs/ENTITIES.md`** (e o `CHANGELOG.md` quando for notável).
- Isto aplica-se a informação vinda do utilizador, do `instituto-renata-fe/docs/SPEC.md` ou de refinamentos de API — o documento de entidades deve reflectir o estado acordado do entendimento.

---

## Histórico de revisões

| Data | Alteração |
|------|-----------|
| 2026-04-18 | Plano inicial: Go, Postgres, Clean Architecture, `cmd/`, fases alinhadas às features do FE. |
| 2026-04-18 | **Stack:** Java, Spring Boot, Fase 1 com Maven/Gradle e Flyway/Liquibase; composição Spring em vez de `cmd/`. |
| 2026-04-18 | Fase 1: build **Gradle** Kotlin DSL (substitui Maven no repositório). |
| 2026-04-18 | Fase 0 e secção **Documentação de domínio:** manutenção obrigatória de `docs/ENTITIES.md` quando o modelo mudar. |

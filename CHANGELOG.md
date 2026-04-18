# Changelog

Todas as mudanças notáveis neste projeto serão documentadas neste arquivo.

O formato segue a ideia de [Keep a Changelog](https://keepachangelog.com/pt-BR/1.0.0/), e este projeto adota [Semantic Versioning](https://semver.org/lang/pt-BR/) onde fizer sentido — **alinhado em espírito** ao repositório `instituto-renata-fe`.

## [Unreleased]

### Changed

- Estrutura de pacotes **Clean Architecture** (`entrypoint`, `domain` com `port`/`usecase`, `dataprovider`, `config`); `HealthController` em `entrypoint`.
- Postgres local: stack principal em **`../docker`** (`Projects/docker`, serviço `postgres`, `localhost:5432`, base **`postgres`**, user/password **`admin`** no `.env` de lá); defaults do perfil `local` e IDE alinhados. `docker-compose.yml` na raiz do repo = Postgres opcional em **`localhost:5433`** (mesma base/credenciais). `README`, `docs/SPEC.md` §7.1, `docs/CONTEXT.md` actualizados.
- Build: migração de **Maven** para **Gradle** Kotlin DSL (`build.gradle.kts`, `settings.gradle.kts`, Gradle Wrapper 9.4.1); removidos `pom.xml`, `mvnw` e `.mvn/`. `README`, `docs/SPEC.md`, `docs/PLAN.md`, `docs/PROMPT.md`, `docs/CONTEXT.md` e `.env.example` actualizados.
- `docs/CONTEXT.md` e `docs/PROMPT.md`: `CONTEXT` como **cérebro** para informação **não sensível** sobre como desenvolver a app (secções **Cérebro de desenvolvimento** e **Preferências do projeto**).

### Added

- `.vscode/launch.json` — depuração Java (`F5`) com perfil local e datasource para Postgres; `README` com passos para breakpoints.
- `docs/CONTEXT.md` — resumo mínimo para sessões (stack, fase, comandos); manter alinhado ao estado; referenciado no `PROMPT` e `README`.
- Projecto **Spring Boot 4.0.5** (inicialmente **Maven**, hoje **Gradle** — ver *Changed* acima), `com.institutorenata:instituto-renata-api`, Java **25**, **Spring Web MVC**, **JPA**, **PostgreSQL**, **Flyway** (`V1__baseline.sql`).
- `GET /api/v1/health` (`HealthController`), perfis `local` / `staging` / `production` + `ENV` e `SPRING_PROFILES_ACTIVE` (testes usam perfil `test` com **H2**).
- `docker-compose.yml` (Postgres 16), `.env.example`, `README.md` com comandos (`./gradlew`, Docker).

### Changed

- `docs/PROMPT.md`, `docs/SPEC.md` (§3.1, §10) e `docs/PLAN.md` (Fase 0 + secção domínio): regra de **manutenção obrigatória** de `docs/ENTITIES.md` quando informação ou implementação alterarem entidades ou relacionamentos.
- `docs/SPEC.md` e `docs/PLAN.md`: stack **Java** (JDK **25** LTS referência), **Spring Boot 4.x**, **Gradle**, **Flyway/Liquibase**; arquitectura em camadas com Spring Boot; **`ENV`** mapeado a perfis Spring e datasource; remoção da orientação **Go** / `cmd/`.
- `docs/PROMPT.md` e `README.md` alinhados à stack Java/Spring Boot.
- `docs/SPEC.md` §8: **CORS** explícito em relação ao frontend (`VITE_API_BASE_URL` no `instituto-renata-fe`) e perfis **`ENV`**.

### Added

- `docs/ENTITIES.md`: modelo de domínio proposto (tenant, utilizador, CRM, vendas, estoque) com diagramas Mermaid e decisões em aberto.
- `docs/SPEC.md`: PostgreSQL em **Docker** para desenvolvimento local (§7.1); variável de ambiente **`ENV`** para perfis que condicionam URL e credenciais do Postgres (§7.2); tabela de stack §4 actualizada.
- `docs/PLAN.md` (Fase 1), `docs/PROMPT.md` e `README.md` alinhados a Docker local e `ENV`.
- Documentação inicial: `docs/SPEC.md` (Go, **PostgreSQL**, Clean Architecture, features alinhadas ao frontend, contrato de sessão, diretrizes de changelog/README) e `docs/PLAN.md` (fases, **`cmd/`** com injeção por feature).
- `README.md` com stack, links para docs e secção **Funcionalidades em produção** (vazia até primeiro deploy relevante).
- Referência de ambiente: **Go 1.26.2** (`darwin/arm64`) na data da documentação; fixar versão mínima em `go.mod` na Fase 1.

## [0.1.0] — 2026-04-18

### Added

- Primeira versão documental do repositório backend (sem código de aplicação ainda).

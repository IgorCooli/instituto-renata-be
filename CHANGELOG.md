# Changelog

Todas as mudanças notáveis neste projeto serão documentadas neste arquivo.

O formato segue a ideia de [Keep a Changelog](https://keepachangelog.com/pt-BR/1.0.0/), e este projeto adota [Semantic Versioning](https://semver.org/lang/pt-BR/) onde fizer sentido — **alinhado em espírito** ao repositório `instituto-renata-fe`.

## [Unreleased]

### Changed

- `docs/PROMPT.md`, `docs/SPEC.md` (§3.1, §10) e `docs/PLAN.md` (Fase 0 + secção domínio): regra de **manutenção obrigatória** de `docs/ENTITIES.md` quando informação ou implementação alterarem entidades ou relacionamentos.
- `docs/SPEC.md` e `docs/PLAN.md`: stack **Java** (JDK **25** LTS referência), **Spring Boot 4.x**, **Maven ou Gradle**, **Flyway/Liquibase**; arquitectura em camadas com Spring Boot; **`ENV`** mapeado a perfis Spring e datasource; remoção da orientação **Go** / `cmd/`.
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

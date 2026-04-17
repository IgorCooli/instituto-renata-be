# Plano de implementação (backend)

Plano incremental para **`instituto-renata-be`** em **Go**, com **PostgreSQL** e **Clean Architecture** (ver `docs/SPEC.md` §2). O **`cmd/`** concentra o bootstrap e a **injeção de dependências por feature** (auth, CRM, vendas, estoque, …).

## Visão das fases

```mermaid
flowchart LR
  subgraph base [Base]
    P0[Fase 0 — Docs]
    P1[Fase 1 — Módulo Go + PostgreSQL]
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
2. `README.md` na raiz: stack (Go, PostgreSQL), como correr (quando existir), links para docs; secção **produção** como no `instituto-renata-fe`.
3. `CHANGELOG.md` na raiz (Keep a Changelog / semver, espírito idêntico ao frontend).

**Saída:** processo claro antes do primeiro `go build` útil.

---

## Fase 1 — Projeto Go executável + PostgreSQL

**Objetivo:** binário sobe, conecta ao Postgres, health check.

**Passos:**

1. `go mod init` / árvore base com **`cmd/api`** (ou nome acordado): `main` mínimo.
2. Pacotes `internal/...` conforme Clean Architecture (domínio / casos de uso / adapters).
3. Driver PostgreSQL (`database/sql` + `pgx` ou `lib/pq` — fixar no código e no SPEC).
4. Ficheiro `.env.example` com variáveis de ligação à BD (sem segredos).
5. Migrações iniciais (schema vazio ou tabela de metadados).
6. `GET /api/v1/health` (liveness).

**Saída:** API responde em desenvolvimento com BD local ou container Postgres.

---

## Fase 2 — Autenticação e contrato de sessão

**Objetivo:** substituir o mock de login do frontend.

**Passos:**

1. Tabelas de utilizador (email, hash de password, `role`, vínculo a tenant quando existir).
2. `POST /api/v1/auth/login`, logout conforme SPEC; payload com `email`, `role`, `enabledFeatures`.
3. Middleware JWT (ou sessão) e `GET /api/v1/me` se necessário.
4. No **`cmd/api`**: wiring que regista serviços de **auth** e handlers HTTP (injeção explícita por construtor).

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

**Passos:** entidades e repositórios PostgreSQL; casos de uso; handlers REST; testes nos casos de uso com ports mockados.

**Wiring:** em `cmd/api`, registar dependências do módulo **CRM** (repositório real + handlers).

---

## Fase 5 — Feature Vendas (`vendas`)

**Objetivo:** orçamentos/oportunidades conforme §4.4 do spec do produto.

**Passos:** idem estrutura CRM; transações onde necessário.

---

## Fase 6 — Feature Estoque (`estoque`)

**Objetivo:** itens e movimentações conforme §4.5 do spec do produto.

**Passos:** consistência de stock em transação; auditoria mínima.

---

## Fase 7 — Qualidade, observabilidade e API estável

**Objetivo:** preparar integração contínua com o frontend e deploy.

**Passos:** testes de integração com Postgres de teste; revisão de segurança; documentação OpenAPI opcional; logs estruturados.

---

## Bootstrap e `cmd/` (injeção por feature)

- Cada binário em **`cmd/<nome>`** deve apenas: ler config, abrir **PostgreSQL**, construir implementações concretas dos **ports** (repositórios), instanciar **casos de uso**, registar **handlers** no router **por feature** (auth, CRM, vendas, estoque).
- Evitar “service locator” global opaco; preferir funções `NewServer(deps ...)` ou composição explícita legível.

---

## Ordem resumida

| Ordem | Foco |
|-------|------|
| 0 | Docs, README, changelog |
| 1 | Go + PostgreSQL + health |
| 2 | Auth + contrato FE |
| 3 | Tenant e features na BD |
| 4 | CRM |
| 5 | Vendas |
| 6 | Estoque |
| 7 | Hardening |

---

## Histórico de revisões

| Data | Alteração |
|------|-----------|
| 2026-04-18 | Plano inicial: Go, Postgres, Clean Architecture, `cmd/`, fases alinhadas às features do FE. |

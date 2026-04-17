# Diretrizes para continuar o desenvolvimento (backend)

Este ficheiro existe para **sessões novas** (outro chat, outro dia, outro dev): lê **primeiro** isto, depois os documentos ligados abaixo.

### Manutenção obrigatória

**Toda entrega ou ajuste relevante** (nova fase, endpoint, migração, mudança de contrato com o frontend, etc.) deve **atualizar este `docs/PROMPT.md`** na mesma alteração (ou no mesmo PR), para refletir:

- **O que já foi feito** — secção *O que já foi feito (resumo)* e, se preciso, tabela ou bullets.
- **Os próximos passos** — secção *Próximo passo sugerido*, alinhada ao `docs/PLAN.md` e ao estado real do código.

O **`CHANGELOG.md`** continua a registar *o quê* mudou por versão; o **PROMPT** é o **estado atual em linguagem natural** para quem abre o projeto de novo. Se o `PLAN.md` ou o `SPEC.md` mudarem de forma material, o PROMPT deve acompanhar.

---

## O que é esta aplicação

**API (REST)** para o mesmo produto que o frontend **`instituto-renata-fe`**: gestão de consultório, **multi-tenant** por pacotes, com as mesmas **features** (`marketing`, `crm`, `vendas`, `estoque`) e papéis **`admin`** / **`common`**.

- **Stack definida:** **Go**, **PostgreSQL**, **Clean Architecture** (dependências para dentro: entidades → casos de uso → adaptadores; ver `docs/SPEC.md` §2).
- **Bootstrap:** pasta **`cmd/`** — composição do binário, **injeção de dependências por feature** (auth, CRM, vendas, estoque), sem lógica de negócio no `main`.
- O **cliente** consome JSON versionado (ex. `/api/v1`); contrato de login/sessão alinhado ao que o FE já modela em mock.

---

## Por onde começar (ordem de leitura)

1. Este guia (**`docs/PROMPT.md`**) — contexto + estado atual.
2. **`docs/SPEC.md`** — stack, arquitetura, features, PostgreSQL, segurança, contrato §6, changelog/README §11.
3. **`docs/PLAN.md`** — fases numeradas; **fonte de verdade** para implementação.
4. **`CHANGELOG.md`** (na raiz) — histórico por versão.
5. **`README.md`** — stack e regras de documentação/produção.

6. *(Opcional)* **`instituto-renata-fe/docs/SPEC.md`** — visão de produto e módulos §4–§5 para alinhar DTOs e autorização.

---

## O que já foi feito (resumo)

| Fase | Tema | Estado |
|------|------|--------|
| 0 | Documentação (`SPEC`, `PLAN`, `README`, `CHANGELOG`), diretrizes alinhadas ao FE | **Feito** (documental) |
| 1 | `go mod`, `cmd/api`, PostgreSQL, health, migrações | **Por fazer** — próximo passo |
| 2 | Auth + contrato `email` / `role` / `enabledFeatures` | Por fazer |
| 3 | Tenant e features na BD | Por fazer |
| 4–6 | APIs CRM, Vendas, Estoque | Por fazer |
| 7 | Hardening, testes, observabilidade | Por fazer |

**Código de aplicação:** ainda não há `go.mod` nem binário — a primeira entrega útil é a **Fase 1** do `docs/PLAN.md`.

**Referência de ambiente** (não versionada como contrato até existir `go.mod`): Go **1.26.2** `darwin/arm64` — mencionado no `CHANGELOG` [Unreleased].

---

## Próximo passo sugerido

1. **`go mod init`** com o path do módulo acordado (ex. `github.com/<org>/instituto-renata-be`).
2. Criar **`cmd/api/main.go`** (ou nome acordado) com servidor mínimo.
3. Ligar **PostgreSQL** (driver + pool), `.env.example`, primeira **migração**.
4. Expor **`GET /api/v1/health`** (ou path definido no SPEC).
5. Atualizar **`README.md`** com comandos reais (`go run ./cmd/api`) e **`CHANGELOG.md`**.

Depois: **Fase 2** — autenticação e mesmo contrato de sessão que o frontend espera.

---

## Como trabalhar em cada entrega

1. Ler a fase no **`docs/PLAN.md`** e requisitos no **`docs/SPEC.md`**.
2. Respeitar **Clean Architecture**: domínio sem importar `net/http` nem drivers SQL; implementações em `internal/...`.
3. Registar wiring novo no **`cmd/`** por feature (repositórios + handlers).
4. Migrações versionadas para qualquer alteração de schema.
5. Atualizar **`CHANGELOG.md`**; **`README.md` “Funcionalidades em produção”** só com o que estiver **em produção** para o cliente (igual ao frontend).

---

## Comandos locais

*(Preencher após existir `go.mod` e o primeiro binário.)*

```bash
# go run ./cmd/api
# go test ./...
```

Usar PostgreSQL local ou container; variáveis documentadas em `.env.example` (sem segredos no Git).

---

## Ligação ao frontend

- Repositório: **`instituto-renata-fe`** — hoje usa **mocks**; a integração substitui `mockLogin` e passa a usar esta API.
- Manter **nomes estáveis** de features e roles alinhados ao SPEC do FE.

---

*Última atualização deste guia: alinhada ao `docs/PLAN.md` e `CHANGELOG.md` na data em que foi editado no repositório.*

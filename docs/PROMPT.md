# Diretrizes para continuar o desenvolvimento (backend)

Este ficheiro existe para **sessões novas** (outro chat, outro dia, outro dev): lê **primeiro** isto, depois os documentos ligados abaixo.

### Manutenção obrigatória

**Toda alteração** que mude o estado do desenvolvimento (código Go, schema PostgreSQL, endpoints, contrato com o frontend, conclusão de fase, nova prioridade, etc.) deve vir acompanhada de **atualização deste `docs/PROMPT.md` na mesma alteração** (ou no mesmo PR), para refletir:

- **O que já foi feito** — secção *O que já foi feito (resumo)* (tabela ou bullets).
- **Os próximos passos** — secção *Próximo passo sugerido*, alinhada ao `docs/PLAN.md` e ao estado real do código.

O **`CHANGELOG.md`** continua a registar *o quê* mudou por versão; o **PROMPT** é o **estado atual em linguagem natural** para quem abre o projeto de novo. Se o `PLAN.md` ou o `SPEC.md` mudarem de forma material, o PROMPT deve acompanhar.

**Regra explícita para assistentes de IA:** ao concluir **qualquer tarefa** que altere o repositório de forma a mudar “o que está feito” ou “o que falta a seguir”, **não encerrar** sem atualizar as secções acima neste ficheiro. Isto inclui novos endpoints, migrações, auth, módulos de domínio e alterações de contrato com o FE. **Única exceção:** mudanças puramente cosméticas sem impacto no produto. Em caso de dúvida, **atualiza o PROMPT**.

### Para assistentes (IA) e novas sessões — checklist mínima

Antes de implementar ou alterar contratos, confirma:

1. **Leste** este `docs/PROMPT.md` e as secções **§2 (arquitetura)**, **§4–6 (auth e contrato)** do **`docs/SPEC.md`**, mais a fase correta no **`docs/PLAN.md`**.
2. O frontend **já espera** um payload de sessão compatível com (campos lógicos): `email` (string), `role` (`admin` \| `common`), `enabledFeatures` (array com subset de `marketing`, `crm`, `vendas`, `estoque`). Ver tipos em `instituto-renata-fe/src/app/auth/types.ts` e `access/types.ts` — **qualquer mudança de nomes/valores** deve ser coordenada com o FE ou versionada na API.
3. **Clean Architecture:** código de domínio **sem** importar `net/http`, drivers SQL ou frameworks no núcleo; adaptadores em `internal/...`; composição só em **`cmd/`**.
4. **PostgreSQL** é a BD oficial; em **local**, o Postgres corre em **Docker** (fluxo padrão — ver `docs/SPEC.md` §7.1). Variável **`ENV`** selecciona o perfil e **condiciona** URL/credenciais da BD (§7.2). Schema só via **migrações** versionadas (ferramenta a fixar na Fase 1).
5. Prefixo HTTP sugerido: **`/api/v1`** (ajustar no SPEC se mudares).
6. **Antes de concluir a tarefa:** atualizar **`docs/PROMPT.md`** conforme a regra de manutenção (obrigatório quando a alteração mudar estado ou próximos passos), **`CHANGELOG.md`**, e o **`README.md`** quando comandos ou stack mudarem de forma material.

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

**Endpoints a implementar (ordem lógica; detalhes no `docs/SPEC.md`):**

| Prioridade | Método | Caminho (rascunho) | Nota |
|------------|--------|---------------------|------|
| 1 | `GET` | `/api/v1/health` | Liveness, sem auth |
| 2 | `POST` | `/api/v1/auth/login` | Corpo: credenciais; resposta: dados alinhados ao `AuthSession` do FE + mecanismo de sessão/JWT definido no SPEC |
| 3 | `POST` | `/api/v1/auth/logout` | Se sessão server-side; senão documentar |
| 4 | `GET` | `/api/v1/me` | Opcional se tudo vier no login/token |

**O que o cliente FE faz hoje:** grava sessão em `sessionStorage` (`ir_auth_session`) após `mockLogin` — a integração real deve substituir isso por resposta da API mantendo o mesmo **significado** dos campos.

---

## Próximo passo sugerido

1. **`go mod init`** com o path do módulo acordado (ex. `github.com/<org>/instituto-renata-be`).
2. Criar **`cmd/api/main.go`** (ou nome acordado) com servidor mínimo.
3. Ligar **PostgreSQL** (driver + pool): **`ENV`** + parâmetros de BD por perfil em `.env.example`; Postgres local via **Docker** (Compose ou equivalente); primeira **migração**.
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

PostgreSQL em **Docker** para desenvolvimento local (padrão); definir **`ENV`** e credenciais/URL conforme `.env.example` (sem segredos no Git).

---

## Ligação ao frontend

- Repositório: **`instituto-renata-fe`** (mesmo diretório pai que este repo) — hoje usa **mocks** (`src/app/auth/mockLogin.ts`); a integração substitui por chamadas HTTP para a base definida por **`VITE_API_BASE_URL`** (`instituto-renata-fe/docs/SPEC.md` §3.1).
- **CORS** no servidor deve permitir a origem do FE em cada ambiente (ver `docs/SPEC.md` §8).
- Manter **nomes estáveis** de features (`marketing`, `crm`, `vendas`, `estoque`) e roles (`admin`, `common`) alinhados ao `instituto-renata-fe/docs/SPEC.md` §5.
- O guia de continuidade do FE está em **`instituto-renata-fe/docs/PROMPT.md`** — útil para ver o que a UI já assume.

---

*Última atualização deste guia: alinhada ao `docs/PLAN.md` e `CHANGELOG.md` na data em que foi editado no repositório.*

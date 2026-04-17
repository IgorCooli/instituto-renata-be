# instituto-renata-be

Backend (API) do sistema de gestão de consultório — repositório irmão do **`instituto-renata-fe`**.

## Stack

- **Go** (ver `go.mod` quando existir; ambiente de referência documentado em `docs/SPEC.md`)
- **PostgreSQL**
- **Clean Architecture** — ver `docs/SPEC.md` §2
- Entrada HTTP planeada sob prefixo versionado (ex.: `/api/v1`); bootstrap e composição em **`cmd/`** (injeção de dependências por feature)

## Documentação

- [Guia de continuidade](docs/PROMPT.md) — contexto, estado do projeto e próximo passo (útil em sessões novas).
- [Especificação (API, domínio, contratos)](docs/SPEC.md) — inclui features alinhadas ao frontend, PostgreSQL e arquitetura.
- [Plano de implementação](docs/PLAN.md) — fases e ordem sugerida.
- [Changelog](CHANGELOG.md) — histórico de mudanças por versão.

Gerenciador de pacotes: **`go`** (`go mod`).

**Ligação ao frontend:** o cliente em `instituto-renata-fe` consome esta API; contrato de sessão (`email`, `role`, `enabledFeatures`) descrito no `docs/SPEC.md` §6.

## Desenvolvimento

*(Comandos concretos serão preenchidos após `go mod init` e o primeiro binário em `cmd/` — ver `docs/PLAN.md` Fase 1.)*

- **PostgreSQL em Docker** é o método previsto para correr a base de dados **localmente** (detalhes no `docs/SPEC.md` §7.1 quando existir `compose` ou equivalente).
- O serviço usará a variável de ambiente **`ENV`** para escolher o perfil (local, staging, produção, …) e **condicionar** host/URL, utilizador e senha do Postgres — ver `docs/SPEC.md` §7.2 e `.env.example` (quando existir).

```bash
# exemplo futuro
# go run ./cmd/api
```

## Funcionalidades em produção (cliente)

Lista **apenas** o que já está **disponível em produção** para o cliente. Desenvolvimento em curso não entra aqui — ver [CHANGELOG.md](CHANGELOG.md) e `docs/SPEC.md` §11 (mesmas diretrizes que o frontend: `instituto-renata-fe/README.md` e `docs/SPEC.md` §7.1).

- *(nenhuma funcionalidade listada — ainda não há release em produção para o cliente)*

## Manutenção do repositório

- **`CHANGELOG.md`:** registrar mudanças notáveis por versão (inclui evolução durante o desenvolvimento).
- **Esta secção “Funcionalidades em produção”:** atualizar **somente** quando uma feature for **implantada em produção** para o cliente.

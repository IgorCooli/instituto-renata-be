# instituto-renata-be

Backend (API) do sistema de gestão de consultório — repositório irmão do **`instituto-renata-fe`**.

## Stack

- **Java** (JDK **25** LTS referência — ver `docs/SPEC.md` §4)
- **Spring Boot 4.x**
- **PostgreSQL**
- **Maven** ou **Gradle** (fixar na Fase 1)
- **Clean Architecture** (mapeada a camadas Spring) — ver `docs/SPEC.md` §2
- API REST sob prefixo versionado (ex.: `/api/v1`); composição com **Spring Boot** (injeção por feature: auth, CRM, vendas, estoque, …)

## Documentação

- [Guia de continuidade](docs/PROMPT.md) — contexto, estado do projeto e próximo passo (útil em sessões novas).
- [Especificação (API, domínio, contratos)](docs/SPEC.md) — inclui features alinhadas ao frontend, PostgreSQL e arquitetura.
- [Plano de implementação](docs/PLAN.md) — fases e ordem sugerida.
- [Changelog](CHANGELOG.md) — histórico de mudanças por versão.

Build: **Maven** (`pom.xml`) ou **Gradle** (`build.gradle.kts`) — conforme o projecto na Fase 1.

**Ligação ao frontend:** o cliente em `instituto-renata-fe` consome esta API; contrato de sessão (`email`, `role`, `enabledFeatures`) descrito no `docs/SPEC.md` §6.

## Desenvolvimento

*(Comandos concretos serão preenchidos após gerar o projecto Spring Boot — ver `docs/PLAN.md` Fase 1.)*

- **PostgreSQL em Docker** é o método previsto para correr a base de dados **localmente** (ver `docs/SPEC.md` §7.1; `docker compose` quando existir no repositório).
- **`ENV`** e perfis Spring condicionam host/URL, utilizador e senha do Postgres — ver `docs/SPEC.md` §7.2 e `.env.example` (quando existir).

```bash
# exemplo futuro (Maven)
# ./mvnw spring-boot:run
```

## Funcionalidades em produção (cliente)

Lista **apenas** o que já está **disponível em produção** para o cliente. Desenvolvimento em curso não entra aqui — ver [CHANGELOG.md](CHANGELOG.md) e `docs/SPEC.md` §11 (mesmas diretrizes que o frontend: `instituto-renata-fe/README.md` e `docs/SPEC.md` §7.1).

- *(nenhuma funcionalidade listada — ainda não há release em produção para o cliente)*

## Manutenção do repositório

- **`CHANGELOG.md`:** registrar mudanças notáveis por versão (inclui evolução durante o desenvolvimento).
- **Esta secção “Funcionalidades em produção”:** atualizar **somente** quando uma feature for **implantada em produção** para o cliente.

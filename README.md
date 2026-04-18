# instituto-renata-be

Backend (API) do sistema de gestão de consultório — repositório irmão do **`instituto-renata-fe`**.

## Stack

- **Java** **25** (Temurin LTS — ver `docs/SPEC.md` §4)
- **Spring Boot 4.0.x** (**Gradle** Kotlin DSL — `build.gradle.kts`, projecto `instituto-renata-api`)
- **PostgreSQL** (runtime)
- **Flyway** (migrações em `src/main/resources/db/migration`)
- **Clean Architecture** (camadas sob `com.institutorenata.api`: `domain`, `application.port` / `usecase`, `adapter.in.web`, `adapter.out.persistence`, `config`) — ver `docs/SPEC.md` §2–3
- API REST sob prefixo versionado (`/api/v1/...`)

## Documentação

- [Contexto resumido](docs/CONTEXT.md) — “cérebro” curto para sessões e IA (stack, fase, comandos); manter actualizado quando a fase ou a stack mudarem.
- [Guia de continuidade](docs/PROMPT.md) — contexto, estado do projeto e próximo passo (útil em sessões novas).
- [Especificação (API, domínio, contratos)](docs/SPEC.md) — inclui features alinhadas ao frontend, PostgreSQL e arquitetura.
- [Plano de implementação](docs/PLAN.md) — fases e ordem sugerida.
- [Entidades e relacionamentos](docs/ENTITIES.md) — modelo de domínio (rascunho; diagramas Mermaid).
- [Changelog](CHANGELOG.md) — histórico de mudanças por versão.

**Ligação ao frontend:** o cliente em `instituto-renata-fe` consome esta API; contrato de sessão (`email`, `role`, `enabledFeatures`) descrito no `docs/SPEC.md` §6.

## Desenvolvimento

### Pré-requisitos

- **JDK 25** (ex.: Temurin via SDKMAN).
- **Docker** (para PostgreSQL local via `docker compose`).

### Variáveis de ambiente

- Copiar [`.env.example`](.env.example) para `.env` e ajustar (não commitar segredos).
- **`ENV`:** `local` \| `staging` \| `production` — selecciona o perfil Spring quando `SPRING_PROFILES_ACTIVE` não está definido.
- **`SPRING_DATASOURCE_*`:** URL JDBC e credenciais (ver `.env.example`).
- **`SPRING_PROFILES_ACTIVE`:** em testes Gradle a tarefa `test` define `test` (H2 em memória); para desenvolvimento normal use `local` via `ENV` ou omita (predefinido `local`).

### Base de dados local (Docker)

O Postgres de desenvolvimento está no compose da pasta **`../docker`** (mesmo nível que `instituto-renata-be` em `Projects/`). Credenciais em `../docker/.env` (`POSTGRES_USER` / `POSTGRES_PASSWORD`, tipicamente **`admin`** / **`admin`**); a base por defeito do serviço é **`postgres`** (ficheiro `docker-compose.yml` dentro dessa pasta, fora deste repositório).

```bash
cd ../docker && docker compose up -d postgres
```

A API assume por defeito **`jdbc:postgresql://localhost:5432/postgres`** com utilizador **`admin`** (ver [`.env.example`](.env.example)).

**Alternativa** (sem o stack de `../docker`): na raiz deste repo existe [`docker-compose.yml`](docker-compose.yml) com Postgres só em **`localhost:5433`** (mesmo user/password e base `postgres`). Nesse caso define `SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5433/postgres` no `.env` ou no IDE.

### Correr a API

```bash
./gradlew bootRun
```

**Breakpoints (Cursor / VS Code):** instala **Extension Pack for Java** e **Gradle for Java** (ou confirma que o Gradle está a ser importado — vê a barra de estado “Importing…”). Abre a pasta **raiz** do repo (onde está `build.gradle.kts`). **Run and Debug** (`⇧⌘D`) → **InstitutoRenataApi (local + Postgres)** → **F5**. O [`.vscode/launch.json`](.vscode/launch.json) compila antes (`compileJava`) e define `ENV` + datasource; [`.vscode/settings.json`](.vscode/settings.json) força import Gradle automático. Se falhar: **Command Palette** → “Java: Clean Java Language Server Workspace” → reload; depois `./gradlew compileJava` no terminal. Postgres: a partir de `../docker` (ver secção acima) ou `docker compose up -d postgres` na raiz deste repo se usares o compose opcional.

Health check: [http://localhost:8080/api/v1/health](http://localhost:8080/api/v1/health) (com Postgres a correr e variáveis apontando para o container).

### Testes

```bash
./gradlew test
```

### Empacotar

```bash
./gradlew build -x test
java -jar build/libs/instituto-renata-api-0.0.1-SNAPSHOT.jar
```

## Funcionalidades em produção (cliente)

Lista **apenas** o que já está **disponível em produção** para o cliente. Desenvolvimento em curso não entra aqui — ver [CHANGELOG.md](CHANGELOG.md) e `docs/SPEC.md` §11 (mesmas diretrizes que o frontend: `instituto-renata-fe/README.md` e `docs/SPEC.md` §7.1).

- *(nenhuma funcionalidade listada — ainda não há release em produção para o cliente)*

## Manutenção do repositório

- **`CHANGELOG.md`:** registrar mudanças notáveis por versão (inclui evolução durante o desenvolvimento).
- **Esta secção “Funcionalidades em produção”:** atualizar **somente** quando uma feature for **implantada em produção** para o cliente.

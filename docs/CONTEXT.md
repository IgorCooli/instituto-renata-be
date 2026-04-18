# CONTEXT — resumo para sessões curtas (IA e humanos)

Ficheiro **curto** para não repetir contexto inteiro em cada chat. **Não substitui** `PROMPT.md`, `SPEC.md`, `PLAN.md` nem `ENTITIES.md` — apenas **ancora** decisões e estado. Quando algo abaixo mudar materialmente, **actualiza este ficheiro no mesmo PR** (e o `PROMPT.md` conforme regra habitual).

### Cérebro de desenvolvimento (informação **não sensível**)

Este ficheiro é o lugar onde se **consolidam** instruções de **como** desenvolver a app quando o utilizador (ou o time) as comunica no chat e **não** são segredos:

- **Incluir aqui:** convenções de código, preferências de ferramentas, ordem de prioridades, estilo de PR, naming, decisões de workflow (“sempre testar X antes de Y”), lembretes de integração com o FE, preferência Gradle vs Maven quando fixada, etc.
- **Nunca incluir aqui:** palavras-passe, chaves API, tokens, credenciais de produção, dados pessoais reais, ou qualquer segredo — esses ficam fora do Git (`.env`, gestores de segredos).

**Regra para assistentes de IA:** quando o utilizador der **nova** informação não sensível sobre o desenvolvimento desta app, **incorporar** um resumo estável neste `CONTEXT.md` (secção adequada ou bullets em **Preferências do projeto** abaixo) **na mesma alteração** em que se aplica ao código ou docs, para as próximas sessões herdarem o “cérebro” sem re-explicar tudo no chat.

---

## Preferências do projeto (preencher ao longo do tempo)

- **Build:** **Gradle** (Kotlin DSL), não Maven.


---

## Produto (1 frase)

API REST **Java / Spring Boot** para **gestão de consultório**, multi-tenant por **pacotes de features**; cliente web: repositório irmão **`instituto-renata-fe`**.

---

## Stack (verdade no repo)

| Item | Valor |
|------|--------|
| Linguagem | Java **25** (Temurin LTS referência) |
| Framework | **Spring Boot 4.0.5** |
| Build | **Gradle** Kotlin DSL (`build.gradle.kts`, `settings.gradle.kts`, projecto `instituto-renata-api`) |
| Pacote base | `com.institutorenata.api` |
| BD | **PostgreSQL** (runtime); migrações **Flyway** (`src/main/resources/db/migration`) |
| Testes unitários / contexto | Perfil `test`, **H2** em memória (tarefa Gradle `test` define `SPRING_PROFILES_ACTIVE=test`) |
| API | Prefixo **`/api/v1`** |
| Local DB | **Docker Compose** em **`../docker`** (pasta irmã em `Projects/`: `POSTGRES_USER` / `POSTGRES_PASSWORD` = `admin` no `.env` de lá; base por defeito **`postgres`**; host **`localhost:5432`**) — opcionalmente o `docker-compose.yml` na raiz deste repo (Postgres só, **`localhost:5433`**) |

---

## Configuração de ambiente

- **`ENV`:** `local` \| `staging` \| `production` — alinha perfil Spring se **`SPRING_PROFILES_ACTIVE`** não estiver definido (`application.yml`: `${SPRING_PROFILES_ACTIVE:${ENV:local}}`).
- **Datasource:** `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD` (ver `.env.example`). Spring **não** lê `.env` sozinho — exportar no shell ou IDE.
- **Frontend → API:** no FE, **`VITE_API_BASE_URL`** (ver `instituto-renata-fe/docs/SPEC.md` §3.1). CORS no backend conforme `docs/SPEC.md` §8.

---

## Domínio e contratos (memória útil)

- **Features (chaves estáveis):** `marketing`, `crm`, `vendas`, `estoque`.
- **Papéis:** `admin`, `common` (no API/JSON alinhar a `docs/SPEC.md` §6 e ao FE).
- **Sessão / login (alvo):** resposta com `email`, `role`, `enabledFeatures` — substituir mock `mockLogin` / `sessionStorage` no FE.
- **Modelo ER e entidades (rascunho):** só em **`docs/ENTITIES.md`**; qualquer mudança de modelo → actualizar **ENTITIES** + código/migrações.

---

## Estado do plano (alto nível)

| Fase | Tema | Estado (última actualização deste CONTEXT) |
|------|------|---------------------------------------------|
| 0 | Docs, convenções | Feito |
| 1 | Spring Boot + Postgres + health + Flyway baseline | Feito (Gradle) |
| 2 | Auth + contrato FE | **Próximo** |
| 3+ | Tenant/features, CRM, Vendas, Estoque, hardening | Por fazer |

**Último endpoint útil:** `GET /api/v1/health` (`HealthController`).

---

## Mapa de documentos (o que ler quando)

| Ficheiro | Uso |
|----------|-----|
| **`docs/CONTEXT.md`** (este) | Atolho rápido + **cérebro** de desenvolvimento não sensível; actualizar quando stack/fase/decisão grossa mudar **ou** quando o utilizador definir novas convenções de desenvolvimento. |
| `docs/PROMPT.md` | Regras de manutenção, checklist, estado detalhado, comandos. |
| `docs/SPEC.md` | Stack, arquitectura, PostgreSQL, `ENV`, contrato sessão, CORS. |
| `docs/PLAN.md` | Fases de implementação. |
| `docs/ENTITIES.md` | Entidades e diagramas Mermaid. |
| `CHANGELOG.md` | Histórico por versão. |
| `instituto-renata-fe/docs/SPEC.md` | Produto e módulos UI. |

---

## Comandos que mais vão repetir

```bash
docker compose up -d postgres
./gradlew bootRun
./gradlew test
```

(Com JDK 25 no `PATH`; ex.: SDKMAN.)

---

## Notas para IA

1. Tratar este **`CONTEXT.md`** como **cérebro persistente** para toda a informação **não sensível** sobre **como** desenvolver a app; **actualizar** quando o utilizador der novas directivas desse tipo (e reflectir em código/docs quando aplicável).
2. Não inventar endpoints ou nomes de feature fora da tabela de features / SPEC §5–6.
3. Alterações de schema → Flyway + **`docs/ENTITIES.md`** + `CHANGELOG`.
4. Alterações de fase ou stack → **`docs/PROMPT.md`** + este **`CONTEXT.md`**.
5. Detalhes longos vivem em **SPEC/PLAN/ENTITIES**; aqui ficam **factos estáveis**, **estado actual** e **preferências de desenvolvimento** acordadas.

---

*Última revisão sugerida: alinhar a coluna “Stack / Fase” sempre que fechar uma fase ou mudar build tool.*

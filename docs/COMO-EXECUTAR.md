# Como Executar — Finance Team

Guia para rodar o projeto no **NetBeans**.

---

## Pré-requisitos

- **JDK 17 ou superior** (validado com OpenJDK 25)
- **NetBeans IDE** com suporte a projetos Java Ant
- Projeto **Supabase** ativo com as tabelas criadas (ver `src/view/src/model/db.sql`)

---

## Passo 1 — Configurar as credenciais (.env)

As credenciais do Supabase são lidas automaticamente do arquivo `.env` (via biblioteca dotenv-java).

O arquivo já existe em `src/view/.env`. Confirme que está preenchido:

```
SUPABASE_URL=https://SEU-PROJETO.supabase.co/rest/v1
SUPABASE_API_KEY=SUA_CHAVE_ANON_PUBLIC
```

> O `.env` está no `.gitignore` e não vai para o GitHub — nunca commite credenciais.

---

## Passo 2 — Abrir o projeto no NetBeans

1. **File > Open Project**
2. Navegue até `src/view` (é a pasta que contém `nbproject/`, `build.xml` e `manifest.mf`)
3. O projeto aparece como **View** na aba Projects com o ícone de xícara de café

As bibliotecas **Gson** e **dotenv-java** já estão em `src/view/libs/` e configuradas no classpath — não é preciso adicionar JARs manualmente.

---

## Passo 3 — Executar

Pressione **F6** (ou clique no botão ▶ verde).

A classe principal é `app.Main`, que monta toda a aplicação e abre a tela de login.

---

## Fluxo da aplicação

```
app.Main
   └── AppContext (instancia DAOs + Services)
        └── LoginView + LoginController
             ├── ADMIN    → AdminUserView + AdminUserController
             ├── MANAGER  → ManagerMainView + ManagerController
             │                 ├── Cadastrar e Otimizar  (InvestorController)
             │                 └── Histórico de Investidores
             └── INVESTOR → InvestorReadOnlyDashboardView (somente leitura)
```

---

## Estrutura de pacotes

| Pacote | Camada | Responsabilidade |
|--------|--------|------------------|
| `app` | Bootstrap | `Main` (entry point) e `AppContext` (injeção de dependências) |
| `model` | Model | Entidades de domínio (POJOs) |
| `dao` | DAO | Acesso ao Supabase via REST (HttpClient + Gson) |
| `service` | Service | Regras de negócio (auth, otimização, logging) |
| `controller` | Controller | Coordenação entre View e Service |
| `view` | View | Telas Swing |
| `util` | Util | ThemeManager, MessageUtil, BaseFrame |

---

## Solução de problemas

| Erro | Causa | Solução |
|------|-------|---------|
| `Variável obrigatória ausente: SUPABASE_URL` | Arquivo `.env` não encontrado | Confirme que `src/view/.env` existe e está preenchido |
| `ConnectException` / `UnresolvedAddressException` | URL do Supabase incorreta | Verifique o valor de `SUPABASE_URL` no `.env` |
| Erro de compilação por falta de Gson/dotenv | JARs não reconhecidos | Confirme que os JARs estão em `src/view/libs/` e que o projeto foi aberto a partir de `src/view` |
| Tela abre mas tabelas aparecem vazias | Banco sem dados | Insira dados no Supabase ou confira se as tabelas foram criadas com o `db.sql` |

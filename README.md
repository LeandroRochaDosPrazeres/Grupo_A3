# Finance Team — Sistema de Otimização de Portfólios

> **Projeto acadêmico A3** — Universidade São Judas Tadeu (USJT)
> Disciplina: Programação de Soluções Computacionais
> Professora: Cristiane



## Descrição

Sistema desktop em **Java Swing** para gestão e otimização de carteiras de investimentos. O sistema é totalmente integrado a um banco de dados em nuvem, permitindo o controle de acessos por níveis de permissão (Admin, Gerente e Investidor), cadastro completo de clientes e ativos, além de contar com um motor de otimização funcional que sugere a alocação percentual ideal com base no perfil de risco do investidor.

---

## Arquitetura

O projeto foi desenvolvido seguindo o padrão arquitetural **MVC + Service + DAO**, garantindo a separação de responsabilidades e facilidade de manutenção:

```
┌─────────┐     ┌────────────┐     ┌─────────┐     ┌─────┐     ┌──────────┐
│  View   │ ──▶ │ Controller │ ──▶ │ Service │ ──▶ │ DAO │ ──▶ │ Supabase │
│ (Swing) │     │            │     │ (regras)│     │(HTTP)│     │(Postgres)│
└─────────┘     └────────────┘     └─────────┘     └─────┘     └──────────┘
                                        ▲
                                        │
                                   ┌─────────┐
                                   │  Model  │
                                   │ (POJOs) │
                                   └─────────┘

```

| Camada | Responsabilidade |
| --- | --- |
| **View** | Telas Swing (JFrame/JPanel) — interação com o usuário, coleta de dados e exibição de resultados. |
| **Controller** | Intermediário direto; coordena o fluxo de dados entre a View e a camada de Serviço. |
| **Service** | Centraliza as regras de negócio, validações de segurança, motor de otimização e geração de logs. |
| **DAO** | Gerencia a persistência de dados consumindo a API REST do Supabase via HTTPClient e transformando JSONs com Gson. |
| **Model** | Entidades de domínio (POJOs) puras que espelham a estrutura das tabelas do banco de dados. |

---

## Estrutura de Pastas

```
src/
├── model/                          # Camada Model (entidades de domínio)
│   ├── Asset.java                  # Ativo financeiro (ticker, nome, categoria, risco)
│   ├── LogEntry.java               # Registro de log de auditoria
│   ├── Optimization.java           # Resultado de uma execução do motor de otimização
│   ├── Portfolio.java              # Carteira de investimentos
│   ├── PortfolioItem.java          # Item da carteira (ativo + quantidade + preço)
│   ├── PortfolioPrice.java         # Preço histórico por ticker/data
│   ├── RiskProfile.java            # Enum: CONSERVATIVE, MODERATE, AGGRESSIVE
│   ├── User.java                   # Usuário do sistema (Credenciais e Perfil)
│   └── UserRole.java               # Enum de papéis (ADMIN, MANAGER, INVESTOR)
│
└── View/                           # Projeto NetBeans (Camadas View, Controller, Service e DAO)
    └── src/
        ├── controller/
        │   ├── LoginController.java
        │   ├── ManagerController.java
        │   ├── InvestorController.java
        │   └── AdminController.java
        ├── service/                # Regras de negócio e motor de otimização
        │   ├── AuthService.java
        │   ├── InvestorService.java
        │   └── PortfolioOptimizerService.java
        ├── dao/                    # Integração HTTP com o Supabase
        │   ├── SupabaseClient.java
        │   ├── UserDAO.java
        │   ├── InvestorDAO.java
        │   └── PortfolioDAO.java
        ├── view/
        │   ├── LoginView.java
        │   ├── ManagerMainView.java
        │   ├── InvestorRegistrationOptimizationView.java
        │   ├── InvestorDashboardView.java
        │   ├── ManagerInvestorHistoryView.java
        │   ├── AdminUserView.java
        │   ├── UserFormDialog.java
        │   ├── InvestorReadOnlyDashboardView.java
        │   └── resources/
        │       ├── logo_sem_background_darkmode.png
        │       └── logo_sem_background_lightmode.png
        └── util/
            └── ThemeManager.java   # Gerenciador de temas (dark/light mode)

```

---

## Status de Implementação

O projeto foi **100% concluído**, com todas as camadas integradas e funcionais, abandonando o uso de dados mockados em prol do banco de dados em produção.

### ✅ Camadas de Core & Dados (Model, DAO e Service)

| Componente | Status | Descrição |
| --- | --- | --- |
| **Model** | ✅ Concluído | Todas as entidades mapeadas corretamente (incluindo correções de `User` e `UserRole`). |
| **SupabaseClient** | ✅ Concluído | Centraliza a comunicação HTTP, tratamento de headers de autenticação e requisições REST. |
| **DAOs Específicos** | ✅ Concluído | `UserDAO`, `InvestorDAO`, `PortfolioDAO`, etc., realizando operações completas no banco. |
| **AuthService** | ✅ Concluído | Regras de autenticação, validação de hash de senha e controle de sessão ativa. |
| **PortfolioOptimizerService** | ✅ Concluído | Motor de otimização baseado no perfil de risco (`RiskProfile`), calculando as alocações ideais de forma automatizada. |

### ✅ Camada de Interface (View & Controller)

| Tela / Fluxo | Status | Descrição |
| --- | --- | --- |
| `LoginView` & Controller | ✅ Concluído | Autenticação real contra o Supabase, carregamento dinâmico de permissões e tema visual adaptativo. |
| **Jornada do Gerente** | ✅ Concluído | Cadastro de investidores, seleção dinâmica de ativos ativos no banco, execução do motor de otimização e histórico de carteiras. |
| **Jornada do Administrador** | ✅ Concluído | Gestão completa (CRUD) de usuários do sistema através da `AdminUserView` e `UserFormDialog`. |
| **Jornada do Investidor** | ✅ Concluído | Acesso exclusivo via `InvestorReadOnlyDashboardView` para visualização em tempo real de gráficos e cards de rentabilidade/risco de seu portfólio. |
| `ThemeManager` | ✅ Concluído | Alternância fluida entre Dark e Light mode preservando a identidade visual azul institucional. |

---

## Funcionalidades por Perfil

```
┌─────────────────────────────────────────────────────────────────────────┐
│                                 PERFIS                                  │
├───────────────────┬──────────────────────────────┬──────────────────────┤
│       ADMIN       │           MANAGER            │       INVESTOR       │
├───────────────────┼──────────────────────────────┼──────────────────────┤
│ • Gestão Total de │ • Cadastrar Investidores     │ • Visualizar Painel  │
│   Usuários        │ • Selecionar Ativos          │   de Investimentos   │
│ • Auditoria de    │ • Rodar Motor Otimização     │ • Acesso em Modo     │
│   Logs do Sistema │ • Histórico de Carteiras     │   Somente Leitura    │
└───────────────────┴──────────────────────────────┴──────────────────────┘

```

---

## Tecnologias Utilizadas

* **Java 17+**: Linguagem base do ecossistema do projeto.
* **Java Swing**: Construção de interfaces gráficas desktop robustas.
* **NetBeans IDE**: IDE utilizada para o design visual das telas e organização modular.
* **Supabase (PostgreSQL)**: Banco de dados relacional hospedado na nuvem, fornecendo persistência segura.
* **HttpClient (Java Native)**: Consumo da API RESTful do Supabase sem dependências pesadas de terceiros.
* **Gson (Google)**: Manipulação, serialização e desserialização de objetos Java para formato JSON.

---

## Como Executar

1. Certifique-se de ter o **Java 17 (ou superior)** instalado em sua máquina.
2. Certifique-se de que as credenciais e a URL de conexão do Supabase estejam configuradas corretamente no arquivo de propriedades ou na classe `SupabaseClient`.
3. Abra o projeto contido na pasta `src/View` utilizando o **NetBeans IDE**.
4. Limpe e construa o projeto (`Clean and Build`).
5. Execute a classe `view.LoginView` (que contém o método `main`).
6. Insira suas credenciais de acesso para iniciar a navegação de acordo com o nível do seu perfil.

---

## Banco de Dados

O banco de dados PostgreSQL é gerenciado remotamente pelo Supabase. O script DDL com a estrutura das tabelas, chaves estrangeiras e triggers de logs encontra-se mapeado em `src/model/db.sql`, englobando as estruturas de:

* `users` (Credenciais, tokens e papéis do sistema)
* `investors` (Dados cadastrais dos clientes finais)
* `assets` (Listagem global de ativos do mercado)
* `portfolios` & `portfolio_items` (Estrutura e amarração de carteiras de investimentos)
* `optimizations` (Histórico de execuções de cálculos do motor)
* `logs` (Trilha de auditoria das ações críticas do sistema)

---

## Equipe

Projeto desenvolvido com sucesso pelo grupo da disciplina de Programação de Soluções Computacionais — **Universidade São Judas Tadeu (USJT)**.

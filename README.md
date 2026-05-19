# Finance Team — Sistema de Otimização de Portfólios

> **Projeto acadêmico A3** — Universidade São Judas Tadeu (USJT)  
> Disciplina: Programação de Soluções Computacionais  
> Professora: Cristiane

---

## Descrição

Sistema desktop em **Java Swing** para gestão e otimização de carteiras de investimentos. Permite que gerentes cadastrem investidores, selecionem ativos e executem um motor de otimização simplificado que sugere a alocação percentual ideal com base no perfil de risco.

---

## Arquitetura

O projeto segue o padrão **MVC + Service + DAO**:

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
|--------|-----------------|
| **View** | Telas Swing (JFrame/JPanel) — coleta dados e exibe resultados |
| **Controller** | Coordena fluxo entre View, Service e DAO |
| **Service** | Regras de negócio (autenticação, otimização, logging) |
| **DAO** | Acesso a dados via REST HTTP ao Supabase (HttpClient + Gson) |
| **Model** | Entidades de domínio (POJOs espelhando tabelas do banco) |

---

## Estrutura de Pastas

```
src/
├── model/                          # Camada Model (entidades de domínio)
│   ├── Asset.java                  # Ativo financeiro (ticker, nome, categoria, risco)
│   ├── LogEntry.java               # Registro de log de ações
│   ├── Optimization.java           # Resultado de uma execução de otimização
│   ├── Portfolio.java              # Carteira de investimentos
│   ├── PortfolioItem.java          # Item da carteira (ativo + quantidade + preço)
│   ├── PortfolioPrice.java         # Preço histórico por ticker/data
│   ├── RiskProfile.java            # Enum: CONSERVATIVE, MODERATE, AGGRESSIVE
│   ├── User.java                   # Usuário do sistema (*)
│   ├── UserRole.java               # Enum de papéis (*)
│   └── db.sql                      # Schema do banco (Supabase/PostgreSQL)
│
└── View/                           # Projeto NetBeans (camadas View + Controller)
    └── src/
        ├── controller/
        │   ├── LoginController.java
        │   ├── ManagerController.java
        │   └── InvestorController.java
        ├── view/
        │   ├── LoginView.java
        │   ├── ManagerMainView.java
        │   ├── InvestorRegistrationOptimizationView.java
        │   ├── InvestorDashboardView.java
        │   ├── ManagerInvestorHistoryView.java
        │   ├── View.java           # Main class (placeholder)
        │   └── resources/
        │       ├── logo_sem_background_darkmode.png
        │       └── logo_sem_background_lightmode.png
        └── util/
            └── ThemeManager.java   # Gerenciador de temas (dark/light mode)
```

> (*) Os arquivos `User.java` e `UserRole.java` atualmente contêm o código da classe `Portfolio` por engano — precisam ser corrigidos.

---

## Status de Implementação

### ✅ Camada Model — Completa

| Classe | Status | Observação |
|--------|--------|------------|
| `Asset` | ✅ Implementada | Getters/setters + `getDisplayName()` |
| `Portfolio` | ✅ Implementada | Inclui `getTotalValue()`, `addItem()`, `removeItem()` |
| `PortfolioItem` | ✅ Implementada | Inclui `getPositionValue()` |
| `Optimization` | ✅ Implementada | Construtores + `toString()` |
| `LogEntry` | ✅ Implementada | Construtores + `toString()` |
| `PortfolioPrice` | ✅ Implementada | Construtores + `toString()` |
| `RiskProfile` | ✅ Implementada | Enum com 3 valores |
| `User` | ⚠️ Conteúdo incorreto | Arquivo contém código de `Portfolio` |
| `UserRole` | ⚠️ Conteúdo incorreto | Arquivo contém código de `Portfolio` |

### ✅ Camada View — Implementada (com dados mockados)

| Tela | Status | Descrição |
|------|--------|-----------|
| `LoginView` | ✅ Funcional | Login com email/senha, logo dinâmico, tema dark/light |
| `ManagerMainView` | ✅ Funcional | Sidebar com navegação, painel dinâmico central |
| `InvestorRegistrationOptimizationView` | ✅ Funcional | Formulário + tabela de ativos com checkbox |
| `InvestorDashboardView` | ✅ Funcional | Cards de retorno/risco + tabela de alocação |
| `ManagerInvestorHistoryView` | ✅ Funcional | Tabela de investidores + botão visualizar carteira |

### ✅ Camada Controller — Implementada (navegação funcional)

| Controller | Status | Descrição |
|------------|--------|-----------|
| `LoginController` | ✅ Parcial | Valida campos vazios; falta integração com AuthService |
| `ManagerController` | ✅ Funcional | Navegação entre telas do gerente |
| `InvestorController` | ✅ Parcial | Navegação OK; falta lógica de persistência e otimização |

### ✅ Utilitários

| Classe | Status | Descrição |
|--------|--------|-----------|
| `ThemeManager` | ✅ Funcional | Dark/Light mode com paleta institucional (azul) |

### ❌ Camadas Pendentes

| Camada | Status |
|--------|--------|
| **DAO** (SupabaseClient, UserDAO, InvestorDAO, etc.) | ❌ Não iniciada |
| **Service** (AuthService, InvestorService, PortfolioOptimizerService) | ❌ Não iniciada |
| Jornada do **Administrador** (AdminUserView, UserFormDialog) | ❌ Não iniciada |
| Jornada do **Investidor** (InvestorReadOnlyDashboardView) | ❌ Não iniciada |

---

## Funcionalidades por Perfil

| Perfil | Funcionalidades |
|--------|----------------|
| **ADMIN** | CRUD de usuários do sistema |
| **MANAGER** | Cadastro de investidores, seleção de ativos, otimização de portfólio, histórico |
| **INVESTOR** | Visualização somente leitura da carteira otimizada |

---

## Tecnologias

| Tecnologia | Uso |
|------------|-----|
| Java 17+ | Linguagem principal |
| Java Swing | Interface gráfica desktop |
| NetBeans IDE | Construção do projeto View |
| Supabase (PostgreSQL) | Banco de dados na nuvem |
| REST API (HttpClient) | Comunicação com o Supabase |
| Gson | Serialização/deserialização JSON |

---

## Como Executar

1. Abrir o projeto `src/View` no **NetBeans IDE**
2. Executar a classe `view.LoginView` (contém `main`)
3. A aplicação inicia na tela de login com tema escuro

> **Nota:** Atualmente a aplicação funciona com dados mockados. A integração com o Supabase será implementada na camada DAO.

---

## Banco de Dados

O schema completo está em `src/model/db.sql`. Tabelas:

- `users` — Usuários internos (ADMIN, MANAGER)
- `investors` — Investidores cadastrados pelos gerentes
- `assets` — Ativos financeiros disponíveis
- `portfolios` — Carteiras de investimento
- `portfolio_items` — Composição das carteiras (ativo + quantidade + preço)
- `optimizations` — Registros de execuções do motor de otimização
- `portfolio_prices` — Preços históricos (para cálculos futuros)
- `logs` — Auditoria de ações do sistema

---

## Próximos Passos

1. **Corrigir** `User.java` e `UserRole.java` (estão com conteúdo duplicado de Portfolio)
2. **Implementar** camada DAO com `SupabaseClient` e DAOs específicos
3. **Implementar** camada Service (AuthService, InvestorService, PortfolioOptimizerService)
4. **Integrar** Controllers com Services/DAOs (substituir dados mockados)
5. **Implementar** jornada do Administrador (AdminUserView + UserFormDialog)
6. **Implementar** jornada do Investidor (tela somente leitura)
7. **Implementar** motor de otimização simplificado

---

## Equipe

Projeto desenvolvido pelo grupo da disciplina de Programação de Soluções Computacionais — USJT 2025.

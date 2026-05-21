# Status do Projeto — O que Falta Fazer

> Última atualização: Maio/2025

---

## Responsáveis por Camada

| Camada | Responsável(is) |
|--------|----------------|
| **Model** | Isabelly, Leandro e Maya |
| **DAO** | João e Iuan |
| **Service** | Maya |
| **Controller** | Jean e Giovanni |
| **View** | Leandro |

---

## O que já está PRONTO

### Model (Isabelly, Leandro, Maya)
| Classe | Status |
|--------|--------|
| `User.java` | ✅ Pronto (obs: arquivo contém código de Portfolio — corrigir) |
| `UserRole.java` | ✅ Pronto (obs: arquivo contém código de Portfolio — corrigir) |
| `Investor.java` | ✅ Pronto |
| `RiskProfile.java` | ✅ Pronto |
| `Asset.java` | ✅ Pronto |
| `Portfolio.java` | ✅ Pronto |
| `PortfolioItem.java` | ✅ Pronto |
| `Optimization.java` | ✅ Pronto |
| `LogEntry.java` | ✅ Pronto |
| `PortfolioPrice.java` | ✅ Pronto |

### Service (Maya)
| Classe | Status |
|--------|--------|
| `AuthService.java` | ✅ Pronto |
| `InvestorService.java` | ✅ Pronto |
| `PortfolioService.java` | ✅ Pronto |
| `PortfolioOptimizerService.java` | ✅ Pronto |

### View (Leandro)
| Classe | Status |
|--------|--------|
| `LoginView.java` | ✅ Pronto |
| `ManagerMainView.java` | ✅ Pronto |
| `InvestorRegistrationOptimizationView.java` | ✅ Pronto |
| `ManagerInvestorHistoryView.java` | ✅ Pronto |
| `InvestorDashboardView.java` | ✅ Pronto |
| `AdminUserView.java` | ✅ Pronto |
| `UserFormDialog.java` | ✅ Pronto |
| `InvestorReadOnlyDashboardView.java` | ✅ Pronto |
| `AssetSelectionPanel.java` | ✅ Pronto |
| `ThemeManager.java` | ✅ Pronto |
| `MessageUtil.java` | ✅ Pronto |
| `BaseFrame.java` | ✅ Pronto |
| `UserTableModel.java` | ✅ Pronto |
| `InvestorTableModel.java` | ✅ Pronto |
| `AssetSelectionTableModel.java` | ✅ Pronto |
| `PortfolioItemTableModel.java` | ✅ Pronto |

### Controller (Jean, Giovanni) — parcialmente feito
| Classe | Status |
|--------|--------|
| `LoginController.java` | ⚠️ Esqueleto pronto (falta integrar com AuthService real) |
| `ManagerController.java` | ⚠️ Esqueleto pronto (falta integrar com Services) |
| `InvestorController.java` | ⚠️ Esqueleto pronto (falta integrar com Services) |

---

## O que FALTA fazer

### 1. DAO — João e Iuan

**Nenhum DAO foi implementado ainda.** Todos precisam ser criados do zero.

| Classe | Prioridade | O que fazer |
|--------|-----------|-------------|
| `SupabaseClient.java` | ALTA | Classe base com métodos `get()`, `post()`, `patch()`, `deleteById()`. Todos os DAOs dependem dela. |
| `UserDAO.java` | ALTA | `create()`, `findByEmail()`, `findById()`, `update()`, `delete()` |
| `InvestorDAO.java` | ALTA | `create()`, `findAll()`, `findByManager()`, `findById()`, `delete()` |
| `AssetDAO.java` | MÉDIA | `create()`, `findAll()`, `findByTicker()` |
| `PortfolioDAO.java` | ALTA | `create()`, `findByInvestor()`, `findById()` |
| `PortfolioItemDAO.java` | ALTA | `create()`, `findByPortfolio()`, `updateSuggestedPercentage()`, `delete()` |
| `OptimizationDAO.java` | MÉDIA | `create()`, `findByPortfolio()` |
| `LogDAO.java` | MÉDIA | `create()`, `findRecentForUser()` |
| `PortfolioPriceDAO.java` | BAIXA | `upsertPrice()`, `findByTicker()` (opcional na V1) |

**Como começar:**
1. Criar `SupabaseClient.java` primeiro (é a base de tudo)
2. Depois `UserDAO.java` (para testar login)
3. Depois `InvestorDAO.java` e `PortfolioDAO.java`

**Referência:** Ver seção 2 do `definicao-clases-novo.md` para detalhes de cada método.

---

### 2. Controller — Jean e Giovanni

Os controllers existem mas estão com **lógica mockada** (só prints no console). Precisam ser integrados com os Services reais.

| Classe | O que falta |
|--------|------------|
| `LoginController.java` | Integrar com `AuthService` real. Após login, abrir a tela correta (Admin/Manager/Investor) |
| `ManagerController.java` | Receber `User loggedUser` e `InvestorController` com Services reais |
| `InvestorController.java` | Integrar com `InvestorService`, `PortfolioService`, `PortfolioOptimizerService` |
| `AdminUserController.java` | **CRIAR DO ZERO** — CRUD de usuários usando `UserDAO` |
| `PortfolioController.java` | **CRIAR** (opcional) — operações extras sobre carteiras |

**Como começar:**
1. Esperar os DAOs ficarem prontos (João e Iuan)
2. Atualizar `LoginController` para usar `AuthService` real
3. Criar `AdminUserController`
4. Atualizar `InvestorController` para usar os Services

---

### 3. Model — Isabelly, Leandro, Maya

**PROBLEMA ENCONTRADO:** Os arquivos `User.java` e `UserRole.java` contêm o código da classe `Portfolio` em vez do conteúdo correto. Precisam ser corrigidos.

| Classe | O que falta |
|--------|------------|
| `User.java` | **CORRIGIR** — o arquivo contém código de Portfolio. Precisa ter a classe User com atributos: id, name, email, passwordHash, role, managerCode, active, createdAt |
| `UserRole.java` | **CORRIGIR** — o arquivo contém código de Portfolio. Precisa ter o enum: ADMIN, MANAGER, INVESTOR |

**Código correto do User.java:**
```java
package model;

import java.time.OffsetDateTime;

public class User {
    private Long id;
    private String name;
    private String email;
    private String passwordHash;
    private UserRole role;
    private String managerCode;
    private boolean active;
    private OffsetDateTime createdAt;

    // Getters e Setters de todos os campos...

    public boolean isAdmin() { return role == UserRole.ADMIN; }
    public boolean isManager() { return role == UserRole.MANAGER; }
    public boolean isInvestor() { return role == UserRole.INVESTOR; }

    public boolean checkPassword(String plainPassword) {
        // Na V1, comparação simples (depois trocar por BCrypt)
        return passwordHash != null && passwordHash.equals(plainPassword);
    }

    public String toString() { return name + " (" + email + ")"; }
}
```

**Código correto do UserRole.java:**
```java
package model;

public enum UserRole {
    ADMIN,
    MANAGER,
    INVESTOR
}
```

---

### 4. Integração Final (TODOS)

Depois que cada camada estiver pronta individualmente, a integração final:

| Tarefa | Responsável | Depende de |
|--------|------------|-----------|
| Corrigir `User.java` e `UserRole.java` | Isabelly/Leandro/Maya | — |
| Criar `SupabaseClient` + todos os DAOs | João e Iuan | Model corrigido |
| Integrar Controllers com Services reais | Jean e Giovanni | DAOs prontos |
| Testar fluxo completo de Login | TODOS | Tudo acima |
| Testar fluxo Cadastrar + Otimizar | TODOS | Tudo acima |
| Testar fluxo Admin (CRUD usuários) | TODOS | Tudo acima |

---

## Ordem de Execução Recomendada

```
SEMANA 1:
├── [Isabelly/Leandro/Maya] Corrigir User.java e UserRole.java
├── [João/Iuan] Criar SupabaseClient.java
└── [João/Iuan] Criar UserDAO.java

SEMANA 2:
├── [João/Iuan] Criar InvestorDAO, PortfolioDAO, PortfolioItemDAO
├── [João/Iuan] Criar AssetDAO, OptimizationDAO, LogDAO
└── [Jean/Giovanni] Integrar LoginController com AuthService + UserDAO

SEMANA 3:
├── [Jean/Giovanni] Criar AdminUserController
├── [Jean/Giovanni] Integrar InvestorController com Services
└── [TODOS] Testar fluxos completos
```

---

## Configuração do Supabase

Para os DAOs funcionarem, vocês precisam:

1. **URL do projeto:** `https://SEU-ID.supabase.co/rest/v1`
2. **API Key:** encontrada em Settings → API → `anon public`
3. **Tabelas já criadas:** ver arquivo `src/model/db.sql`

Coloquem essas informações em variáveis de ambiente ou em um arquivo de configuração (NÃO committem a API key no GitHub).

---

## Resumo Visual

```
                    ┌─────────────────────────────────┐
                    │         PRONTO ✅                │
                    │  Model, Service, View            │
                    └─────────────────────────────────┘
                                    │
                    ┌─────────────────────────────────┐
                    │      FALTA FAZER ❌              │
                    │  DAO (João/Iuan)                 │
                    │  Controller integrado (Jean/Gio) │
                    │  Corrigir User.java (Isabelly)   │
                    └─────────────────────────────────┘
                                    │
                    ┌─────────────────────────────────┐
                    │      INTEGRAÇÃO FINAL 🔗         │
                    │  Conectar tudo e testar          │
                    │  (TODOS juntos)                  │
                    └─────────────────────────────────┘
```

---

*Documento criado para o grupo Finance Team — Projeto A3*

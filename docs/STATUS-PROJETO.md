# Status do Projeto — Finance Team

> Última atualização: Junho/2025

---

## Resumo

**O projeto está 100% implementado e integrado.** Todas as camadas (Model, DAO, Service, Controller, View) estão funcionais e conectadas entre si. O sistema roda diretamente com o banco Supabase — basta ter o `.env` configurado e pressionar F6 no NetBeans.

---

## Responsáveis por Camada

| Camada | Responsável(is) |
|--------|----------------|
| **Model** | Isabelly, Leandro e Maya |
| **DAO** | João e Iuan |
| **Service** | Maya |
| **Controller** | Jean, Giovanni e Leandro |
| **View** | Leandro |

---

## Status por Camada

### Model ✅

| Classe | Status |
|--------|--------|
| `User.java` | ✅ Completo |
| `UserRole.java` | ✅ Completo |
| `Investor.java` | ✅ Completo |
| `RiskProfile.java` | ✅ Completo |
| `Asset.java` | ✅ Completo |
| `Portfolio.java` | ✅ Completo |
| `PortfolioItem.java` | ✅ Completo |
| `Optimization.java` | ✅ Completo |
| `LogEntry.java` | ✅ Completo |
| `PortfolioPrice.java` | ✅ Completo |

### DAO ✅

| Classe | Status |
|--------|--------|
| `SupabaseClient.java` | ✅ Completo — GET, POST, PATCH, DELETE, UPSERT |
| `UserDAO.java` | ✅ Completo — create, findByEmail, findById, findAll, update, delete |
| `InvestorDAO.java` | ✅ Completo — create, findAll, findByManager, findById, delete |
| `AssetDAO.java` | ✅ Completo — create, findAll, findByTicker |
| `PortfolioDAO.java` | ✅ Completo — create, findByInvestor, findById |
| `PortfolioItemDAO.java` | ✅ Completo — create, findByPortfolio, updateSuggestedPercentage, delete |
| `OptimizationDAO.java` | ✅ Completo — create, findByPortfolio |
| `LogDAO.java` | ✅ Completo — create, findRecentForUser |
| `PortfolioPriceDAO.java` | ✅ Completo — upsertPrice, findByTicker |

### Service ✅

| Classe | Status |
|--------|--------|
| `AuthService.java` | ✅ Completo — login com validação e log de auditoria |
| `InvestorService.java` | ✅ Completo — createInvestor, getInvestorById, deleteInvestor, findByManager |
| `PortfolioService.java` | ✅ Completo — createPortfolio, addItem, removeItem, loadPortfolioWithItems |
| `PortfolioOptimizerService.java` | ✅ Completo — motor de otimização por perfil de risco (CONSERVATIVE/MODERATE/AGGRESSIVE) |

### Controller ✅

| Classe | Status |
|--------|--------|
| `LoginController.java` | ✅ Completo — autentica e redireciona por perfil (Admin/Manager/Investor) |
| `AdminUserController.java` | ✅ Completo — CRUD de usuários |
| `ManagerController.java` | ✅ Completo — navegação entre telas do Gerente |
| `InvestorController.java` | ✅ Completo — cadastro, otimização, histórico e dashboard |
| `InvestorReadOnlyController.java` | ✅ Completo — carrega carteira do investidor logado |

### View ✅

| Classe | Jornada | Status |
|--------|---------|--------|
| `LoginView.java` | Comum | ✅ Completo |
| `AdminUserView.java` | Administrador | ✅ Completo |
| `UserFormDialog.java` | Administrador | ✅ Completo |
| `ManagerMainView.java` | Gerente | ✅ Completo |
| `InvestorRegistrationOptimizationView.java` | Gerente | ✅ Completo |
| `InvestorDashboardView.java` | Gerente | ✅ Completo |
| `ManagerInvestorHistoryView.java` | Gerente | ✅ Completo |
| `InvestorReadOnlyDashboardView.java` | Investidor | ✅ Completo |

### Utilitários ✅

| Classe | Status |
|--------|--------|
| `ThemeManager.java` | ✅ Completo — Dark/Light mode + estilização de tabelas, combos e scroll |
| `MessageUtil.java` | ✅ Completo — showSuccess, showError, showWarning, confirm |
| `BaseFrame.java` | ✅ Completo — classe base para JFrames |
| `AppContext.java` | ✅ Completo — injeção de dependências (DAOs + Services) |

---

## Fluxo Completo Integrado

```
app.Main
  └── AppContext
       ├── DAOs (conectados ao Supabase via .env)
       └── Services (recebem os DAOs)
            └── LoginView + LoginController
                 ├── ADMIN
                 │    └── AdminUserView + AdminUserController
                 │         └── UserDAO (list/create/delete usuários)
                 ├── MANAGER
                 │    └── ManagerMainView + ManagerController
                 │         ├── InvestorRegistrationOptimizationView
                 │         │    └── InvestorController.registerAndOptimize()
                 │         │         ├── InvestorService.createInvestor()
                 │         │         ├── PortfolioService.createPortfolio()
                 │         │         ├── PortfolioService.addItem() (por ativo)
                 │         │         ├── PortfolioOptimizerService.optimizePortfolio()
                 │         │         └── InvestorDashboardView (resultado)
                 │         └── ManagerInvestorHistoryView
                 │              └── InvestorController.openSelectedInvestorDashboard()
                 └── INVESTOR
                      └── InvestorReadOnlyDashboardView
                           └── InvestorReadOnlyController.loadMyPortfolio()
```

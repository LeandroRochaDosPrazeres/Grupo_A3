# Guia de Integração entre Camadas

> Como as camadas do sistema se conectam: View → Controller → Service → DAO → Supabase.

---

## Visão Geral do Fluxo

```
[Usuário clica na tela]
        ↓
    VIEW (Swing)         → Coleta dados da interface
        ↓
    CONTROLLER           → Coordena o fluxo, chama o Service
        ↓
    SERVICE              → Aplica regras de negócio
        ↓
    DAO                  → Faz requisição HTTP ao Supabase
        ↓
    SUPABASE (banco)     → Retorna JSON com os dados
        ↓
    DAO                  → Converte JSON em objeto Java (Model)
        ↓
    SERVICE              → Retorna o objeto para o Controller
        ↓
    CONTROLLER           → Atualiza a View com os dados
        ↓
    VIEW                 → Exibe para o usuário
```

---

## 1. Injeção de Dependências — AppContext

O `AppContext` é criado uma única vez no `Main` e entregue para todos os controllers. Ele instancia os DAOs (que leem as credenciais do `.env`) e os Services (que recebem os DAOs).

```java
// app/Main.java
AppContext context = new AppContext();
LoginView loginView = new LoginView();
loginView.setController(new LoginController(loginView, context));
loginView.showView();
```

```java
// app/AppContext.java — simplificado
public AppContext() {
    this.userDAO       = new UserDAO(null, null);     // lê .env automaticamente
    this.investorDAO   = new InvestorDAO(null, null);
    // ... demais DAOs

    this.authService      = new AuthService(userDAO, logDAO);
    this.investorService  = new InvestorService(investorDAO, logDAO);
    this.portfolioService = new PortfolioService(portfolioDAO, portfolioItemDAO, investorDAO, logDAO);
    this.optimizerService = new PortfolioOptimizerService(portfolioService, portfolioPriceDAO, optimizationDAO, portfolioItemDAO);
}
```

---

## 2. Fluxo de Login

```
LoginView  →  LoginController.handleLogin()  →  AuthService.login()  →  UserDAO.findByEmail()
```

O `LoginController` lê email/senha da view, chama `AuthService.login()` e, se bem-sucedido, abre a tela correta por perfil:

- `ADMIN` → `AdminUserView` + `AdminUserController`
- `MANAGER` → `ManagerMainView` + `ManagerController`
- `INVESTOR` → `InvestorReadOnlyDashboardView` + `InvestorReadOnlyController`

---

## 3. Fluxo: Cadastrar e Otimizar Investidor

O fluxo principal do sistema, disparado pelo botão **CADASTRAR E OTIMIZAR**:

```
InvestorRegistrationOptimizationView
  └── InvestorController.registerAndOptimize()
       ├── 1. InvestorService.createInvestor()         → POST /investors
       ├── 2. PortfolioService.createPortfolio()       → POST /portfolios
       ├── 3. PortfolioService.addItem() (por ativo)   → POST /portfolio_items
       ├── 4. PortfolioOptimizerService.optimizePortfolio()
       │        ├── Carrega itens da carteira
       │        ├── Calcula % por perfil de risco (CONSERVATIVE/MODERATE/AGGRESSIVE)
       │        ├── PATCH /portfolio_items (suggested_percentage)
       │        └── POST /optimizations
       └── 5. InvestorDashboardView (exibe resultado)
```

---

## 4. Motor de Otimização

O `PortfolioOptimizerService` distribui os percentuais de acordo com o perfil de risco do investidor:

| Perfil | Lógica |
|--------|--------|
| **CONSERVATIVE** | Primeiro ativo recebe 60%, os demais dividem os 40% restantes igualmente |
| **MODERATE** | Todos os ativos recebem percentuais iguais (100% / N ativos) |
| **AGGRESSIVE** | Último ativo recebe 60%, os demais dividem os 40% restantes igualmente |

Após calcular os percentuais, atualiza cada `PortfolioItem` no banco e grava um registro em `optimizations` com `expectedReturn` e `totalRisk`.

---

## 5. Como o DAO se conecta ao Supabase

Todos os DAOs estendem `SupabaseClient`, que lê as credenciais do `.env` e expõe métodos HTTP genéricos:

```java
// Busca investidores de um gerente
public List<Investor> findByManager(Long managerId) {
    String path = "/investors?responsible_manager_id=eq." + managerId + "&select=*";
    JsonArray response = get(path);   // método da SupabaseClient
    // converte cada JsonObject → Investor e retorna a lista
}

// Cria um novo portfólio
public Portfolio create(Portfolio portfolio) {
    JsonObject body = new JsonObject();
    body.addProperty("investor_id", portfolio.getInvestorId());
    body.addProperty("name", portfolio.getName());
    JsonArray response = post("/portfolios", body);  // POST com Prefer: return=representation
    return fromJson(response.get(0).getAsJsonObject());
}
```

---

## 6. Onde colocar cada coisa

| Eu quero... | Coloco em... |
|-------------|-------------|
| Mostrar ou coletar dados da tela | **View** |
| Reagir ao clique de um botão | **Controller** |
| Validar regras de negócio | **Service** |
| Enviar/buscar dados do banco | **DAO** |
| Representar uma entidade | **Model** |

**Erros comuns:**
- `HttpClient` na View → errado, vai no DAO
- `JOptionPane` no Controller → errado, vai na View (via `showError()`)
- Validação de negócio no DAO → errado, vai no Service
- JSON ou SQL na View → errado, vai no DAO

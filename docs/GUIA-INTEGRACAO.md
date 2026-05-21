# Guia de Integração entre Camadas

> Este guia explica, passo a passo, como conectar as camadas do sistema (View → Controller → Service → DAO → Supabase). Foi escrito para alunos de nível básico — leia com calma, siga os exemplos e pergunte se tiver dúvida.

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

## 1. Como a View se conecta ao Controller

A View **nunca** acessa o banco diretamente. Ela apenas coleta dados e delega para o Controller.

### Exemplo: LoginView → LoginController

**Na View (LoginView.java):**
```java
// O botão de login chama o controller
btnLogin.addActionListener(e -> {
    if (controller != null) controller.handleLogin();
});
```

**No Controller (LoginController.java):**
```java
public void handleLogin() {
    // 1. Pega os dados da View
    String email = loginView.getEmail();
    String senha = loginView.getPassword();

    // 2. Chama o Service para validar
    try {
        User user = authService.login(email, senha);
        
        // 3. Se deu certo, abre a tela correta
        if (user.isAdmin()) {
            // Abre AdminUserView
        } else if (user.isManager()) {
            // Abre ManagerMainView
        }
        loginView.closeView();
        
    } catch (RuntimeException ex) {
        // 4. Se deu erro, mostra na View
        loginView.showError(ex.getMessage());
    }
}
```

**Regra de ouro:** A View só tem métodos `get` (pegar dados) e `show` (mostrar dados). Quem decide o que fazer é o Controller.

---

## 2. Como o Controller se conecta ao Service

O Controller instancia o Service passando os DAOs necessários.

### Exemplo: Criando o AuthService

```java
// No main ou na inicialização do sistema:
SupabaseClient client = new SupabaseClient(
    "https://SEU-PROJETO.supabase.co/rest/v1",
    "SUA-API-KEY"
);

UserDAO userDAO = new UserDAO(client);
LogDAO logDAO = new LogDAO(client);

// O Service recebe os DAOs pelo construtor
AuthService authService = new AuthService(userDAO, logDAO);

// O Controller recebe a View e o Service
LoginController controller = new LoginController(loginView, authService);
```

### Exemplo: InvestorController chamando InvestorService

```java
public void registerAndOptimize() {
    // 1. Pega dados da View
    String nome = registrationView.getInvestorName();
    String documento = registrationView.getDocumentId();
    String perfil = registrationView.getSelectedRiskProfile();

    // 2. Monta o objeto Model
    Investor investor = new Investor();
    investor.setName(nome);
    investor.setDocumentId(documento);
    investor.setRiskProfile(RiskProfile.valueOf(perfil));

    // 3. Chama o Service (que vai chamar o DAO internamente)
    Investor salvo = investorService.createInvestor(investor, loggedUser);

    // 4. Cria o portfólio
    Portfolio portfolio = new Portfolio();
    portfolio.setInvestorId(salvo.getId());
    portfolio.setName("Carteira de " + salvo.getName());
    Portfolio portfolioCriado = portfolioService.createPortfolio(portfolio);

    // 5. Adiciona os ativos selecionados
    List<String> tickers = registrationView.getSelectedAssets();
    for (String ticker : tickers) {
        PortfolioItem item = new PortfolioItem();
        item.setPortfolioId(portfolioCriado.getId());
        // ... preenche assetId, quantity, averagePrice
        portfolioService.addItem(item);
    }

    // 6. Otimiza
    Optimization resultado = optimizerService.optimizePortfolio(
        portfolioCriado.getId(), loggedUser
    );

    // 7. Mostra resultado na View
    dashboardView.setInvestorName(salvo.getName());
    dashboardView.setExpectedReturn(resultado.getExpectedReturn().toString());
}
```

---

## 3. Como o Service se conecta ao DAO

O Service contém as **regras de negócio** e usa o DAO para persistir/buscar dados.

### Exemplo: InvestorService.createInvestor()

```java
public Investor createInvestor(Investor investor, User currentUser) {
    // REGRA 1: Só gerente pode criar investidor
    if (!currentUser.isManager()) {
        throw new RuntimeException("Apenas gerentes podem criar investidores");
    }

    // REGRA 2: Preenche o gerente responsável automaticamente
    investor.setResponsibleManagerId(currentUser.getId());

    // CHAMA O DAO: persiste no banco
    Investor salvo = investorDAO.create(investor);

    // REGRA 3: Registra log da ação
    LogEntry log = new LogEntry();
    log.setUserId(currentUser.getId());
    log.setAction("CREATE_INVESTOR");
    log.setDetails("Criou investidor: " + salvo.getName());
    logDAO.create(log);

    return salvo;
}
```

**Importante:** O Service NÃO sabe como o DAO faz a requisição HTTP. Ele só chama `investorDAO.create(investor)` e recebe o resultado.

---

## 4. Como o DAO se conecta ao Supabase

O DAO monta a URL, envia a requisição HTTP e converte o JSON de volta para objeto Java.

### Exemplo: InvestorDAO.create()

```java
public Investor create(Investor investor) {
    // 1. Monta o JSON para enviar
    JsonObject body = new JsonObject();
    body.addProperty("name", investor.getName());
    body.addProperty("document_id", investor.getDocumentId());
    body.addProperty("risk_profile", investor.getRiskProfile().name());
    body.addProperty("responsible_manager_id", investor.getResponsibleManagerId());

    // 2. Envia POST para o Supabase
    JsonArray response = client.post("/investors", body);

    // 3. Converte a resposta JSON em objeto Java
    JsonObject obj = response.get(0).getAsJsonObject();
    return fromJson(obj);
}

private Investor fromJson(JsonObject obj) {
    Investor inv = new Investor();
    inv.setId(obj.get("id").getAsLong());
    inv.setName(obj.get("name").getAsString());
    inv.setDocumentId(obj.get("document_id").getAsString());
    inv.setRiskProfile(RiskProfile.valueOf(obj.get("risk_profile").getAsString()));
    inv.setResponsibleManagerId(obj.get("responsible_manager_id").getAsLong());
    return inv;
}
```

### Exemplo: InvestorDAO.findByManager()

```java
public List<Investor> findByManager(Long managerId) {
    // Monta a URL com filtro query-string do Supabase
    String path = "/investors?responsible_manager_id=eq." + managerId + "&select=*";
    
    JsonArray response = client.get(path);
    
    List<Investor> lista = new ArrayList<>();
    for (int i = 0; i < response.size(); i++) {
        lista.add(fromJson(response.get(i).getAsJsonObject()));
    }
    return lista;
}
```

---

## 5. Como o SupabaseClient funciona

É a classe base que todos os DAOs usam. Ela cuida dos headers e da comunicação HTTP.

```java
public class SupabaseClient {
    private String projectUrl;  // Ex: "https://xyz.supabase.co/rest/v1"
    private String apiKey;      // Chave do Supabase
    private HttpClient httpClient;

    // Método GET genérico
    protected JsonArray get(String path) {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(projectUrl + path))
            .header("apikey", apiKey)
            .header("Authorization", "Bearer " + apiKey)
            .header("Content-Type", "application/json")
            .GET()
            .build();

        HttpResponse<String> response = httpClient.send(request, 
            HttpResponse.BodyHandlers.ofString());

        // Converte a string JSON em JsonArray (usando Gson)
        return JsonParser.parseString(response.body()).getAsJsonArray();
    }

    // Método POST genérico
    protected JsonArray post(String path, JsonObject body) {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(projectUrl + path))
            .header("apikey", apiKey)
            .header("Authorization", "Bearer " + apiKey)
            .header("Content-Type", "application/json")
            .header("Prefer", "return=representation")
            .POST(HttpRequest.BodyPublishers.ofString(body.toString()))
            .build();

        HttpResponse<String> response = httpClient.send(request,
            HttpResponse.BodyHandlers.ofString());

        return JsonParser.parseString(response.body()).getAsJsonArray();
    }
}
```

---

## 6. Fluxo Completo: Login do Início ao Fim

Vamos seguir o caminho completo de um login:

```
1. Usuário digita "maria@finance.com" e "123456" na LoginView
2. Clica no botão "Login"
3. LoginView chama: controller.handleLogin()
4. LoginController pega: loginView.getEmail() → "maria@finance.com"
5. LoginController pega: loginView.getPassword() → "123456"
6. LoginController chama: authService.login("maria@finance.com", "123456")
7. AuthService chama: userDAO.findByEmail("maria@finance.com")
8. UserDAO monta URL: "/users?email=eq.maria@finance.com&select=*"
9. UserDAO envia GET para o Supabase
10. Supabase retorna JSON: [{"id":2, "name":"Maria", "role":"MANAGER", ...}]
11. UserDAO converte JSON → objeto User
12. AuthService verifica: user.isActive() → true
13. AuthService verifica: user.checkPassword("123456") → true
14. AuthService registra log de sucesso via logDAO.create(...)
15. AuthService retorna o User para o LoginController
16. LoginController verifica: user.isManager() → true
17. LoginController abre ManagerMainView
18. LoginController fecha LoginView
```

---

## 7. Fluxo Completo: Cadastrar e Otimizar Investidor

```
1. Gerente preenche nome, CPF, perfil na InvestorRegistrationOptimizationView
2. Gerente marca ativos na tabela (PETR4, VALE3, BOVA11)
3. Gerente clica "CADASTRAR E OTIMIZAR"
4. InvestorController.registerAndOptimize() é chamado
5. Controller lê dados da View
6. Controller chama investorService.createInvestor(investor, gerente)
7. InvestorService valida que é gerente, preenche managerId
8. InvestorService chama investorDAO.create(investor) → POST /investors
9. Supabase salva e retorna o investidor com ID
10. InvestorService registra log
11. Controller cria Portfolio via portfolioService.createPortfolio(...)
12. PortfolioService chama portfolioDAO.create(...) → POST /portfolios
13. Controller adiciona cada ativo via portfolioService.addItem(...)
14. PortfolioItemDAO faz POST /portfolio_items para cada ativo
15. Controller chama optimizerService.optimizePortfolio(portfolioId, gerente)
16. OptimizerService carrega portfolio + itens
17. OptimizerService calcula percentuais por perfil de risco
18. OptimizerService atualiza cada item via portfolioItemDAO.updateSuggestedPercentage(...)
19. OptimizerService cria registro Optimization via optimizationDAO.create(...)
20. Controller recebe Optimization e exibe na InvestorDashboardView
```

---

## 8. Dicas Importantes

### Onde colocar cada coisa:

| Eu quero... | Coloco em... |
|-------------|-------------|
| Mostrar dados na tela | **View** |
| Decidir o que fazer quando clica um botão | **Controller** |
| Validar regras (ex: "só gerente pode criar") | **Service** |
| Enviar/buscar dados do banco | **DAO** |
| Representar uma entidade (User, Investor...) | **Model** |

### Erros comuns:

- Colocar `HttpClient` na View → ERRADO (vai no DAO)
- Colocar `JOptionPane` no Controller → ERRADO (vai na View)
- Colocar validação de negócio no DAO → ERRADO (vai no Service)
- Colocar SQL ou JSON na View → ERRADO (vai no DAO)

### Dependências (bibliotecas necessárias):

- **Gson** (para converter JSON ↔ objetos Java) — `com.google.gson`
- **HttpClient** (para requisições HTTP) — já vem no Java 11+

---

## 9. Como Testar sua Camada Isoladamente

### Testando o DAO (sem View):
```java
public static void main(String[] args) {
    SupabaseClient client = new SupabaseClient("URL", "KEY");
    InvestorDAO dao = new InvestorDAO(client);
    
    // Testa busca
    List<Investor> lista = dao.findAll();
    System.out.println("Investidores encontrados: " + lista.size());
}
```

### Testando o Service (sem View):
```java
public static void main(String[] args) {
    SupabaseClient client = new SupabaseClient("URL", "KEY");
    InvestorDAO investorDAO = new InvestorDAO(client);
    LogDAO logDAO = new LogDAO(client);
    InvestorService service = new InvestorService(investorDAO, logDAO);
    
    // Simula um gerente
    User gerente = new User();
    gerente.setId(1L);
    gerente.setRole(UserRole.MANAGER);
    gerente.setName("Maria");
    
    // Testa criação
    Investor inv = new Investor();
    inv.setName("Teste");
    inv.setDocumentId("000.000.000-00");
    inv.setRiskProfile(RiskProfile.MODERATE);
    
    Investor salvo = service.createInvestor(inv, gerente);
    System.out.println("Criado: " + salvo.getId());
}
```

---

*Documento criado para o grupo Finance Team — Projeto A3*

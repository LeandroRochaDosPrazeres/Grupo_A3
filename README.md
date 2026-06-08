# Finance Team — Sistema de Otimização de Portfólios

> **Projeto acadêmico A3** — Universidade São Judas Tadeu (USJT)  
> Disciplina: Programação de Soluções Computacionais  
> Professora: Cristiane  
> **Status: ✅ Projeto finalizado — todas as camadas implementadas e integradas**

---
OBS: Os testes automatizados foram realizados na branch main do GitHub.
---


## Descrição

Sistema desktop em **Java Swing** para gestão e otimização de carteiras de investimentos. Permite que gerentes cadastrem investidores, selecionem ativos e executem um motor de otimização que sugere a alocação percentual ideal com base no perfil de risco do investidor.

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

## Estrutura do Projeto

O projeto NetBeans fica em `src/view/`. Dentro do source root (`src/view/src/`):

```
src/view/src/
├── app/
│   ├── Main.java                              # Ponto de entrada da aplicação
│   └── AppContext.java                        # Injeção de dependências (DAOs + Services)
├── model/
│   ├── User.java / UserRole.java              # Usuário do sistema e enum de papéis
│   ├── Investor.java / RiskProfile.java       # Investidor e enum de perfil de risco
│   ├── Asset.java                             # Ativo financeiro
│   ├── Portfolio.java / PortfolioItem.java    # Carteira e seus itens
│   ├── Optimization.java                      # Resultado de otimização
│   ├── LogEntry.java / PortfolioPrice.java    # Log de ações e preço histórico
│   └── db.sql                                 # Schema do banco (Supabase/PostgreSQL)
├── dao/
│   ├── SupabaseClient.java                    # Cliente HTTP base (GET/POST/PATCH/DELETE)
│   ├── UserDAO.java / InvestorDAO.java        # CRUD de usuários e investidores
│   ├── AssetDAO.java / PortfolioDAO.java      # CRUD de ativos e carteiras
│   ├── PortfolioItemDAO.java                  # Itens da carteira
│   ├── OptimizationDAO.java / LogDAO.java     # Otimizações e logs
│   └── PortfolioPriceDAO.java                 # Preços históricos
├── service/
│   ├── AuthService.java                       # Autenticação com log de auditoria
│   ├── InvestorService.java                   # CRUD de investidores com validação
│   ├── PortfolioService.java                  # Operações sobre carteiras
│   └── PortfolioOptimizerService.java         # Motor de otimização por perfil de risco
├── controller/
│   ├── LoginController.java                   # Autenticação e redirecionamento por perfil
│   ├── AdminUserController.java               # CRUD de usuários (jornada Admin)
│   ├── ManagerController.java                 # Navegação da jornada do Gerente
│   ├── InvestorController.java                # Cadastro, otimização e histórico
│   └── InvestorReadOnlyController.java        # Carrega carteira do investidor logado
├── view/
│   ├── LoginView.java                         # Tela de login
│   ├── AdminUserView.java / UserFormDialog.java  # Jornada do Administrador
│   ├── ManagerMainView.java                   # Painel principal do Gerente
│   ├── InvestorRegistrationOptimizationView.java # Cadastro + seleção de ativos
│   ├── InvestorDashboardView.java             # Dashboard pós-otimização (gerente)
│   ├── ManagerInvestorHistoryView.java        # Histórico de investidores
│   ├── InvestorReadOnlyDashboardView.java     # Dashboard somente leitura (investidor)
│   └── resources/
│       ├── logo_sem_background_darkmode.png
│       └── logo_sem_background_lightmode.png
└── util/
    ├── ThemeManager.java                      # Dark/Light mode + helpers de estilização
    ├── MessageUtil.java                       # Mensagens padronizadas (JOptionPane)
    └── BaseFrame.java                         # Classe base para JFrames
```

---

## Status do Projeto

**Todas as camadas estão implementadas e integradas.**

| Camada | Status |
|--------|--------|
| Model | ✅ Completo |
| DAO | ✅ Completo |
| Service | ✅ Completo |
| Controller | ✅ Completo e integrado |
| View | ✅ Completo |

---

## Funcionalidades por Perfil

| Perfil | Funcionalidades |
|--------|----------------|
| **ADMIN** | Listar, cadastrar e excluir usuários do sistema |
| **MANAGER** | Cadastrar investidores, selecionar ativos, otimizar portfólio, consultar histórico |
| **INVESTOR** | Visualizar a carteira otimizada (somente leitura) |

---

## Tecnologias

| Tecnologia | Uso |
|------------|-----|
| Java 17+ | Linguagem principal (validado com JDK 25) |
| Java Swing | Interface gráfica desktop |
| NetBeans IDE | IDE com suporte a projetos Java Ant |
| Supabase (PostgreSQL) | Banco de dados na nuvem |
| HttpClient (Java 11+) | Comunicação REST com o Supabase |
| Gson 2.10.1 | Serialização/deserialização JSON |
| dotenv-java 3.2.0 | Leitura de variáveis de ambiente do arquivo `.env` |

---

## Como Executar

1. Certifique-se de que o arquivo `src/view/.env` existe com as credenciais do Supabase:
   ```
   SUPABASE_URL=https://SEU-PROJETO.supabase.co/rest/v1
   SUPABASE_API_KEY=SUA_CHAVE_ANON_PUBLIC
   ```
2. Abra o **NetBeans** → **File > Open Project** → selecione a pasta `src/view`
3. Pressione **F6** para compilar e executar

Veja `docs/COMO-EXECUTAR.md` para o guia completo.

---

## Banco de Dados

Schema completo em `src/view/src/model/db.sql`. Tabelas:

| Tabela | Conteúdo |
|--------|---------|
| `users` | Usuários internos (ADMIN, MANAGER, INVESTOR) |
| `investors` | Investidores cadastrados pelos gerentes |
| `assets` | Ativos financeiros disponíveis |
| `portfolios` | Carteiras de investimento |
| `portfolio_items` | Composição das carteiras (ativo + quantidade + preço) |
| `optimizations` | Registros de execuções do motor de otimização |
| `portfolio_prices` | Preços históricos por ticker/data |
| `logs` | Auditoria de ações do sistema |

---

## Equipe

Projeto desenvolvido pelo grupo Finance Team — disciplina de Programação de Soluções Computacionais, USJT 2025.

| Membro | Camada |
|--------|--------|
| Leandro Rocha | View + Controller + Integração |
| Maya | Service + Model |
| João / Iuan | DAO |
| Jean / Giovanni | Controller |
| Isabelly | Model |

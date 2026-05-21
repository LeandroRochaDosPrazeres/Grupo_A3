# Documentação das Interfaces (Camada View)

Este documento detalha as funcionalidades e implementações visuais realizadas nas telas do **Middleware de Otimização de Portfólio**, seguindo a arquitetura MVC proposta para o projeto.

---

## Estrutura de Arquivos

```
src/
├── controller/
│   ├── LoginController.java
│   ├── ManagerController.java
│   └── InvestorController.java
├── util/
│   ├── ThemeManager.java
│   ├── MessageUtil.java
│   └── BaseFrame.java
└── view/
    ├── LoginView.java
    ├── ManagerMainView.java
    ├── InvestorRegistrationOptimizationView.java
    ├── ManagerInvestorHistoryView.java
    ├── InvestorDashboardView.java
    ├── AdminUserView.java
    ├── UserFormDialog.java
    ├── InvestorReadOnlyDashboardView.java
    ├── AssetSelectionPanel.java
    ├── UserTableModel.java
    ├── InvestorTableModel.java
    ├── AssetSelectionTableModel.java
    ├── PortfolioItemTableModel.java
    └── resources/
        ├── logo_sem_background_darkmode.png
        └── logo_sem_background_lightmode.png
```

---

## 1. LoginView (Portal de Acesso)

Primeira tela do sistema. Interface minimalista para autenticação de usuários internos (Admin, Gerente, Investidor).

- **Campos**: E-mail e Senha
- **Ação**: Botão "Login" conectado ao `LoginController.handleLogin()`
- **Tema**: Alternador de tema (Modo Claro / Modo Escuro)
- **Logo**: Dinâmica conforme o tema ativo
- **Tipo**: JFrame independente

---

## 2. ManagerMainView (Painel do Gerente)

Tela principal da jornada do gerente operacional. Funciona como contêiner dinâmico com sidebar de navegação e painel central que carrega sub-views.

- **Sidebar**: Logo, nome do gerente logado, botões de navegação (Novo Investidor, Histórico, Sair), alternador de tema
- **Painel central**: Exibe mensagem de boas-vindas ou carrega sub-views conforme navegação
- **Sub-views carregadas**: InvestorRegistrationOptimizationView, ManagerInvestorHistoryView, InvestorDashboardView
- **Tipo**: JFrame com sidebar + área dinâmica

---

## 3. InvestorRegistrationOptimizationView (Cadastro de Investidor)

Tela de cadastro de novo investidor com seleção de ativos e disparo do fluxo de otimização.

- **Formulário**: Nome Completo, Documento (CPF), Perfil de Risco (Conservador/Moderado/Agressivo)
- **Tabela de ativos**: Lista de 8 ativos mockados com checkbox para seleção múltipla
- **Ações**: Cadastrar e Otimizar, Limpar Formulário, Voltar
- **ComboBox estilizado**: Renderer customizado com cores do tema
- **Tipo**: JPanel (carregado dentro da ManagerMainView)

---

## 4. ManagerInvestorHistoryView (Histórico de Investidores)

Tela de consulta dos investidores cadastrados pelo gerente logado.

- **Tabela**: Nome, Documento, Perfil de Risco, Ativos Selecionados, Data de Cadastro
- **Ações**: Visualizar Carteira Otimizada, Atualizar
- **Seleção**: Linha única com destaque visual em azul
- **Dados mockados**: 5 investidores fictícios para demonstração
- **Tipo**: JPanel (carregado dentro da ManagerMainView)

---

## 5. InvestorDashboardView (Dashboard de Otimização)

Tela de resultado do portfólio otimizado, exibida após o gerente cadastrar e otimizar um investidor.

- **Cabeçalho**: Nome do investidor e perfil de risco
- **Cards de performance**: Retorno Esperado (verde) e Risco/Volatilidade (vermelho)
- **Gráfico Donut**: Visualização da alocação percentual com legenda vertical
- **Tabela**: Ticker, Nome do Ativo, Categoria, Alocação (%), Valor (R$)
- **Ações**: Voltar ao Menu, Novo Investidor
- **Tipo**: JPanel (carregado dentro da ManagerMainView)

---

## 6. AdminUserView (Painel do Administrador)

Tela principal da jornada do administrador para gestão de usuários do sistema.

- **Sidebar**: Logo, nome do admin, botões (Novo Usuário, Excluir Usuário, Atualizar Lista, Sair), alternador de tema
- **Cards de resumo**: Contagem por perfil (Administradores, Gerentes, Investidores)
- **Gráfico de barras**: Distribuição visual de usuários por perfil com percentuais
- **Tabela**: ID, Nome, Email, Perfil, Cód. Gerente, Ativo, Data de Criação
- **CRUD**: Cadastro via modal (UserFormDialog), exclusão com confirmação
- **Dados mockados**: 5 usuários fictícios para demonstração
- **Tipo**: JFrame com sidebar + área de conteúdo

---

## 7. UserFormDialog (Cadastro de Usuário)

Janela modal para cadastro de novos usuários, aberta a partir da AdminUserView.

- **Campos**: Nome, E-mail, Senha, Perfil (Administrador/Gerente/Investidor), Código Gerente, Status (ativo/inativo)
- **Regra visual**: Campo "Código Gerente" aparece apenas quando perfil = Gerente
- **ComboBox estilizado**: Renderer customizado com cores do tema
- **Ações**: Salvar (com validação de campos obrigatórios), Cancelar
- **Tipo**: JDialog modal

---

## 8. InvestorReadOnlyDashboardView (Painel do Investidor)

Tela principal da jornada do investidor após login. Exibe o portfólio otimizado em modo somente leitura.

- **Sidebar**: Logo, nome do investidor, botões (Meu Portfólio destacado, Sair), alternador de tema
- **Cabeçalho**: "Meu Portfólio" e perfil de risco
- **Cards de performance**: Retorno Esperado (verde) e Risco (vermelho)
- **Gráfico Donut**: Alocação percentual com legenda vertical completa
- **Tabela**: Ticker, Nome do Ativo, Categoria, Alocação (%), Valor (R$)
- **Restrição**: Nenhum botão de edição/otimização — apenas visualização
- **Dados mockados**: Perfil conservador com 5 ativos
- **Tipo**: JFrame com sidebar + área de conteúdo

---

## 9. AssetSelectionPanel (Painel Reutilizável)

Painel reutilizável para seleção de ativos financeiros, embutível em qualquer tela.

- **Tabela**: Checkbox de seleção, Ticker, Nome, Categoria, Risco Base
- **Ações rápidas**: Selecionar Todos, Limpar Seleção
- **Métodos**: `loadAssets()`, `getSelectedAssets()`, `clearSelection()`, `selectAll()`
- **Tipo**: JPanel (componente embutível)

---

## 10. Componentes Utilitários

### ThemeManager
Gerenciador centralizado de temas (Dark/Light) com métodos de estilização para ComboBox, JTable e JScrollPane.

- Cores: Background, Card, Text, SubText, Accent, Selection, Border, Hover
- Métodos: `estilizarComboBox()`, `estilizarTabela()`, `estilizarScrollPane()`
- Renderers customizados para seleção visível e linhas alternadas (zebra)

### MessageUtil
Classe utilitária para mensagens padronizadas via JOptionPane.

- `showSuccess(parent, message)` — mensagem de sucesso
- `showError(parent, message)` — mensagem de erro
- `confirm(parent, message)` — diálogo de confirmação Sim/Não

### BaseFrame
Classe base abstrata para JFrames com configurações comuns.

- `configureFrame(title)` — título, tamanho mínimo, centralização
- `showError(message)` / `showSuccess(message)` — delegam para MessageUtil

### TableModels Customizados
Modelos de tabela tipados que encapsulam lógica de dados:

- **UserTableModel**: `addUser()`, `removeUser()`, `getUserId()`, `clear()`
- **InvestorTableModel**: `addInvestor()`, `getInvestorDocument()`, `clear()`
- **AssetSelectionTableModel**: `addAsset()`, `selectAll()`, `clearSelection()`, `getSelectedTickers()`
- **PortfolioItemTableModel**: `addItem()`, `getTicker()`, `clear()`

---

## Tecnologias e Padrões Aplicados

- **Framework**: Java Swing (NetBeans)
- **Arquitetura**: MVC — separação total da lógica de apresentação (View) das regras de negócio (Controller/Service)
- **Estilização**: ThemeManager centralizado com suporte a Dark Mode e Light Mode
- **Gráficos**: Renderização customizada via `Graphics2D` (donut chart, barras horizontais)
- **Tabelas**: Renderers customizados com linhas alternadas, seleção visível e header estilizado
- **ComboBox**: Renderer customizado para visibilidade em ambos os temas

---

## Resumo por Jornada

| Jornada | Telas |
|---------|-------|
| **Comum** | LoginView |
| **Administrador** | AdminUserView, UserFormDialog |
| **Gerente** | ManagerMainView, InvestorRegistrationOptimizationView, ManagerInvestorHistoryView, InvestorDashboardView |
| **Investidor** | InvestorReadOnlyDashboardView |

---

*Projeto acadêmico — Finance Team*

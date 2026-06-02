# Camada View — Finance Team

Documentação das telas e utilitários visuais do sistema.

---

## Estrutura de arquivos

```
src/view/src/
├── view/
│   ├── LoginView.java
│   ├── AdminUserView.java
│   ├── UserFormDialog.java
│   ├── ManagerMainView.java
│   ├── InvestorRegistrationOptimizationView.java
│   ├── InvestorDashboardView.java
│   ├── ManagerInvestorHistoryView.java
│   ├── InvestorReadOnlyDashboardView.java
│   └── resources/
│       ├── logo_sem_background_darkmode.png
│       └── logo_sem_background_lightmode.png
└── util/
    ├── ThemeManager.java
    ├── MessageUtil.java
    └── BaseFrame.java
```

---

## Telas por jornada

| Jornada | Telas |
|---------|-------|
| **Comum** | `LoginView` |
| **Administrador** | `AdminUserView`, `UserFormDialog` |
| **Gerente** | `ManagerMainView`, `InvestorRegistrationOptimizationView`, `InvestorDashboardView`, `ManagerInvestorHistoryView` |
| **Investidor** | `InvestorReadOnlyDashboardView` |

---

## 1. LoginView

Primeira tela do sistema. Autentica usuários internos (Admin, Gerente, Investidor).

- Campos: e-mail e senha
- Botão Login → `LoginController.handleLogin()`
- Alternador de tema (Dark/Light)
- Logo dinâmica conforme o tema ativo
- Tipo: `JFrame`

---

## 2. AdminUserView

Painel principal do Administrador. Gerencia os usuários do sistema.

- Sidebar: logo, nome do admin, botões (Novo Usuário, Excluir, Atualizar, Sair)
- Cards de resumo: contagem por perfil (Admins, Gerentes, Investidores)
- Gráfico de barras: distribuição visual por perfil
- Tabela: ID, Nome, Email, Perfil, Cód. Gerente, Ativo, Data de Criação
- CRUD: cadastro via `UserFormDialog`, exclusão com confirmação
- Tipo: `JFrame`

---

## 3. UserFormDialog

Modal de cadastro de usuário, aberto a partir da `AdminUserView`.

- Campos: Nome, E-mail, Senha, Perfil, Código do Gerente, Status (ativo/inativo)
- Campo "Código Gerente" aparece somente quando perfil = GERENTE
- Botões: Salvar, Cancelar
- Tipo: `JDialog` modal

---

## 4. ManagerMainView

Painel principal do Gerente. Funciona como contêiner com sidebar e área central dinâmica.

- Sidebar: logo, nome do gerente, botões (Novo Investidor, Histórico, Sair)
- Área central: exibe boas-vindas ou carrega sub-painéis conforme navegação
- Sub-painéis carregados: `InvestorRegistrationOptimizationView`, `ManagerInvestorHistoryView`, `InvestorDashboardView`
- Tipo: `JFrame`

---

## 5. InvestorRegistrationOptimizationView

Tela de cadastro de investidor com seleção de ativos e disparo da otimização.

- Formulário: Nome, CPF, Perfil de Risco (Conservador/Moderado/Agressivo)
- Tabela de ativos: lista do banco com checkbox para seleção múltipla
- Botões: Cadastrar e Otimizar, Limpar Formulário, Voltar
- Tipo: `JPanel` (carregado dentro da `ManagerMainView`)

---

## 6. InvestorDashboardView

Resultado do portfólio otimizado, exibido após cadastrar e otimizar um investidor.

- Cabeçalho: nome do investidor e perfil de risco
- Cards: Retorno Esperado (verde) e Risco/Volatilidade (vermelho)
- Gráfico Donut: alocação percentual com legenda vertical
- Tabela: Ticker, Nome do Ativo, Categoria, Alocação (%), Valor (R$)
- Botões: Voltar ao Menu, Novo Investidor
- Tipo: `JPanel` (carregado dentro da `ManagerMainView`)

---

## 7. ManagerInvestorHistoryView

Histórico dos investidores cadastrados pelo gerente logado.

- Tabela: Nome, CPF, Perfil de Risco, Data de Cadastro
- Botões: Visualizar Carteira Otimizada, Atualizar
- Tipo: `JPanel` (carregado dentro da `ManagerMainView`)

---

## 8. InvestorReadOnlyDashboardView

Painel do Investidor após login. Exibe a carteira otimizada em modo somente leitura.

- Sidebar: logo, nome do investidor, botões (Meu Portfólio, Sair)
- Cards: Retorno Esperado e Risco
- Gráfico Donut: alocação percentual
- Tabela: Ticker, Nome do Ativo, Categoria, Alocação (%), Valor (R$)
- Sem nenhum botão de edição ou otimização
- Tipo: `JFrame`

---

## Utilitários

### ThemeManager

Gerencia o tema Dark/Light de toda a aplicação.

- Cores: Background, Card, Text, SubText, Accent, Selection, Border, Hover
- Métodos de estilização: `estilizarComboBox()`, `estilizarTabela()`, `estilizarScrollPane()`
- Renderers customizados: linhas alternadas (zebra), seleção visível, header estilizado

### MessageUtil

Mensagens padronizadas via `JOptionPane`.

- `showSuccess(parent, message)` — caixa de sucesso
- `showError(parent, message)` — caixa de erro
- `showWarning(parent, message)` — caixa de aviso
- `confirm(parent, message)` — confirmação Sim/Não, retorna `boolean`

### BaseFrame

Classe base abstrata para `JFrame` com configurações comuns: título, tamanho mínimo, centralização na tela.

---

## Padrões aplicados

- **MVC estrito**: a View nunca acessa banco, não tem regras de negócio e não monta objetos de domínio. Ela apenas coleta dados (`get*`) e exibe resultados (`show*`, `load*`, `set*`).
- **Tema centralizado**: todas as cores e estilos vêm do `ThemeManager` — nenhum valor hardcoded de cor nas Views.
- **Gráficos customizados**: Donut chart e barras horizontais renderizados via `Graphics2D` diretamente no Swing, sem biblioteca externa.

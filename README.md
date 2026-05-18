# 🖥️ Documentação das Interfaces (Camada View) - V1

Este documento detalha as funcionalidades, implementações visuais e a arquitetura de navegação das telas do **Middleware de Otimização de Portfólio**. Toda a interface foi desenvolvida em Java Swing seguindo rigorosamente o padrão arquitetural MVC (Model-View-Controller) e os princípios de UI/UX modernos.

---

## 🧭 Mapa das Telas Implementadas

### 1. LoginView (Portal de Acesso)
A porta de entrada do sistema, projetada para autenticação segura de usuários internos.
* **Autenticação:** Interface preparada para o login de Administradores e Gerentes de ativos.
* **Componentes:**
  * Campo de texto para inserção de e-mail corporativo.
  * Campo de senha protegido (`JPasswordField`).
  * Botão de ação conectado ao `LoginController` para validação de credenciais.
* **Identidade Visual:** Design minimalista focado na redução de distrações visuais.

### 2. ManagerMainView (Contêiner Principal do Gerente)
A "casca" estrutural da jornada operacional do gerente. Esta tela evoluiu a partir da antiga DashboardView para atuar como um contêiner dinâmico e responsivo.
* **Área Central Dinâmica (`pnlContent`):** Utiliza ordenação por `BorderLayout` para ejetar e alternar sub-painéis (`JPanel`) em tempo real sem fechar a janela principal.
* **Sidebar (Menu Lateral Corporativo):**
  * Navegação centralizada entre os fluxos: **Novo Investidor**, **Histórico** e **Sair**.
  * Exibição em tempo real do nome do usuário logado.
  * Mantém a logo do projeto com redimensionamento suave (`SCALE_SMOOTH`).

### 3. InvestorRegistrationOptimizationView (Formulário de Cadastro e Ativos)
Painel embutido dedicado à captação de novos clientes e seleção de ativos para o modelo matemático de Markowitz.
* **Localização Linguística (PT-BR):** Combo box configurada com os perfis de risco em português (`CONSERVADOR`, `MODERADO`, `AGRESSIVO`) para melhor experiência do usuário, convertendo os termos internamente para inglês antes do envio ao Supabase.
* **Tabela de Ativos Selecionáveis:** Listagem de ativos estruturada via `DefaultTableModel` com suporte nativo a caixas de seleção (`Boolean.class` / Checkboxes) na primeira coluna.
* **Controles Operacionais:** Botões de retorno, limpeza de formulário e o botão principal "Cadastrar e Otimizar".

### 4. ManagerInvestorHistoryView (Histórico de Clientes)
Painel focado na leitura e gerenciamento dos investidores já armazenados na base de dados.
* **Tabela de Auditoria:** Apresenta de forma limpa o Nome, Documento (CPF/ID), Perfil de Risco e os Ativos atualmente selecionados por cada investidor.
* **Ação Estratégica:** Botão inferior "Visualizar Carteira Otimizada" que identifica a linha selecionada na tabela através do método `getSelectedInvestorDocument()` e comanda a abertura dos resultados.

### 5. InvestorDashboardView (Painel de Resultados Otimizados)
A entrega final de valor do sistema, exibindo as métricas calculadas pelo motor de alocação de ativos.
* **Cards de Performance Financeira:** Componentes customizados com cantos arredondados (`RoundRectangle2D`) e cores semânticas de alto contraste para indicar:
  * **Retorno Esperado da Carteira** (Destaque em Verde).
  * **Risco Estimado / Volatilidade** (Destaque em Vermelho).
* **Tabela de Alocação de Pesos:** Demonstra o Ticker, Categoria, Percentual exato de alocação (%) e o valor financeiro sugerido (R$) para cada ativo que compõe o portfólio ideal.

---

## 🔄 Arquitetura de Controle e Navegação (MVC)

O sistema opera com desacoplamento total por meio de circuitos de controladores injetados:

<img width="677" height="192" alt="image" src="https://github.com/user-attachments/assets/73d7b905-ef92-4cdf-b3e9-269c88cce286" />

* **`ManagerController`:** Controla a janela principal (`JFrame`) e gerencia qual painel central deve ser renderizado.
* **`InvestorController`:** Centraliza as regras visuais das sub-views, possuindo construtores sobrecarregados para coordenar o fluxo entre o cadastro, a listagem do histórico e a exibição do dashboard final de resultados.

---

## 🎨 Gestão de UI/UX (Acessibilidade e Temas)

O ecossistema visual é governado pela classe utilitária **`ThemeManager`**, permitindo alternância dinâmica em tempo real:
* **Modo Escuro (Dark Mode):** Cores escuras profundas de paleta corporativa com textos em alto contraste para reduzir a fadiga ocular.
* **Modo Claro (Light Mode):** Divisões visuais nítidas e contornos limpos para garantir visibilidade em ambientes muito iluminados.
* **Componentes Customizados:** Sobrescrita do método `paintComponent` com ativação de *Anti-Aliasing* para renderização de cartões e botões com cantos arredondados e suaves.

## 🛠️ Tecnologias Aplicadas

* **Ambiente de Desenvolvimento:** NetBeans IDE
* **Linguagem & Framework:** Java 25 / Java Swing (Desenvolvimento 100% via código/Handcoded para máxima performance e controle de layout).
* **Versionamento:** Git & GitHub (Branching estruturado).

---
*Projeto Acadêmico 




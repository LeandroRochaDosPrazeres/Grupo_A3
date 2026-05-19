# 🖥️ Documentação das Interfaces (Camada View)

[cite_start]Este documento detalha as funcionalidades e implementações visuais realizadas nas telas do **Middleware de Otimização de Portfólio**, seguindo a arquitetura MVC proposta para o projeto[cite: 30, 77, 107].

## 1. LoginView (Portal de Acesso)
[cite_start]A porta de entrada do sistema, projetada para ser simples e funcional para usuários internos[cite: 22, 399].

* [cite_start]**Autenticação**: Interface preparada para o login de Administradores e Gerentes de ativos[cite: 22, 79, 341].
* **Componentes de Interface**:
    * [cite_start]Campo de texto para inserção de **Email** (usuário)[cite: 402].
    * [cite_start]Campo de senha protegido (**JPasswordField**)[cite: 402].
    * [cite_start]Botão de ação (**btnLogin**) conectado ao respectivo Controller para validação[cite: 402, 408].
* [cite_start]**Identidade Visual**: Design minimalista com foco em usabilidade e redução de distrações visuais[cite: 74, 81].

## 2. DashboardView (Painel Administrativo)
[cite_start]A central de controle do sistema, onde o usuário visualiza dados consolidados e indicadores gerais[cite: 9, 63, 409].

* [cite_start]**Cards de Indicadores**: Exibição de métricas essenciais para a gestão do middleware[cite: 410, 411]:
    * [cite_start]**Usuários**: Quantidade total de perfis cadastrados no sistema[cite: 411].
    * [cite_start]**Investidores**: Total de clientes atendidos pelos gerentes[cite: 411].
    * [cite_start]**Portfólios**: Quantidade de carteiras de ativos registradas[cite: 411].
    * [cite_start]**Otimizações**: Histórico de execuções do motor de otimização de ativos[cite: 411].
* **Gráfico Analítico de Barras**:
    * [cite_start]Implementação de visualização gráfica para as métricas do sistema[cite: 25, 41, 47].
    * Inclusão de **eixos (X/Y)** e **linhas de grade** para facilitar a leitura técnica dos dados.
    * Legendas claras para identificação de cada categoria de indicador.
* **Sidebar (Menu Lateral)**:
    * [cite_start]Navegação centralizada entre as entidades do modelo: **Investidores**, **Ativos** e **Portfólios**[cite: 108, 413].
    * [cite_start]Organização hierárquica que mantém a logo do projeto sempre visível[cite: 131].
* **Gestão de UI/UX (Acessibilidade)**:
    * **Alternador de Tema**: Opção minimalista no canto inferior esquerdo para alternar entre planos de fundo.
    * **Modo Escuro (Dark Mode)**: Layout de alto contraste para ambientes de baixa luminosidade.
    * **Modo Claro (Light Mode)**: Divisões visuais nítidas e contornos definidos para garantir a separação clara entre componentes.

## 🛠️ Tecnologias e Padrões Aplicados
* [cite_start]**Framework**: Java Swing (NetBeans)[cite: 4, 76, 107].
* [cite_start]**Arquitetura**: Separação total da lógica de apresentação (View) das regras de negócio (Controller)[cite: 30, 31, 109, 111].
* **Estilização**: Uso de um gerenciador de temas centralizado para garantir consistência visual em todo o middleware.

---
*Projeto acadêmico 

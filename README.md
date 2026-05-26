<div align="center">

<h1>🚗 Sistema de Locação de Veículos</h1>

<p>Aplicação web completa para gestão e reserva de frotas de veículos, construída com Java Servlets, padrões GoF e arquitetura MVC.</p>

<br/>

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-005C84?style=for-the-badge&logo=mysql&logoColor=white)
![HTML5](https://img.shields.io/badge/HTML5-E34F26?style=for-the-badge&logo=html5&logoColor=white)
![CSS3](https://img.shields.io/badge/CSS3-1572B6?style=for-the-badge&logo=css3&logoColor=white)
![JavaScript](https://img.shields.io/badge/JavaScript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black)
![Apache Tomcat](https://img.shields.io/badge/Apache%20Tomcat-F8DC75?style=for-the-badge&logo=apachetomcat&logoColor=black)

[![GitHub stars](https://img.shields.io/github/stars/76Falcone/LocacaoVeiculos?style=social)](https://github.com/76Falcone/LocacaoVeiculos/stargazers)
[![GitHub forks](https://img.shields.io/github/forks/76Falcone/LocacaoVeiculos?style=social)](https://github.com/76Falcone/LocacaoVeiculos/network)

</div>

---

## 📋 Índice

- [Sobre o Projeto](#-sobre-o-projeto)
- [Funcionalidades](#-funcionalidades)
- [Arquitetura](#-arquitetura)
- [Design Patterns](#-design-patterns-gof)
- [Princípios SOLID](#-princípios-solid)
- [Tecnologias](#-tecnologias)
- [Pré-requisitos](#-pré-requisitos)
- [Como Executar](#-como-executar)
- [Estrutura do Projeto](#-estrutura-do-projeto)
- [Endpoints](#-endpoints)
- [Banco de Dados](#-banco-de-dados)
- [Segurança](#-segurança)
- [Colaboradores](#-colaboradores)

---

## 💡 Sobre o Projeto

O **LocacaoVeiculos** é um sistema web de gerenciamento de frotas que permite o controle completo do ciclo de vida de locações de veículos — desde o cadastro de veículos e usuários até a realização e acompanhamento de reservas com cálculo dinâmico de seguros.

O projeto foi desenvolvido como trabalho acadêmico com foco na aplicação prática de **Design Patterns GoF**, **princípios SOLID** e **boas práticas de engenharia de software**.

---

## ✨ Funcionalidades

| Módulo | Funcionalidades |
|---|---|
| 🚙 **Veículos** | Cadastrar, listar, editar, visualizar e remover veículos da frota |
| 👤 **Usuários** | Cadastrar, editar e gerenciar clientes do sistema |
| 📋 **Locações** | Realizar reservas com cálculo automático de valor total e seguros |
| 🔐 **Autenticação** | Login e logout com controle de sessão |
| 🛡️ **Seguros** | Adicionar Seguro Terceiros e/ou Cobertura Total dinamicamente na reserva |
| 📊 **Listagens** | Visualizar frota disponível, reservas ativas e histórico de usuários |

---

## 🏛️ Arquitetura

O sistema segue o padrão **MVC (Model-View-Controller)** com a camada de Commands desacoplando os Controllers das regras de negócio:

```
┌─────────────────────────────────────────────────────────┐
│                      CLIENTE (Browser)                  │
│              HTML5 + CSS3 + JavaScript                  │
└────────────────────────┬────────────────────────────────┘
                         │ HTTP Request
                         ▼
┌─────────────────────────────────────────────────────────┐
│                  CONTROLLER (Servlet)                   │
│         ControleVeiculo / ControleUsuario /             │
│                   ControleLocacao                       │
│          Despacha para → IComando via HashMap           │
└────────────────────────┬────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────┐
│               COMMAND LAYER (Regras de Negócio)         │
│   CadastrarVeiculoComando, ListarLocacoesComando, ...   │
│           (aplica Decorator, usa Builder)               │
└────────────────────────┬────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────┐
│                 DAO LAYER (Persistência)                 │
│        DAOFactory → IVeiculoDAO / IUsuarioDAO /         │
│              ILocacaoDAO → Implementações               │
└────────────────────────┬────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────┐
│                  BANCO DE DADOS (MySQL)                  │
│              FabricaConexao (JDBC Driver)                │
└─────────────────────────────────────────────────────────┘
```

---

## 🎨 Design Patterns (GoF)

### 🎀 1. Decorator
> **Pacote:** `model.decorator`

Permite adicionar comportamentos (seguros) a uma locação de forma dinâmica e composível, sem alterar a classe original.

```
«interface»
ItemLocacao
 ├── LocacaoBase          (Concrete Component)
 └── SeguroDecorator      (Decorator Abstrato)
      ├── SeguroTerceiros           (+R$ 40/dia)
      └── SeguroCoberturaTotal      (+R$ 80/dia)
```

**Uso prático em `CadastrarLocacaoComando`:**
```java
ItemLocacao item = new LocacaoBase(locacao);

if (request.getParameter("seguroTerceiros") != null)
    item = new SeguroTerceiros(item, qtdDias);

if (request.getParameter("seguroCoberturaTotal") != null)
    item = new SeguroCoberturaTotal(item, qtdDias);

locacao.setValorTotal(item.getValorTotal());
```

---

### ⚡ 2. Command
> **Pacote:** `command`

Cada operação de negócio é encapsulada em uma classe concreta que implementa `IComando`. Os Controllers atuam como *Invokers*, despachando os comandos via `HashMap<String, IComando>`.

| Domínio | Commands disponíveis |
|---|---|
| Veículo | `Cadastrar`, `Atualizar`, `Editar`, `Deletar`, `Listar`, `BuscarPorId` |
| Usuário | `Cadastrar`, `Atualizar`, `Deletar`, `Listar`, `BuscarPorId`, `Login`, `Logout` |
| Locação | `Cadastrar`, `Atualizar`, `Deletar`, `Listar`, `BuscarPorId` |

---

### 🏭 3. Factory Method
> **Arquivo:** `dao/DAOFactory.java`

Centraliza e desacopla a criação de todos os objetos DAO, retornando sempre pelas interfaces.

```java
IVeiculoDAO dao = DAOFactory.getVeiculoDAO();  // retorna VeiculoDAO
ILocacaoDAO dao = DAOFactory.getLocacaoDAO();  // retorna LocacaoDAO
```

---

### 🔨 4. Builder
> **Pacote:** `model`

Facilita a construção de objetos complexos com muitos atributos opcionais, com sintaxe fluente.

```java
Veiculo veiculo = new VeiculoBuilder()
    .comModeloVeiculo("Civic")
    .comPlacaVeiculo("ABC1D23")
    .comValorDiaria(180.00)
    .comDisponibilidade(true)
    .build();
```

---

## 🏛️ Princípios SOLID

| Princípio | Aplicação no Projeto |
|---|---|
| **S** — Single Responsibility | Cada Command executa uma única operação; DAOs só persistem dados |
| **O** — Open/Closed | Novos tipos de seguro = novo Decorator, sem alterar código existente |
| **L** — Liskov Substitution | `SeguroTerceiros` e `SeguroCoberturaTotal` substituem `ItemLocacao` sem quebrar comportamento |
| **I** — Interface Segregation | `IVeiculoDAO`, `IUsuarioDAO`, `ILocacaoDAO` e `ILoginDAO` são interfaces separadas e focadas |
| **D** — Dependency Inversion | Controllers e Commands dependem de interfaces DAO, não de implementações concretas |

---

## 🛠️ Tecnologias

| Camada | Tecnologia | Versão |
|---|---|---|
| Linguagem | Java | 17+ |
| Servidor de Aplicação | Apache Tomcat | 10+ |
| Banco de Dados | MySQL | 8.0+ |
| Frontend | HTML5 / CSS3 / JavaScript | — |
| Persistência | JDBC (mysql-connector-j) | 8.x |
| Build | Apache Ant / NetBeans | — |

---

## 📦 Pré-requisitos

Antes de começar, certifique-se de ter instalado:

- [Java JDK 17+](https://www.oracle.com/java/technologies/downloads/)
- [Apache Tomcat 10+](https://tomcat.apache.org/download-10.cgi)
- [MySQL Server 8.0+](https://dev.mysql.com/downloads/mysql/)
- [MySQL Connector/J](https://dev.mysql.com/downloads/connector/j/) (driver JDBC)
- IDE recomendada: [Apache NetBeans](https://netbeans.apache.org/) ou [IntelliJ IDEA](https://www.jetbrains.com/idea/)

---

## 🚀 Como Executar

### 1. Clone o repositório
```bash
git clone https://github.com/76Falcone/LocacaoVeiculos.git
cd LocacaoVeiculos
```

### 2. Configure o Banco de Dados

Crie o banco de dados e as tabelas executando o script SQL:

```sql
CREATE DATABASE projLocacao CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE projLocacao;

CREATE TABLE usuario (
    id         INT AUTO_INCREMENT PRIMARY KEY,
    nome       VARCHAR(100) NOT NULL,
    email      VARCHAR(100) NOT NULL UNIQUE,
    cpf        VARCHAR(14)  NOT NULL UNIQUE,
    telefone   VARCHAR(20),
    senha      VARCHAR(255) NOT NULL
);

CREATE TABLE veiculo (
    id                   INT AUTO_INCREMENT PRIMARY KEY,
    placa                VARCHAR(10)  NOT NULL UNIQUE,
    modelo               VARCHAR(80)  NOT NULL,
    cor                  VARCHAR(40),
    valorDiaria          DECIMAL(10,2) NOT NULL,
    funcionalidade       VARCHAR(100),
    disponibilidade      BOOLEAN DEFAULT TRUE,
    arCondicionado       BOOLEAN DEFAULT FALSE,
    tipoCambio           VARCHAR(20)
);

CREATE TABLE locacao (
    id             INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario     INT NOT NULL,
    id_veiculo     INT NOT NULL,
    qtdDias        INT NOT NULL,
    seguro         DECIMAL(10,2) DEFAULT 0.00,
    localRetirada  VARCHAR(150),
    valorTotal     DECIMAL(10,2) NOT NULL,
    data_retirada  DATE NOT NULL,
    data_entrega   DATE,
    FOREIGN KEY (id_usuario) REFERENCES usuario(id),
    FOREIGN KEY (id_veiculo) REFERENCES veiculo(id)
);
```

### 3. Configure a Conexão

Edite o arquivo `src/java/util/FabricaConexao.java` com suas credenciais locais:

```java
String URL      = "jdbc:mysql://localhost:3306/projLocacao";
String USERNAME = "seu_usuario";
String PASSWORD = "sua_senha";
```

> ⚠️ **Nunca suba suas credenciais reais para o repositório.** O arquivo `ModeloConexao.java` serve como template seguro para versionamento.

### 4. Adicione o Driver JDBC

Coloque o arquivo `mysql-connector-j-x.x.x.jar` na pasta `web/WEB-INF/lib/`.

### 5. Execute no Tomcat

- Abra o projeto na sua IDE
- Configure um servidor Apache Tomcat 10+
- Execute o projeto (`Run` ou `Deploy`)
- Acesse: **`http://localhost:8080/LocacaoVeiculos/`**

---

## 📁 Estrutura do Projeto

```
LocacaoVeiculos/
│
├── src/java/
│   ├── command/                    # Padrão Command (GoF)
│   │   ├── IComando.java           # Interface do Command
│   │   ├── locacao/                # Commands de Locação (CRUD + extras)
│   │   ├── usuario/                # Commands de Usuário (CRUD + Login/Logout)
│   │   └── veiculo/                # Commands de Veículo (CRUD + Editar)
│   │
│   ├── controller/                 # Servlets (Invokers do padrão Command)
│   │   ├── ControleLocacao.java
│   │   ├── ControleUsuario.java
│   │   └── ControleVeiculo.java
│   │
│   ├── dao/                        # Data Access Objects
│   │   ├── DAOFactory.java         # Padrão Factory Method (GoF)
│   │   ├── ILocacaoDAO.java        # Interface (princípio SOLID - I e D)
│   │   ├── IUsuarioDAO.java
│   │   ├── IVeiculoDAO.java
│   │   ├── ILoginDAO.java
│   │   ├── LocacaoDAO.java
│   │   ├── UsuarioDAO.java
│   │   ├── VeiculoDAO.java
│   │   └── LoginDAO.java
│   │
│   ├── model/                      # Entidades de domínio
│   │   ├── Locacao.java
│   │   ├── LocacaoBuilder.java     # Padrão Builder (GoF)
│   │   ├── Usuario.java
│   │   ├── UsuarioBuilder.java     # Padrão Builder (GoF)
│   │   ├── Veiculo.java
│   │   ├── VeiculoBuilder.java     # Padrão Builder (GoF)
│   │   └── decorator/              # Padrão Decorator (GoF) ★
│   │       ├── ItemLocacao.java    # Interface Component
│   │       ├── LocacaoBase.java    # Concrete Component
│   │       ├── SeguroDecorator.java       # Decorator Abstrato
│   │       ├── SeguroTerceiros.java       # Concrete Decorator
│   │       └── SeguroCoberturaTotal.java  # Concrete Decorator
│   │
│   └── util/                       # Utilitários
│       ├── FabricaConexao.java     # Fábrica de conexão JDBC
│       └── ModeloConexao.java      # Template seguro (sem credenciais)
│
└── web/                            # Frontend
    ├── index.html                  # Página inicial / Login
    ├── html/                       # Páginas do sistema
    │   ├── frota.html              # Catálogo de veículos disponíveis
    │   ├── reserva.html            # Formulário de reserva
    │   ├── listarReservas.html     # Listagem de locações
    │   ├── listarVeiculos.html     # Painel de veículos (admin)
    │   ├── listarUsuarios.html     # Painel de usuários (admin)
    │   ├── cadastroVeiculo.html    # Cadastro de veículo
    │   ├── cadastroUsuario.html    # Cadastro de usuário
    │   ├── editarVeiculo.html      # Edição de veículo
    │   ├── editarReserva.html      # Edição de reserva
    │   └── login.html              # Página de login
    ├── css/                        # Folhas de estilo
    └── js/                         # Scripts JavaScript
```

---

## 🔌 Endpoints

Todos os endpoints são acessados via parâmetro `?op=OPERACAO`.

### Veículos — `/ControleVeiculo`

| Operação (`op`) | Método | Descrição |
|---|---|---|
| `LISTAR` | GET | Lista todos os veículos |
| `BUSCAR_POR_ID` | GET | Busca veículo por ID |
| `CADASTRAR` | POST | Cadastra novo veículo |
| `EDITAR` | GET | Carrega formulário de edição |
| `ATUALIZAR` | POST | Salva alterações do veículo |
| `DELETAR` | POST | Remove veículo |

### Usuários — `/ControleUsuario`

| Operação (`op`) | Método | Descrição |
|---|---|---|
| `LISTAR` | GET | Lista todos os usuários |
| `BUSCAR_POR_ID` | GET | Busca usuário por ID |
| `CADASTRAR` | POST | Cadastra novo usuário |
| `ATUALIZAR` | POST | Atualiza dados do usuário |
| `DELETAR` | POST | Remove usuário |
| `LOGIN` | POST | Autentica usuário na sessão |
| `LOGOUT` | GET | Encerra a sessão |

### Locações — `/ControleLocacao`

| Operação (`op`) | Método | Descrição |
|---|---|---|
| `LISTAR` | GET | Lista todas as locações |
| `BUSCAR_POR_ID` | GET | Busca locação por ID |
| `CADASTRAR` | POST | Cria nova locação (aplica Decorators de seguro) |
| `ATUALIZAR` | POST | Atualiza locação existente |
| `DELETAR` | POST | Cancela locação |

---

## 🗄️ Banco de Dados

O sistema utiliza **MySQL** com relacionamentos via chaves estrangeiras:

```
┌──────────┐         ┌──────────┐         ┌──────────┐
│ usuario  │ 1     N │  locacao │ N     1 │ veiculo  │
│──────────│─────────│──────────│─────────│──────────│
│ id (PK)  │         │ id (PK)  │         │ id (PK)  │
│ nome     │         │ id_usu.  │         │ placa    │
│ email    │         │ id_veic. │         │ modelo   │
│ cpf      │         │ qtdDias  │         │ cor      │
│ telefone │         │ seguro   │         │ diaria   │
│ senha    │         │ valorTot.│         │ disponib.│
└──────────┘         │ dt_retir.│         └──────────┘
                     │ dt_entre.│
                     └──────────┘
```

---

## 🔒 Segurança

- **Credenciais protegidas:** As credenciais do banco de dados **nunca** são versionadas. Use `ModeloConexao.java` como template local.
- **Sessão de usuário:** O controle de autenticação é gerenciado via `HttpSession` nos Commands de Login/Logout.
- **Prepared Statements:** Todas as queries utilizam `PreparedStatement` para prevenção de **SQL Injection**.

---

## 👥 Colaboradores

<table>
  <tr>
    <td align="center">
      <a href="https://github.com/76Falcone">
        <img src="https://github.com/76Falcone.png" width="80px;" alt="Nicolas Falcone"/><br/>
        <b>Nicolas Falcone</b>
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/76ersGust">
        <img src="https://github.com/76ersGust.png" width="80px;" alt="Gustavo Palmeira"/><br/>
        <b>Gustavo Palmeira</b>
      </a>
    </td>
  </tr>
</table>

---

<div align="center">
  <sub>Desenvolvido com ☕ Java e dedicação — © 2026 LocacaoVeiculos</sub>
</div>

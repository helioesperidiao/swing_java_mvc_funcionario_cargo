# 🧑‍💼 Sistema de Gestão de RH (Java + MySQL + MVC)

![Java](https://img.shields.io/badge/Java-17-blue?logo=java)
![MySQL](https://img.shields.io/badge/Database-MySQL-orange?logo=mysql)
![Swing](https://img.shields.io/badge/UI-Java%20Swing-lightgrey)
![License](https://img.shields.io/badge/License-MIT-green)
![Status](https://img.shields.io/badge/Status-Em%20Desenvolvimento-yellow)

> 💼 Aplicação desktop desenvolvida em **Java Swing**, utilizando o padrão **MVC (Model-View-Controller)**  
> com camadas **DAO** e **Service** conectadas a um **banco de dados MySQL**.

---

## 🧭 Sumário

- [📖 Visão Geral](#-visão-geral)
- [🎯 Funcionalidades](#-funcionalidades)
- [🏗️ Arquitetura do Projeto](#️-arquitetura-do-projeto)
- [🧱 Estrutura de Pastas](#-estrutura-de-pastas)
- [⚙️ Tecnologias Utilizadas](#️-tecnologias-utilizadas)
- [💾 Configuração do Banco de Dados](#-configuração-do-banco-de-dados)
- [🚀 Como Executar](#-como-executar)
- [🔐 Login e Autenticação](#-login-e-autenticação)
- [📚 Convenções de Código e Commits](#-convenções-de-código-e-commits)
- [🧠 Notas de Aula e Documentação](#-notas-de-aula-e-documentação)
- [👨‍💻 Autor](#-autor)
- [🪪 Licença](#-licença)

---

## 📖 Visão Geral

O **Sistema de Gestão de RH** permite o **cadastro, listagem e autenticação de funcionários** e **gerenciamento de cargos**.  
É uma aplicação desktop educativa, projetada para demonstrar o uso prático do padrão **MVC com camadas DAO e Service** em **Java Swing**.  

🎯 **Objetivos principais:**
- Aplicar boas práticas de **arquitetura em camadas (MVC + DAO + Service)**  
- Utilizar **MySQL** como persistência de dados  
- Garantir **validação de regras de domínio** no `model`  
- Implementar **autenticação segura** com **BCrypt**  
- Documentar com **JavaDoc e emojis pedagógicos** 🧠  

---

## 🎯 Funcionalidades

### 👥 Módulo de Funcionários
- ✅ Cadastro de funcionários
- ✅ Listagem de funcionários  
- ✅ Edição de dados
- ✅ Exclusão de registros

### 💼 Módulo de Cargos
- ✅ Cadastro de cargos
- ✅ Vinculação funcionário-cargo

### 🔐 Sistema de Autenticação
- ✅ Login seguro com BCrypt
- ✅ Controle de acesso básico

---

## 🏗️ Arquitetura do Projeto

+-----------------------+
| VIEW (Swing) | ← Telas e componentes
+-----------------------+
↓
+-----------------------+
| CONTROLLER | ← Recebe eventos da View
+-----------------------+
↓
+-----------------------+
| SERVICE | ← Regras de negócio e validações
+-----------------------+
↓
+-----------------------+
| DAO | ← Operações de banco de dados
+-----------------------+
↓
+-----------------------+
| DATABASE (MySQL) | ← Persistência dos dados
+-----------------------+


📘 **Padrões aplicados:**
- **MVC:** separação entre interface, controle e modelo  
- **DAO (Data Access Object):** abstração da camada de persistência  
- **Service Layer:** centralização das regras de negócio  
- **Dependency Injection:** passagem de DAO → Service → Controller  

---

## 🧱 Estrutura de Pastas

📦 gestao_rh
┣ 📂 src
┃ ┣ 📂 control # Controllers (ex: FuncionarioControl)
┃ ┣ 📂 dao # Camada de acesso a dados (MySQL)
┃ ┣ 📂 database # Configuração de conexão JDBC
┃ ┣ 📂 forms # Telas Java Swing (Login, MainForm)
┃ ┣ 📂 model # Entidades (Funcionario, Cargo)
┃ ┗ 📂 service # Camada de regras de negócio
┗ 📜 README.md



---

## ⚙️ Tecnologias Utilizadas

| Tipo | Tecnologia |
|------|-------------|
| ☕ Linguagem | Java SE 17+ |
| 💻 Interface | Java Swing |
| 🧠 Arquitetura | MVC + DAO + Service |
| 🗄️ Banco de Dados | MySQL 8+ |
| 🔒 Segurança | BCrypt para hash de senhas |
| 🧰 Dependências | `mysql-connector-j.jar`, `jBCrypt` |

---

## 💾 Configuração do Banco de Dados

### 📍 Requisitos:
- MySQL 8 ou superior  
- Usuário: `root`  
- Senha: *(vazia ou conforme seu ambiente)*  
- Banco: `gestao_rh`

### 🧩 Script SQL:

```sql
DROP SCHEMA IF EXISTS `gestao_rh`;

CREATE SCHEMA IF NOT EXISTS `gestao_rh` DEFAULT CHARACTER SET utf8;
USE `gestao_rh`;

DROP TABLE IF EXISTS `Funcionario`;
DROP TABLE IF EXISTS `Cargo`;

CREATE TABLE IF NOT EXISTS `Cargo` (
  `idCargo` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `nomeCargo` VARCHAR(64) NOT NULL,
  PRIMARY KEY (`idCargo`),
  UNIQUE INDEX `idCargo_UNIQUE` (`idCargo` ASC),
  UNIQUE INDEX `nomeCargo_UNIQUE` (`nomeCargo` ASC)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `Funcionario` (
  `idFuncionario` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `nomeFuncionario` VARCHAR(128) NULL,
  `email` VARCHAR(64) NULL,
  `senha` VARCHAR(64) NULL,
  `recebeValeTransporte` TINYINT(1) NULL,
  `Cargo_idCargo` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`idFuncionario`),
  UNIQUE INDEX `email_UNIQUE` (`email` ASC),
  INDEX `fk_Funcionario_Cargo_idx` (`Cargo_idCargo` ASC),
  CONSTRAINT `fk_Funcionario_Cargo`
    FOREIGN KEY (`Cargo_idCargo`)
    REFERENCES `Cargo` (`idCargo`)
) ENGINE = InnoDB;

INSERT INTO `Cargo` (`idCargo`, `nomeCargo`) VALUES 
(1, 'Administrador'),
(2, 'Técnico em Informática Jr'),
(3, 'Técnico em Informática Pleno'),
(4, 'Analista de Sistemas Jr');

INSERT INTO `Funcionario` (`nomeFuncionario`, `email`, `senha`, `recebeValeTransporte`, `Cargo_idCargo`) 
VALUES 
('adm', 'adm@adm.com', '$2a$12$axuJefWt3TtXuHc4jzFfVeb4irESaE6Y5K297MQluljqbM0GCgeI.', 1, 1),
('Hélio', 'helioesperidiao@gmail.com', '$2a$12$axuJefWt3TtXuHc4jzFfVeb4irESaE6Y5K297MQluljqbM0GCgeI.', 1, 1);


🚀 Como Executar


Clone o repositório
git clone https://github.com/helioesperidiao/swing_java_mvc_funcionario_cargo



Abra o projeto em sua IDE Java
(IntelliJ, Eclipse, NetBeans ou VS Code)


Adicione as dependências:


mysql-connector-j-8.x.jar


jBCrypt-x.x.jar




Compile e execute


Classe principal: forms.LoginForm.java




Faça login

"usuario": "helioesperidiao@gmail.com"
"senha": "@Helio123456"

Informe um e-mail e senha cadastrados no banco


Após autenticação, o sistema abrirá a tela principal (MainForm)





🔐 Login e Autenticação
O sistema utiliza BCrypt para validação segura de senha 🔒:
if (BCrypt.checkpw(senhaDigitada, funcionario.getSenha())) {
    // ✅ Login bem-sucedido
} else {
    // ❌ Senha incorreta
}

💡 Para gerar uma senha com hash:
String hash = BCrypt.hashpw("minhasenha", BCrypt.gensalt());
System.out.println(hash);


📚 Convenções de Código e Commits
🧱 Padrão de Commits (Conventional Commits)
TipoDescriçãodocs:Atualizações na documentação (JavaDoc, README, etc.)feat:Nova funcionalidadefix:Correção de bugsrefactor:Refatoração de código sem alterar lógicastyle:Alterações visuais e formataçãochore:Tarefas de manutenção e build
📘 Exemplo:
docs(service): adicionar JavaDoc com emojis pedagógicos
feat(control): implementar método de login com validação BCrypt


🧠 Notas de Aula e Documentação
📖 Cada classe possui comentários JavaDoc e emojis explicativos, seguindo uma linha didática:


💡 Conceito teórico (MVC, DAO, Service)


⚙️ Processo técnico (conexão, lógica, eventos)


🧩 Responsabilidade da camada


🔒 Regra de domínio ou segurança


🚀 Método principal de execução


Essas notas foram criadas para uso acadêmico e demonstração de boas práticas de engenharia de software.

👨‍💻 Autor
Nome: Hélio Lourenço Esperidião Ferreira
📧 Email: helioesperidiao@gmail.com
💻 GitHub: @helioesperidiao
🔗 LinkedIn: linkedin.com/in/helioesperidiao

🪪 Licença
Este projeto está licenciado sob a licença MIT — sinta-se livre para usar, estudar e modificar.
Veja o arquivo LICENSE para mais detalhes.

---

Pronto 🎉  
Esse conteúdo está **completo, formatado e validado para GitHub** — é só copiar e colar no arquivo `README.md` da raiz do seu repositório.

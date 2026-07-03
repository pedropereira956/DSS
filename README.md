# Projeto DSS - Grupo 10

Aplicação Java para gestão integrada de restaurantes.

---

## 🗂️ Estrutura do Repositório

* **`/dss_project/src/main/java`**: Código-fonte estruturado em camadas (Interface Visual, Lógica de Negócio e Dados).
* **`/dss_project/src/db.sql`**: Script SQL para a criação e povoamento inicial da base de dados.
* **`/dss_project/build.gradle`**: Ficheiro de configuração do Gradle para gestão do projeto.
* **Casos de Uso e Diagramas**: Ficheiros CSV com os casos de uso, documentação em PDF e diagramas do Visual Paradigm (`.vpp`).

---

## 🚀 Como Executar o Programa

Este projeto é gerido com o **Gradle**. Antes de correres a aplicação, certifica-te de que importaste o ficheiro `db.sql` para o teu servidor de base de dados local (ex: MySQL/MariaDB).

Abre o teu terminal, navega até à pasta **`dss_project`** (onde estão os ficheiros do Gradle) e segue os passos:

### 1️⃣ Compilar o código
Utiliza o executável do Gradle incluído no projeto para compilar tudo:
* **No Windows:**
  ```cmd
  gradlew.bat build

    No Mac/Linux:
    Bash

    ./gradlew build

2️⃣ Iniciar a aplicação

Após a compilação terminar com sucesso, executa a classe principal da aplicação:
Bash

java -cp build/classes/java/main Main

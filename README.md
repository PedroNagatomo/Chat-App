# Chat-App — Aplicação de Chat em Tempo Real

[![Java](https://img.shields.io/badge/Java-17+-orange)](https://www.oracle.com/java/)
[![JavaFX](https://img.shields.io/badge/JavaFX-17+-blue)](https://openjfx.io/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.0+-green)](https://spring.io/projects/spring-boot)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

Uma aplicação de chat moderna composta por um backend em Spring Boot e um frontend em JavaFX. Comunicação em tempo real via WebSocket, autenticação por JWT e histórico de mensagens persistente.

---

## Índice
- [Funcionalidades](#funcionalidades)
- [Arquitetura do Projeto](#arquitetura-do-projeto)
- [Pré-requisitos](#pré-requisitos)
- [Instalação e execução](#instalação-e-execução)
  - [Clonar o repositório](#clonar-o-repositório)
  - [Executar o backend (Spring Boot)](#executar-o-backend-spring-boot)
  - [Executar o frontend (JavaFX)](#executar-o-frontend-javafx)
- [Endpoints da API & WebSocket](#endpoints-da-api--websocket)
- [Configurações importantes](#configurações-importantes)
- [Estrutura de pastas](#estrutura-de-pastas)
- [Desenvolvimento e testes](#desenvolvimento-e-testes)
- [Contribuição](#contribuição)
- [Licença](#licença)
- [Contato](#contato)

---

## Funcionalidades
- Autenticação de usuários com JWT (login / registro)
- Perfis de usuário personalizáveis (nome, status, biografia, avatar colorido)
- Status online/ausente/ocupado
- Salas de chat públicas e privadas
- Mensagens em tempo real via WebSocket
- Histórico de mensagens persistido (H2 por padrão; suporta MySQL/Postgres)
- Interface moderna em JavaFX com CSS customizado e animações suaves

---

## Arquitetura do projeto
O projeto está dividido em dois módulos independentes:

- `backend/` — API REST em Spring Boot (porta padrão: 8080)
- `frontend/` — Aplicação JavaFX (cliente gráfico)

Comunicação:
- API REST para autenticação, usuários, salas e histórico
- WebSocket para mensagens em tempo real

---

## Pré-requisitos
- Java JDK 17 ou superior
- Maven 3.6+
- Git
- (Opcional) JavaFX SDK se executar a partir de um JAR sem empacotar dependências

Links úteis:
- Java: [https://www.oracle.com/java/](https://www.oracle.com/java/)
- JavaFX: [https://openjfx.io/](https://openjfx.io/)
- Maven: [https://maven.apache.org/](https://maven.apache.org/)

---

## Instalação e execução

### 1. Clonar o repositório
```bash
git clone https://github.com/PedroNagatomo/ChatApplication.git
cd ChatApplication
```

### 2. Executar o backend (Spring Boot)
Navegue até a pasta do backend:
```bash
cd backend
```

Configuração (opcional):
- Por padrão o projeto usa H2 em memória.
- Para trocar para MySQL/Postgres, atualize `src/main/resources/application.properties` ou use variáveis de ambiente.

Exemplo rápido (modo desenvolvimento):
```bash
# Com Maven (execução direta)
mvn clean spring-boot:run

# Ou empacotar e executar o JAR
mvn clean package
java -jar target/chatfx-backend-1.0.0.jar
```

Verificações:
- API: [http://localhost:8080/api](http://localhost:8080/api)
- Console H2 (se habilitado): [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
- WebSocket: `ws://localhost:8080/chat`

### 3. Executar o frontend (JavaFX)
Abra um novo terminal e navegue até o diretório do frontend:
```bash
cd frontend
```

Executando com Maven (recomendado para desenvolvimento):
```bash
mvn clean compile
mvn javafx:run
```

Executando como JAR (dependendo de como o JAR é empacotado, pode ser necessário fornecer o module-path do JavaFX):
```bash
mvn clean package
java --module-path "caminho/para/javafx-sdk/lib" \
     --add-modules javafx.controls,javafx.fxml \
     -jar target/chatfx-frontend-1.0.0.jar
```

Dica: Para desenvolvimento use sua IDE (IntelliJ, Eclipse, VS Code). Importe como Maven project e execute a classe principal (`MainApp`).

---

## Endpoints da API & WebSocket

Autenticação
- POST /api/auth/login — Login de usuário
- POST /api/auth/register — Registro de novo usuário

Usuários
- GET /api/users/me — Retorna usuário atual
- GET /api/users/online — Lista usuários online
- PUT /api/users/update — Atualiza perfil

Salas
- GET /api/rooms — Lista todas as salas
- POST /api/rooms/create — Cria nova sala

Mensagens
- GET /api/messages/room/{id} — Histórico da sala

WebSocket
- ws://localhost:8080/chat — Endpoint para mensagens em tempo real

(Confirme os caminhos no código caso tenham sido alterados.)

---

## Configurações importantes

Exemplo simplificado de `application.properties` (backend):
```properties
server.port=8080

# H2 (padrão)
spring.datasource.url=jdbc:h2:mem:chatdb
spring.datasource.username=sa
spring.datasource.password=
spring.datasource.driverClassName=org.h2.Driver
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# JWT (substitua por um segredo forte)
app.jwt.secret=troque-este-segredo
app.jwt.expiration-ms=86400000
```

Variáveis de ambiente recomendadas para produção:
- SPRING_DATASOURCE_URL
- SPRING_DATASOURCE_USERNAME
- SPRING_DATASOURCE_PASSWORD
- APP_JWT_SECRET

---

## Estrutura de pastas (resumida)

backend/
- src/main/java/com/backend-chat/
  - config/ — Configurações (Security, WebSocket)
  - controller/ — Controladores REST
  - model/ — Entidades (User, Message, Room)
  - repository/ — Repositórios JPA
  - service/ — Lógica de negócio
  - security/ — Configurações de segurança

frontend/
- src/main/java/com/chat-client/
  - controller/ — Controladores FXML
  - model/ — Modelos de dados
  - service/ — Serviços (API, WebSocket)
  - views/ — FXML
  - styles.css — Estilos

---

## Desenvolvimento e testes
- Recomenda-se executar backend e frontend em terminais separados.
- Para testes unitários e de integração, use os comandos Maven:
```bash
# Executar testes no backend
cd backend
mvn test

# Executar testes no frontend (se aplicável)
cd frontend
mvn test
```

---

## Contribuição
Contribuições são bem-vindas! Para colaborar:
1. Fork o repositório
2. Crie uma branch feature: `git checkout -b feature/minha-feature`
3. Faça commits claros e pequenos
4. Abra um pull request descrevendo o que foi alterado

Siga o padrão de código do projeto e escreva testes quando possível.

---

## Licença
Este projeto está licenciado sob a MIT License — veja o arquivo [LICENSE](LICENSE) para detalhes.

---

## Contato
Desenvolvedor: Pedro Nagatomo  
Repositório: [https://github.com/PedroNagatomo/ChatApplication](https://github.com/PedroNagatomo/ChatApplication)

Se quiser, posso:
- Ajustar o README para incluir screenshots e GIFs de demonstração
- Gerar badges dinâmicos de build/coverage (GitHub Actions)
- Adicionar exemplos de requests (cURL / Postman collections)

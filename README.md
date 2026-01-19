ChatFX - Aplicacao de Chat em Tempo Real
https://img.shields.io/badge/Java-17+-orange
https://img.shields.io/badge/JavaFX-17+-blue
https://img.shields.io/badge/Spring%2520Boot-3.0+-green
https://img.shields.io/badge/License-MIT-yellow

Uma aplicacao de chat moderna e completa desenvolvida com JavaFX no frontend e Spring Boot no backend, com comunicacao em tempo real via WebSocket.

Funcionalidades
Autenticacao e Perfis
Login e Registro com autenticacao JWT

Perfis de usuario personalizaveis

Status online/ausente/ocupado

Avatar colorido gerado automaticamente

Biografia personalizada

Chat em Tempo Real
Comunicacao instantanea via WebSocket

Salas de chat publicas e privadas

Historico de mensagens persistente

Interface responsiva e moderna

Multiplos usuarios simultaneos

Interface Moderna
Design limpo e intuitivo

CSS personalizado com efeitos visuais

Responsivo para diferentes tamanhos de tela

Icones e avatares coloridos

Animaçoes suaves

Pre-requisitos
Antes de comecar, voce precisa ter instalado:

Java JDK 17 ou superior (Download)

Maven 3.6+ (Download)

Git (Download)

Instalacao
1. Clone o repositorio
bash
git clone https://github.com/seu-usuario/chatfx.git
cd chatfx
2. Estrutura do projeto
O projeto esta dividido em duas partes independentes:

text
chatfx/
├── backend/      # API Spring Boot (porta 8080)
└── frontend/     # Aplicacao JavaFX (interface grafica)
Configuracao e Execucao
PRIMEIRO: Execute o Backend (Spring Boot)
Navegue ate a pasta do backend:

bash
cd backend
Configure o banco de dados (opcional):

O projeto usa H2 Database em memoria por padrao. Para usar outro banco, edite o arquivo:

text
backend/src/main/resources/application.properties
Compile e execute o backend:

bash
# Com Maven
mvn clean spring-boot:run

# Ou se preferir executar o JAR
mvn clean package
java -jar target/chatfx-backend-1.0.0.jar
Verifique se o backend esta rodando:

Acesse no seu navegador:

API: http://localhost:8080/api

Console H2: http://localhost:8080/h2-console

WebSocket: ws://localhost:8080/chat

Dados do H2 Console (se habilitado):

URL: jdbc:h2:mem:chatdb

Usuario: sa

Senha: (deixe em branco)

SEGUNDO: Execute o Frontend (JavaFX)
Abra um novo terminal e navegue ate a pasta do frontend:

bash
cd frontend
Configure as dependencias do JavaFX:

Certifique-se de que seu JavaFX SDK esta configurado. Se usar Maven, as dependencias ja estao no pom.xml.

Compile o frontend:

bash
mvn clean compile
Execute a aplicacao:

bash
# Com Maven
mvn javafx:run

# Ou executando a classe principal
java --module-path "caminho/para/javafx-sdk/lib" \
     --add-modules javafx.controls,javafx.fxml \
     -jar target/chatfx-frontend-1.0.0.jar
Para desenvolvimento com IDE:

Importe o projeto como Maven Project em sua IDE favorita (IntelliJ IDEA, Eclipse, VS Code) e execute a classe MainApp.java.

Como Usar o ChatFX
1. Primeiro Acesso
Execute o backend (Spring Boot)

Execute o frontend (JavaFX)

Na tela de login, clique em "Registrar"

Crie uma nova conta

2. Criando uma Sala de Chat
Apos login, clique no botao "Nova Sala"

Digite um nome para a sala

A sala aparecera na lista a esquerda

3. Convidando Amigos
Peca para seus amigos instalarem o ChatFX

Cada um deve criar sua conta

Todos veraõ as salas publicas automaticamente

4. Editando Seu Perfil
Clique no seu nome no canto superior direito

Altere status, biografia ou avatar

Clique em "Salvar"

5. Enviando Mensagens
Selecione uma sala na lista

Digite sua mensagem no campo inferior

Pressione Enter ou clique no botao de enviar

Endpoints da API
Autenticacao
text
POST   /api/auth/login     - Login de usuario
POST   /api/auth/register  - Registro de novo usuario
Usuarios
text
GET    /api/users/me       - Retorna usuario atual
GET    /api/users/online   - Lista usuarios online
PUT    /api/users/update   - Atualiza perfil
Salas
text
GET    /api/rooms          - Lista todas as salas
POST   /api/rooms/create   - Cria nova sala
Mensagens
text
GET    /api/messages/room/{id} - Historico da sala
WebSocket
text
ws://localhost:8080/chat   - Conexao em tempo real
Tecnologias Utilizadas
Backend (Spring Boot)
Spring Boot 3.0+ - Framework principal

Spring Security - Autenticacao e autorizacao

Spring WebSocket - Comunicacao em tempo real

JPA/Hibernate - Persistencia de dados

H2 Database - Banco em memoria (pode trocar por MySQL/PostgreSQL)

JWT - Tokens de autenticacao

Maven - Gerenciamento de dependencias

Frontend (JavaFX)
JavaFX 17+ - Interface grafica

CSS3 - Estilizacao moderna

WebSocket Client - Conexao com backend

Jackson - Serializacao JSON

Java 11+ - Linguagem base

Estrutura de Pastas
Backend
text
backend/src/main/java/com/backend-chat/
├── config/           # Configuraçoes (Security, WebSocket)
├── controller/       # Controladores REST
├── model/           # Entidades (User, Message, Room)
├── repository/      # Repositorios JPA
├── service/         # Logica de negocio
└── security/        # Configuraçoes de seguranca
Frontend
text
frontend/src/main/java/com/chat-client/
├── controller/      # Controladores FXML
├── model/          # Modelos de dados
├── service/        # Serviços (API, WebSocket)
└── views/          # Arquivos FXML
└── styles.css      # Estilos da aplicacao
Comandos Rapidos
Desenvolvimento
bash
# Executar backend
cd backend && mvn spring-boot:run

# Executar frontend (em outro terminal)
cd frontend && mvn javafx:run

# Limpar e recompilar tudo
./scripts/clean-build.sh
Producao
bash
# Criar JARs
mvn clean package

# Executar backend
java -jar backend/target/chatfx-backend-1.0.0.jar

# Executar frontend
java -jar frontend/target/chatfx-frontend-1.0.0.jar

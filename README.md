
# Socket-Roleta-Cassino

## Descrição
Este repositório contém a implementação de uma aplicação cliente-servidor simples para um jogo de roleta de cassino usando Java. O projeto demonstra a programação básica de sockets, onde o servidor gerencia a lógica do jogo e a interface gráfica do cliente permite que os usuários interajam com o jogo. Foi desenvolvido como parte do curso de Sistemas Distribuídos na Universidade de São Paulo.

## Instalação
Para executar este projeto, você precisa ter o Java instalado na sua máquina. 

Clone o repositório:
```bash
git clone https://github.com/seu-usuario/seu-repositorio.git
cd seu-repositorio
```

## Uso
Compile os arquivos Java:
```bash
javac Server.java
javac ClientGUI.java
```

Inicie o servidor:
```bash
java Server
```

Inicie o cliente:
```bash
java ClientGUI
```

## Estrutura dos Arquivos
- `Server.java`: Contém a implementação do lado do servidor do jogo de roleta de cassino. O servidor gerencia a lógica do jogo e se comunica com o cliente.
- `ClientGUI.java`: Contém a implementação do lado do cliente com uma interface gráfica (GUI) que permite aos usuários interagir com o jogo.

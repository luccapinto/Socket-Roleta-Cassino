import java.io.*;
import java.net.*;
import java.util.Random;

public class Server {

    private double saldo;
    private boolean apostaEspecialEscolhida = false;

    public static void main(String[] args) {
        new Server().startServer();
    }

    public void startServer() {
        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            System.out.println("Servidor iniciado na porta 12345.");

            try (Socket clientSocket = serverSocket.accept();
                 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                 BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

                out.println("Cliente conectado.");

                // Solicita depósito inicial
                out.println("Digite o valor inicial a ser depositado:");
                saldo = Double.parseDouble(in.readLine());

                boolean continuar = true;
                while (continuar) {
                    if (!apostaEspecialEscolhida) {
                        out.println("Escolha o tipo de aposta: 1 para número, 2 para cor (vermelho/preto), 3 para aposta especial. Saldo atual: " + saldo);
                    } else {
                        out.println("Escolha o tipo de aposta: 1 para número, 2 para cor (vermelho/preto). Saldo atual: " + saldo);
                    }

                    int tipoAposta = Integer.parseInt(in.readLine());

                    if (tipoAposta == 1) {
                        out.println("Qual número deseja apostar?");
                    } else if (tipoAposta == 2) {
                        out.println("Qual cor deseja apostar?");
                    } else if (tipoAposta == 3 && !apostaEspecialEscolhida) {
                        out.println("Você gostaria de dar nota 10 para o EP? (sim/nao)");
                        String respostaEspecial = in.readLine();
                        if (respostaEspecial.equalsIgnoreCase("sim")) {
                            saldo += 1000000; // Adiciona 1 milhão ao saldo
                            apostaEspecialEscolhida = true; // Marca a aposta especial como escolhida
                            out.println("Você deu nota 10 para o EP e ganhou UM MILHAO DE REAIS! Saldo atualizado: " + saldo);
                        } else if (respostaEspecial.equalsIgnoreCase("nao")) {
                            saldo = 0; // Define o saldo como zero
                            apostaEspecialEscolhida = true; // Marca a aposta especial como escolhida
                            out.println("Você não deu nota 10 para o EP e perdeu todo seu dinheiro misteriosamente. Saldo atual: " + saldo);
                        } else {
                            out.println("Resposta inválida. Use 'sim' ou 'nao'.");
                        }
                        continue;
                    } else {
                        out.println("Tipo de aposta inválido.");
                        continue;
                    }

                    String aposta = in.readLine();

                    out.println("Quanto deseja apostar?");
                    double valorAposta = Double.parseDouble(in.readLine());

                    if (valorAposta <= 0) {
                        out.println("Valor da aposta inválido. Aposta deve ser maior que zero.");
                        continue;
                    }

                    if (valorAposta > saldo) {
                        out.println("Saldo insuficiente para esta aposta. Seu saldo atual é: " + saldo);
                        continue;
                    }

                    processarAposta(valorAposta, tipoAposta, aposta, out);

                    in.readLine();

                    // Pergunta se quer continuar
                    out.println("Deseja 'continuar', 'sacar' ou 'depositar'?");
                    String decisao = in.readLine();

                    switch (decisao.toLowerCase()) {
                        case "sacar" -> {
                            out.println("Você sacou: " + saldo + ". Conexão encerrada.");
                            continuar = false;
                        }

                        case "depositar" -> {
                            out.println("Quanto deseja depositar?");
                            double deposito = Double.parseDouble(in.readLine());
                            saldo += deposito;
                        }

                        case "continuar" -> {
                        }

                        default -> {
                            out.println("Opção inválida.");
                            continuar = false;
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Erro no Servidor: " + e.getMessage());
        }
    }

    private void processarAposta(double valor, int tipoAposta, String aposta, PrintWriter out) {
        int resultado = new Random().nextInt(37); // Gera um número entre 0 e 36
        boolean ganhou = false;

        switch (tipoAposta) {
            case 1: // Aposta em um número específico
                int numeroApostado = Integer.parseInt(aposta);
                if (resultado == numeroApostado) {
                    ganhou = true;
                    saldo += valor * 35; // Pagamento de 35:1 para aposta em número
                }
                break;
            case 2: // Aposta em vermelho ou preto
                boolean isRed = isRed(resultado); // Implementar método isRed
                if ((aposta.equalsIgnoreCase("vermelho") && isRed) ||
                        (aposta.equalsIgnoreCase("preto") && !isRed)) {
                    ganhou = true;
                    saldo += valor; // Pagamento de 1:1
                }
                break;
            // Adicione mais casos para outros tipos de apostas
        }

        if (ganhou) {
            out.println("Você ganhou! Número sorteado: " + resultado + " Saldo atualizado: " + saldo);
        } else {
            saldo -= valor;
            out.println("Você perdeu! Número sorteado: " + resultado + " Saldo atualizado: " + saldo);
        }
    }

    private boolean isRed(int number) {
        // Implemente a lógica para determinar se um número é vermelho na roleta
        // Por exemplo, 1, 3, 5, 7, 9, 12, 14, 16, 18, 19, 21, 23, 25, 27, 30, 32 e 34 são vermelhos
        return (number > 0 && (number <= 10 || (number >= 19 && number <= 28))) == (number % 2 != 0);
    }
}

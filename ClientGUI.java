import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

public class ClientGUI {

    private JFrame frame;
    private JTextField inputField;
    private JTextArea textArea;
    private JButton sendButton;
    private JButton enterButton; // Botão Enter modificado
    private JButton redButton; // Botão para escolher vermelho
    private JButton blackButton; // Botão para escolher preto
    private JButton yesButton; // Botão para escolher "Sim"
    private JButton noButton; // Botão para escolher "Não"
    private JButton continuarButton; // Botão para escolher "Continuar"
    private JButton sacarButton; // Botão para escolher "Sacar"
    private JButton depositarButton; // Botão para escolher "Depositar"
    private boolean isChoosingColor = false; // Variável para controlar se o cliente está escolhendo uma cor
    private boolean isChoosingNota = false; // Variável para controlar se o cliente está respondendo à pergunta da nota
    private boolean isChoosingAcao = false; // Variável para controlar se o cliente está respondendo à pergunta de ação
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public ClientGUI() {
        initialize();
        connectToServer();
    }

    private void initialize() {
        frame = new JFrame("Cliente - Jogo de Apostas");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 800);
        frame.setLayout(new BorderLayout());

        // Estilo da Área de Texto
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setBackground(new Color(0, 102, 0)); // Verde escuro
        textArea.setForeground(Color.YELLOW); // Texto dourado
        textArea.setFont(new Font("Serif", Font.BOLD, 18));
        JScrollPane scrollPane = new JScrollPane(textArea);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Painel de Entrada com Estilo
        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.setBackground(new Color(32, 32, 32)); // Fundo escuro

        inputField = new JTextField(20);
        inputField.setFont(new Font("Serif", Font.PLAIN, 16));
        inputPanel.add(inputField);

        sendButton = new JButton("Enviar");
        sendButton.setFont(new Font("Serif", Font.BOLD, 16));
        sendButton.setBackground(Color.BLACK);
        sendButton.setForeground(Color.YELLOW); // Texto dourado
        sendButton.addActionListener(e -> sendMessage());
        inputPanel.add(sendButton);

        // Botão Enter modificado para enviar uma resposta vazia
        enterButton = new JButton();
        enterButton.setFont(new Font("Serif", Font.BOLD, 16));
        enterButton.addActionListener(e -> sendEmptyMessage());
        enterButton.setVisible(false); // Inicialmente invisível
        inputPanel.add(enterButton);

        // Adicione os botões para escolher vermelho ou preto, inicialmente ocultos
        redButton = new JButton("Vermelho");
        redButton.setFont(new Font("Serif", Font.BOLD, 16));
        redButton.setBackground(Color.RED);
        redButton.setForeground(Color.WHITE);
        redButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendChoice("vermelho");
                displayMessage("Cliente: Vermelho"); // Exibir escolha do cliente na área de texto
            }
        });
        redButton.setVisible(false); // Inicialmente invisível
        inputPanel.add(redButton);

        blackButton = new JButton("Preto");
        blackButton.setFont(new Font("Serif", Font.BOLD, 16));
        blackButton.setBackground(Color.BLACK);
        blackButton.setForeground(Color.WHITE);
        blackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendChoice("preto");
                displayMessage("Cliente: Preto"); // Exibir escolha do cliente na área de texto
            }
        });
        blackButton.setVisible(false); // Inicialmente invisível
        inputPanel.add(blackButton);

        // Adicione os botões "Sim" e "Não" para a pergunta especial, inicialmente ocultos
        yesButton = new JButton("Sim");
        yesButton.setFont(new Font("Serif", Font.BOLD, 16));
        yesButton.setBackground(Color.GREEN);
        yesButton.setForeground(Color.WHITE);
        yesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendChoice("sim");
                displayMessage("Cliente: Sim"); // Exibir escolha do cliente na área de texto
            }
        });
        yesButton.setVisible(false); // Inicialmente invisível
        inputPanel.add(yesButton);

        noButton = new JButton("Não");
        noButton.setFont(new Font("Serif", Font.BOLD, 16));
        noButton.setBackground(Color.RED);
        noButton.setForeground(Color.WHITE);
        noButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendChoice("nao");
                displayMessage("Cliente: Não"); // Exibir escolha do cliente na área de texto
            }
        });
        noButton.setVisible(false); // Inicialmente invisível
        inputPanel.add(noButton);

        // Adicione os botões "Continuar", "Sacar" e "Depositar" para a pergunta de ação, inicialmente ocultos
        continuarButton = new JButton("Continuar");
        continuarButton.setFont(new Font("Serif", Font.BOLD, 16));
        continuarButton.setBackground(Color.GREEN);
        continuarButton.setForeground(Color.WHITE);
        continuarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendChoice("continuar");
                displayMessage("Cliente: Continuar"); // Exibir escolha do cliente na área de texto
            }
        });
        continuarButton.setVisible(false); // Inicialmente invisível
        inputPanel.add(continuarButton);

        sacarButton = new JButton("Sacar");
        sacarButton.setFont(new Font("Serif", Font.BOLD, 16));
        sacarButton.setBackground(Color.RED);
        sacarButton.setForeground(Color.WHITE);
        sacarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendChoice("sacar");
                displayMessage("Cliente: Sacar"); // Exibir escolha do cliente na área de texto
            }
        });
        sacarButton.setVisible(false); // Inicialmente invisível
        inputPanel.add(sacarButton);

        depositarButton = new JButton("Depositar");
        depositarButton.setFont(new Font("Serif", Font.BOLD, 16));
        depositarButton.setBackground(Color.BLUE);
        depositarButton.setForeground(Color.WHITE);
        depositarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendChoice("depositar");
                displayMessage("Cliente: Depositar"); // Exibir escolha do cliente na área de texto
            }
        });
        depositarButton.setVisible(false); // Inicialmente invisível
        inputPanel.add(depositarButton);

        frame.add(inputPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private void connectToServer() {
        try {
            socket = new Socket("localhost", 12345);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            new Thread(new ServerListener()).start();
        } catch (IOException e) {
            textArea.append("Erro ao conectar ao servidor: " + e.getMessage() + "\n");
        }
    }

    private void sendMessage() {
        String message = inputField.getText();
        if (!message.isEmpty()) {
            out.println(message);
            displayMessage("Cliente: " + message); // Exibir mensagem do cliente na área de texto
            inputField.setText("");
        }
    }

    private void sendEmptyMessage() {
        out.println("");
    }

    private void sendChoice(String choice) {
        out.println(choice);
    }

    private class ServerListener implements Runnable {
        public void run() {
            try {
                String fromServer;
                while ((fromServer = in.readLine()) != null) {
                    String finalFromServer = fromServer;
                    SwingUtilities.invokeLater(() -> {
                        textArea.append("Cassino do JB: " + finalFromServer + "\n");
                        if (finalFromServer.contains("Você ganhou")) {
                            enterButton.setText("uhuu!");
                            enterButton.setBackground(Color.GREEN);
                            toggleComponents(false);
                        } else if (finalFromServer.contains("Você perdeu")) {
                            enterButton.setText("vou ter que vender o corsakkkk");
                            enterButton.setBackground(Color.RED);
                            toggleComponents(false);
                        } else {
                            toggleComponents(true);
                            if (finalFromServer.contains("Qual cor deseja apostar?")) {
                                isChoosingColor = true;
                                redButton.setVisible(true);
                                blackButton.setVisible(true);
                                inputField.setVisible(false);
                                sendButton.setVisible(false);
                            } else if (finalFromServer.contains("de dar nota 10 para o EP? (sim/nao)")) {
                                isChoosingNota = true;
                                redButton.setVisible(false);
                                blackButton.setVisible(false);
                                inputField.setVisible(false);
                                sendButton.setVisible(false);
                                yesButton.setVisible(true); // Mostrar botão "Sim"
                                noButton.setVisible(true); // Mostrar botão "Não"
                            } else if (finalFromServer.contains("Deseja 'continuar', 'sacar' ou 'depositar'?")) {
                                isChoosingAcao = true;
                                redButton.setVisible(false);
                                blackButton.setVisible(false);
                                inputField.setVisible(false);
                                sendButton.setVisible(false);
                                continuarButton.setVisible(true); // Mostrar botão "Continuar"
                                sacarButton.setVisible(true); // Mostrar botão "Sacar"
                                depositarButton.setVisible(true); // Mostrar botão "Depositar"
                            } else {
                                isChoosingColor = false;
                                isChoosingNota = false;
                                isChoosingAcao = false;
                                redButton.setVisible(false);
                                blackButton.setVisible(false);
                                inputField.setVisible(true);
                                sendButton.setVisible(true);
                                yesButton.setVisible(false); // Ocultar botão "Sim"
                                noButton.setVisible(false); // Ocultar botão "Não"
                                continuarButton.setVisible(false); // Ocultar botão "Continuar"
                                sacarButton.setVisible(false); // Ocultar botão "Sacar"
                                depositarButton.setVisible(false); // Ocultar botão "Depositar"
                            }
                        }
                    });
                }
            } catch (IOException e) {
                SwingUtilities.invokeLater(() -> textArea.append("Erro ao receber mensagem do servidor: " + e.getMessage() + "\n"));
            }
        }
    }

    private void toggleComponents(boolean showSend) {
        sendButton.setVisible(showSend);
        inputField.setVisible(showSend);
        enterButton.setVisible(!showSend);
        redButton.setVisible(isChoosingColor && showSend); // Mostra os botões vermelho e preto apenas se estiver escolhendo cor
        blackButton.setVisible(isChoosingColor && showSend);
        yesButton.setVisible(isChoosingNota && showSend); // Mostra o botão "Sim" se estiver respondendo à pergunta especial
        noButton.setVisible(isChoosingNota && showSend); // Mostra o botão "Não" se estiver respondendo à pergunta especial
        continuarButton.setVisible(isChoosingAcao && showSend); // Mostra o botão "Continuar" se estiver respondendo à pergunta de ação
        sacarButton.setVisible(isChoosingAcao && showSend); // Mostra o botão "Sacar" se estiver respondendo à pergunta de ação
        depositarButton.setVisible(isChoosingAcao && showSend); // Mostra o botão "Depositar" se estiver respondendo à pergunta de ação
    }

    private void displayMessage(String message) {
        textArea.append(message + "\n");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ClientGUI());
    }
}

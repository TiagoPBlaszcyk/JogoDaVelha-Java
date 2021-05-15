// Classico Jogo da velha completo

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Scanner;
import java.io.Serializable;

public class JogoDaVelha {
    private static int jog;
    private static int[][] casa = new int[3][3];
    private static int linha, coluna, win;
    private static Scanner leitor = new Scanner(System.in);
    private static long inicio;
    private static Jogador[] jogadores = new Jogador[50];
    private static Jogador jogador1, jogador2;
    private static int quantidadeJogadores = 0;
    private static File arquivo = new File("ranking.obj");
    private static ObjectInputStream saida;
    private static ObjectOutputStream saida2;

    public static class Jogador implements Serializable {
        public String nome;
        public int vitorias;
        public int derrotas;
    }

    public static void main(String[] args) {
        cadastro();
        setTempo();
        int i = 0;

        for (i = 0; i < 9; i++) {
            jogo();// chama a função jogo(), que desenha o tabuleiro
            if (i % 2 == 0) {
                jogar(2);
            } else {
                jogar(1);
            }
            // chama a função check (), para ver se alguém ganhou
            check();
            if (win == 1 || win == 2) {
                // sai do laço antes de completar o tabuleiro,
                i = 10;
            }
        }

        // chama a função jogo(), para desenhar novamente o tabuleiro
        jogo();

        // verifica se houve vencedor
        System.out.println();

        if (win == 1) {
            // informa o vencedor
            System.out.println("Jogador " + jogador1.nome + " é o ganhador!");
            jogador1.vitorias = jogador1.vitorias + 1;
            jogador2.derrotas = jogador2.derrotas + 1;
        } else if (win == 2) {
            System.out.println("Jogador " + jogador2.nome + " é o ganhador!");
            jogador2.vitorias = jogador2.vitorias + 1;
            jogador1.derrotas = jogador1.derrotas + 1;
        } else {
            System.out.println("Não houve vencedor! O jogo foi empate!!");
        }
        System.out.println("O tempo total de jogo foi de " + getTempo() + "s");
        imprimirJogadores();

        lerJogadores();

    }

    public static void cadastro() {
        System.out.println("Digite o nome do jogador 1:");
        String nome_jogador1 = leitor.next();
        jogador1 = buscarJogador(nome_jogador1);
        System.out.println("Digite o nome do jogador 2:");
        String nome_jogador2 = leitor.next();
        jogador2 = buscarJogador(nome_jogador2);
    }

    public static Jogador buscarJogador(String nome) {
        Jogador jogador = null;
        for (int i = 0; (i < quantidadeJogadores) && (jogador == null); i++) {
            if (jogadores[i] != null && jogadores[i].nome.equalsIgnoreCase(nome)) {
                jogador = jogadores[i];
            }
        }
        if (jogador == null) {
            jogador = new Jogador();
            jogador.nome = nome;

            if (quantidadeJogadores < 50) {
                jogadores[quantidadeJogadores] = jogador;
                quantidadeJogadores = quantidadeJogadores + 1;
            }

        }
        return jogador;
    }

    public static void jogar(int jogador) {
        int i = 0;
        if (jogador == 1) {
            jog = 1;
            System.out.println("\n\n Vez do Jogador " + jogador1.nome);
        } else {
            jog = 2;
            System.out.println("\n\n Vez do Jogador " + jogador2.nome);
        }
        System.out.println("\n\n Vez do Jogador " + jog);
        while (i == 0) {
            linha = 0;
            coluna = 0;
            while (linha < 1 || linha > 3) {
                System.out.print("Escolha a Linha (1,2,3):");
                // lendo a linha escolhida
                linha = leitor.nextInt();
                // Aviso de linha inválida,
                if (linha < 1 || linha > 3) {
                    System.out.println("Linha invalida! Escolha uma linha entre 1 e 3.");
                }
            }
            while (coluna < 1 || coluna > 3) {
                System.out.print("Escolha a Coluna (1,2,3):");
                coluna = leitor.nextInt();
                if (coluna < 1 || coluna > 3) {
                    System.out.println("Coluna invalida! Escolha uma coluna entre 1 e 3.");
                }
            }
            // Ajusta índices para começar do zero
            linha = linha - 1;
            coluna = coluna - 1;
            if (casa[linha][coluna] == 0) {
                casa[linha][coluna] = jog;
                i = 1;
            } else {
                System.out.println("Posição ocupada!");
            }
        }
    }

    public static void salvarJogadores() {
        try {
            saida2 = new ObjectOutputStream(new FileOutputStream(arquivo));
            saida2.writeObject(jogadores);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void lerJogadores() {
        try {
            saida = new ObjectInputStream(new FileInputStream(arquivo));
            jogadores = (Jogador[]) saida.readObject();
            while (jogadores[quantidadeJogadores] != null && quantidadeJogadores < 50) {
                quantidadeJogadores = quantidadeJogadores + 1;
            }
        } catch (FileNotFoundException e) {
            // Não faz nada
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void check() {
        int i = 0;
        for (i = 0; i < 3; i++) {
            if (casa[i][0] == casa[i][1] && casa[i][0] == casa[i][2]) {
                if (casa[i][0] == 1)
                    win = 1;
                if (casa[i][0] == 2)
                    win = 2;
            }
        }
        for (i = 0; i < 3; i++) {
            if (casa[0][i] == casa[1][i] && casa[0][i] == casa[2][i]) {
                if (casa[0][i] == 1)
                    win = 1;
                if (casa[0][i] == 2)
                    win = 2;
            }
        }
        if (casa[0][0] == casa[1][1] && casa[0][0] == casa[2][2]) {
            if (casa[0][0] == 1)
                win = 1;
            if (casa[0][0] == 2)
                win = 2;
        }
        if (casa[0][2] == casa[1][1] && casa[0][2] == casa[2][0]) {
            if (casa[0][2] == 1)
                win = 1;
            if (casa[0][2] == 2)
                win = 2;
        }
    }

    public static void setTempo() {
        inicio = System.currentTimeMillis();
    }

    public static long getTempo() {
        return (System.currentTimeMillis() - inicio) / 1000;
    }

    public static void jogo() {
        System.out.print("\n  1   2   3\n");
        System.out.print("1 ");
        desenha(0, 0);
        System.out.print("  | ");
        desenha(0, 1);
        System.out.print("  | ");
        desenha(0, 2);
        System.out.print("\n -----------\n2 ");
        desenha(1, 0);
        System.out.print("  | ");
        desenha(1, 1);
        System.out.print("  | ");
        desenha(1, 2);
        System.out.print("\n -----------\n3 ");
        desenha(2, 0);
        System.out.print("  | ");
        desenha(2, 1);
        System.out.print("  | ");
        desenha(2, 2);
    }

    public static void desenha(int x, int y) {
        if (casa[x][y] == 1) {
            System.out.print("X");
        } else if (casa[x][y] == 2) {
            System.out.print("O");
        } else {
            System.out.print(" ");
        }
    }

    public static void imprimirJogadores() {
        System.out.println("------ Resultado dos jogadores --------");
        for (int i = 0; i < quantidadeJogadores; i++) {
            System.out.println("Nome: " + jogadores[i].nome);
            System.out.println(" | vitórias:" + jogadores[i].vitorias);
            System.out.println(" | derrotas:" + jogadores[i].derrotas);
        }
    }

}

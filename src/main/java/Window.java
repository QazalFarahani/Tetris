import javax.swing.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Scanner;

public class Window {

    public JFrame window;
    private Board board;

    public Window(){
        window = new JFrame("Tetris");
        window.setSize(Constants.n*30 + 400, Constants.m*30 + 37);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setLocationRelativeTo(null);

        board = new Board();
        board.setBounds(0, 0, Constants.n*30 + 400, Constants.m*30);
        window.add(board);
        window.addKeyListener(board);

        window.setVisible(true);
        window.setLayout(null);

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                board.saveFile("src\\main\\resources\\scores.txt");
            }
        }));
    }

    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        int n = 0 , m = 0;
        while(n > Constants.maxWidth || n < Constants.minWidth || m > Constants.maxHeight || m < Constants.minHeight) {
            System.out.println("ENTER WIDTH AND HEIGHT BETWEEN 15 N 25.");
            n = sc.nextInt();
            m = sc.nextInt();
        }
        Constants.setN(n);
        Constants.setM(m);
        new Window();
    }
}

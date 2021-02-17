import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.*;

public class Board extends JPanel implements KeyListener {

    private BufferedImage blocks;
    private BufferedImage backGround;
    private final int blockSize = 30;
    private final int boardWidth = Constants.n, boardHeight = Constants.m;
    private int[][] board = new int[boardHeight][boardWidth];
    private Shape[] shapes = new Shape[7];
    private Shape currentShape;
    private Timer timer;
    private final int fps = 100;
    private final int delay = 1000 / fps;
    private boolean gameover = false;
    private Shape NextShape;
    private int Score;
    private int level;
    private int line;
    private JLabel ScoreLabel;
    private JLabel Lines;
    private JLabel levels;
    private JLabel[] jl = new JLabel[10];
    private String[] topScore = new String[10];
    private boolean left, right;
    private static BufferedImage[] anim = new BufferedImage[3];


    public Board() {
        setBorder(BorderFactory.createLineBorder(Color.black));
        try {
            blocks = ImageIO.read(Board.class.getResource("/tiles.png"));
            backGround = ImageIO.read(Board.class.getResource("/star.jpg"));
            anim[0] = ImageIO.read(Board.class.getResource("/left.png"));
            anim[1] = ImageIO.read(Board.class.getResource("/right.png"));
            anim[2] = ImageIO.read(Board.class.getResource("/down.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //labels///////////////////////////////////
        setLayout(null);

        for (int i = 0; i < 10; i++) {
            topScore[i] = "";
        }

        readFile("src\\main\\resources\\scores.txt");

        JLabel scoretext = new JLabel("SCORE : ");
        scoretext.setBounds(Constants.n * 30 + 20, 90, 200, 200);
        scoretext.setFont(new Font("Serif", Font.BOLD, Constants.fontsize));
        scoretext.setForeground(Color.white);
        add(scoretext);
        JLabel leveltext = new JLabel("LEVEL : ");
        leveltext.setBounds(Constants.n * 30 + 20, 110, 200, 200);
        leveltext.setFont(new Font("Serif", Font.BOLD, Constants.fontsize));
        leveltext.setForeground(Color.white);
        add(leveltext);
        JLabel linestext = new JLabel("LINES : ");
        linestext.setBounds(Constants.n * 30 + 20, 130, 200, 200);
        linestext.setFont(new Font("Serif", Font.BOLD, Constants.fontsize));
        linestext.setForeground(Color.white);
        add(linestext);
        JLabel next = new JLabel("NEXT SHAPE : ");
        next.setBounds(Constants.n * 30 + 20, -50, 200, 200);
        next.setFont(new Font("Serif", Font.BOLD, Constants.fontsize));
        next.setForeground(Color.white);
        add(next);
        JLabel topscore = new JLabel("TOP SCORES : ");
        topscore.setBounds(Constants.n * 30 + 220, -50, 200, 200);
        topscore.setFont(new Font("Serif", Font.BOLD, Constants.fontsize));
        topscore.setForeground(Color.white);
        add(topscore);
        JLabel s = new JLabel("PRESS S TO START");
        s.setBounds(Constants.n * 30 + 20, 200, 200, 200);
        s.setFont(new Font("Serif", Font.BOLD, Constants.fontsize));
        s.setForeground(Color.white);
        add(s);
        JLabel r = new JLabel("PRESS R TO RESTART");
        r.setBounds(Constants.n * 30 + 20, 230, 200, 200);
        r.setFont(new Font("Serif", Font.BOLD, Constants.fontsize));
        r.setForeground(Color.white);
        add(r);
        JLabel t = new JLabel("PRESS T TO TOBE-.-");
        t.setBounds(Constants.n * 30 + 20, 260, 200, 200);
        t.setFont(new Font("Serif", Font.BOLD, Constants.fontsize));
        t.setForeground(Color.white);
        add(t);
        JLabel p = new JLabel("PRESS P TO PAUSE");
        p.setBounds(Constants.n * 30 + 20, 290, 200, 200);
        p.setFont(new Font("Serif", Font.BOLD, Constants.fontsize));
        p.setForeground(Color.white);
        add(p);


        ScoreLabel = new JLabel("0");
        ScoreLabel.setBounds(Constants.n * 30 + 100, 90, 200, 200);
        ScoreLabel.setFont(new Font("Serif", Font.BOLD, Constants.fontsize));
        ScoreLabel.setForeground(Color.white);
        add(ScoreLabel);
        Lines = new JLabel("0");
        Lines.setBounds(Constants.n * 30 + 100, 130, 200, 200);
        Lines.setFont(new Font("Serif", Font.BOLD, Constants.fontsize));
        Lines.setForeground(Color.white);
        add(Lines);
        levels = new JLabel("0");
        levels.setBounds(Constants.n * 30 + 100, 110, 200, 200);
        levels.setFont(new Font("Serif", Font.BOLD, Constants.fontsize));
        levels.setForeground(Color.white);
        add(levels);


        for (int i = 0; i < 10; i++) {
            jl[i] = new JLabel();
            if (Integer.parseInt(topScore[i]) != 0)
                jl[i].setText(topScore[i]);
            jl[i].setBounds(Constants.n * 30 + 220, -20 + 30 * i, 200, 200);
            jl[i].setFont(new Font("Serif", Font.BOLD, Constants.fontsize));
            jl[i].setForeground(Color.white);
            add(jl[i]);
        }

        timer = new Timer(delay, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                update();
                repaint();
            }
        });

        //I
        shapes[0] = new Shape(blocks.getSubimage(0, 0, blockSize, blockSize), new int[][]{{1, 1, 1, 1}}, this, 1);
        //Z
        shapes[1] = new Shape(blocks.getSubimage(blockSize, 0, blockSize, blockSize), new int[][]{{1, 1, 0}, {0, 1, 1}}, this, 2);
        //S
        shapes[2] = new Shape(blocks.getSubimage(blockSize * 2, 0, blockSize, blockSize), new int[][]{{0, 1, 1}, {1, 1, 0}}, this, 3);
        //J
        shapes[3] = new Shape(blocks.getSubimage(blockSize * 3, 0, blockSize, blockSize), new int[][]{{1, 1, 1}, {0, 0, 1}}, this, 4);
        //L
        shapes[4] = new Shape(blocks.getSubimage(blockSize * 4, 0, blockSize, blockSize), new int[][]{{1, 1, 1}, {1, 0, 0}}, this, 5);
        //T
        shapes[5] = new Shape(blocks.getSubimage(blockSize * 5, 0, blockSize, blockSize), new int[][]{{1, 1, 1}, {0, 1, 0}}, this, 6);

        shapes[6] = new Shape(blocks.getSubimage(blockSize * 6, 0, blockSize, blockSize), new int[][]{{1, 1}, {1, 1}}, this, 7);

        currentShape = shapes[3];
        createNextShape();

    }

    private void start() {
        if(!timer.isRunning())
        timer.start();
    }

    private void reset(){
        ScoreLabel.setText("0");
        levels.setText("0");
        Lines.setText("0");
    }

    private void restart() {
        gameover = false;
        createNextShape();
        currentShape.setX(boardWidth/2);
        currentShape.setY(0);
        board = new int[boardHeight][boardWidth];
        reset();
        start();
    }

    private void pause() {
        if (timer.isRunning())
            timer.stop();
        else
            timer.start();
    }

    private void tobe() {
        currentShape = new Shape(shapes[currentShape.getcolor()-1].getBlock(),
                shapes[currentShape.getcolor()-1].getCoords(), this, currentShape.getcolor());
        currentShape.setX(boardWidth/2);
        currentShape.setY(0);
    }

    private void updateScores() {
        for (int i = 0; i < 10; i++) {
            if (Score >= Integer.parseInt(topScore[i])) {
                topScore[i] = String.valueOf(Score + 1);
                break;
            }
        }
    }

    private void setScores() {
        for (int i = 0; i < 10; i++) {
            if (topScore[i] != jl[i].getText() && Integer.parseInt(topScore[i]) != 0)
                jl[i].setText(topScore[i]);
        }
    }

    public void update() {
        currentShape.update();
        if (gameover)
            timer.stop();
    }

    public void setLine(int line) {
        this.line = line;
    }

    public int getLine() {
        return line;
    }

    public JLabel getLines() {
        return Lines;
    }

    public int getScore() {
        return Score;
    }

    public void setScore(int score) {
        Score = score;
    }

    public int getBlockSize() {
        return blockSize;
    }

    public int[][] getBoard() {
        return board;
    }

    public boolean isLeft() {
        return left;
    }

    public boolean isRight() {
        return right;
    }

    public static BufferedImage[] getAnim() {
        return anim;
    }

    public int getBoardWidth() {
        return boardWidth;
    }

    public void paintComponent(Graphics g) {
        g.drawImage(backGround, 0, 0, this.getWidth(), this.getHeight(), null);

        currentShape.render(g);
        for (int row = 0; row < board.length; row++)
            for (int col = 0; col < board[row].length; col++)
                if (board[row][col] != 0)
                    g.drawImage(blocks.getSubimage((board[row][col] - 1) * blockSize, 0, blockSize, blockSize),
                            col * blockSize, row * blockSize, null);

        for (int row = 0; row < NextShape.getCoords().length; row++) {
            for (int col = 0; col < NextShape.getCoords()[row].length; col++) {
                if (NextShape.getCoords()[row][col] != 0)
                    g.drawImage(NextShape.getBlock(), col * NextShape.getBoard().getBlockSize() + Constants.n * 30 + 30,
                            row * NextShape.getBoard().getBlockSize() + 80, null);
            }
        }

        for (int i = 0; i <= boardHeight; i++) {
            g.drawLine(0, i * blockSize, boardWidth * blockSize, i * blockSize);
        }
        for (int j = 0; j <= boardWidth; j++) {
            g.drawLine(j * blockSize, 0, j * blockSize, blockSize * boardHeight);
        }
    }

    private void createNextShape() {
        int index = (int) (Math.random() * shapes.length);
        Shape nextShape = new Shape(shapes[index].getBlock(), shapes[index].getCoords(), this, index + 1);
        NextShape = nextShape;
    }

    public void setNextShape() {
        updateScores();
        setScores();
        currentShape = NextShape;
        createNextShape();
        ScoreLabel.setText(String.valueOf(++Score));
        levels.setText(String.valueOf(++level));
        for (int row = 0; row < currentShape.getCoords().length; row++)
            for (int col = 0; col < currentShape.getCoords()[row].length; col++)
                if (currentShape.getCoords()[row][col] != 0) {

                    if (board[row][col + boardWidth/2] != 0)
                        gameover = true;
                }
    }

    private void readFile(String path) {
        String str = "";
        String line;
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            while ((line = br.readLine()) != null) {
                str += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        topScore = str.split(" ");
    }

    public void saveFile(String path) {
        String str = "";
        for (int i = 0; i < 10; i++) {
            str += topScore[i] + " ";
        }
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(path));
            if(str != "")
            bw.write(str);
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            currentShape.setDeltax(-1);
            left = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            currentShape.setDeltax(1);
            right = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN)
            currentShape.speedDown();
        if (e.getKeyCode() == KeyEvent.VK_UP)
            currentShape.rotate();
        if (e.getKeyCode() == KeyEvent.VK_S)
            start();
        if (e.getKeyCode() == KeyEvent.VK_R)
            restart();
        if (e.getKeyCode() == KeyEvent.VK_T)
            tobe();
        if (e.getKeyCode() == KeyEvent.VK_P)
            pause();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_DOWN)
            currentShape.normalSpeed();
        else if (e.getKeyCode() == KeyEvent.VK_LEFT)
            left = false;
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT)
            right = false;

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }
}

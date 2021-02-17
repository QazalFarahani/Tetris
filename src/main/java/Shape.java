import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;


public class Shape {

    private BufferedImage block;
    private int[][] coords;
    private Board board;
    private int deltax;
    private int x, y;
    private int color;
    private int normalSpeed = 1000;
    private int speedDown = 60;
    private int currentSpeed;
    private long time, lastTime;
    private boolean collision = false, moveX = false;

    public Shape(BufferedImage block, int[][] coords, Board board, int color){
        this.block = block;
        this.coords = coords;
        this.board = board;
        this.color = color;

        currentSpeed = normalSpeed;
        time = 0;
        lastTime = System.currentTimeMillis();
        x = board.getBoardWidth()/2;
        y = 0;
    }
    public void update(){
        time += System.currentTimeMillis() - lastTime;
        lastTime = System.currentTimeMillis();

        if(collision){
            for(int row = 0; row < coords.length; row++)
                for(int col = 0; col < coords[row].length; col++)
                    if(coords[row][col] != 0)
                        board.getBoard()[y + row][x + col] = color;
            checkLine();
            board.setNextShape();
        }

        if(!(x + deltax + coords[0].length > Constants.n) && !(x + deltax < 0)) {
            for(int row = 0; row < coords.length; row++)
                for(int col = 0; col < coords[row].length; col++)
                    if(coords[row][col] != 0){
                        if(board.getBoard()[y + row][col + x + deltax] != 0)
                            moveX = false;
                    }
            if (moveX)
            x += deltax;
        }

        if(!(y + 1 + coords.length > Constants.m)){
            for(int row = 0; row < coords.length; row++)
                for(int col = 0; col < coords[row].length; col++)
                    if(coords[row][col] != 0){
                        if(board.getBoard()[y + row + 1][col + x] != 0)
                            collision = true;
                    }

            if(time > currentSpeed){
                y++;
                time = 0;
            }
        }
        else {
            collision = true;
        }

        deltax = 0;
        moveX = true;
    }
    public void render(Graphics g){
        for(int row = 0; row < coords.length; row++){
            for(int col = 0; col < coords[row].length; col++){
                if(coords[row][col] != 0) {
                    g.drawImage(block, col * board.getBlockSize() + x * board.getBlockSize(),
                            row * board.getBlockSize() + y * board.getBlockSize(), null);
                    if(board.isLeft())
                        g.drawImage(board.getAnim()[0],col*board.getBlockSize() + x*board.getBlockSize() - 10,
                                row*board.getBlockSize() + y*board.getBlockSize() - 10, 60, 60, null);
                    else if (board.isRight())
                        g.drawImage(board.getAnim()[1], col*board.getBlockSize() + x*board.getBlockSize() - 10,
                                row*board.getBlockSize() + y*board.getBlockSize() - 10, 60, 60,  null);
                    else
                        g.drawImage(board.getAnim()[2], col*board.getBlockSize() + x*board.getBlockSize() - 10,
                                row*board.getBlockSize() + y*board.getBlockSize() - 10,60, 60,  null);

                }
            }
        }
    }
    private int[][] getTranspose(int[][] matrix){
        int[][] newMatrix = new int[matrix[0].length][matrix.length];
        for(int i = 0; i < matrix.length; i++){
            for(int j = 0; j < matrix[0].length; j++){
                newMatrix[j][i] = matrix[i][j];
            }
        }
        return newMatrix;
    }
    private int[][] getReverseMatrix(int[][] matrix){
        int middle = matrix.length/2;
        for(int i = 0; i < middle; i++){
            int[] m = matrix[i];
            matrix[i] = matrix[matrix.length - 1 - i];
            matrix[matrix.length - i - 1] = m;
        }
        return matrix;
    }
    private void checkLine(){
        int height = board.getBoard().length - 1;
        for(int i = height; i > 0; i--){
            int count = 0;
            for(int j = 0; j < board.getBoard()[0].length; j++){
                if(board.getBoard()[i][j] != 0)
                    count++;
                board.getBoard()[height][j] = board.getBoard()[i][j];
            }
            if(count < board.getBoard()[0].length) {
                height--;
            }
            else {
                board.setScore(board.getScore() + 10);
                board.setLine(board.getLine() + 1);
                board.getLines().setText(String.valueOf(board.getLine()));
                Sound.sound.play("/res.wav");
            }
        }
    }
    public void rotate(){
        if(collision)
            return;
        int[][] rotatedMtrix = null;
        rotatedMtrix = getTranspose(coords);
        rotatedMtrix = getReverseMatrix(rotatedMtrix);
        if(x + rotatedMtrix[0].length > Constants.n || y + rotatedMtrix.length > Constants.m)
            return;
        for(int row = 0; row < rotatedMtrix.length; row++){
            for(int col = 0; col < rotatedMtrix[0].length; col++){
                if(board.getBoard()[y + row][x + col] != 0)
                    return;
            }
        }
        coords = rotatedMtrix;
    }
    public void setDeltax(int deltax){
        this.deltax = deltax;
    }
    public void normalSpeed(){
        currentSpeed = normalSpeed;
    }
    public void speedDown(){
        currentSpeed = speedDown;
    }

    public BufferedImage getBlock() {
        return block;
    }

    public int[][] getCoords() {
        return coords;
    }
    public int getcolor(){
        return color;
    }

    public Board getBoard() {
        return board;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

}

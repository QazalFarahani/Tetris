import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.logging.FileHandler;

public class Constants {
    public static int n;
    public static int m;
    public static int minWidth = 15;
    public static int maxWidth = 25;
    public static int minHeight = 15;
    public static int maxHeight = 25;
    public static int fontsize = 16;


    public static void setN(int n) {
        Constants.n = n;
    }

    public static void setM(int m) {
        Constants.m = m;
    }
}

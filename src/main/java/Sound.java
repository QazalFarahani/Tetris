import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class Sound {
    public static Sound sound = new Sound();

    public Sound(){}
    public void play(String filename){
        try {
            Clip clip;
            AudioInputStream ais = AudioSystem.getAudioInputStream(new File(filename));
            clip = AudioSystem.getClip();
            clip.open(ais);
            clip.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

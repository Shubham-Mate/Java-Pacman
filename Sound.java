import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Sound {
    Clip clip;
    URL url[] = new URL[10];

    Sound () {
        url[0] = getClass().getResource("./music/43 Aquatic Race.wav");
        url[1] = getClass().getResource("./music/gameover.wav");
    }

    void setFile(int i) {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(url[i]);
            try {
                clip = AudioSystem.getClip();
                clip.open(ais);
            } catch (LineUnavailableException e) {
                e.printStackTrace();
            }
            
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void play() {clip.start();}
    void loop() {clip.loop(Clip.LOOP_CONTINUOUSLY);}
    void stop() {clip.stop();}
}
import java.awt.Color;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SnakeTester {
	public static void main(String[] args) throws InterruptedException, IOException, UnsupportedAudioFileException, LineUnavailableException {
		
		Clip clip = AudioSystem.getClip();
		File themePath = new File("theme.wav");
		AudioInputStream s1 = AudioSystem.getAudioInputStream(themePath);
		clip.open(s1);
		clip.start();
		
		Color bodyC = new Color(93,200,253);
		Snake game = new Snake(bodyC);
		game.move(clip);
	}
}

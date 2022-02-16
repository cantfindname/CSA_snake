import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import doodlepad.Oval;
import doodlepad.Pad;
import doodlepad.Text;

public class Snake extends Pad {
	
	private ArrayList<Oval> body;
	private Oval head;
	private Oval apple;
	private final int WIDTH = 30;
	private int numApples;
	private boolean headRight, headLeft, headUp, headDown;
	
	public Snake(Color bodyC) {
		super(500,500);
		setBackground(65,169,76);
		head = new Oval(250, 250, WIDTH, WIDTH);
		head.setFillColor(Color.YELLOW);
		headRight = true;
		
		body = new ArrayList<Oval>();
		Oval b = new Oval(220,250,WIDTH,WIDTH);
		b.setFillColor(bodyC);
		Oval c = new Oval(190,250,WIDTH,WIDTH);
		c.setFillColor(bodyC);
		Oval d = new Oval(160,250,WIDTH,WIDTH);
		d.setFillColor(bodyC);
		body.add(b);
		body.add(c);
		body.add(d);

		makeApple();
	
	}
	
	public void makeApple() {
		int x = (int)(Math.random()*455)+15;
		int y = (int)(Math.random()*455)+15;
		apple = new Oval(x,y,WIDTH,WIDTH);

		while (intersectApple()) {
			x = (int)(Math.random()*455)+15;
			y = (int)(Math.random()*455)+15;
			
			removeShape(apple);
			apple = new Oval(x,y,WIDTH,WIDTH);
			//apple.setLocation(x, y);
		}
		apple.setFillColor(Color.RED);
	}
	
	public boolean intersectApple() {
		
		if (Math.abs(head.getX()-apple.getX())<=30 && Math.abs(head.getY()-apple.getY())<=30) {
			return true;
		}
		
		for (int i=0; i<body.size(); i++) {
			if (Math.abs(body.get(i).getX()-apple.getX())<=30 && Math.abs(body.get(i).getY()-apple.getY())<=30) {

				return true;
			}
		}
		return false;
	}
	
	public boolean ateApple() {
		if (Math.abs(head.getX()-apple.getX())<=30 && Math.abs(head.getY()-apple.getY())<=30) {
			numApples++;
			removeShape(apple);
			return true;
		}
		return false;
	}
	
	public void moveSnake(int changeX, int changeY) {
		
		double x = head.getX();
		double y = head.getY();
		head.move(changeX, changeY);
		if (ateApple()) {
			Oval d = new Oval(x,y,WIDTH,WIDTH);
			d.setFillColor(body.get(body.size()-1).getFillColor());
			body.add(0,d);
			makeApple();
		}
		else {
			removeShape(body.remove(body.size()-1));
			
			body.add(0,new Oval(x,y,WIDTH,WIDTH));
			body.get(0).setFillColor(body.get(body.size()-1).getFillColor());
		}
	}
	
	public void moveRight() {
		if (!headLeft) {
			headRight = true;
			headUp = false;
			headDown = false;
			moveSnake(30,0);
		}
	}
	
	public void moveLeft() {
		if (!headRight) {
			headLeft = true;
			headUp = false;
			headDown = false;
			moveSnake(-30,0);
		}
	}
	
	public void moveUp() {
		if (!headDown) {
			headUp = true;
			headLeft = false;
			headRight = false;
			moveSnake(0,-30);
		}
	}
	
	public void moveDown() {
		if (!headUp) {
			headDown = true;
			headLeft = false;
			headRight = false;
			moveSnake(0,30);
		}
	}
	
	public boolean gameOver() {
		
		if (head.getX()<15 || head.getX()>485 || head.getY()<15 || head.getY()>485) {
			return true;
		}
		
		for (int i=1; i<body.size(); i++) {
			if (Math.abs(body.get(i).getX()-head.getX())<30 && Math.abs(body.get(i).getY()-head.getY())<30) {
				return true;
			}
		}
		
		return false;
	}
	
	public void onKeyPressed(String keyText, String e) {
		
		if (!gameOver()) {
			if (keyText.equals("W")) {
				moveUp();
			}
			if (keyText.equals("S")) {
				moveDown();
			}
			if (keyText.equals("A")) {
				moveLeft();
			}
			if (keyText.equals("D")) {
				moveRight();
			}
		}
	}
	
	public void move(Clip clip) throws InterruptedException, UnsupportedAudioFileException, IOException, LineUnavailableException{
		boolean u, d, l, r;
		while (!gameOver()) {
			if (headRight) {
				moveRight();
			}
			else if(headLeft) {
				moveLeft();
			}
			else if(headUp) {
				moveUp();
			}
			else if(headDown) {
				moveDown();
			}
			TimeUnit.MILLISECONDS.sleep(250);
		}
		clip.stop();
		clip.close();
		File gameOverPath = new File("gameover.wav");
		AudioInputStream s2 = AudioSystem.getAudioInputStream(gameOverPath);
		clip.open(s2);
		clip.start();
		
		TimeUnit.SECONDS.sleep(2);
		clip.stop();
		clip.close();
		setEventsEnabled(false);
		
		Text t;
		if (numApples == 0) {
			t = new Text("NO APPLE EATEN!", 30, 50, 50);
		}
		else if (numApples == 1) {
			t = new Text(numApples + " APPLE EATEN!",30,50,50);
		}
		else {
			t = new Text(numApples + " APPLES EATEN!", 30, 50, 50);
		}
	}
}









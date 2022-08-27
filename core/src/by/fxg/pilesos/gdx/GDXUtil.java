package by.fxg.pilesos.gdx;

import java.util.Random;

import com.badlogic.gdx.Gdx;

public class GDXUtil {
	public static int getMouseX() {
		return Gdx.input.getX();
	}
	
	public static int getMouseY() {
		return Gdx.graphics.getHeight() + -Gdx.input.getY();
	}
	
	public static boolean isMouseIn(float x, float y, float x1, float y1) {
		return x <= Gdx.input.getX() && Gdx.input.getX() <= x1 && y <= (Gdx.graphics.getHeight() + -Gdx.input.getY()) && (Gdx.graphics.getHeight() + -Gdx.input.getY()) <= y1;
	}
	
	public static boolean isMouseInArea(float x, float y, float w, float h) {
		return isMouseIn(x, y, x + w, y + h);
	}
	
	public static long getTime() {
		return System.currentTimeMillis();
	}
	
	public static Random rand = new Random();
	public static boolean getRandom(double chance) {
		rand = new Random();
		return 100.0D * rand.nextDouble() - (100.0D - chance) > 0;
	}
}

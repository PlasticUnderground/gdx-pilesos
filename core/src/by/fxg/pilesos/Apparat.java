package by.fxg.pilesos;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;

import by.fxg.pilesos.gdx.SpriteBuffer;

public abstract class Apparat<T extends InputProcessor> extends ApplicationAdapter {
	protected long tick = 1;
	protected T input;
	public int width, height;
	
	public void onCreate(Apparat<T> app) {
		this.width = Gdx.graphics.getWidth();
		this.height = Gdx.graphics.getHeight();
		Pilesos.setApp(app);
	}
	
	public void render() {
		this.tick++;
		this.update(this.width, this.height);
		this.render(this.width, this.height);
	}
	
	public void dispose() {
		super.dispose();
		SpriteBuffer.dispose();
	}
	
	public void stop() {
		Gdx.app.exit();
	}
	
	public T getInput() { return this.input; }
	public long getTick() { return this.tick; }
	abstract public void render(int width, int height);
	abstract public void update(int width, int height);
}

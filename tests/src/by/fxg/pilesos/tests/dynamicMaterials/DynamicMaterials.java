package by.fxg.pilesos.tests.dynamicMaterials;

import org.jrenner.smartfont.SmartFontGenerator;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import by.fxg.pilesos.Apparat;
import by.fxg.pilesos.PilesosInputImpl;

public class DynamicMaterials extends Apparat<Input3D> {
	public static DynamicMaterials instance;
	public BitmapFont appFont;
	public TestManager manager;
	public WorldRenderer worldRenderer;
	
	public void create() {
		this.onCreate(instance = this);
		Gdx.input.setInputProcessor(super.input = new Input3D());
		this.input.setCursorCatched(false);
		SmartFontGenerator fontGenerator = new SmartFontGenerator();
		this.appFont = fontGenerator.createFont(PilesosInputImpl.ALLOWED_CHARACTERS, Gdx.files.internal("assets/font/monogram-extended.ttf"), "monogram", 32); //16 base size
		this.appFont.setUseIntegerPositions(true);
		this.manager = new TestManager();
		this.worldRenderer = new WorldRenderer(this);
	}
	
	public void update(int width, int height) {
		this.worldRenderer.update(this, width, height);
	}
	
	public void render(int width, int height) {
		Gdx.gl.glClearColor(0.12f, 0.12f, 0.12f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT|GL20.GL_DEPTH_BUFFER_BIT);
		this.worldRenderer.render(this, width, height);
	}
	
	public void dispose() {
		super.dispose();
		this.manager.assetManager.dispose();
	}
	
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1280;
		config.height = 720;
		config.resizable = false;
		config.backgroundFPS = 0;
		config.title = "Environment3D Test";
		new LwjglApplication(new DynamicMaterials(), config);
	}
}

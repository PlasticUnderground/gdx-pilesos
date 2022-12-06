package by.fxg.pilesos.tests.dynamicMaterials;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.PixmapPacker;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.Hinting;

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
		this.appFont = this.generateFont(Gdx.files.internal("assets/font/monogram-extended.ttf"), 32);
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
	
	private BitmapFont generateFont(FileHandle file, int size) {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(file);
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.packer = new PixmapPacker(4096, 4096, Format.RGBA8888, 2, false);
		parameter.hinting = Hinting.AutoSlight;
		parameter.flip = false;
		parameter.size = size;
		parameter.characters = PilesosInputImpl.ALLOWED_CHARACTERS;
		parameter.incremental = true;
		return generator.generateFont(parameter);
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

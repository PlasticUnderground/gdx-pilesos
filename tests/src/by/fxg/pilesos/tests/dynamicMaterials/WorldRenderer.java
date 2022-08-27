package by.fxg.pilesos.tests.dynamicMaterials;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;

import by.fxg.pilesos.graphics.SpriteStack;
import by.fxg.pilesos.graphics.TextureFrameBuffer;
import by.fxg.pilesos.graphics.font.Foster;
import by.fxg.pilesos.tests.dynamicMaterials.Input3D.ICameraMovement;
import net.mgsx.gltf.scene3d.scene.SceneAsset;

public class WorldRenderer implements ICameraMovement {
	public PerspectiveCamera camera;
	private Matrix4 tempMatrix = new Matrix4();
	private Vector3 tempMove = new Vector3(), position = new Vector3(), look = new Vector3();
	private Vector2 tempLook = new Vector2();
	private boolean isPaused;
	
	private ModelBatch batch;
	private Environment environment;
	private ModelInstance healthbag, fork, pickle;
	
	private SpriteBatch spriteBatch;
	private Foster foster;
	private TextureFrameBuffer main, stencil;
	private Texture mask;
	private ShaderProgram stencilReplacerProgram;
	
	public WorldRenderer(DynamicMaterials app) {
		this.camera = new PerspectiveCamera(67f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		this.camera.position.set(0.0F, 0.0F, 0.0F);
		this.camera.lookAt(0.0F, 0.0F, 0.0F);
		this.camera.near = 0.1F;
		this.camera.far = 20.0F;
		
		this.batch = new ModelBatch();
		this.environment = new Environment();
		this.environment.set(ColorAttribute.createAmbientLight(1.0F, 1.0F, 1.0F, 1.0F));
		this.environment.set(ColorAttribute.createFog(0.12f, 0.12F, 0.12F, 1.0F));
		
		this.healthbag = new ModelInstance(app.manager.get("assets/models/kuban-healthbag.gltf", SceneAsset.class).scene.model);
		this.healthbag.materials.get(0).set(TextureAttribute.createDiffuse(SpriteStack.getTexture("assets/textures/kuban-healthbag.png")));
		TextureRegion forkPickleTexture = SpriteStack.getTextureRegion("resources/dynamicmaterials/spacepickle/fork-with-pickle.png");
		forkPickleTexture.flip(false, true);
		this.fork = new ModelInstance(app.manager.get("resources/dynamicmaterials/spacepickle/fork.obj", Model.class));
		this.fork.materials.get(0).set(TextureAttribute.createDiffuse(forkPickleTexture));
		this.pickle = new ModelInstance(app.manager.get("resources/dynamicmaterials/spacepickle/pickle.obj", Model.class));
		this.pickle.materials.get(0).set(TextureAttribute.createDiffuse(forkPickleTexture));
		
		this.spriteBatch = new SpriteBatch();
		this.foster = new Foster(app.appFont).setBatch(this.spriteBatch);
		this.main = new TextureFrameBuffer().flip(false, true);
		this.stencil = new TextureFrameBuffer().flip(false, true);
		this.mask = SpriteStack.getTexture("resources/dynamicmaterials/spacepickle/space.png");
		this.mask.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		ShaderProgram.pedantic = false;
		this.stencilReplacerProgram = new ShaderProgram(Gdx.files.internal("resources/dynamicmaterials/spacepickle/pickle.vert").readString(), Gdx.files.internal("resources/dynamicmaterials/spacepickle/pickle.frag").readString());
		if (!this.stencilReplacerProgram.isCompiled()) {
			System.out.println(this.stencilReplacerProgram.getLog());
		} else {
			this.stencilReplacerProgram.bind();
			this.stencilReplacerProgram.setUniformi("u_mainTexture", 1);
			this.stencilReplacerProgram.setUniformi("u_maskTexture", 2);
		}
		Input3D.cameraMovement = this;
	}
	
	public void render(DynamicMaterials app, int width, int height) {
		this.main.capture(0.12F, 0.12F, 0.12F, 1.0F);
		this.batch.begin(this.camera);
		this.batch.render(this.healthbag, this.environment);
		this.batch.render(this.fork, this.environment);
		this.batch.render(this.pickle, this.environment);
		this.batch.flush();
		this.batch.end();
		this.main.endCapture();
		
		//capturing pickle to fbo as a stencil texture to cut off parts of mask in future.
		this.stencil.capture(0F, 0F, 0F, 0F);
		this.batch.begin(this.camera);
		this.batch.render(this.pickle, this.environment);
		this.batch.flush();
		this.batch.end();
		this.stencil.endCapture();
		
		Gdx.gl.glClearColor(0.5f, 0.12f, 0.12f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT|GL20.GL_DEPTH_BUFFER_BIT);
		this.spriteBatch.begin();
		this.spriteBatch.draw(this.main.getTexture(), 0, 0); //rendering main fbo texture
		
		this.spriteBatch.setShader(this.stencilReplacerProgram); //setting shader that will cut off mask texture and render it
		this.mask.bind(2); //binding mask to 2nd channel
		Gdx.gl.glTexParameteri(GL20.GL_TEXTURE_2D, GL20.GL_TEXTURE_WRAP_S, GL20.GL_REPEAT); //making mask repeating uv's
		Gdx.gl.glTexParameteri(GL20.GL_TEXTURE_2D, GL20.GL_TEXTURE_WRAP_T, GL20.GL_REPEAT);
		this.main.getTexture().getTexture().bind(1); //binding main fbo to 1st channel
		Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0); //selecting 0 texture
		this.spriteBatch.draw(this.stencil.getTexture(), 0, 0); //binding and drawing 0th channel
		this.spriteBatch.setShader(null); //resetting shader to draw screen text
		this.foster.setString(String.format("Shader compiled: %b, press \'Enter\' to recompile", this.stencilReplacerProgram.isCompiled())).draw(10, height - 10, Align.left);
		this.spriteBatch.end();
	}
	
	public void update(DynamicMaterials app, int width, int height) {
		float rot360 = app.getTick() % 180 * 2;
		this.healthbag.transform.setToTranslation(0, 0, 0);
		this.healthbag.transform.rotate(0F, 1F, 0F, rot360);
		this.healthbag.transform.translate(0, 0, 2);
		
		this.isPaused = !app.getInput().isCursorCatched();
		if (this.isPaused && app.getInput().isMouseDown(0, false)) {
			app.getInput().setCursorCatched(true);
		} else if (!this.isPaused) {
			if (app.getInput().isKeyboardDown(Keys.ESCAPE, false)) {
				app.getInput().setCursorCatched(false);
				Gdx.input.setCursorPosition(width / 2, height / 2);
			}
			
			float speed = 0.125f;
			if (app.getInput().isKeyboardDown(Keys.W, true)) this.tempMove.add(0.0F, 0.0F, speed);
			if (app.getInput().isKeyboardDown(Keys.S, true)) this.tempMove.add(0.0F, 0.0F, -speed);
			if (app.getInput().isKeyboardDown(Keys.A, true)) this.tempMove.add(speed, 0.0F, 0.0F);
			if (app.getInput().isKeyboardDown(Keys.D, true)) this.tempMove.add(-speed, 0.0F, 0.0F);
			if (app.getInput().isKeyboardDown(Keys.SHIFT_LEFT, true)) this.position.add(0.0F, -speed * 2, 0.0F);
			else if (app.getInput().isKeyboardDown(Keys.SPACE, true)) this.position.add(0.0F, speed * 2, 0.0F);
			
			this.look.add(this.tempLook.x, this.tempLook.y, 0);
			if (this.look.y > 90f) this.look.y = 90f;
			else if (this.look.y < -90f) this.look.y = -90f;
			this.tempMove.rotate(-this.look.x, 0.0F, -1.0F, 0.0F);
			this.position.add(this.tempMove);
			
			this.tempMatrix = new Matrix4();
			this.tempMatrix.setFromEulerAngles(this.look.x, -this.look.y, 0.0F);
			this.camera.direction.set(0, 0, 1);
			this.camera.up.set(0, 1, 0);
			this.camera.rotate(this.tempMatrix);
			this.camera.position.set(this.position);
			
			this.look.x %= 360;
			this.look.y %= 360;
		}
		this.tempLook.setZero();
		this.tempMove.setZero();
		this.camera.update();
	}
	
	public void onCameraMovement(float dx, float dy) {
		this.tempLook.add(dx, dy);
	}
}

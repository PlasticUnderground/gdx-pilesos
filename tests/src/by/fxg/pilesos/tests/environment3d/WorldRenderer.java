package by.fxg.pilesos.tests.environment3d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import by.fxg.pilesos.graphics.SpriteStack;
import by.fxg.pilesos.tests.environment3d.Input3D.ICameraMovement;
import net.mgsx.gltf.scene3d.scene.SceneAsset;

public class WorldRenderer implements ICameraMovement {
	public PerspectiveCamera camera;
	private Matrix4 tempMatrix = new Matrix4();
	private Vector3 tempMove = new Vector3(), position = new Vector3(), look = new Vector3();
	private Vector2 tempLook = new Vector2();
	private boolean isPaused;
	
	private ModelBatch batch;
	private Environment environment;
	private ModelInstance healthbag;
	
	public WorldRenderer(Environment3D app) {
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
		
		Input3D.cameraMovement = this;
	}
	
	public void render(Environment3D app, int width, int height) {
		this.batch.begin(this.camera);
		this.batch.render(this.healthbag, this.environment);
		this.batch.end();
	}
	
	public void update(Environment3D app, int width, int height) {
		this.isPaused = !app.getInput().isCursorCatched();
		if (this.isPaused && app.getInput().isMouseDown(0, false)) {
			app.getInput().setCursorCatched(true);
		} else if (!this.isPaused) {
			if (app.getInput().isKeyboardDown(Keys.ESCAPE, false)) app.getInput().setCursorCatched(false);
			
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

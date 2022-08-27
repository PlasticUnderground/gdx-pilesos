package by.fxg.pilesos.tests.dynamicMaterials;

import com.badlogic.gdx.Gdx;

import by.fxg.pilesos.PilesosInputImpl;

public class Input3D extends PilesosInputImpl {
	public void setCursorCatched(boolean bool) {
		Gdx.input.setCursorCatched(bool);
		this.isCursorCatched = bool;
		if (bool) Gdx.input.setCursorPosition((int)this.tempMX, (int)this.tempMY);
	}
	
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		this.moveCamera(screenX, screenY);
		return false;
	}
	
	public boolean mouseMoved(int screenX, int screenY) {
		this.moveCamera(screenX, screenY);
		return false;
	}
	
	public static ICameraMovement cameraMovement;
	private float tempMX = -1048576F, tempMY = -1048576F;
	public void moveCamera(int screenX, int screenY) {
		if (this.isCursorCatched() && cameraMovement != null) {
			float dx = this.tempMX - screenX;
			float dy = this.tempMY - screenY;
			cameraMovement.onCameraMovement(dx / 10, dy / 10);
			this.tempMX = screenX;
			this.tempMY = screenY;
		} else {
			this.tempMX = this.tempMY = -1048576F;
		}
	}
	
	public static interface ICameraMovement {
		void onCameraMovement(float dx, float dy);
	}
}


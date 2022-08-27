package by.fxg.pilesos.tests.dynamicMaterials;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Model;

import by.fxg.pilesos.utils.JarUtils;
import net.mgsx.gltf.loaders.gltf.GLTFAssetLoader;
import net.mgsx.gltf.scene3d.scene.SceneAsset;

public class TestManager {
	private final Map<String, Class<?>> assetMarkers = new HashMap<>();
	public AssetManager assetManager;
	public boolean isLoaded = false;
	
	public TestManager() {
		this.assetManager = new AssetManager();
		this.assetManager.setLoader(SceneAsset.class, ".gltf", new GLTFAssetLoader());
		this.assetMarkers.put("gltf", SceneAsset.class);
		this.assetMarkers.put("obj", Model.class);
		this.assetMarkers.put("png", Texture.class);
		
		this.loadAssetsFrom("assets/");
		this.loadAssetsFrom("resources/dynamicmaterials/");
		this.assetManager.finishLoading();
		this.isLoaded = true;
	}
	
	private void loadAssetsFrom(String path) {
		for (FileHandle fh : JarUtils.listFromJarIfNecessary(path, true)) {
			if (!fh.isDirectory()) {
				for (String str : this.assetMarkers.keySet()) {
					if (fh.extension().equalsIgnoreCase(str)) {
						this.assetManager.load(fh.path(), this.assetMarkers.get(str));	
					}
				}
			} else {
				this.loadAssetsFrom(path + fh.name() + "/");
			}
		}
	}
	
	public <T> T get(String object, Class<T> clazz) {
		return this.assetManager.get(object, clazz);
	}
}

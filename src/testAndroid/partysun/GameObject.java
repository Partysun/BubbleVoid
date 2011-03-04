package testAndroid.partysun;

import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.opengl.texture.region.TextureRegion;

public class GameObject extends Sprite implements ICollidable
{
	public GameObject(float pX, float pY, float pWidth, float pHeight,
			TextureRegion pTextureRegion) {
		super(pX, pY, pWidth, pHeight, pTextureRegion);
	}

	private int type;

	/**
	 * @param type the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}
	
}

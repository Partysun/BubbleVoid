package testAndroid.partysun;

import org.anddev.andengine.entity.sprite.TiledSprite;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class Enemy extends TiledSprite implements ICollidable{

	private Body body = null;
	private int type;
	
	public Enemy(float pX, float pY, TiledTextureRegion pTiledTextureRegion) {
		super(pX, pY, pTiledTextureRegion);
	}
	
	@Override
	protected void onPositionChanged() {
		super.onPositionChanged();
		// determining enemy speed
		float enemy_speed=Math.abs(body.getLinearVelocity().x)+Math.abs(body.getLinearVelocity().y);
		// if the speed is too slow... (lower than 10)
		if (enemy_speed<15) {
			// calculating the offset
			float speed_offset=15/enemy_speed;
			// multiplying the horizontal and vertical components of the speed
			// by the offset
			Vector2 s = body.getLinearVelocity();
			s.x = body.getLinearVelocity().x*speed_offset;
			s.y = body.getLinearVelocity().y*speed_offset;
			body.setLinearVelocity(s);
		}
	}

	public void setBody(Body body) {
		this.body = body;
	}

	@Override
	public int getType() {
		return type;
	}

	@Override
	public void setType(int type) {
		this.type = type;
	}

}

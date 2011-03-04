package testAndroid.partysun;

import java.util.Iterator;

import org.anddev.andengine.audio.music.Music;
import org.anddev.andengine.audio.sound.Sound;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.camera.hud.HUD;
import org.anddev.andengine.entity.primitive.Rectangle;
import org.anddev.andengine.entity.scene.CameraScene;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.IOnSceneTouchListener;
import org.anddev.andengine.entity.scene.background.SpriteBackground;
import org.anddev.andengine.entity.shape.IShape;
import org.anddev.andengine.entity.shape.Shape;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.sprite.TiledSprite;
import org.anddev.andengine.entity.text.ChangeableText;
import org.anddev.andengine.extension.physics.box2d.PhysicsConnector;
import org.anddev.andengine.extension.physics.box2d.PhysicsFactory;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.sensor.accelerometer.AccelerometerData;
import org.anddev.andengine.sensor.accelerometer.IAccelerometerListener;
import org.anddev.andengine.util.MathUtils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import android.hardware.SensorManager;

/**
 * @author Zatsepin Yura
 */
public class SceneFactory implements IOnSceneTouchListener , IAccelerometerListener {

	private static SceneFactory sceneFactory;
	
	// Local variables
	private MainGameActivity activity;	
	private Camera mainCamera;
	private MenuVoidScene menu;
	
	private GameObject curSprite = null;
	private float scaleOut = 1.0f;
	private MaxStepPhysicsWorld mPhysicsWorld;
	private static final FixtureDef FIXTURE_DEF = PhysicsFactory.createFixtureDef(2, 0.7f, 2.0f);
	private static final FixtureDef FIXTURE_ENEMYDEF = PhysicsFactory.createFixtureDef(7, 1.0f, 2.0f);
	
	private ChangeableText voidText;
	private ChangeableText sqrText;
	private ChangeableText lifeText;
	
	private int voids = 1;
	private int lifes = 3;
	private float VICTORY_SQR = 70f;
	
	private boolean isDrawing = false;
	private boolean isGame = false;
	
	private float curSQRT = 0;
	
	public Sound mCollideSound = null;
	public Sound mSoundBirt = null;
	
	public Music mMusic = null;
	
	/**
	 *  If we made instance of SceneFactory ,
	 *   we only return this instance
	 * @return instance of SceneFactory. 
	 */
	public static SceneFactory getInstance() {
		if(sceneFactory == null)
			sceneFactory = new SceneFactory();
		return sceneFactory;
	}

	public SceneFactory setActivity(MainGameActivity activity) {
		this.activity = activity;
		setCamera(activity.getEngine().getCamera());
		this.menu = new MenuVoidScene(activity);
		return this;
	}
	
	public SceneFactory setCollideSound(Sound mSoundCollision) {
		this.mCollideSound = mSoundCollision;		
		return this;
	}
	
	public SceneFactory setBirtSound(Sound mSoundBirt) {
		this.mSoundBirt = mSoundBirt;		
		return this;
	}
	
	public SceneFactory setMusic(Music mMusic2) {
		this.mMusic = mMusic2;		
		return this;
	}
	//
	
	public static void realiseSceneFactory()
	{
		sceneFactory = null;
	}
	
	private void setCamera(Camera mCamera) {
		this.mainCamera = mCamera;
	}
	
	public Scene createPauseScene() {		
		return menu.createPauseScene();
	}
	
	public Scene createGameScene(int voids, int lvl) {
	//Initialization Game Scene
		isGame = true;
		this.mPhysicsWorld = null;
		this.curSprite=null;
		isDrawing = false;
		this.curSQRT = 0.0f;
		this.voids = voids;
		this.lifes = 3;
	//
		
		final Scene gameScene = new Scene(1)
		{
			 @Override
			protected void onManagedUpdate(float pSecondsElapsed) {
				super.onManagedUpdate(pSecondsElapsed);
				if (curSprite != null && isDrawing && isInGameWorld(curSprite.getX(), curSprite.getY(), curSprite.getWidthScaled())){
					curSprite.setScale(scaleOut);
					scaleOut += 0.04f;	
				}	
	if (mPhysicsWorld!=null)
				  for (Iterator<Body> i =mPhysicsWorld.getBodies().iterator(); i.hasNext();){
					  	Body bb = (Body) i.next();
						IShape check = (IShape) bb.getUserData();
							// determining the distance between the current ball and the one I am drawing
						if(check!=null && isDrawing && curSprite!=null){
							float radiusS = curSprite.getWidthScaled()*0.5f;
							float radiusC = check.getWidthScaled()*0.5f;
							float dist_x=(curSprite.getX())-(check.getX());
							float dist_y=(curSprite.getY())-(check.getY());
							// notice I am not using any square root to make the script lighter
							float distance=dist_x*dist_x+dist_y*dist_y;
							// if balls collide...
							if (distance<(radiusS+radiusC)*(radiusS+radiusC)) {
								isDrawing = false;
								if(check instanceof Enemy)
								{	
									decLifes();
									mCollideSound.play();
									this.getTopLayer().removeEntity(curSprite);
								}
								if(check instanceof GameObject)
								{
									addPhysicBody();									
									mSoundBirt.play();
								}
							}
					}
				  }
			}
		};
		gameScene.setBackground(new SpriteBackground(new Sprite(0, 0, Textures.mBackground.getWidth(), Textures.mBackground.getHeight(), Textures.mBackground)));
		
		Textures.mLvlsTextureRegion.setCurrentTileIndex(lvl-1);
		final float xlvl = ((MainGameActivity.CAMERA_WIDTH - Textures.mLvlsTextureRegion.getTileWidth()) / 2);
		final float ylvl = ((MainGameActivity.CAMERA_HEIGHT - Textures.mLvlsTextureRegion.getTileHeight()) / 2);
		gameScene.getTopLayer().addEntity(new TiledSprite(xlvl, ylvl, Textures.mLvlsTextureRegion));
		final HUD hud = new HUD();
		voidText = new ChangeableText(20, MainGameActivity.CAMERA_HEIGHT-Textures.mScoreBar.getHeight()*0.5f-12, Textures.mFont,
				"VOIDS: ", "VOIDS: XXXX".length());
		
		voidText.setText("VOIDS: "+voids+"");
		
		sqrText = new ChangeableText(120, MainGameActivity.CAMERA_HEIGHT-Textures.mScoreBar.getHeight()*0.5f-12, Textures.mFont,
				"", "XXX+% +  : +XXX+%".length());
		sqrText.setText(""+curSQRT/MainGameActivity.SQRCAMERA*100+"%"+" : "+VICTORY_SQR+"%");
		
		lifeText = new ChangeableText(300, MainGameActivity.CAMERA_HEIGHT-Textures.mScoreBar.getHeight()*0.5f-12, Textures.mFont,
				"LIFES: "+this.lifes, "LIFES: XX".length());
		
		final Sprite scoreBar = new Sprite(3, MainGameActivity.CAMERA_HEIGHT-Textures.mScoreBar.getHeight()-3, Textures.mScoreBar);
		
		hud.getTopLayer().addEntity(lifeText);
		hud.getTopLayer().addEntity(scoreBar);
		hud.getTopLayer().addEntity(sqrText);
		hud.getTopLayer().addEntity(voidText);
		this.mainCamera.setHUD(hud);
		
		gameScene.setOnSceneTouchListener(this);
		
		mPhysicsWorld = new MaxStepPhysicsWorld(40, new Vector2(0, SensorManager.GRAVITY_DEATH_STAR_I), false);

		final Shape ground = new Rectangle(-2, MainGameActivity.CAMERA_HEIGHT+4, MainGameActivity.CAMERA_WIDTH+4, 2);
		final Shape roof = new Rectangle(-2, -4, MainGameActivity.CAMERA_WIDTH+4, 2);
		final Shape left = new Rectangle(-2, -4, 2, MainGameActivity.CAMERA_HEIGHT+8);
		final Shape right = new Rectangle(MainGameActivity.CAMERA_WIDTH+2, -4, 2,MainGameActivity.CAMERA_HEIGHT+8);

		final FixtureDef wallFixtureDef = PhysicsFactory.createFixtureDef(0, 0.5f, 0.5f);
		PhysicsFactory.createBoxBody(mPhysicsWorld, ground, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(mPhysicsWorld, roof, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(mPhysicsWorld, left, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(mPhysicsWorld, right, BodyType.StaticBody, wallFixtureDef);

		gameScene.getBottomLayer().addEntity(ground);
		gameScene.getBottomLayer().addEntity(roof);
		gameScene.getBottomLayer().addEntity(left);
		gameScene.getBottomLayer().addEntity(right);

		gameScene.registerUpdateHandler(mPhysicsWorld);
		
		for(int i=0;i<lvl;i++)
		{
			createEnemyBubble(gameScene, MathUtils.random(40, 400) , MathUtils.random(40, 250));			
		}
		
		return gameScene;
	}

	public void createEnemyBubble(Scene gameScene, int i, int j) {
		Textures.mEnemyTextureRegion.setCurrentTileIndex(MathUtils.random(0, 1));
		Enemy enemy = new Enemy(i, j, Textures.mEnemyTextureRegion.clone());
		enemy.setType(GameObject.GOODBUBBLE);
		gameScene.getTopLayer().addEntity(enemy);
		Body body;		
		body = PhysicsFactory.createCircleBody(SceneFactory.this.mPhysicsWorld,
				enemy, BodyType.DynamicBody, FIXTURE_ENEMYDEF);
		body.setUserData(enemy);
		enemy.setUpdatePhysics(false);
		SceneFactory.this.mPhysicsWorld 
				.registerPhysicsConnector(new PhysicsConnector(
						enemy, body, true, true, true, false));
		enemy.setBody(body);
	}

	@Override
	public void onAccelerometerChanged(final AccelerometerData pAccelerometerData) {
		if (this.mPhysicsWorld!=null)
		this.mPhysicsWorld.setGravity(new Vector2(pAccelerometerData.getY(), pAccelerometerData.getX()));
	}
	
	@Override
	public boolean onSceneTouchEvent(final Scene pScene,
			final TouchEvent pSceneTouchEvent) {
		float x = pSceneTouchEvent.getX();
		float y = pSceneTouchEvent.getY();
		if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN) {
			if (isInGameWorld(x, y,Textures.mEnemyTextureRegion.getTileWidth())) {
				activity.runOnUpdateThread(new Runnable() {
					@Override
					public void run() {
						float x = pSceneTouchEvent.getX();
						float y = pSceneTouchEvent.getY();
						GameObject bubble = new GameObject(x,y,32,32,Textures.mMainTextureRegion);
						bubble.setPosition(x - bubble.getWidth() * 0.5f, y
								- bubble.getHeight() * 0.5f);
						scaleOut = 1.0f;
						curSprite = bubble;
						isDrawing = true;
						pScene.getTopLayer().addEntity(bubble);
					}
				});
			}

			return true;
		}
		if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_MOVE) {
			if (curSprite != null && isInGameWorld(x , y, curSprite.getWidthScaled())) {					
				curSprite.setPosition(x - curSprite.getWidth() * 0.5f, y- curSprite.getHeight() * 0.5f);}

			return true;
		}
		if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP) {
			if (isDrawing) {
				isDrawing = false;
				addPhysicBody();
				mSoundBirt.play();
				}
			return true;
		}
		return false;
	}
	

	synchronized
	private void addPhysicBody()
	{
		activity.runOnUpdateThread(new Runnable() {
			@Override
			public void run() {
		GameObject clonecur = curSprite;
		curSprite = null;
		
		if(clonecur!=null){			
		Body body;
		body = PhysicsFactory.createCircleBody(SceneFactory.this.mPhysicsWorld,
				clonecur, BodyType.DynamicBody, FIXTURE_DEF);
		clonecur.setUpdatePhysics(false);
		body.setUserData(clonecur);
		SceneFactory.this.mPhysicsWorld
				.registerPhysicsConnector(new PhysicsConnector(
						clonecur, body, true, true, false, false));
		incSQRT(clonecur);	
		decVoids();			
		}}});
	}
	
	/**
	 * Метод вычисления площади кругов, которые заполняет игрок.
	 * @param clonecur - последний выросший объект
	 */
	protected void incSQRT(GameObject clonecur) {
		curSQRT += clonecur.getWidthScaled()*clonecur.getHeightScaled();
		float sqr = curSQRT/MainGameActivity.SQRCAMERA*100;
		java.text.DecimalFormat dec_format = new java.text.DecimalFormat("##.#");
		sqrText.setText(""+dec_format.format(sqr)+"%" + " : "+VICTORY_SQR+"%");
		if (Float.compare(sqr, VICTORY_SQR)>=0){
			sqrText.setText("VICTORY");
			activity.WinGame();}
			}
	

	/*
	 * Метод уменьшения количества Voids. Если их будет меньше 
	 * ограничения на уровень то игрок проиграл.
	 * Также происходит вывод очков в консоль экрана.
	 */
	protected void decVoids() {
		--this.voids;
		voidText.setText("VOIDS: "+voids+"");
		if (voids==0 && isGame)	{		
			isDrawing = false;
			curSprite = null;
			activity.gameOver();
		}
	}
	
	private void decLifes()
	{
		--this.lifes;
		lifeText.setText("LIFES: "+lifes+"");
		if (lifes<1 && isGame)	{		
			isDrawing = false;
			curSprite = null;
			activity.gameOver();
		}
	}

	private boolean isInGameWorld(float x , float y, float wx )
	{
		float wxh = wx*0.5f;
		//if(x>wxh && x <MainGameActivity.CAMERA_WIDTH-wxh&& y>wxh && y<MainGameActivity.CAMERA_HEIGHT-wxh)
		//	return true;
		if (x-wxh<0||x+wxh>MainGameActivity.CAMERA_WIDTH||y-wxh<0||y+wxh>MainGameActivity.CAMERA_HEIGHT) {
			return false;
		}

		
		return true;
	}

	public CameraScene GameOverScene() {
		return this.menu.makeGameOverScene();
	}	
	
	public Scene startGameMenu()
	{
		return this.menu.makeGameMenuScene();
	}

}


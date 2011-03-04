package testAndroid.partysun;

import org.anddev.andengine.entity.scene.CameraScene;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.SpriteBackground;
import org.anddev.andengine.entity.shape.modifier.MoveModifier;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.util.modifier.ease.EaseBounceIn;
import org.anddev.andengine.util.modifier.ease.EaseLinear;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class MenuVoidScene {
	
	MainGameActivity activity;
	
	public MenuVoidScene(MainGameActivity activity) {
		this.activity = activity;
	}
	
	public CameraScene makeGameOverScene()
	{
		CameraScene mGameOverScene = new CameraScene(1, activity.getEngine().getCamera());
		/* Make the 'GameOver'-label centered on the camera. */
		final float x = ((MainGameActivity.CAMERA_WIDTH - Textures.mGameOverTextureRegion.getWidth()) / 2);
		final float y = 50;
		final Sprite gameOverSprite = new Sprite(x, y, Textures.mGameOverTextureRegion);
		mGameOverScene.getTopLayer().addEntity(gameOverSprite);
		/* Makes the paused Game look through. */
		mGameOverScene.setBackgroundEnabled(false);
		float center_x = ((MainGameActivity.CAMERA_WIDTH - Textures.mMenuResetTextureRegion.getWidth()) / 2);
		MoveModifier moveModifier = new MoveModifier(1.0f, 0,center_x ,120 , 120, EaseBounceIn.getInstance());
		moveModifier.setRemoveWhenFinished(true);
		final Sprite menuRetry =  new Sprite(center_x, 120, Textures.mMenuResetTextureRegion) {
	            @Override
				public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
	            	activity.curLvl = 1;
	             	loadSoundPref();
	            	activity.CreateNewLevel();
	                return false;
	            }
	        };
	    menuRetry.addShapeModifier(moveModifier);
	    
	    final Sprite menuOptions =  new Sprite(center_x, 170, Textures.mMenuOptionsTextureRegion) {
	    	@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
	    			switchActivity(Preference.class,false);
	    			return false;
	            }
	        };
	        
	        final Sprite quitMenuItem = new Sprite(center_x, 220,Textures.mMenuQuitTextureRegion){
	        	public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
	    			System.exit(1);
	    			return false;
	            }
	        };
	        moveModifier = new MoveModifier(1.0f, MainGameActivity.CAMERA_WIDTH ,center_x ,170 , 170, EaseLinear.getInstance());
	        menuOptions.addShapeModifier(moveModifier);
	        moveModifier = new MoveModifier(1.0f, 0,center_x ,220 , 220, EaseLinear.getInstance());
	        quitMenuItem.addShapeModifier(moveModifier);
	        mGameOverScene.registerTouchArea(menuRetry);
	        mGameOverScene.registerTouchArea(menuOptions);
	        mGameOverScene.registerTouchArea(quitMenuItem);
	
	        mGameOverScene.getTopLayer().addEntity(menuOptions);
	        mGameOverScene.getTopLayer().addEntity(menuRetry);
	        mGameOverScene.getTopLayer().addEntity(quitMenuItem);
	    
	   // float center_x_text = (MainGameActivity.CAMERA_WIDTH - 200) * 0.5f;
	    
	/*    //Load Score from preference
	    SharedPreferences prefs = getPreferences(0);
	    int highscore = prefs.getInt("highScore", -1);
	    int bestscore = prefs.getInt("bestScore", -1);
	    
	    hud.getTopLayer().addEntity(new Text(center_x_text, 170, Textures.mFont,"Last Score: "+highscore+""));
	    hud.getTopLayer().addEntity( new Text(center_x_text, 192, Textures.mFont,"Best Score: "+ bestscore+""));
	  */  
   // activity.mMusic.pause();
		return mGameOverScene;
	} 
	
	public Scene createPauseScene() {		
		CameraScene pausemenu = new CameraScene(1, activity.getEngine().getCamera());

		final float x = ((MainGameActivity.CAMERA_WIDTH - Textures.mMenuPauseTextureRegion.getWidth()) / 2);
		final Sprite pauseSprite = new Sprite(x, 40, Textures.mMenuPauseTextureRegion);
		pausemenu.getTopLayer().addEntity(pauseSprite);

		pausemenu.setBackgroundEnabled(false);
		
		//pausemenu.setOnAreaTouchListener(this);	
		float center_x = ((MainGameActivity.CAMERA_WIDTH - Textures.mMenuResumeTextureRegion.getWidth()) / 2);
		
		MoveModifier moveModifier = new MoveModifier(1.0f, 0,center_x ,120 , 120, EaseLinear.getInstance());
		moveModifier.setRemoveWhenFinished(true);
	
		final Sprite menuPlay =  new Sprite(center_x, 120, Textures.mMenuResumeTextureRegion) {
	            @Override
				public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
	            	if (activity.getEngine().getScene().hasChildScene()){ 
	            	 	loadSoundPref();
	            		activity.getEngine().getScene().back();
		    			}
	                return false;
	            }
	        };
	        menuPlay.addShapeModifier(moveModifier);
	    final Sprite menuOptions =  new Sprite(center_x, 170, Textures.mMenuOptionsTextureRegion) {
	    	@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
	    		switchActivity(Preference.class,false);
	    			return false;
	            }
	        };
	        
	        final Sprite quitMenuItem = new Sprite(center_x, 220,Textures.mMenuQuitTextureRegion){
	        	public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
	    			System.exit(1);
	    			return false;
	            }
	        };
	        moveModifier = new MoveModifier(1.0f, MainGameActivity.CAMERA_WIDTH ,center_x ,170 , 170, EaseLinear.getInstance());
	        menuOptions.addShapeModifier(moveModifier);
	        moveModifier = new MoveModifier(1.0f, 0,center_x ,220 , 220, EaseLinear.getInstance());
	        quitMenuItem.addShapeModifier(moveModifier);
	        pausemenu.registerTouchArea(menuPlay);
	        pausemenu.registerTouchArea(menuOptions);
	        pausemenu.registerTouchArea(quitMenuItem);
	
	        pausemenu.getTopLayer().addEntity(menuOptions);
	        pausemenu.getTopLayer().addEntity(menuPlay);
	        pausemenu.getTopLayer().addEntity(quitMenuItem);

		return pausemenu;
	}
	
	public Scene makeGameMenuScene()
	{
		Scene menu = new Scene(1);

		final float x = ((MainGameActivity.CAMERA_WIDTH - Textures.mTitleTextureRegion.getWidth()) / 2);
		final Sprite pauseSprite = new Sprite(x, 40, Textures.mTitleTextureRegion);
		menu.getTopLayer().addEntity(pauseSprite);

		menu.setBackground(new SpriteBackground(new Sprite(0, 0, Textures.mBackground.getWidth(), Textures.mBackground.getHeight(), Textures.mBackground)));
		
		//pausemenu.setOnAreaTouchListener(this);	
		float center_x = ((MainGameActivity.CAMERA_WIDTH - Textures.mMenuPlayTextureRegion.getWidth()) / 2);
		
		MoveModifier moveModifier = new MoveModifier(1.0f, 0,center_x ,120 , 120, EaseLinear.getInstance());
		moveModifier.setRemoveWhenFinished(true);
	
		//Textures.mEnemyTextureRegion.setCurrentTileIndex(MathUtils.random(0, 1));
		//final TiledSprite face = new TiledSprite(MathUtils.random(40,300), MathUtils.random(40,250), Textures.mEnemyTextureRegion);
		//face.addShapeModifier(new ScaleModifier(3, 1, MathUtils.random(2, 3), EaseBounceOut.getInstance()));
		//menu.getTopLayer().addEntity(face);
		
		final Sprite menuPlay =  new Sprite(center_x, 120, Textures.mMenuPlayTextureRegion) {
	            @Override
				public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
	            	//START THE GAME
	            	loadSoundPref();
	            	activity.CreateNewLevel();	            	
	                return false;
	            }
	        };
	        menuPlay.addShapeModifier(moveModifier);
	    final Sprite menuOptions =  new Sprite(center_x, 170, Textures.mMenuOptionsTextureRegion) {
	    	@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
	    		switchActivity(Preference.class,false);
	    			return false;
	            }
	        };
	        
	        final Sprite quitMenuItem = new Sprite(center_x, 220,Textures.mMenuQuitTextureRegion){
	        	public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
	    			System.exit(1);
	    			return false;
	            }
	        };
			
	        moveModifier = new MoveModifier(1.0f, MainGameActivity.CAMERA_WIDTH ,center_x ,170 , 170, EaseLinear.getInstance());
	        menuOptions.addShapeModifier(moveModifier);
	        moveModifier = new MoveModifier(1.0f, 0,center_x ,220 , 220, EaseLinear.getInstance());
	        quitMenuItem.addShapeModifier(moveModifier);
	        menu.registerTouchArea(menuPlay);
	        menu.registerTouchArea(menuOptions);
	        menu.registerTouchArea(quitMenuItem);
	
	        menu.getTopLayer().addEntity(menuOptions);
	        menu.getTopLayer().addEntity(menuPlay);
	        menu.getTopLayer().addEntity(quitMenuItem);

		return menu;
	}
	
	public void switchActivity(Class <?> cls, boolean finish) {
		Intent gameIntent = new Intent(activity,cls);
		activity.startActivity(gameIntent);		
		if (finish)
			activity.finish();
	}
	
	private void loadSoundPref()
	{
		SharedPreferences prefs = PreferenceManager
		.getDefaultSharedPreferences(activity);
		boolean isSound = prefs.getBoolean("soundPref", true);
		if (!isSound)
		{
			//mMusic.setVolume(0f);
			SceneFactory.getInstance().mSoundBirt.setVolume(0f);
			SceneFactory.getInstance().mCollideSound.setVolume(0f);
			SceneFactory.getInstance().mMusic.setVolume(0f);
		}else
		{
			SceneFactory.getInstance().mSoundBirt.setVolume(4f);
			SceneFactory.getInstance().mCollideSound.setVolume(4f);
			SceneFactory.getInstance().mMusic.setVolume(4f);
		}
	}
}

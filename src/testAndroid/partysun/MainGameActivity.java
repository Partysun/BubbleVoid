package testAndroid.partysun;

import java.io.IOException;

import org.anddev.andengine.audio.music.Music;
import org.anddev.andengine.audio.music.MusicFactory;
import org.anddev.andengine.audio.sound.Sound;
import org.anddev.andengine.audio.sound.SoundFactory;
import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.camera.hud.HUD;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.opengl.view.RenderSurfaceView;
import org.anddev.andengine.ui.activity.BaseGameActivity;
import org.anddev.andengine.util.Debug;


import android.view.KeyEvent;


public class MainGameActivity extends BaseGameActivity {
	
    public static final int CAMERA_WIDTH = 480;
    public static final int CAMERA_HEIGHT = 320;
    
	private static final int STATUS_GAMEOVER = 1;
	private int statusScene = 0;
	
	public static final float SQRCAMERA = CAMERA_WIDTH * CAMERA_HEIGHT;

	private Camera mCamera;
	
	public int curLvl = 1;
	
	Music mMusic2;
	  protected int getLayoutID() {
          return R.layout.main;
  }

  protected int getRenderSurfaceViewID() {
          return R.id.xml_rendersurfaceview;
  }
  @Override
  protected void onSetContentView() {
          super.setContentView(this.getLayoutID());
          this.mRenderSurfaceView = (RenderSurfaceView) this.findViewById(this.getRenderSurfaceViewID());
          this.mRenderSurfaceView.setEGLConfigChooser(false);
          this.mRenderSurfaceView.setRenderer(this.mEngine);
  }
    @Override
    public Engine onLoadEngine() {
            this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
            return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.mCamera).setNeedsMusic(true).setNeedsSound(true));
    }

    @Override
    public void onLoadResources() {
		Textures.load(this);
		MusicFactory.setAssetBasePath("music/");
		SoundFactory.setAssetBasePath("music/");
		Sound mSoundCollision = null , mSoundBirt = null;
		try {
			mMusic2 = MusicFactory.createMusicFromAsset(this.mEngine
					.getMusicManager(), this,
			"music2.ogg");
	this.mMusic2.setLooping(true);
			mSoundCollision = SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, "collide.ogg");
			mSoundBirt =  SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, "birth.ogg");			
		} catch (final IOException e) {
			Debug.e("Error", e);
		}
		SceneFactory.getInstance().setActivity(this).setCollideSound(mSoundCollision).setBirtSound(mSoundBirt).setMusic(mMusic2);
		this.enableAccelerometerSensor(SceneFactory.getInstance());
    }

    @Override
    public Scene onLoadScene() {
    	mMusic2.play();
		return SceneFactory.getInstance().startGameMenu();
    }

    @Override
    public void onLoadComplete() {

    }

    @Override
	public boolean onKeyDown(final int pKeyCode, final KeyEvent pEvent) {
    	if (statusScene != STATUS_GAMEOVER){
    	if (pKeyCode == KeyEvent.KEYCODE_MENU
				&& pEvent.getAction() == KeyEvent.ACTION_DOWN) {
			if (this.mEngine.getScene().hasChildScene()) {
				/* Remove the menu and reset it. */
				this.mEngine.getScene().back();
				mMusic2.play();
			} else {
				/* Attach the menu. */
				pause();
			}
		}}
		return true;
	}

    private void pause()
    {
		mMusic2.pause();
		this.mEngine.getScene().setChildScene(
				SceneFactory.getInstance().createPauseScene(),
				false, true, true);
    }
    
	public void gameOver() {
		statusScene = STATUS_GAMEOVER;
		this.mEngine.getScene().setChildScene(
				SceneFactory.getInstance().GameOverScene(), false, true, true);	
		
	}

	public void WinGame() {
		for (int i = 0; i < mEngine.getScene().getLayerCount(); ++i) {
			mEngine.getScene().getLayer(i).clear();
		}
		mEngine.getCamera().setHUD(new HUD());
		mEngine.getScene().reset();
		mEngine.getScene().clearChildScene();
		mEngine.getScene().clearTouchAreas();
		mEngine.getScene().clearUpdateHandlers();

		++curLvl;
		CreateNewLevel();	
	}
	
	public void CreateNewLevel()
	{
		statusScene = 2;
		mEngine.setScene(SceneFactory.getInstance().createGameScene(curLvl+3,curLvl));	
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		if (statusScene !=STATUS_GAMEOVER)
			pause();
	}
	
	
	
}



 


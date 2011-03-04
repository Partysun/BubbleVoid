package testAndroid.partysun;

import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.font.FontFactory;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.ui.activity.BaseGameActivity;

import android.graphics.Color;

public class Textures
{
	public static Texture mMenuTexture;
	public static Texture mTexture;
	public static Texture mFontTexture;
	public static Texture mBackTexture;
	public static Texture mStatusTexture;
	public static Texture mTexture2;
	public static TextureRegion mMenuResetTextureRegion;
	public static TextureRegion mMenuQuitTextureRegion;
	public static TextureRegion mGameOverTextureRegion;
	public static TextureRegion mMenuPauseTextureRegion;
	public static TextureRegion mMenuOptionsTextureRegion;
	public static TextureRegion mMenuPlayTextureRegion;
	public static TextureRegion mMenuResumeTextureRegion;
	public static TextureRegion mTitleTextureRegion;
	public static TextureRegion mMainTextureRegion;
	public static TextureRegion mBackground;
	public static TextureRegion mScoreBar;
	public static Font mFont;
	public static TiledTextureRegion mEnemyTextureRegion;
	public static TiledTextureRegion mLvlsTextureRegion;
	public static Texture mTextureLvls;
	
	public static void load(BaseGameActivity activity)
	{
		TextureRegionFactory.setAssetBasePath("gfx/");
		
		mMenuTexture = new Texture(256, 512, TextureOptions.BILINEAR);
		mMenuResetTextureRegion = TextureRegionFactory.createFromAsset(
				mMenuTexture, activity, "retrybutton.png", 0, 0);
		mMenuQuitTextureRegion = TextureRegionFactory.createFromAsset(
				mMenuTexture, activity, "quitbutton.png", 0, 42);
		mMenuPauseTextureRegion = TextureRegionFactory.createFromAsset(
				mMenuTexture, activity, "paused.png", 0, 83);
		mMenuOptionsTextureRegion = TextureRegionFactory.createFromAsset(
				mMenuTexture, activity, "options.png", 0, 135);
		mMenuPlayTextureRegion = TextureRegionFactory.createFromAsset(
				mMenuTexture, activity, "play_button.png", 0, 178);
		mMenuResumeTextureRegion = TextureRegionFactory.createFromAsset(
				mMenuTexture, activity, "resume.png", 0, 220);
		
		mFontTexture = new Texture(256, 256,
				TextureOptions.BILINEAR);
		FontFactory.setAssetBasePath("font/");
		mFont = FontFactory.createFromAsset(mFontTexture, activity, "font.TTF", 16, true, Color.WHITE);

		activity.getEngine().getTextureManager().loadTexture(mFontTexture);
		activity.getEngine().getFontManager().loadFont(mFont);

		mTexture = new Texture(128, 64,
				TextureOptions.BILINEAR);

		mEnemyTextureRegion = TextureRegionFactory.createTiledFromAsset(
				mTexture, activity, "balls.png", 0, 0, 2, 1);
		mTexture2 = new Texture(512,512,TextureOptions.BILINEAR);
		mMainTextureRegion = TextureRegionFactory.createFromAsset(
				mTexture2, activity, "mainball.png", 0, 0);
		
		mStatusTexture = new Texture(512,128, TextureOptions.BILINEAR);
		mGameOverTextureRegion = TextureRegionFactory.createFromAsset(mStatusTexture, activity, "game_over.png", 0, 0);	
		mTitleTextureRegion = TextureRegionFactory.createFromAsset(mStatusTexture, activity, "voidname.png", 0, 54);
		
		mTextureLvls = new Texture(512,128, TextureOptions.BILINEAR);
		mLvlsTextureRegion =  TextureRegionFactory.createTiledFromAsset(
				mTextureLvls, activity, "lvls.png", 0, 0, 5, 1);
		
		mBackTexture = new Texture(512, 512, TextureOptions.BILINEAR);
		mBackground = TextureRegionFactory.createFromAsset(mBackTexture, activity, "voidback.png", 0, 0);		
		mScoreBar = TextureRegionFactory.createFromAsset(mBackTexture, activity, "scorebar.png", 0, 321);
		
		activity.getEngine().getTextureManager().loadTextures(mTexture, mMenuTexture, mStatusTexture,mBackTexture,mTexture2,mTextureLvls);
		activity.getEngine().getFontManager().loadFont(mFont);
       	}
	public static void unload(BaseGameActivity activity)
	{
		//TODO add unload textures...
		activity.getEngine().getTextureManager().unloadTextures(mFontTexture, mMenuTexture, mStatusTexture,mBackTexture,mTexture2,mTextureLvls);
	}
}

package testAndroid.partysun;

import org.anddev.andengine.extension.physics.box2d.PhysicsWorld;

import com.badlogic.gdx.math.Vector2;

public class MaxStepPhysicsWorld extends PhysicsWorld {
	  private final float mStepLength;
	  public MaxStepPhysicsWorld(final int pStepsPerSecond, final Vector2 pGravity,
	    final boolean pAllowSleep) {
	      super(pGravity, pAllowSleep);
	      this.mStepLength = 1.0f / pStepsPerSecond;
	  }
	 
	  @Override
	  public void onUpdate(final float pSecondsElapsed) {
	      float stepLength = pSecondsElapsed;
	      if(pSecondsElapsed>= this.mStepLength){
	          stepLength = this.mStepLength;
	      }
	      this.mRunnableHandler.onUpdate(pSecondsElapsed);
	      this.mWorld.step(stepLength, this.mVelocityIterations,
	        this.mPositionIterations);
	      this.mPhysicsConnectorManager.onUpdate(pSecondsElapsed);
	  }
	}
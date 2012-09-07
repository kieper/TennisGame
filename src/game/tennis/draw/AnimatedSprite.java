/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game.tennis.draw;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Rect;

/**
 *
 * @author Kieper
 */
public class AnimatedSprite extends GraphicObject {

    private Rect mSRectangle;
    private int mFPS; // FPS of animation
    private int mNoOfFrames; //Number of frames
    private int mCurrentFrame; //Currently set frame
    private long mFrameTimer;  //Timer to remember time of execution(need to set speed animation)
    private Canvas mCan;
    private int mSpriteHeight; //Height of 1 frame
    private int mSpriteWidth;  //Width of 1 frame
    private Rect mDst;
    private Bitmap bmpA = null; 

    public AnimatedSprite(Bitmap bitmap) {
        super(bitmap);

        mSRectangle = new Rect(0, 0, 0, 0);
        mFrameTimer = 0;
        mCurrentFrame = 0;
        mDst = new Rect(0, 0, 0, 0);
    }

    public AnimatedSprite(Bitmap bitmap, Speed speed) {
        super(bitmap, speed);

        mSRectangle = new Rect(0, 0, 0, 0);
        mFrameTimer = 0;
        mCurrentFrame = 0;
        mDst = new Rect(0, 0, 0, 0);
    }

    public void Initalise(int Height, int Width, int theFPS, int theFrameCount) {
        mSpriteHeight = Height;
        mSpriteWidth = Width;
        mDst.bottom = mSRectangle.bottom = mSpriteHeight;
        mDst.right = mSRectangle.right = mSpriteWidth;
        mFPS = 1000 / theFPS;
        mNoOfFrames = theFrameCount;
        bmpA = Bitmap.createBitmap(this.mSpriteWidth, this.mSpriteHeight, Config.ALPHA_8);
    }

    public void Update(long GameTime) {
        mSRectangle.left = mCurrentFrame * mSpriteWidth;
        mSRectangle.right = mSRectangle.left + mSpriteWidth;
        if (GameTime > mFrameTimer + mFPS) {
            mFrameTimer = GameTime;
            mCurrentFrame += 1;

            if (mCurrentFrame >= mNoOfFrames) {
                mCurrentFrame = 0;
            }
        }
    }

    @Override
    public int getWidth() {
        return this.mSpriteWidth;
    }

    @Override
    public int getHeight() {
        return this.mSpriteWidth;
    }

    @Override
    public Bitmap getGraphic() {
        bmpA = Bitmap.createBitmap(this.mSpriteWidth, this.mSpriteHeight, Config.ALPHA_8);
        mCan = new Canvas(bmpA);

        mCan.drawBitmap(super.getGraphic(), mSRectangle, mDst, null);
        return bmpA;
    }
    
    public void draw(Canvas canvas){
    	canvas.drawBitmap(getGraphic(),(int) getSpeed().getX(),(int) getSpeed().getY(), null);
    }
}

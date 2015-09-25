package com.example.applecatcher;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import com.example.applecatcher.R;

public class TheGame extends GameThread {

	List<Apple> apples = new ArrayList<Apple>();
	int counter = 0; 
	Random random = new Random();
	
	
	Bitmap mPaddle;
	Bitmap appleBitmap;

	
	float mPaddleX = 0;

	
	public TheGame(GameView gameView) {
		super(gameView);
		
		appleBitmap = BitmapFactory.decodeResource(gameView.getContext()
				.getResources(), R.drawable.jabuka);

		
		mPaddle = BitmapFactory.decodeResource(gameView.getContext()
				.getResources(), R.drawable.kosara);

	}

	
	@Override
	public void setupBeginning() {
		
		mPaddleX = mCanvasWidth / 2;
		apples.clear();
		Apple apple = new Apple(mCanvasWidth / 2, -20, mCanvasHeight / 4,
				appleBitmap);
		apples.add(apple);
		apple = new Apple(mCanvasWidth / 4, -20, mCanvasHeight / 3, appleBitmap);
		apples.add(apple);
		apple = new Apple(mCanvasWidth / 6, -12, mCanvasHeight / 2, appleBitmap);
		apples.add(apple);
	}

	@Override
	protected void doDraw(Canvas canvas) {
		
		if (canvas == null)
			return;

		
		super.doDraw(canvas);


		
		for (Apple apple : apples) {
			apple.draw(canvas);
		}

		
		canvas.drawBitmap(mPaddle, mPaddleX - mPaddle.getWidth() / 2,
				mCanvasHeight - mPaddle.getHeight() / 2, null);

	}

	@Override
	protected void actionOnTouch(float x, float y) {
		if (x < mPaddle.getWidth() / 2) {
			mPaddleX = mPaddle.getWidth() / 2;
		} else if (x > mPaddle.getWidth() / 2) {
			mPaddleX = mPaddle.getWidth() / 2;
		} else {
			mPaddleX = x;

		}
	}


	@Override
	protected void actionWhenPhoneMoved(float xDirection, float yDirection,
			float zDirection) {

		if (mPaddleX >= 0 && mPaddleX <= mCanvasWidth) {

			mPaddleX = mPaddleX - xDirection;


			if (mPaddleX < 0)
				mPaddleX = 0;
			if (mPaddleX > mCanvasWidth)
				mPaddleX = mCanvasWidth;
		}

	}

	
	@Override
	protected void updateGame(float secondsElapsed) {

		for (Apple apple : apples) {
			if (hasCollision(apple)) {
				updateScore(1);
				apple.reset(random.nextInt(mCanvasWidth), -20, mCanvasHeight / 3);
			}
			apple.update(secondsElapsed);
			if (apple.getPosY() >= mCanvasHeight) {
				setState(GameThread.STATE_LOSE);
				return;
			}
		}
	}

	private boolean hasCollision(Apple apple) {
		float appleY = apple.getPosY();
		float basketY = mCanvasHeight - mPaddle.getHeight() / 2;
		if (appleY >= mCanvasHeight || appleY < basketY) {
			return false;
		}

		float rightBasket = mPaddleX + mPaddle.getWidth() / 2;
		float leftBasket = mPaddleX - mPaddle.getWidth() / 2;
		float right = apple.getPosX() + apple.getWidth() / 2;
		float left = apple.getPosX() - apple.getWidth() / 2;
		if (left > leftBasket && left < rightBasket)
			return true;
		else if (right > leftBasket && right < rightBasket)
			return true;

		return false;
	}

}
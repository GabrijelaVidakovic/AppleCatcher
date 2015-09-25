package com.example.applecatcher;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Apple {
	
	private float posX;
	private float posY;
	private float speed;
	private Bitmap bitmap;
	
	public Apple(float posX, float posY, float speed, Bitmap bitmap) {
		reset(posX, posY, speed);
		this.bitmap = bitmap;
	}

	public float getPosX() {
		return posX;
	}

	public float getPosY() {
		return posY;
	}

	public float getSpeed() {
		return speed;
	}

	public void draw(Canvas canvas) {
		canvas.drawBitmap(bitmap, posX - bitmap.getWidth() / 2, posY - bitmap.getHeight() / 2, null);
	}

	public void update(float secondsElapsed) {
		posY += secondsElapsed * speed;
	}

	public void reset(float posX, float posY, float speed) {
		this.posX = posX;
		this.posY = posY;
		this.speed = speed;
	}

	public int getWidth() {
		return bitmap.getWidth();
	}
	
	
}

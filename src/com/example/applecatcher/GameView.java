package com.example.applecatcher;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
	private volatile GameThread thread;

	private SensorEventListener sensorAccelerometer;

	
	private Handler mHandler;
	
	
	private TextView mScoreView;
	private TextView mStatusView;


	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);

		
		SurfaceHolder holder = getHolder();
		holder.addCallback(this);
		
		
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message m) {
				if(m.getData().getBoolean("score")) {
					mScoreView.setText(m.getData().getString("text"));
				}
				else {		
					
					mStatusView.setVisibility(m.getData().getInt("viz"));
					mStatusView.setText(m.getData().getString("text"));
				}
 			}
		};
	}
	
	
	public void cleanup() {
		this.thread.setRunning(false);
		this.thread.cleanup();
		
		this.removeCallbacks(thread);
		thread = null;
		
		this.setOnTouchListener(null);
		sensorAccelerometer = null;
		
		SurfaceHolder holder = getHolder();
		holder.removeCallback(this);
	}
	
	

	public void setThread(GameThread newThread) {

		thread = newThread;

		setOnTouchListener(new View.OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				if(thread!=null) {
					return thread.onTouch(event);
				}
				else return false;
			}

		});

		this.sensorAccelerometer = new SensorEventListener() {

			public void onAccuracyChanged(Sensor arg0, int arg1) {
				
			}

			public void onSensorChanged(SensorEvent event) {
				if(thread!=null) {
					if (thread.isAlive()) {
						thread.onSensorChanged(event);
					}
				}
			}
		};

		setClickable(true);
		setFocusable(true);
	}
	
	public GameThread getThread() {
		return thread;
	}

	public TextView getStatusView() {
		return mStatusView;
	}

	public void setStatusView(TextView mStatusView) {
		this.mStatusView = mStatusView;
	}
	
	public TextView getScoreView() {
		return mScoreView;
	}

	public void setScoreView(TextView mScoreView) {
		this.mScoreView = mScoreView;
	}
	

	public Handler getmHandler() {
		return mHandler;
	}

	public void setmHandler(Handler mHandler) {
		this.mHandler = mHandler;
	}
	
	

	@Override
	public void onWindowFocusChanged(boolean hasWindowFocus) {
		if(thread!=null) {
			if (!hasWindowFocus)
				thread.pause();
		}
	}

	public void surfaceCreated(SurfaceHolder holder) {
		if(thread!=null) {
			thread.setRunning(true);
			
			if(thread.getState() == Thread.State.NEW){
				
				thread.start();
			}
			else {
				if(thread.getState() == Thread.State.TERMINATED){
					 
					thread = new TheGame(this); 
					thread.setRunning(true);
					thread.start();
				}
			}
		}
	}
	
	
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		if(thread!=null) {
			thread.setSurfaceSize(width, height);			
		}
	}

	
	public void surfaceDestroyed(SurfaceHolder arg0) {
		
		boolean retry = true;
		if(thread!=null) {
			thread.setRunning(false);
		}
		
		
		while (retry) {
			try {
				if(thread!=null) {
					thread.join();
				}
				retry = false;
			} 
			catch (InterruptedException e) {
				
			}
		}
	}
	
	

	@SuppressWarnings("deprecation")
	public void startSensor(SensorManager sm) {
		sm.registerListener(this.sensorAccelerometer, 
				sm.getDefaultSensor(Sensor.TYPE_ORIENTATION),	
				SensorManager.SENSOR_DELAY_GAME);
	}
	
	public void removeSensor(SensorManager sm) {
		sm.unregisterListener(this.sensorAccelerometer);
		this.sensorAccelerometer = null;
	}
	
}



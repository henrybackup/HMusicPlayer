package com.example.hmusicplayer;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.example.hmusicplayer.service.MusicInterface;
import com.example.hmusicplayer.service.MusicService;

public class MainActivity extends Activity {
	private Button play;
	private Button pause;
	private Button continueplay;
	private Button stop;
	
	private MusicInterface mi;
	private static SeekBar sb;
	public static Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Bundle bundle = msg.getData();
			int duration = bundle.getInt("duration");
			int currPosition = bundle.getInt("currPosition");
			sb.setMax(duration);
			sb.setProgress(currPosition);
	
	
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		play = (Button) findViewById(R.id.play);
		pause = (Button) findViewById(R.id.pause);
		continueplay = (Button) findViewById(R.id.continueplay);
		stop = (Button) findViewById(R.id.stop);
		sb = (SeekBar) findViewById(R.id.sb);
		
		Intent intent = new Intent(this, MusicService.class);
		startService(intent);
		bindService(intent, new MyServiceConn(), BIND_AUTO_CREATE);
		sb.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				int progress = seekBar.getProgress();
				mi.seekTo(progress);
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
			
				
			}
		});
	}

	class MyServiceConn implements ServiceConnection {

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mi = (MusicInterface) service;
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub

		}

	}

	public void play(View v) {
		mi.play();
	}

	public void pause(View v) {
		mi.pause();
	}

	public void continueplay(View v) {
		mi.continueplay();
	}

	public void stop(View v) {
		mi.stop();
	}

}

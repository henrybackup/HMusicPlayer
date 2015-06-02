package com.example.hmusicplayer.service;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import com.example.hmusicplayer.MainActivity;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Binder;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.Message;

//服务是运行在主线程的，如果你下载一个东西还是会报错
public class MusicService extends Service {
	private MediaPlayer mediaPlayer;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return new MusicBinder();
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mediaPlayer = new MediaPlayer();

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// 释放资源，置为空
		mediaPlayer.release();
		mediaPlayer = null;
	}

	class MusicBinder extends Binder implements MusicInterface {

		@Override
		public void play() {
			MusicService.this.play();
		}

		@Override
		public void pause() {
			MusicService.this.pause();
		}

		@Override
		public void continueplay() {
			MusicService.this.continueplay();
		}

		@Override
		public void stop() {
			MusicService.this.stop();
		}

		@Override
		public void seekTo(int progress) {
			MusicService.this.seekTo(progress);
			
		}

	}

	private void play() {
		mediaPlayer.reset();
		File file = new File(Environment.getExternalStorageDirectory(),
				"Moonlight.mp3");
		try {
			mediaPlayer.setDataSource(file.toString());
			// mediaPlayer.prepare();

			// 如果是操作网络的话要用异步准备,所以统一使用异步
			mediaPlayer.prepareAsync();
			mediaPlayer.setOnPreparedListener(new OnPreparedListener() {

				@Override
				public void onPrepared(MediaPlayer mp) {
					addSeekBar();
					mediaPlayer.start();

				}
			});

		} catch (Exception e) {
		
			e.printStackTrace();
		}

	}

	private void pause() {
		mediaPlayer.pause();
	}

	private void continueplay() {
		// 没有reset的话就是从上一次暂停的地方开始播放
		mediaPlayer.start();
	
	}

	private void stop() {
		mediaPlayer.stop();
	}

	/**
	 * 获取当前播放进度
	 */
	private void addSeekBar() {

		// 使用计时器不断执行获取播放进度的代码
		Timer timer = new Timer();
		// 设置即时计时任务
		/**
		 * 5:毫秒后开始执行 500:每500毫秒执行一次
		 */
		
		TimerTask task = new TimerTask() {
		

			@Override
			public void run() {
				int duration = mediaPlayer.getDuration();
				int currPosition = mediaPlayer.getCurrentPosition();
				Message msg = MainActivity.handler.obtainMessage();
				// 用bundle可以传很多消息
				Bundle bundle = new Bundle();
				bundle.putInt("duration", duration);
				bundle.putInt("currPosition", currPosition);
				msg.setData(bundle);
				//发信息，让进度条更新,下面两个方法都是一样的,最主要是确定要obtainmessage的handler的msg对象
				// MainActivity.handler.sendMessage(msg);
				msg.sendToTarget();

			}
		};
		timer.schedule(task, 5, 500);
	}
private void seekTo(int progress){
	mediaPlayer.seekTo(progress);
}
}

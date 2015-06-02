package com.example.hmusicplayer.service;

public interface MusicInterface {
	void play();

	void pause();

	void continueplay();

	void stop();

	void seekTo(int progress);
}

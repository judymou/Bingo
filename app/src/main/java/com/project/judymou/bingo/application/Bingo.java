package com.project.judymou.bingo.application;

import android.app.Application;

import com.firebase.client.Firebase;

public class Bingo extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		Firebase.setAndroidContext(this);
	}
}

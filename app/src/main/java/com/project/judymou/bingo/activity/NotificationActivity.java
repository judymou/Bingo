package com.project.judymou.bingo.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.project.judymou.bingo.ListviewFragment;
import com.project.judymou.bingo.MakeBoardListviewFragment;
import com.project.judymou.bingo.PlayFragment;
import com.project.judymou.bingo.R;
import com.project.judymou.bingo.data.FirebaseHelper;
import com.project.judymou.bingo.data.Record;

public class NotificationActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		FragmentManager fragmentManager = getFragmentManager();
		String boardName = getIntent().getStringExtra("playFragment");
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		if (boardName != null) {
			PlayFragment playFragment = new PlayFragment();
			Bundle args = new Bundle();
			args.putString("boardPath", boardName);
			playFragment.setArguments(args);
		}
	}
}
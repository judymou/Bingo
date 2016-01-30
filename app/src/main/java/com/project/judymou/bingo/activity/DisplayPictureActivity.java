package com.project.judymou.bingo.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.project.judymou.bingo.R;
import com.project.judymou.bingo.data.FirebaseHelper;
import com.project.judymou.bingo.data.Record;

public class DisplayPictureActivity extends Activity {
	private String boardName;
	private int position;
	private ImageView imgView;
	private TextView imgText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_picture);
		boardName = getIntent().getExtras().getString("boardName");
		position = getIntent().getExtras().getInt("position");

		imgView = (ImageView) findViewById(R.id.imgView);
		imgText = (TextView) findViewById(R.id.imgText);
		FirebaseHelper.getInstance().getRef()
				.child("records/" + boardName + "/" + FirebaseHelper.getInstance().getOtherUserName() + "/" + position)
				.addListenerForSingleValueEvent(
						new ValueEventListener() {
							@Override
							public void onDataChange(DataSnapshot snapshot) {
								Record record = snapshot.getValue(Record.class);
								if (record == null) {
									Toast.makeText(getApplicationContext(), "No image available",
											Toast.LENGTH_SHORT).show();
									return;
								}
								byte[] decodedString = Base64.decode(record.getImageContent(), Base64.DEFAULT);
								Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
								imgView.setImageBitmap(decodedByte);
								imgText.setText(record.getImageText());
							}

							@Override
							public void onCancelled(FirebaseError firebaseError) {
							}
						});
	}
}
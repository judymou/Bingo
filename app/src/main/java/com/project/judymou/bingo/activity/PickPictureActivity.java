package com.project.judymou.bingo.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.project.judymou.bingo.R;
import com.project.judymou.bingo.data.FirebaseHelper;

import java.io.ByteArrayOutputStream;

public class PickPictureActivity extends Activity {
	private static int RESULT_LOAD_IMG = 1;
	private String imgDecodableString;
	private String boardName;
	private int position;
	private String imgContent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pick_picture);
		boardName = getIntent().getExtras().getString("boardName");
		position = getIntent().getExtras().getInt("position");
	}

	public void loadImagefromGallery(View view) {
		imgContent = null;
		// Create intent to Open Image applications like Gallery, Google Photos
		Intent galleryIntent = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		// Start the Intent
		startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
	}

	public void saveMove(View view) {
		FirebaseHelper.getInstance().makeMove(boardName, position, imgContent);
		finish();
	}

	public void cancelMove(View view) {
		finish();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		try {
			// When an Image is picked
			if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
					&& null != data) {
				// Get the Image from data

				Uri selectedImage = data.getData();
				String[] filePathColumn = { MediaStore.Images.Media.DATA };

				// Get the cursor
				Cursor cursor = getContentResolver().query(selectedImage,
						filePathColumn, null, null, null);
				// Move to first row
				cursor.moveToFirst();

				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				imgDecodableString = cursor.getString(columnIndex);
				cursor.close();
				ImageView imgView = (ImageView) findViewById(R.id.imgView);
				// Set the Image in ImageView after decoding the String
				Bitmap imageBitmap = BitmapFactory.decodeFile(imgDecodableString);
				imgView.setImageBitmap(imageBitmap);
				imgContent = convertImageToBase64String(imageBitmap);
			} else {
				Toast.makeText(this, "You haven't picked Image",
						Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
					.show();
		}
	}

	private String convertImageToBase64String(Bitmap bitmap) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 1, baos); //bm is the bitmap object
		byte[] b = baos.toByteArray();
		return Base64.encodeToString(b, Base64.DEFAULT);
	}
}
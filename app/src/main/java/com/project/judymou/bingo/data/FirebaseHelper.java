package com.project.judymou.bingo.data;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseHelper {
	private static FirebaseHelper instance = null;
	Firebase ref;

	public FirebaseHelper() {
		ref = new Firebase("https://dazzling-inferno-2760.firebaseio.com/");
	}

	public void saveBoard(String name, List<String> items) {
		Board board = new Board(name, items);
		ref.child("boards").child(name).setValue(board);
	}

	public Firebase getRef() {
		return ref;
	}

	public static FirebaseHelper getInstance() {
		if (instance == null) {
			instance = new FirebaseHelper();
		}
		return instance;
	}
}

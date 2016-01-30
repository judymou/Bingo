package com.project.judymou.bingo.data;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseHelper {
	private static FirebaseHelper instance = null;
	private Firebase ref;
	private String userName = "largeCrab";
	private String otherUserName = "smallCrab";
	//private String otherUserName = "largeCrab";
	//private String userName = "smallCrab";

	public FirebaseHelper() {
		ref = new Firebase("");
	}

	public void saveBoard(String name, List<String> items) {
		Board board = new Board(name, items);
		ref.child("boards").child(name).setValue(board);
	}

	public void makeMove(String boardName, int itemIndex, String imgContent, String imgText) {
		Action action = new Action(itemIndex);
		ref.child("scores").child(boardName).child(userName).push().setValue(action);
		ref.child("scores").child(boardName).child(userName).child("newstatus").push().setValue(action);

		Record record = new Record(imgContent, imgText);
		ref.child("records").child(boardName).child(userName).child("" + itemIndex).setValue(record);
	}

	public void removeStatus(String boardName) {
		ref.child("scores").child(boardName).child(otherUserName).child("newstatus").setValue(null);
	}

	public Firebase getRef() {
		return ref;
	}

	public String getUserName() {
		return userName;
	}

	public String getOtherUserName() {
		return otherUserName;
	}

	public static FirebaseHelper getInstance() {
		if (instance == null) {
			instance = new FirebaseHelper();
		}
		return instance;
	}
}

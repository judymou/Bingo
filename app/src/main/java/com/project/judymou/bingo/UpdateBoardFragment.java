package com.project.judymou.bingo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.project.judymou.bingo.data.Board;
import com.project.judymou.bingo.data.FirebaseHelper;

import java.util.ArrayList;
import java.util.List;

public class UpdateBoardFragment extends Fragment implements OnItemClickListener {
	private List<GridviewItem> mItems;    // GridView items list
	private GridviewAdapter mAdapter;    // GridView adapter
	private GridView gridView;
	private EditText nameEditText;
	private String boardName;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// initialize the items list
		mItems = new ArrayList<GridviewItem>();
		Resources resources = getResources();

		for (int i = 0; i < 16; i++) {
			mItems.add(new GridviewItem(""));
		}

		boardName = getArguments().getString("boardPath");
		/*
		mItems.add(new GridviewItem(getString(R.string.item_0)));
		mItems.add(new GridviewItem(getString(R.string.item_1)));
		mItems.add(new GridviewItem(getString(R.string.item_2)));
		mItems.add(new GridviewItem(getString(R.string.item_3)));
		mItems.add(new GridviewItem(getString(R.string.item_4)));
		mItems.add(new GridviewItem(getString(R.string.item_5)));
		mItems.add(new GridviewItem(getString(R.string.item_6)));
		mItems.add(new GridviewItem(getString(R.string.item_7)));
		mItems.add(new GridviewItem(getString(R.string.item_8)));
		mItems.add(new GridviewItem(getString(R.string.item_9)));
		mItems.add(new GridviewItem(getString(R.string.item_10)));
		mItems.add(new GridviewItem(getString(R.string.item_11)));
		mItems.add(new GridviewItem(getString(R.string.item_12)));
		mItems.add(new GridviewItem(getString(R.string.item_13)));
		mItems.add(new GridviewItem(getString(R.string.item_14)));
		mItems.add(new GridviewItem(getString(R.string.item_15)));
		*/

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
													 Bundle savedInstanceState) {
		// inflate the root view of the fragment
		final View fragmentView = inflater.inflate(R.layout.fragment_gridview, container, false);

		// initialize the adapter
		mAdapter = new GridviewAdapter(getActivity(), mItems);

		// initialize the GridView
		gridView = (GridView) fragmentView.findViewById(R.id.gridView);
		gridView.setAdapter(mAdapter);
		gridView.setOnItemClickListener(this);

		nameEditText = (EditText) fragmentView.findViewById(R.id.name);
		nameEditText.setText(boardName);

		// Initialize the button
		Button button = (Button) fragmentView.findViewById(R.id.save);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				FirebaseHelper.getInstance().saveBoard(nameEditText.getText().toString(), convertGridviewItemToString());
				Toast.makeText(getActivity(), "Board saved!", Toast.LENGTH_SHORT).show();
			}
		});

		FirebaseHelper.getInstance().getRef().child("boards/" + boardName).addListenerForSingleValueEvent(
				new ValueEventListener() {
					@Override
					public void onDataChange(DataSnapshot snapshot) {
						Board board = (Board) snapshot.getValue(Board.class);
						if (board == null) {
							return;
						}
						mItems.clear();
						for (String item : board.getItems()) {
							mItems.add(new GridviewItem(item));
						}
						mAdapter = new GridviewAdapter(getActivity(), mItems);
						gridView.setAdapter(mAdapter);
					}

					@Override
					public void onCancelled(FirebaseError firebaseError) {
					}
				});
		return fragmentView;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
													long id) {
		final GridviewItem item = mItems.get(position);

		// get prompts.xml view
		LayoutInflater li = LayoutInflater.from(getActivity());
		View promptsView = li.inflate(R.layout.prompt, null);

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				getActivity());

		// set prompts.xml to alertdialog builder
		alertDialogBuilder.setView(promptsView);

		final EditText userInput = (EditText) promptsView
				.findViewById(R.id.editTextDialogUserInput);

		// set dialog message
		alertDialogBuilder
				.setCancelable(false)
				.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {
								// get user input and set it to result
								// edit text
								item.content = userInput.getText().toString();
							}
						})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
	}

	private List<String> convertGridviewItemToString() {
		List<String> itemStrings = new ArrayList<String>();
		for (GridviewItem item : mItems) {
			itemStrings.add(item.content);
		}
		return itemStrings;
	}
}
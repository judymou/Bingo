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

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.project.judymou.bingo.data.Action;
import com.project.judymou.bingo.data.Board;
import com.project.judymou.bingo.data.FirebaseHelper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PlayFragment extends Fragment implements OnItemClickListener {
	private List<GridviewItem> mItems;    // GridView items list
	private GridviewAdapter mAdapter;    // GridView adapter
	private GridView gridView;
	private Button smallCrab;
	private Button largeCrab;
	private boolean isUser;
	private String boardName;
	private Set<Integer> selectedIndex;

	private FirebaseHelper firebaseHelper;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		firebaseHelper = FirebaseHelper.getInstance();

		// initialize the items list
		mItems = new ArrayList<GridviewItem>();
		selectedIndex = new HashSet<Integer>();

		boardName = getArguments().getString("boardPath");
		FirebaseHelper.getInstance().getRef().child("boards/" + boardName).addListenerForSingleValueEvent(
				new ValueEventListener() {
					@Override
					public void onDataChange(DataSnapshot snapshot) {
						Board board = (Board) snapshot.getValue(Board.class);
						if (board == null) {
							return;
						}
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
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
													 Bundle savedInstanceState) {
		// inflate the root view of the fragment
		final View fragmentView = inflater.inflate(R.layout.fragment_play, container, false);

		// initialize the adapter
		mAdapter = new GridviewAdapter(getActivity(), mItems);

		// initialize the GridView
		gridView = (GridView) fragmentView.findViewById(R.id.gridView);
		gridView.setAdapter(mAdapter);
		gridView.setOnItemClickListener(this);

		smallCrab = (Button) fragmentView.findViewById(R.id.small_crab);
		smallCrab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				smallCrab.setTextColor(getResources().getColor(R.color.SecondaryTextColor));
				largeCrab.setTextColor(getResources().getColor(R.color.PrimaryTextColor));
				if (firebaseHelper.getUserName().equals("smallCrab")) {
					isUser = true;
				} else {
					isUser = false;
				}
				getSelectedIndex();
			}
		});

		largeCrab = (Button) fragmentView.findViewById(R.id.large_crab);
		largeCrab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				largeCrab.setTextColor(getResources().getColor(R.color.SecondaryTextColor));
				smallCrab.setTextColor(getResources().getColor(R.color.PrimaryTextColor));
				if (firebaseHelper.getUserName().equals("largeCrab")) {
					isUser = true;
				} else {
					isUser = false;
				}
				getSelectedIndex();
			}
		});

		return fragmentView;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
													long id) {
		final GridviewItem item = mItems.get(position);
		// Making a move.
		if (isUser) {
			firebaseHelper.makeMove(boardName, position);
			view.findViewById(R.id.content).setBackgroundColor(getResources().getColor(R.color.GridSelected));
		}
	}

	public void getSelectedIndex() {
		String queryUser = firebaseHelper.getUserName();
		if (!isUser) {
			queryUser = firebaseHelper.getOtherUserName();
		}
		FirebaseHelper.getInstance().getRef().child("scores/" + boardName + "/" + queryUser).addListenerForSingleValueEvent(
				new ValueEventListener() {
					@Override
					public void onDataChange(DataSnapshot snapshot) {
						selectedIndex.clear();
						for(DataSnapshot s : snapshot.getChildren()) {
							Action action = s.getValue(Action.class);
							if (action == null) {
								return;
							}
							selectedIndex.add(action.getItemIndex());

							mAdapter = new GridviewAdapter(getActivity(), mItems);
							mAdapter.setSelectedIndex(selectedIndex);
							gridView.setAdapter(mAdapter);
						}
					}

					@Override
					public void onCancelled(FirebaseError firebaseError) {
					}
				});
	}
}
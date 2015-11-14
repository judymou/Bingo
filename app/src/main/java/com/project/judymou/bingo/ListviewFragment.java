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
import com.project.judymou.bingo.data.FirebaseHelper;

import java.util.ArrayList;
import java.util.List;

public class ListviewFragment extends Fragment implements OnItemClickListener {
	private List<GridviewItem> mItems;    // GridView items list
	private GridviewAdapter mAdapter;   // GridView adapter
	private GridView gridView;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// initialize the items list
		mItems = new ArrayList<GridviewItem>();

		FirebaseHelper.getInstance().getRef().child("boards").addListenerForSingleValueEvent(
				new ValueEventListener() {
					@Override
					public void onDataChange(DataSnapshot snapshot) {
						for(DataSnapshot s : snapshot.getChildren()) {
							String name = (String) s.getKey();
							mItems.add(new GridviewItem(name));
						}
						mAdapter = new GridviewAdapter(getActivity(), mItems);
						mAdapter.setBiggerFont(true);
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
		final View fragmentView = inflater.inflate(R.layout.fragment_listview, container, false);

		// initialize the adapter
		mAdapter = new GridviewAdapter(getActivity(), mItems);
		mAdapter.setBiggerFont(true);

		// initialize the GridView
		gridView = (GridView) fragmentView.findViewById(R.id.listView);
		gridView.setAdapter(mAdapter);
		gridView.setOnItemClickListener(this);

		return fragmentView;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
													long id) {
		final GridviewItem item = mItems.get(position);

		PlayFragment playFragment = new PlayFragment();
		Bundle args = new Bundle();
		args.putString("boardPath", item.content);
		playFragment.setArguments(args);

		getFragmentManager().beginTransaction()
				.replace(R.id.container, playFragment)
				.commit();
	}
}
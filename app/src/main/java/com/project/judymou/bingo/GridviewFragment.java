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

import com.project.judymou.bingo.data.FirebaseHelper;

import java.util.ArrayList;
import java.util.List;

public class GridviewFragment extends Fragment implements OnItemClickListener {
  private List<GridviewItem> mItems;    // GridView items list
  private GridviewAdapter mAdapter;    // GridView adapter

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
   }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // inflate the root view of the fragment
    final View fragmentView = inflater.inflate(R.layout.fragment_gridview, container, false);

    // initialize the adapter
    mAdapter = new GridviewAdapter(getActivity(), mItems);

    // initialize the GridView
    GridView gridView = (GridView) fragmentView.findViewById(R.id.gridView);
    gridView.setAdapter(mAdapter);
    gridView.setOnItemClickListener(this);

		// Initialize the button
		Button button = (Button) fragmentView.findViewById(R.id.save);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				EditText name = (EditText) fragmentView.findViewById(R.id.name);
				FirebaseHelper.getInstance().saveBoard(name.getText().toString(), convertGridviewItemToString());
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
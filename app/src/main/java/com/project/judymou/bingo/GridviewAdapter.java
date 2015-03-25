package com.project.judymou.bingo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class GridviewAdapter extends BaseAdapter {
  private Context mContext;
  private List<GridviewItem> mItems;

  public GridviewAdapter(Context context, List<GridviewItem> items) {
    mContext = context;
    mItems = items;
  }

  @Override
  public int getCount() {
    return mItems.size();
  }

  @Override
  public Object getItem(int position) {
    return mItems.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    ViewHolder viewHolder;

    if(convertView == null) {
      // inflate the GridView item layout
      LayoutInflater inflater = LayoutInflater.from(mContext);
      convertView = inflater.inflate(R.layout.gridview_item, parent, false);

      // initialize the view holder
      viewHolder = new ViewHolder();
      viewHolder.content = (TextView) convertView.findViewById(R.id.content);
      convertView.setTag(viewHolder);
    } else {
      // recycle the already inflated view
      viewHolder = (ViewHolder) convertView.getTag();
    }

    // update the item view
    GridviewItem item = mItems.get(position);
    viewHolder.content.setText(item.content);

    return convertView;
  }

  /**
   * The view holder design pattern prevents using findViewById()
   * repeatedly in the getView() method of the adapter.
   *
   * @see http://developer.android.com/training/improving-layouts/smooth-scrolling.html#ViewHolder
   */
  private static class ViewHolder {
    TextView content;
  }
}

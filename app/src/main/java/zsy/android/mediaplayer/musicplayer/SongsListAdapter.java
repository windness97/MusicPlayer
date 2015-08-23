package zsy.android.mediaplayer.musicplayer;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import zsy.android.mediaplayer.R;

public class SongsListAdapter extends ArrayAdapter<SongItem>{

	private int resource;

//	private ViewHolder[] holderArray = new ViewHolder[];

	public SongsListAdapter(Context context, int resource,
							List<SongItem> objects) {
		super(context, resource, objects);
		// TODO Auto-generated constructor stub
		this.resource = resource;
	}

	public void setAnimFlag(int flag){

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		SongItem item =(SongItem) getItem(position);

		View view;
		ViewHolder viewHolder;

		if (convertView == null){
			view = LayoutInflater.from(getContext()).inflate(resource, null);
			viewHolder = new ViewHolder();

			viewHolder.textTitle = (TextView) view.findViewById(R.id.text_list_name_id);
			viewHolder.textSinger = (TextView) view.findViewById(R.id.text_list_singer_id);
			viewHolder.imageAnim = (ImageView) view.findViewById(R.id.image_list_playing_id);

			view.setTag(viewHolder);
		}else{
			view = convertView;
			viewHolder = (ViewHolder) view.getTag();
		}

		viewHolder.textTitle.setText(item.getTitle());
		viewHolder.textSinger.setText(item.getSinger());

		AnimationDrawable animationDrawable = (AnimationDrawable) viewHolder.imageAnim.getDrawable();
		if (item.getAnimFlag() == SongItem.FLAG_SHOW){
			viewHolder.imageAnim.setVisibility(View.VISIBLE);
			animationDrawable.start();
			view.setBackgroundColor(Color.parseColor("#15ffffff"));
		}else if (item.getAnimFlag() == SongItem.FLAG_DISAPPEARED){
			viewHolder.imageAnim.setVisibility(View.INVISIBLE);
			animationDrawable.stop();
			view.setBackgroundColor(Color.parseColor("#00000000"));
		}else if (item.getAnimFlag() == SongItem.FLAG_PAUSE){
			viewHolder.imageAnim.setVisibility(View.INVISIBLE);
			animationDrawable.stop();
			view.setBackgroundColor(Color.parseColor("#15ffffff"));
		}

		return view;
	}

	public void updateSingleRow(ListView listView, long id) {

		if (listView != null) {
			int start = listView.getFirstVisiblePosition();
			for (int i = start, j = listView.getLastVisiblePosition(); i <= j; i++)
				if (listView.getItemAtPosition(i) == getItem((int)id)){
					View view = listView.getChildAt(i - start);
					getView(i, view, listView);
					break;
				}
		}
	}


	private class ViewHolder{
		private TextView textTitle;
		private TextView textSinger;
		private ImageView imageAnim;

	}

}

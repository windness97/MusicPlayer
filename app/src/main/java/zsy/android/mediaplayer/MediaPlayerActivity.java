package zsy.android.mediaplayer;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import zsy.android.mediaplayer.mp3.Mp3Info;
import zsy.android.mediaplayer.musicplayer.MusicPlayer;
import zsy.android.mediaplayer.musicplayer.SongItem;
import zsy.android.mediaplayer.musicplayer.SongsListAdapter;
import zsy.android.mediaplayer.utils.Utils;

public class MediaPlayerActivity extends Activity implements OnClickListener,OnItemClickListener{

	private TextView text_title;
	private Mp3Info currentMp3Info;

	private List<SongItem> list = new ArrayList<SongItem>();
	public SongsListAdapter adapter;
	private ListView listView;

	//
	private AnimationDrawable TitleAnimationDrawable;

	private ImageView image_playing;

	private ImageButton button_play;
	private ImageButton button_previous;
	private ImageButton button_next;
	private ImageButton button_loop;

	private TextView text_presentTime;
	private TextView text_totalTime;

	private SeekBar bar;

	private int total;
	private int current;

	private MusicPlayer musicPlayer;

	private List<View> viewPages;
	private View songListPage;
	private View titlePage;
	private MyViewPager pager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		musicPlayer = new MusicPlayer(handler, this);

		initSongsList();

		total = musicPlayer.getDuration();

		initPager();

		initAlbumPage();
		initListPage();
		initOtherViews();

	}

	private void initPager() {
		viewPages = new ArrayList<View>();
		LinearLayout contentLayout = (LinearLayout) findViewById(R.id.layout_pager_id);

		LayoutInflater mInflater = getLayoutInflater();
		songListPage = mInflater.inflate(R.layout.listview_pager, null);
		titlePage = mInflater.inflate(R.layout.album_pager, null);
		viewPages.add(songListPage);
		viewPages.add(titlePage);

		List<String> pagesNameList = new ArrayList<String>();
		pagesNameList.add("list");
		pagesNameList.add("album");
		pager = new MyViewPager(this, contentLayout, viewPages, pagesNameList);
		pager.init(MyViewPager.FLAG_DONOTCHANGE, Color.parseColor("#ffffff"), MyViewPager.FLAG_DONOTCHANGE, MyViewPager.FLAG_DONOTCHANGE);
		pager.prepare();
	}

	private void initAlbumPage() {
		text_title = (TextView) titlePage.findViewById(R.id.text_title_id);
		text_title.setText(musicPlayer.getCurrentMp3Info().getTitle());

		image_playing = (ImageView) titlePage.findViewById(R.id.image_id);
		image_playing.setVisibility(View.INVISIBLE);
		TitleAnimationDrawable = (AnimationDrawable) image_playing.getDrawable();

		button_play = (ImageButton) titlePage.findViewById(R.id.button_play_id);
		button_previous = (ImageButton) titlePage.findViewById(R.id.button_previous_id);
		button_next = (ImageButton) titlePage.findViewById(R.id.button_next_id);
		button_loop = (ImageButton) titlePage.findViewById(R.id.button_loop_id);

		button_play.setOnClickListener(this);
		button_previous.setOnClickListener(this);
		button_next.setOnClickListener(this);
		button_loop.setOnClickListener(this);
	}

	private void initListPage() {
		listView = (ListView) songListPage.findViewById(R.id.list_songs_id);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	private void initSongsList() {
		List<Mp3Info> infoList = musicPlayer.getMp3Infos();

		for (Mp3Info info: infoList){
			list.add(new SongItem(info));
		}

		adapter = new SongsListAdapter(this, R.layout.layout_item_songlist, list);
	}


	private void initOtherViews() {

		text_presentTime = (TextView) findViewById(R.id.text_presenttime_id);
		text_totalTime = (TextView) findViewById(R.id.text_totaltime_id);

		bar = (SeekBar) findViewById(R.id.bar_id);
		bar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {


			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

				musicPlayer.seekTo(seekBar.getProgress());
				if (musicPlayer.isPlaying()) {

					musicPlayer.prepareTimerTask(500);
				} else {
				}
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				musicPlayer.cancelTask();

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
										  boolean fromUser) {
				// TODO Auto-generated method stub
				text_presentTime.setText(Utils.toTime(progress));
			}
		});
		bar.setProgress(0);
		bar.setMax(total);

		text_presentTime.setText("00:00");
		text_totalTime.setText(Utils.toTime(total));

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		if (musicPlayer.isPlaying()){
			musicPlayer.prepareTimerTask(500);
		}
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		musicPlayer.stop();
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if (musicPlayer != null){
			musicPlayer.stop();
			musicPlayer.release();
		}
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		SongItem currentItem = (SongItem) listView.getItemAtPosition(currentPosition);
		SongItem previousItem = (SongItem) listView.getItemAtPosition(previousPosition);
		SongItem targetItem;

		switch (v.getId()) {
			case R.id.button_play_id:
				playOrPause(currentItem);
				break;
			case R.id.button_previous_id:
				playPrevious();
				break;
			case R.id.button_next_id:
				playNext();
				break;
			case R.id.button_loop_id:
				switch (musicPlayer.getLoopingType()){
					case MusicPlayer.FLAG_SINGLE_LOOP:
						musicPlayer.setLoopingType(MusicPlayer.FLAG_LIST_LOOP);

//						button_loop.setBackgroundResource(R.drawable.ic_loop_list);
						button_loop.setImageResource(R.drawable.ic_loop_list);
						break;
					case MusicPlayer.FLAG_LIST_LOOP:
						musicPlayer.setLoopingType(MusicPlayer.FLAG_SINGLE_LOOP);

//						button_loop.setBackgroundResource(R.drawable.ic_loop_single);
						button_loop.setImageResource(R.drawable.ic_loop_single);
						break;
				}
				break;
			case R.id.layout_bar_id:
//			if ((Integer)layoutBar.getTag(R.id.tag_1) == FLAG_DISAPPEARED){
//				AbsoluteLayout.LayoutParams params = new AbsoluteLayout.LayoutParams(
//						android.widget.AbsoluteLayout.LayoutParams.WRAP_CONTENT, 
//						android.widget.AbsoluteLayout.LayoutParams.WRAP_CONTENT,
//						1, 1);
//				layoutBar.setLayoutParams(params);
//			}else if ((Integer)layoutBar.getTag(R.id.tag_1) == FLAG_SHOW){
//				
//			}

				break;
			default:
				break;
		}

	}

	private void playOrPause(SongItem currentItem) {
		if (!musicPlayer.isPlaying()){
            musicPlayer.play();
            musicPlayer.prepareTimerTask(500);

            setPlayingViews(0);

            if (currentPosition == -1) currentPosition = 0;
            currentItem.setListItemStatus(SongItem.FLAG_SHOW);
            adapter.updateSingleRow(listView, currentPosition);

        }else{
            musicPlayer.pause();

            setPlayingViews(0);

            currentItem.setListItemStatus(SongItem.FLAG_PAUSE);
            adapter.updateSingleRow(listView, currentPosition);

        }
	}

	private void playPrevious() {
		if (currentPosition == 0) {
            currentPosition = adapter.getCount() - 1;
        }else {
            currentPosition = currentPosition - 1;
        }

		playAccordingToPosition(listView, currentPosition);
		/////

		if (previousPosition != -1){
            cancelChosenStatus(previousPosition);
        }
		previousPosition = currentPosition;

		newChosenStatus(currentPosition);
		/////

		total = musicPlayer.getDuration();
		bar.setMax(total);

		setPlayingViews(1);
	}

	public void playNext() {
		if (currentPosition == adapter.getCount() - 1) {
            currentPosition = 0;
        }else {
            currentPosition = currentPosition + 1;
        }
		playAccordingToPosition(listView, currentPosition);
		/////

		if (previousPosition != -1){
            cancelChosenStatus(previousPosition);
        }
		previousPosition = currentPosition;

		newChosenStatus(currentPosition);
		/////

		total = musicPlayer.getDuration();
		bar.setMax(total);

		setPlayingViews(1);
	}

	//////
	private int currentPosition = 0;
	private int previousPosition = 0;

	@Override
	public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
		// TODO Auto-generated method stub
		currentPosition = position;
		/////
		playAccordingToPosition(listView, currentPosition);
		/////

		if (previousPosition != -1){
			cancelChosenStatus(previousPosition);
		}
		previousPosition = currentPosition;

		newChosenStatus(currentPosition);
		/////

		total = musicPlayer.getDuration();
		bar.setMax(total);

		setPlayingViews(1);
	}

	private void playAccordingToPosition(ListView listView, int position) {
		SongItem item = (SongItem) listView.getItemAtPosition(position);
		currentMp3Info = item.getMp3Info();
		playAndRefreshTask(currentMp3Info);
	}

	private void playAndRefreshTask(Mp3Info mp3Info) {
		musicPlayer.playTheMp3(mp3Info);
		musicPlayer.cancelTask();
		musicPlayer.prepareTimerTask(500);
	}

	private void newChosenStatus(int targetPosition) {
		SongItem currentItem = (SongItem) listView.getItemAtPosition(targetPosition);
		currentItem.setListItemStatus(SongItem.FLAG_SHOW);
		adapter.updateSingleRow(listView, currentPosition);
	}

	private void cancelChosenStatus(int targetPosition) {
		SongItem targetItem = (SongItem) listView.getItemAtPosition(targetPosition);
		targetItem.setListItemStatus(SongItem.FLAG_DISAPPEARED);
		adapter.updateSingleRow(listView, previousPosition);
	}

	private void setPlayingViews(int status) {

		if (musicPlayer.isPlaying()) {
			TitleAnimationDrawable.start();
			image_playing.setVisibility(View.VISIBLE);
			button_play.setBackgroundResource(R.drawable.ic_pause);
		}else {
			TitleAnimationDrawable.stop();
			image_playing.setVisibility(View.INVISIBLE);
			button_play.setBackgroundResource(R.drawable.ic_play);
		}
		switch (status) {
			case 1:
				text_title.setText(musicPlayer.getCurrentMp3Info().getTitle());
				text_totalTime.setText(Utils.toTime(total));
				break;
			case 0:
				break;
			default:

				break;
		}
	}


	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			int presentMilli = msg.what;
			bar.setProgress(presentMilli);
			super.handleMessage(msg);
		}
	};


}

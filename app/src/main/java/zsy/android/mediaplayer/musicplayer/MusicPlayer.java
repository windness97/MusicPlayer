package zsy.android.mediaplayer.musicplayer;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import zsy.android.mediaplayer.MediaPlayerActivity;
import zsy.android.mediaplayer.mp3.Mp3Info;
import zsy.android.mediaplayer.mp3.Mp3Manager;

public class MusicPlayer {

	private int loopStatus = FLAG_SINGLE_LOOP;
	final public static int FLAG_SINGLE_LOOP = 0;
	final public static int FLAG_LIST_LOOP = 1;
//	final public int FLAG_PLAYING_TYPE_
	private int playingType = 0;
	
	final private MediaPlayerActivity activity;
	private Mp3Manager mp3Manager;
	
	//private File currentFile; 
	
	private List<Mp3Info> mp3List;
	private Mp3Info currentMp3Info;
	
	private MediaPlayer mediaPlayer;
	
	private Timer timer;
	private MyTimerTask task;
	private Handler handler;
	
	public MusicPlayer(Handler handler, Activity activity){
		this.activity = (MediaPlayerActivity) activity;
		
		mp3Manager = new Mp3Manager(this.activity);
		mp3List = mp3Manager.getMp3Infos();
		
		currentMp3Info = mp3List.get(0);
		
		this.handler = handler;
		
		mediaPlayer = new MediaPlayer();
		mediaPlayer.setLooping(true);
		
		initMusicPlayer();
		
		timer = new Timer();
		task = new MyTimerTask();

		mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mp) {
				if (MusicPlayer.this.getLoopingType() == MusicPlayer.FLAG_LIST_LOOP) {
					MusicPlayer.this.activity.playNext();
				}
			}
		});
	}
		
	public void setCurrentMp3Info(Mp3Info currentMp3Info){
		this.currentMp3Info = currentMp3Info;
	}
	
	public Mp3Info getCurrentMp3Info(){
		return currentMp3Info;
	}
	
	public void play(){
		mediaPlayer.start();
	}
	
	public void pause(){
		mediaPlayer.pause();
		task.cancel();
	}
	
	public void stop(){
		task.cancel();
		timer.purge();
	}
	
	public void initMusicPlayer(){
		try{
			mediaPlayer.reset();
			mediaPlayer.setDataSource(currentMp3Info.getUrl());
			mediaPlayer.prepare();
			Log.d("MediaPlayer", "duration : " + mediaPlayer.getDuration());
			
		}catch(Exception e){
			e.printStackTrace();
			Log.e("MediaPlayer", e.toString());
		}
		setLoopingType(loopStatus);
	}
	
	public void release(){
		mediaPlayer.release();
	}
	
	public int getDuration() {
		return mediaPlayer.getDuration();
	}
	
	public int getCurrentPosition() {
		return mediaPlayer.getCurrentPosition();
	}
	
	public boolean isPlaying(){
		return mediaPlayer.isPlaying();
	}

	public boolean isLooping() {
		return mediaPlayer.isLooping();
	}
	
	public void seekTo(int progress){
		mediaPlayer.seekTo(progress);
	}
	
	public void playTheMp3(Mp3Info currentMp3Info){
		setCurrentMp3Info(currentMp3Info);
		initMusicPlayer();
		play();
	}
	
	public void prepareTimerTask(int period){
		
		task = new MyTimerTask();
		timer.schedule(task, 0, period);
	}
	
	public void cancelTask(){
		task.cancel();
	}
	
	public void setLoopingType(int type){
		switch (type) {
		case FLAG_SINGLE_LOOP:
			mediaPlayer.setLooping(true);
			loopStatus = type;
			break;
		case FLAG_LIST_LOOP:
			mediaPlayer.setLooping(false);
			loopStatus = type;
			break;
		default:
			break;
		}
	}

	public int getLoopingType(){
		return loopStatus;
	}
	
	public List<Mp3Info> getMp3Infos() {
		// TODO Auto-generated method stub
		return mp3List;
	}
	
	class MyTimerTask extends TimerTask{
		
		private int n = 0;
	
		@Override
		public void run() {
			// TODO Auto-generated method stub
			int presentMilli = getCurrentPosition();
			Message msg = new Message();
			msg.what = presentMilli;
			handler.sendMessage(msg);
			n = n + 1;
			Log.d("MediaPlayer", n + "");
		}
		
	}




	
}

package zsy.android.mediaplayer.musicplayer;

import zsy.android.mediaplayer.mp3.Mp3Info;

public class SongItem {

	public static final int FLAG_SHOW = 1;
	public static final int FLAG_DISAPPEARED = 0;
	public static final int FLAG_PAUSE = 2;

	private Mp3Info info;
	private int animFlag;

	public void setListItemStatus(int status){
//		if (b){
//			setAnimFlag(FLAG_SHOW);
//		}else {
//			setAnimFlag(FLAG_DISAPPEARED);
//		}
		if (status == FLAG_DISAPPEARED || status == FLAG_PAUSE || status == FLAG_SHOW) setAnimFlag(status);
	}

	public void setAnimFlag(int animFlag){
		this.animFlag = animFlag;
	}

	public int getAnimFlag(){
		return animFlag;
	}

	public SongItem(Mp3Info info){
		this.info = info;
	}

	public String getTitle(){
		return info.getTitle();
	}

	public String getSinger(){
		return info.getArtist();
	}

	public Mp3Info getMp3Info() {
		return info;
	}


}

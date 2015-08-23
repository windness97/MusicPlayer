package zsy.android.mediaplayer.mp3;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

public class Mp3Manager {
	
	private Activity activity;
	
	public Mp3Manager(Activity activity){
		this.activity = activity;
	}

	public List<Mp3Info> getMp3Infos() {
		Cursor cursor = activity.getContentResolver().query(
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
				MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
		List<Mp3Info> mp3Infos = new ArrayList<Mp3Info>();
		for (int i = 0; i < cursor.getCount(); i++) {
			Mp3Info mp3Info = new Mp3Info();
			cursor.moveToNext();
			long id = cursor.getLong(cursor
					.getColumnIndex(MediaStore.Audio.Media._ID));
			String title = cursor.getString((cursor
					.getColumnIndex(MediaStore.Audio.Media.TITLE)));
			String artist = cursor.getString(cursor
					.getColumnIndex(MediaStore.Audio.Media.ARTIST));
			long duration = cursor.getLong(cursor
					.getColumnIndex(MediaStore.Audio.Media.DURATION));
			long size = cursor.getLong(cursor
					.getColumnIndex(MediaStore.Audio.Media.SIZE));
			String url = cursor.getString(cursor
					.getColumnIndex(MediaStore.Audio.Media.DATA));
			int isMusic = cursor.getInt(cursor
					.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC));
//			String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
//			album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
//			Log.v("MyLog", album);
			if (isMusic != 0) {
				mp3Info.setId(id);
				mp3Info.setTitle(title);
				mp3Info.setArtist(artist);
				mp3Info.setDuration(duration);
				mp3Info.setSize(size);
				mp3Info.setUrl(url);
				mp3Infos.add(mp3Info);
				Log.v("MediaPlayer", url);
			}
		}
		
		String PATH1 = "/storage/sdcard/Music";
		String PATH2 = "/storage/sdcard0/music";
		String path;
		List<Mp3Info> list = new ArrayList<Mp3Info>();
		for (Mp3Info info: mp3Infos){
			path = info.getUrl();
			if (path.startsWith(PATH1) || path.startsWith(PATH2)){
				list.add(info);
			}
		}

		if (list.isEmpty()){
			for (Mp3Info info: mp3Infos){
				list.add(info);
			}
		}
		return list;
	}
	
}

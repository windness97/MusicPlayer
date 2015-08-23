package zsy.android.mediaplayer.mp3;

public class Mp3Info {

	private long id;
	private String title;
	private String artist;
	private long duration;
	private long size;
	private String url;
	private int bitrate;
	

	public void setId(long id) {
		// TODO Auto-generated method stub
		this.id = id;
	}

	public void setTitle(String title) {
		// TODO Auto-generated method stub
		this.title = title;
	}

	public void setArtist(String artist) {
		// TODO Auto-generated method stub
		this.artist = artist;
	}

	public void setDuration(long duration) {
		// TODO Auto-generated method stub
		this.duration = duration;
	}

	public void setSize(long size) {
		// TODO Auto-generated method stub
		this.size = size;
	}

	public void setUrl(String url) {
		// TODO Auto-generated method stub
		this.url = url;
	}

	public long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getArtist() {
		return artist;
	}

	public long getDuration() {
		return duration;
	}

	public long getSize() {
		return size;
	}

	public String getUrl() {
		return url;
	}

}

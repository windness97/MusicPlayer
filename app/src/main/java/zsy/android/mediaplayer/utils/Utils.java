package zsy.android.mediaplayer.utils;

import java.io.File;

import android.os.Environment;
import android.util.Log;

public class Utils {

	
	public static String toTime(int number){
		
		int totalSeconds = number / 1000;
		int minutes = totalSeconds / 60;
		int seconds = totalSeconds % 60;
		
		String m = null;
		String s = null;
		
		if (minutes < 10) m = "0" + minutes;
		else m = minutes + "";
		if (seconds < 10) s = "0" + seconds;
		else s = seconds + "";
		
		String time = m + ":" + s;
		return time;
	}

}

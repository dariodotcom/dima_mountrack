package it.polimi.dima.dacc.mountainroute.commons.utils;

import android.util.Log;

public class Logger {
	
	private String tag;

	public Logger(String tag) {
		this.tag = tag;
	}
	
	public void d(String msg){
		Log.d(tag, msg);
	}
	
}

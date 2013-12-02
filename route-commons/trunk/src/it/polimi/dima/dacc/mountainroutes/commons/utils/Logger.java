package it.polimi.dima.dacc.mountainroutes.commons.utils;

import android.util.Log;

public class Logger {
	
	private String tag;

	public Logger(String tag) {
		this.tag = tag;
	}
	
	public void i(String msg){
		Log.i(tag, msg);
	}
	
	public void d(String msg){
		Log.d(tag, msg);
	}
	
	public void e(String msg){
		Log.e(tag, msg);
	}
	
	public void v(String msg){
		Log.v(tag, msg);
	}
	
	public void w(String msg){
		Log.w(tag, msg);
	}
}

package it.polimi.dima.dacc.mountainroutes.walktracker.service;

import it.polimi.dima.dacc.mountainroutes.types.ExcursionReport;
import it.polimi.dima.dacc.mountainroutes.types.Route;
import android.content.Intent;

public class BroadcastFactory {

	public static Intent createTrackingUpdateBroadcast(float completeIndex){
		return null;
	}
	
	public static Intent createTrackingStartBroadcast(Route route){
		return null;
	}
	
	public static Intent createTrackingStopBroadcast(ExcursionReport report){
		return null;
	}
	
	public static Intent createStatusUpdateBroadcast(UpdateType update){
		return null;
	}	
}

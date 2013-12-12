package it.polimi.dima.dacc.mountainroutes.walktracker.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.location.LocationManager;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

public class TrackingService extends Service {

	private final static String TAG = "tracking-service";

	private TrackingWorker tWorker;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onCreate() {
		super.onCreate();

		// Create and start tracking worker thread
		tWorker = new TrackingWorker(this);
		
		// Retrieve route to track from resources
		

		// Register Location Listener

		Log.d(TAG, "service created");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (tWorker.isTracking()) {
			Log.e(TAG, "Tracking service destroyed with worker still tracking");
			tWorker.stopTracking();
		}

		Log.d(TAG, "service destroyed");
	}
}

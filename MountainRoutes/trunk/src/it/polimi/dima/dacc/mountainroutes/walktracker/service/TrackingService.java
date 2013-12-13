package it.polimi.dima.dacc.mountainroutes.walktracker.service;

import it.polimi.dima.dacc.mountainroutes.types.Route;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class TrackingService extends Service {

	private TrackingWorker tWorker;
	private LaggardBackup laggardBackup;

	@Override
	public void onCreate() {
		super.onCreate();

		// Create and register laggard backup
		laggardBackup = new LaggardBackup(this);
		laggardBackup.register();

		// Create and start tracking worker thread
		tWorker = new TrackingWorker(this);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		tWorker.quit();
		laggardBackup.unregister();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return START_STICKY;
	}

	// Tracking control - allows the activity that binds to the service to
	// control its operations
	@Override
	public IBinder onBind(Intent intent) {
		return new TrackingControl();
	}

	public class TrackingControl extends Binder {
		public void startTracking(Route route) {
			tWorker.startTracking(route);
		}

		public void pause() {
			tWorker.pause();
		}

		public void resume() {
			tWorker.resume();
		}

		public void stop() {
			TrackingService.this.stopSelf();
		}

		public boolean isTracking() {
			return tWorker.isTracking();
		}

		public LaggardBackup getLaggardBackup() {
			return laggardBackup;
		}
	}
}
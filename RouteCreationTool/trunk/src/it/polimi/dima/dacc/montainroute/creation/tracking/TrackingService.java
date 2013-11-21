package it.polimi.dima.dacc.montainroute.creation.tracking;

import it.polimi.dima.dacc.mountainroute.commons.utils.Logger;

import com.google.android.gms.maps.model.LatLng;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;

public class TrackingService extends Service {

	private final static int POSITION_UPDATE_INTERVAL = 10000;
	private final static int POSITION_UPDATE_DISTANCE = 10;

	private Logger logger = new Logger("tracking-service");
	private TrackingBinder binder = new TrackingBinder();
	private TrackingUpdateBroadcaster broadcaster = new TrackingUpdateBroadcaster(
			this);
	
	private Timer timer = new Timer(broadcaster);
	private LocationManager locationManager;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		// Register location listener
		this.locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		String providerName = LocationManager.GPS_PROVIDER;
		locationManager.requestLocationUpdates(providerName,
				POSITION_UPDATE_INTERVAL, POSITION_UPDATE_DISTANCE,
				locationListener);

		// Start time measuring thread
		timer.start();
		
		logger.d("Tracking service started");
		return super.onStartCommand(intent, flags, startId);

	}

	@Override
	public void onDestroy() {
		// Remove location listener
		this.locationManager.removeUpdates(locationListener);

		// Stop time measuring thread
		timer.stop();
		
		// Call up to superclass
		logger.d("Tracking service stopped");
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return binder;
	}

	// Listener to track user location
	private LocationListener locationListener = new LocationListener() {

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderEnabled(String provider) {
			broadcaster.sendProviderEnabledUpdate();
		}

		@Override
		public void onProviderDisabled(String provider) {
			broadcaster.sendProviderDisabledUpdate();
		}

		@Override
		public void onLocationChanged(Location loc) {
			LatLng p = new LatLng(loc.getLatitude(), loc.getLongitude());
			broadcaster.sendPositionUpdate(p);
		}
	};

	// Class to let activity control the service
	public class TrackingBinder extends Binder {

		public void stopService() {
			stopSelf();
		}

		public void pauseUpdates() {
			broadcaster.pauseUpdates();
		}

		public void resumeUpdates() {
			broadcaster.resumeUpdates();
		}
	}
}

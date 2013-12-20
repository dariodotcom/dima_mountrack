package it.polimi.dima.dacc.mountainroutes.walktracker.views;

import com.google.android.gms.maps.model.LatLng;

import it.polimi.dima.dacc.mountainroutes.R;
import it.polimi.dima.dacc.mountainroutes.remote.AltitudeResolver;
import it.polimi.dima.dacc.mountainroutes.types.ExcursionReport;
import it.polimi.dima.dacc.mountainroutes.types.Route;
import it.polimi.dima.dacc.mountainroutes.walktracker.receiver.LaggardBackup;
import it.polimi.dima.dacc.mountainroutes.walktracker.receiver.TrackerListener;
import it.polimi.dima.dacc.mountainroutes.walktracker.service.UpdateType;
import it.polimi.dima.dacc.mountainroutes.walktracker.tracker.TrackResult;
import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AltitudeViewFragment extends Fragment implements TrackerListener, LoaderManager.LoaderCallbacks<Double> {

	private static final String INITIAL_ALTITUDE = "initialAltitude";
	private static final int LOADER_ID = 0;
	private static final int RESOLVE_INTERVAL_MILLIS = 1000;
	private static final String EMPTY = "-";

	private Long lastResolveInstant;
	private Double initialAltitude;
	private TextView textView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.altitude_view_fragment, container);
		textView = (TextView) root.findViewById(R.id.altitude_textview);
		return root;
	}

	@Override
	public void onActivityCreated(Bundle savedState) {
		super.onActivityCreated(savedState);
		if (savedState != null) {
			initialAltitude = savedState.getDouble(INITIAL_ALTITUDE);
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putDouble(INITIAL_ALTITUDE, initialAltitude);
	}

	/* -- Tracker Listener Callbacks -- */
	@Override
	public void onStartTracking(Route route) {
		if (initialAltitude == null) {
			resolve(route.getPath().getList().get(0));
		}
	}

	@Override
	public void onStopTracking(ExcursionReport report) {
	}

	@Override
	public void onStatusUpdate(UpdateType update) {
	}

	@Override
	public void onTrackingUpdate(TrackResult result) {
		resolve(result.getPointOnPath());
	}

	@Override
	public void onRegister(LaggardBackup backup) {
		if (backup.amILate()) {
			onStartTracking(backup.getRouteBeingTracked());
			return;
		}
	}

	@Override
	public void onUnregister(LaggardBackup backup) {

	}

	@Override
	public Loader<Double> onCreateLoader(int id, Bundle args) {
		LatLng location = args.getParcelable("LOCATION");
		if (location == null) {
			throw new IllegalArgumentException("Args do not contain a location");
		}

		return new AltitudeLoader(getActivity(), location);
	}

	@Override
	public void onLoadFinished(Loader<Double> loader, Double data) {
		if (data == null) {
			textView.setText(EMPTY);
		}

		if (initialAltitude == null) {
			initialAltitude = data;
			textView.setText(String.valueOf(0) + "m");
		}

		int delta = (int) (initialAltitude - data);
		textView.setText(String.valueOf(delta) + "m");
	}

	@Override
	public void onLoaderReset(Loader<Double> loader) {

	}

	private static class AltitudeLoader extends AsyncTaskLoader<Double> {

		LatLng location;

		public AltitudeLoader(Context context, LatLng location) {
			super(context);
			this.location = location;
		}

		@Override
		public Double loadInBackground() {
			return AltitudeResolver.resolve(location);
		}
	}

	private void resolve(LatLng point) {
		long now = SystemClock.elapsedRealtime();
		if (lastResolveInstant != null && now - lastResolveInstant < RESOLVE_INTERVAL_MILLIS) {
			return;
		}

		lastResolveInstant = now;

		Bundle b = new Bundle();
		b.putParcelable("LOCATION", point);

		LoaderManager lm = getLoaderManager();
		if (lm.getLoader(LOADER_ID) != null) {
			lm.restartLoader(LOADER_ID, b, this).forceLoad();
		} else {
			lm.initLoader(LOADER_ID, b, this).forceLoad();
			
		}
	}
}
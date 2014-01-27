package it.polimi.dima.dacc.mountainroutes.routeselector.pages;

import com.google.android.gms.maps.model.LatLng;

import it.polimi.dima.dacc.mountainroutes.R;
import it.polimi.dima.dacc.mountainroutes.commons.Holder;
import it.polimi.dima.dacc.mountainroutes.routeselector.RouteSelector;
import it.polimi.dima.dacc.mountainroutes.routeselector.listfragment.OnRouteSelected;
import it.polimi.dima.dacc.mountainroutes.routeselector.listfragment.RouteListFragment;
import it.polimi.dima.dacc.mountainroutes.routeselector.sources.NearMeLoader;
import it.polimi.dima.dacc.mountainroutes.types.RouteSummary;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Fragment that contains the page that allows the user to retrieve the routes
 * available in the currently selected data source and near the user's position
 */
public class NearMePage extends Fragment implements LocationListener {

	private static final long LOCATION_UPDATE_TIME = 10000;
	private static final float LOCATION_UPDATE_DISTANCE = 50;
	private RouteListFragment fragment;
	private Holder<LatLng> locationHolder;
	private String gpsDisabledMessage = "gps disabled";
	private LocationManager locMan;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View inflated = inflater.inflate(R.layout.page_near_me, null);
		return inflated;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		locationHolder = new Holder<LatLng>();
		
		fragment = (RouteListFragment) getFragmentManager().findFragmentById(R.id.near_me_list_fragment);
		fragment.setLoaderFactory(new NearMeLoader.Factory(getActivity(), locationHolder));
		
		fragment.setOnRouteSelectListener(new OnRouteSelected() {

			@Override
			public void onRouteSelected(RouteSummary summary) {
				RouteSelector selector = (RouteSelector) getActivity();
				selector.startViewer(summary.getId());
			}
		});

		String svcName = Context.LOCATION_SERVICE;
		locMan = (LocationManager) getActivity().getSystemService(svcName);
	}

	@Override
	public void onResume() {
		super.onResume();
		String pvdName = LocationManager.GPS_PROVIDER;
		locMan.requestLocationUpdates(pvdName, LOCATION_UPDATE_TIME, LOCATION_UPDATE_DISTANCE, this);
	}

	@Override
	public void onPause() {
		super.onPause();
		locMan.removeUpdates(this);
	}

	@Override
	public void onLocationChanged(Location l) {
		// Get location
		LatLng point = new LatLng(l.getLatitude(), l.getLongitude());
		locationHolder.setValue(point);
		fragment.update();
	}

	@Override
	public void onProviderDisabled(String arg0) {
		if (arg0 == LocationManager.GPS_PROVIDER) {
			fragment.showMessage(gpsDisabledMessage);
		}
	}

	@Override
	public void onProviderEnabled(String arg0) {

	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		if (arg0 == LocationManager.GPS_PROVIDER && arg1 != LocationProvider.AVAILABLE) {
			fragment.showMessage(gpsDisabledMessage);
		}
	}
}

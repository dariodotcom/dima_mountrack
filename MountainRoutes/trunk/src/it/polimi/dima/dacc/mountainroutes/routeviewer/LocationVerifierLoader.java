package it.polimi.dima.dacc.mountainroutes.routeviewer;

import it.polimi.dima.dacc.mountainroutes.commons.LocationLoader;
import it.polimi.dima.dacc.mountainroutes.loader.GenericLoader;
import it.polimi.dima.dacc.mountainroutes.loader.LoadResult;
import it.polimi.dima.dacc.mountainroutes.types.Route;
import it.polimi.dima.dacc.mountainroutes.walktracker.tracker.Tracker;
import android.content.Context;

import com.google.android.gms.maps.model.LatLng;

public class LocationVerifierLoader extends GenericLoader<Boolean> {

	private Route route;

	public LocationVerifierLoader(Route route, Context context) {
		super(context);
		this.route = route;
	}

	@Override
	protected void onReleaseResources(LoadResult<Boolean> result) {

	}

	@Override
	public LoadResult<Boolean> loadInBackground() {
		LoadResult<LatLng> currentPosition = new LocationLoader(getContext()).getLocation();

		if (currentPosition.getType() == LoadResult.ERROR) {
			return new LoadResult<Boolean>(currentPosition.getError());
		}

		return new LoadResult<Boolean>(Tracker.canWalkOn(route, currentPosition.getResult()));
	}
}
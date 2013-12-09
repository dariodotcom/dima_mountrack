package it.polimi.dima.dacc.mountainroutes.routeselector.sources;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;
import it.polimi.dima.dacc.mountainroutes.loader.LoadResult;
import it.polimi.dima.dacc.mountainroutes.types.RouteSummaryList;

// Basically the same class as generic loader, but this one is based on support AsyncTaskLoader.
public abstract class SummaryListLoader extends
		AsyncTaskLoader<LoadResult<RouteSummaryList>> {

	private LoadResult<RouteSummaryList> result;

	public SummaryListLoader(Context context) {
		super(context);
	}

	protected abstract String getTag();

	@Override
	public void deliverResult(LoadResult<RouteSummaryList> data) {
		Log.d(getTag(), "deliverResult called");
		if (isReset()) {
			Log.d(getTag(), "Result not delivered as loader has been reset.");
			if (this.result != null) {
				onReleaseResources(this.result);
			}
		}

		LoadResult<RouteSummaryList> oldResult = this.result;
		this.result = data;

		if (isStarted()) {
			super.deliverResult(result);
		}

		if (oldResult != null) {
			onReleaseResources(oldResult);
		}
	}

	protected void onReleaseResources(LoadResult<RouteSummaryList> result) {

	}

	@Override
	protected void onStartLoading() {
		super.onStartLoading();

		Log.d(getTag(), "startLoading called");

		if (result != null) {
			deliverResult(result);
		}

		if (result == null || takeContentChanged()) {
			forceLoad();
		}
	}

	@Override
	protected void onReset() {
		Log.d(getTag(), "onReset called");
		super.onReset();
	}

	@Override
	protected void onAbandon() {
		// TODO Auto-generated method stub
		super.onAbandon();
		Log.d(getTag(), "onAbadon called");
	}

	@Override
	protected void onStopLoading() {
		super.onStopLoading();
		Log.d(getTag(), "stopLoading called");
	}
}
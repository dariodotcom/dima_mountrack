package it.polimi.dima.dacc.mountainroutes.routeselector.sources;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;
import it.polimi.dima.dacc.mountainroutes.loader.LoadResult;
import it.polimi.dima.dacc.mountainroutes.types.RouteSummaryList;

public abstract class SummaryListLoader extends
		AsyncTaskLoader<LoadResult<RouteSummaryList>> {

	private final static String TAG = "summary-list-loader";

	private LoadResult<RouteSummaryList> result;

	public SummaryListLoader(Context context) {
		super(context);
	}

	@Override
	public void deliverResult(LoadResult<RouteSummaryList> data) {
		if (isReset()) {
			Log.d(TAG, "Result not delivered as loader has been reset.");
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

		if (result != null) {
			deliverResult(result);
		}

		if (result == null || takeContentChanged()) {
			forceLoad();
		}
	}

	@Override
	protected void onReset() {
		Log.d(TAG, "Load reset.");
		super.onReset();
	}
}
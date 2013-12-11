package it.polimi.dima.dacc.mountainroutes.routeselector.sources;

import android.app.Fragment;
import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;
import it.polimi.dima.dacc.mountainroutes.loader.GenericLoader;
import it.polimi.dima.dacc.mountainroutes.loader.LoadResult;
import it.polimi.dima.dacc.mountainroutes.types.RouteSummaryList;

/**
 * Version of <code>GenericLoader</code> based on support
 * {@link AsyncTaskLoader} so that it can be used with support {@link Fragment}
 * 
 * @see GenericLoader
 */
public abstract class RouteSummaryLoader extends
		AsyncTaskLoader<LoadResult<RouteSummaryList>> {

	private LoadResult<RouteSummaryList> result;

	/**
	 * Constructs a new instance
	 * 
	 * @param context
	 *            - the context in which this loader is running
	 */
	public RouteSummaryLoader(Context context) {
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
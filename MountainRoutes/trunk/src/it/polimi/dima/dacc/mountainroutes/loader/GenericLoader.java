package it.polimi.dima.dacc.mountainroutes.loader;

import android.content.AsyncTaskLoader;
import android.content.Context;

public abstract class GenericLoader<E> extends AsyncTaskLoader<LoadResult<E>> {

	private LoadResult<E> result;

	public GenericLoader(Context context) {
		super(context);
	}

	@Override
	public void deliverResult(LoadResult<E> data) {
		if (isReset()) {
			if (this.result != null) {
				onReleaseResources(this.result);
			}
		}

		LoadResult<E> oldResult = this.result;
		this.result = data;

		if (isStarted()) {
			super.deliverResult(result);
		}

		if (oldResult != null) {
			onReleaseResources(oldResult);
		}
	}

	protected abstract void onReleaseResources(LoadResult<E> result);

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
}
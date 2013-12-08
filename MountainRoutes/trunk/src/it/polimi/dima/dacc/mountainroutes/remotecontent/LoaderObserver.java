package it.polimi.dima.dacc.mountainroutes.remotecontent;

import it.polimi.dima.dacc.mountainroutes.loader.LoadError;

public interface LoaderObserver {
	public void onLoadStart();

	public void onLoadError(LoadError type);

	public void onLoadResult(LoaderResult result);
}

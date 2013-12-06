package it.polimi.dima.dacc.mountainroutes.remotecontent;

public interface LoaderObserver {
	public void onLoadStart();

	public void onLoadError(LoadError type);

	public void onLoadResult(LoaderResult result);
}

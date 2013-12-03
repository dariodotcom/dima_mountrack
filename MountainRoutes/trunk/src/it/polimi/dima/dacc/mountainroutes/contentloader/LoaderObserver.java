package it.polimi.dima.dacc.mountainroutes.contentloader;

public interface LoaderObserver {
	public void onLoadStart();

	public void onLoadError(ContentErrorType type);

	public void onLoadResult(LoaderResult result);
}

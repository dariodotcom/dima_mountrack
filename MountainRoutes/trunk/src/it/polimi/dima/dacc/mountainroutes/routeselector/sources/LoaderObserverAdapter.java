package it.polimi.dima.dacc.mountainroutes.routeselector.sources;

import it.polimi.dima.dacc.mountainroutes.contentloader.ContentErrorType;
import it.polimi.dima.dacc.mountainroutes.contentloader.LoaderObserver;
import it.polimi.dima.dacc.mountainroutes.contentloader.LoaderResult;
import it.polimi.dima.dacc.mountainroutes.routeselector.sources.RouteSource.ResultObserver;
import it.polimi.dima.dacc.mountainroutes.types.RouteSummaryList;

public class LoaderObserverAdapter implements LoaderObserver{

	private ResultObserver observer;
	
	public LoaderObserverAdapter(ResultObserver observer){
		this.observer = observer;
	}
	
	@Override
	public void onLoadStart() {
		if(observer != null){
			observer.onLoadStart();
		}
	}

	@Override
	public void onLoadError(ContentErrorType type) {
		if(observer != null){
			observer.onError(type);
		}
	}

	@Override
	public void onLoadResult(LoaderResult result) {
		if(observer != null){
			RouteSummaryList list = result.getResult(RouteSummaryList.class);
			observer.onResultReceived(list);
		}
	}
	
}

package it.polimi.dima.dacc.mountainroutes.routeselector.sources;

import android.content.Context;
import android.text.Editable;
import it.polimi.dima.dacc.mountainroutes.loader.LoadError;
import it.polimi.dima.dacc.mountainroutes.loader.LoadResult;
import it.polimi.dima.dacc.mountainroutes.persistence.PersistenceException;
import it.polimi.dima.dacc.mountainroutes.persistence.RoutePersistence;
import it.polimi.dima.dacc.mountainroutes.types.RouteSummaryList;

public class SavedRouteListLoader extends SummaryListLoader {

	private Editable searchTerm;
	private Context context;

	public SavedRouteListLoader(Context context, Editable searchTerm) {
		super(context);
		this.context = context;
		this.searchTerm = searchTerm;
	}

	@Override
	public LoadResult<RouteSummaryList> loadInBackground() {
		try {
			String term = searchTerm.toString();
			RoutePersistence persistence = RoutePersistence.create(context);
			RouteSummaryList result = persistence.getAvailableRoutes(term);
			return new LoadResult<RouteSummaryList>(result);
		} catch (PersistenceException e) {
			return new LoadResult<RouteSummaryList>(LoadError.INTERNAL_ERROR);
		}
	}

}

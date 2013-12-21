package it.polimi.dima.dacc.mountainroutes.reportmanager;

import it.polimi.dima.dacc.mountainroutes.loader.GenericLoader;
import it.polimi.dima.dacc.mountainroutes.loader.LoadError;
import it.polimi.dima.dacc.mountainroutes.loader.LoadResult;
import it.polimi.dima.dacc.mountainroutes.persistence.PersistenceException;
import it.polimi.dima.dacc.mountainroutes.persistence.report.ReportPersistence;
import it.polimi.dima.dacc.mountainroutes.types.ExcursionList;

import android.content.Context;

public class ReportLoader extends GenericLoader<ExcursionList> {

	public ReportLoader(Context context) {
		super(context);
	}

	@Override
	public LoadResult<ExcursionList> loadInBackground() {
		Context context = getContext();
		ReportPersistence persistence = ReportPersistence.create(context);
		 try {
			ExcursionList result = persistence.getAvailableExcursions();
			return new LoadResult<ExcursionList>(result);
		} catch (PersistenceException e) {
			return new LoadResult<ExcursionList>(LoadError.INTERNAL_ERROR);
		}
		
	}

	@Override
	protected void onReleaseResources(LoadResult<ExcursionList> result) {
		// TODO Auto-generated method stub
		
	}


}

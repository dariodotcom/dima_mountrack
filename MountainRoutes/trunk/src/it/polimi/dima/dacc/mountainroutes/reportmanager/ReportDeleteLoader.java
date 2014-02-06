package it.polimi.dima.dacc.mountainroutes.reportmanager;

import android.content.Context;
import it.polimi.dima.dacc.mountainroutes.loader.GenericLoader;
import it.polimi.dima.dacc.mountainroutes.loader.LoadError;
import it.polimi.dima.dacc.mountainroutes.loader.LoadResult;
import it.polimi.dima.dacc.mountainroutes.persistence.PersistenceException;
import it.polimi.dima.dacc.mountainroutes.persistence.report.ReportPersistence;
import it.polimi.dima.dacc.mountainroutes.types.ExcursionList;

/**
 * Loader that deletes a report from the database and returns available reports.
 */
public class ReportDeleteLoader extends GenericLoader<ExcursionList> {

	private int reportId;

	public ReportDeleteLoader(Context context, int reportId) {
		super(context);
		this.reportId = reportId;
	}

	@Override
	protected void onReleaseResources(LoadResult<ExcursionList> result) {

	}

	@Override
	public LoadResult<ExcursionList> loadInBackground() {

		try {
			ReportPersistence persistence = ReportPersistence.create(getContext());
			persistence.removeExcursion(reportId);
			ExcursionList list = persistence.getAvailableExcursions();
			return new LoadResult<ExcursionList>(list);
		} catch (PersistenceException e) {
			e.printStackTrace();
			return new LoadResult<ExcursionList>(LoadError.INTERNAL_ERROR);
		}

	}
}

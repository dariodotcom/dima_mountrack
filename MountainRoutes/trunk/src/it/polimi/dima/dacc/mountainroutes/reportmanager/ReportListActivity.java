package it.polimi.dima.dacc.mountainroutes.reportmanager;

import it.polimi.dima.dacc.mountainroutes.R;
import it.polimi.dima.dacc.mountainroutes.loader.LoadResult;
import it.polimi.dima.dacc.mountainroutes.reportmanager.ReportListAdapter.onDeleteExcursionListener;

import it.polimi.dima.dacc.mountainroutes.types.ExcursionList;

import android.os.Bundle;
import android.app.Activity;
import android.app.LoaderManager;
import android.content.Loader;

import android.view.Menu;
import android.view.View;
import android.widget.ListView;

public class ReportListActivity extends Activity implements LoaderManager.LoaderCallbacks<LoadResult<ExcursionList>>, onDeleteExcursionListener {

	private final static int LOADER_ID = 1;
	private ReportListAdapter adapter;
	private ReportLoader loader;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_report_list);
		ListView list = (ListView) findViewById(R.id.report_list);
		View emptyView = findViewById(R.id.empty_list_message_text);
		list.setEmptyView(emptyView);

		adapter = new ReportListAdapter(this);
		list.setAdapter(adapter);
		adapter.setOnDeleteExcursionListener(this);
		loader = new ReportLoader(this);
		getLoaderManager().initLoader(LOADER_ID, null, this).forceLoad();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.report_list, menu);
		return true;
	}

	@Override
	public Loader<LoadResult<ExcursionList>> onCreateLoader(int id, Bundle args) {
		return loader;
	}

	@Override
	public void onLoadFinished(Loader<LoadResult<ExcursionList>> loader, LoadResult<ExcursionList> result) {
		switch (result.getType()) {
		case LoadResult.ERROR:

			break;
		case LoadResult.RESULT:
			ExcursionList list = result.getResult();

			this.adapter.clear();
			this.adapter.addAll(list.asList());
			this.adapter.notifyDataSetChanged();

			break;
		}

	}

	@Override
	public void onLoaderReset(Loader<LoadResult<ExcursionList>> loader) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDelete(int excursionId) {
		// TODO Auto-generated method stub

	}

}

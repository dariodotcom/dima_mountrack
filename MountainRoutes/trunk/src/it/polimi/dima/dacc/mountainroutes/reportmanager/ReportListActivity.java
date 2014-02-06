package it.polimi.dima.dacc.mountainroutes.reportmanager;

import it.polimi.dima.dacc.mountainroutes.R;
import it.polimi.dima.dacc.mountainroutes.loader.LoadResult;
import it.polimi.dima.dacc.mountainroutes.reportmanager.ReportListAdapter.onDeleteExcursionListener;
import it.polimi.dima.dacc.mountainroutes.reportviewer.ReportViewerActivity;

import it.polimi.dima.dacc.mountainroutes.types.ExcursionList;
import it.polimi.dima.dacc.mountainroutes.types.ExcursionReport;

import android.os.Bundle;
import android.app.Activity;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;

import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * Activity that shows report available in the database.
 */
public class ReportListActivity extends Activity implements LoaderManager.LoaderCallbacks<LoadResult<ExcursionList>>,
		onDeleteExcursionListener {

	private static final String DELENDUM_REPORT_ID = "id_to_delete";
	private final static int LOADER_ID = 1;
	private final static int DELETE_LOADER_ID = 2;
	private ReportListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_report_list);
		ListView list = (ListView) findViewById(R.id.report_list);
		View emptyView = findViewById(R.id.empty_list_message_text);
		list.setEmptyView(emptyView);
		list.setOnItemClickListener(reportClickListener);

		adapter = new ReportListAdapter(this);
		list.setAdapter(adapter);
		adapter.setOnDeleteExcursionListener(this);

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
		switch (id) {
		case LOADER_ID:
			return new ReportLoader(this);
		case DELETE_LOADER_ID:
			int reportId = args.getInt(DELENDUM_REPORT_ID);
			return new ReportDeleteLoader(this, reportId);
		default:
			return null;
		}
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
		Bundle b = new Bundle();
		b.putInt(DELENDUM_REPORT_ID, excursionId);
		getLoaderManager().restartLoader(DELETE_LOADER_ID, b, this).forceLoad();
	}

	private OnItemClickListener reportClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> adapter, View view, int index, long id) {
			Log.d("ReportListAvtivity", "click!");
			ExcursionReport report = (ExcursionReport) adapter.getItemAtPosition(index);
			Intent i = new Intent(ReportListActivity.this, ReportViewerActivity.class);
			i.putExtra(ReportViewerActivity.REPORT_TO_DISPLAY, report);
			startActivity(i);
		}
	};
}

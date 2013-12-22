package it.polimi.dima.dacc.mountainroutes.reportviewer;

import it.polimi.dima.dacc.mountainroutes.R;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class ReportViewerActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_report_viewer);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.report_viewer, menu);
		return true;
	}

}

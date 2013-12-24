package it.polimi.dima.dacc.mountainroutes.reportviewer;

import it.polimi.dima.dacc.mountainroutes.R;
import it.polimi.dima.dacc.mountainroutes.commons.RouteProgressionMapFragment;
import it.polimi.dima.dacc.mountainroutes.types.ExcursionReport;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.TextView;

public class ReportViewerActivity extends Activity {

	private ExcursionReport displayedReport;
	private RouteProgressionMapFragment fragment;
	private TextView spentTime;
	private TextView traveledMeters;
	private TextView gap;

	public static String REPORT_TO_DISPLAY = "report_to_display";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_report_viewer);

		Intent intent = getIntent();
		if (intent != null) {
			displayedReport = intent.getParcelableExtra(REPORT_TO_DISPLAY);
		} else if (savedInstanceState != null) {
			displayedReport = savedInstanceState.getParcelable(REPORT_TO_DISPLAY);
		}
		if (displayedReport == null) {
			throw new IllegalStateException("no route to display");
		}
		fragment = (RouteProgressionMapFragment) getFragmentManager().findFragmentById(R.id.viewer_map);
		spentTime = (TextView) findViewById(R.id.spent_time_value);
		traveledMeters = (TextView) findViewById(R.id.traveled_meters_value);
		gap = (TextView) findViewById(R.id.gap_value);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.report_viewer, menu);
		return true;
	}

	@Override
	protected void onResume() {
		super.onResume();
		fragment.setPath(displayedReport.getPath());
		fragment.panToPath();

		spentTime.setText(String.format("%s" + "/" + "%s", displayedReport.getElapsedDuration(), displayedReport.getRouteDuration()));
		traveledMeters.setText(String.format("%s" + "/" + "%s", displayedReport.getElapsedLength(), displayedReport.getRouteLenght()));
		gap.setText(String.format("%s" + "/" + "%s", displayedReport.getElapsedGap(), displayedReport.getRouteGap()));
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putParcelable(REPORT_TO_DISPLAY, displayedReport);
	}

}

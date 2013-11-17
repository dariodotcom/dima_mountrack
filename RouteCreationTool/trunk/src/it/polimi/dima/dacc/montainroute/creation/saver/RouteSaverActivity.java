package it.polimi.dima.dacc.montainroute.creation.saver;

import it.polimi.dima.dacc.montainroute.creation.R;
import it.polimi.dima.dacc.montainroute.creation.TrackedPoints;
import it.polimi.dima.dacc.mountainroute.commons.connector.StorageClient;
import it.polimi.dima.dacc.mountainroute.commons.connector.StorageClient.ResultCallback;
import it.polimi.dima.dacc.mountainroute.commons.connector.query.CreateRouteQuery;
import it.polimi.dima.dacc.mountainroute.commons.connector.query.Query;
import it.polimi.dima.dacc.mountainroute.commons.connector.query.QueryResult;
import it.polimi.dima.dacc.mountainroute.commons.types.Route;
import android.os.Bundle;
import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class RouteSaverActivity extends Activity implements ResultCallback {

	public final static String TRACKED_POINTS_KEY = "TRACKED_POINTS_KEY";
	private final static String CREATION_KEY = "CREATION_KEY";

	private CreationState creationState;

	private OnClickListener saveListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Route r = RouteSaverActivity.this.creationState.createRoute();
			Button self = (Button) v;
			self.setEnabled(false);

			Query q = new CreateRouteQuery(r);
			new StorageClient(RouteSaverActivity.this).execute(q);
		}
	};

	private TextWatcher nameWatcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int st, int bef, int c) {
			RouteSaverActivity.this.creationState.setName(s.toString());
		}

		@Override
		public void beforeTextChanged(CharSequence s, int st, int c, int af) {
		}

		@Override
		public void afterTextChanged(Editable s) {
		}
	};

	private TextWatcher durationWatcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int st, int bef, int c) {
			Integer duration = Integer.decode(s.toString());
			RouteSaverActivity.this.creationState.setDuration(duration);
		}

		@Override
		public void beforeTextChanged(CharSequence s, int st, int c, int af) {
		}

		@Override
		public void afterTextChanged(Editable s) {
		}
	};

	@Override
	protected void onCreate(Bundle savedState) {
		super.onCreate(savedState);
		setContentView(R.layout.activity_route_saver);

		if (savedState != null) {
			creationState = (CreationState) savedState
					.getSerializable(CREATION_KEY);
		} else {
			TrackedPoints points = (TrackedPoints) getIntent()
					.getSerializableExtra(TRACKED_POINTS_KEY);
			this.creationState = new CreationState(points);
		}

		// Append listeners
		EditText name = (EditText) findViewById(R.id.route_name);
		EditText duration = (EditText) findViewById(R.id.route_duration);
		Button save = (Button) findViewById(R.id.save_button);

		name.addTextChangedListener(nameWatcher);
		duration.addTextChangedListener(durationWatcher);
		save.setOnClickListener(saveListener);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.route_saver, menu);
		return true;
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putSerializable(CREATION_KEY, creationState);
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onResult(QueryResult result) {
		Log.d("saver","route saved");
		setResult(RESULT_OK);
		finish();
	}

}

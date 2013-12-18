package it.polimi.dima.dacc.mountainroutes;

import it.polimi.dima.dacc.mountainroutes.persistence.PersistenceException;
import it.polimi.dima.dacc.mountainroutes.persistence.RoutePersistence;
import it.polimi.dima.dacc.mountainroutes.types.ExcursionReport;
import it.polimi.dima.dacc.mountainroutes.types.Route;
import it.polimi.dima.dacc.mountainroutes.types.RouteID;
import it.polimi.dima.dacc.mountainroutes.walktracker.receiver.LaggardBackup;
import it.polimi.dima.dacc.mountainroutes.walktracker.receiver.TrackerListener;
import it.polimi.dima.dacc.mountainroutes.walktracker.receiver.TrackerListenerManager;
import it.polimi.dima.dacc.mountainroutes.walktracker.service.TrackingService;
import it.polimi.dima.dacc.mountainroutes.walktracker.service.TrackingService.TrackingControl;
import it.polimi.dima.dacc.mountainroutes.walktracker.service.UpdateType;
import it.polimi.dima.dacc.mountainroutes.walktracker.tracker.TrackResult;
import it.polimi.dima.dacc.mountainroutes.walktracker.views.TimerView;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class TestActivity extends FragmentActivity {

	private Button startButton;
	private Button stopButton;
	private Button pauseButton;
	private Button resumeButton;
	private TimerView timerView;
	private TrackingControl control;
	private ArrayAdapter<String> adapter;

	private TrackerListenerManager trackMan;

	private final ServiceConnection serviceConnection = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {
			Toast.makeText(TestActivity.this, "Service disconnected",
					Toast.LENGTH_SHORT).show();
			control = null;

			trackMan.unregisterListener(viewController);
			trackMan.unregisterListener(loggerController);
			trackMan.unregisterListener(timerView);
			trackMan = null;
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			Toast.makeText(TestActivity.this, "Service connected",
					Toast.LENGTH_SHORT).show();
			control = (TrackingControl) service;
			trackMan = TrackerListenerManager.inject(TestActivity.this);
			trackMan.registerListener(viewController);
			trackMan.registerListener(loggerController);
			trackMan.registerListener(timerView);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);

		ListView loggingView = (ListView) findViewById(R.id.logging_view);
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1);
		loggingView.setAdapter(adapter);

		startButton = (Button) findViewById(R.id.service_start);
		stopButton = (Button) findViewById(R.id.service_stop);
		pauseButton = (Button) findViewById(R.id.service_pause);
		resumeButton = (Button) findViewById(R.id.service_resume);

		startButton.setOnClickListener(startListener);
		stopButton.setOnClickListener(stopListener);
		pauseButton.setOnClickListener(pauseListener);
		resumeButton.setOnClickListener(resumeListener);

		timerView = (TimerView) findViewById(R.id.timer_view);

		Intent i = new Intent(this, TrackingService.class);
		startService(i);
		bindService(i, serviceConnection, BIND_AUTO_CREATE);

		stopButton.setEnabled(false);
		pauseButton.setEnabled(false);
		resumeButton.setEnabled(false);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.test, menu);
		return true;
	}

	@Override
	protected void onDestroy() {
		if (control != null) {
			control.stop();
		}
		unbindService(serviceConnection);
		super.onDestroy();
	}

	private TrackerListener viewController = new TrackerListener() {

		@Override
		public void onStartTracking(Route route) {
			stopButton.setEnabled(true);
			pauseButton.setEnabled(true);
			startButton.setEnabled(false);
		}

		@Override
		public void onStopTracking(ExcursionReport report) {
			stopButton.setEnabled(false);
			pauseButton.setEnabled(false);
			resumeButton.setEnabled(false);
		}

		@Override
		public void onStatusUpdate(UpdateType update) {
			switch (update) {
			case EXCURSION_PAUSED:
				pauseButton.setEnabled(false);
				resumeButton.setEnabled(true);
				break;
			case EXCURSION_RESUME:
				pauseButton.setEnabled(true);
				resumeButton.setEnabled(false);
				break;
			case FORCE_QUIT:
				onStopTracking(null);
				break;
			default:
				break;
			}
		}

		@Override
		public void onTrackingUpdate(TrackResult result) {

		}

		@Override
		public void onRegister(LaggardBackup backup) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onUnregister(LaggardBackup backup) {
			// TODO Auto-generated method stub

		}
	};

	private TrackerListener loggerController = new TrackerListener() {

		@Override
		public void onTrackingUpdate(TrackResult result) {
			logMessage("[SERVICE] completion index update: " + result);
		}

		@Override
		public void onStopTracking(ExcursionReport report) {
			logMessage("[SERVICE] stop tracking");
		}

		@Override
		public void onStatusUpdate(UpdateType update) {
			logMessage("[SERVICE] status update: " + update);
		}

		@Override
		public void onStartTracking(Route route) {
			logMessage("[SERVICE] start tracking");
		}

		@Override
		public void onRegister(LaggardBackup backup) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onUnregister(LaggardBackup backup) {
			// TODO Auto-generated method stub

		}
	};

	private void logMessage(String msg) {
		adapter.add(msg);
		adapter.notifyDataSetChanged();
	}

	private OnClickListener startListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (control == null) {
				logMessage("[LISTENER] service not connected");
			} else {
				RouteID id = new RouteID(
						"e6brx2:ahlzfmRpbWEtZGFjYy1tb3VudGFpbnJvdXRlcg0LEgVSb3V0ZRiRvwUM");
				Route dummy;
				try {
					dummy = RoutePersistence.create(TestActivity.this)
							.loadRoute(id);
				} catch (PersistenceException e) {
					e.printStackTrace();
					return;
				}
				control.startTracking(dummy);
			}
		}
	};

	private OnClickListener stopListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (control == null) {
				logMessage("[LISTENER] service not connected");
			} else {
				control.stop();
			}
		}
	};

	private OnClickListener pauseListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (control == null) {
				logMessage("[LISTENER] service not connected");
			} else {
				control.pause();
			}
		}
	};

	private OnClickListener resumeListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (control == null) {
				logMessage("[LISTENER] service not connected");
			} else {
				control.resume();
			}
		}
	};

}
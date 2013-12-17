package it.polimi.dima.dacc.mountainroutes.walktracker.receiver;

import it.polimi.dima.dacc.mountainroutes.types.ExcursionReport;
import it.polimi.dima.dacc.mountainroutes.types.Route;
import it.polimi.dima.dacc.mountainroutes.walktracker.service.BroadcastFactory;
import it.polimi.dima.dacc.mountainroutes.walktracker.service.LaggardBackup;
import it.polimi.dima.dacc.mountainroutes.walktracker.service.UpdateType;

import java.util.HashMap;
import java.util.Map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

public class TrackerListenerManager {

	private static Map<Context, TrackerListenerManager> managers = new HashMap<Context, TrackerListenerManager>();

	public static TrackerListenerManager create(Context context,
			LaggardBackup backup) {
		if (context == null || backup == null) {
			throw new NullPointerException("context or backup are null");
		}

		if (managers.keySet().contains(context)) {
			TrackerListenerManager man = managers.get(context);
			man.setBackup(backup);
		}

		TrackerListenerManager man = new TrackerListenerManager(context, backup);
		managers.put(context, man);
		return man;
	}

	private Map<TrackerListener, ListenerAdapter> mappings;
	private Context context;
	private LaggardBackup backup;

	private TrackerListenerManager(Context context, LaggardBackup backup) {
		this.context = context;
		this.backup = backup;
		this.mappings = new HashMap<TrackerListener, ListenerAdapter>();
	}

	public void registerListener(TrackerListener listener) {
		if (listener == null) {
			throw new NullPointerException("Listener must not be null.");
		}

		if (mappings.keySet().contains(listener)) {
			return;
		}

		ListenerAdapter adapter = new ListenerAdapter(listener);
		mappings.put(listener, adapter);
		LocalBroadcastManager bMan = LocalBroadcastManager.getInstance(context);
		bMan.registerReceiver(adapter,
				BroadcastFactory.getCompleteIntentFilter());
		listener.onRegister(backup);
	}

	public void unregisterListener(TrackerListener listener) {
		if (!mappings.keySet().contains(listener)) {
			return;
		}

		ListenerAdapter adapter = mappings.get(listener);
		mappings.remove(listener);
		LocalBroadcastManager bMan = LocalBroadcastManager.getInstance(context);
		bMan.unregisterReceiver(adapter);
		listener.onUnregister();
	}

	private void setBackup(LaggardBackup backup) {
		this.backup = backup;
	}

	private static class ListenerAdapter extends BroadcastReceiver {

		private TrackerListener listener;

		public ListenerAdapter(TrackerListener listener) {
			this.listener = listener;
		}

		@Override
		public void onReceive(Context context, Intent intent) {
			String[] parts = intent.getAction().split("\\.");
			String action = parts[parts.length - 1];

			if (action.equals(BroadcastFactory.ACTION_START)) {
				Route route = (Route) intent
						.getParcelableExtra(BroadcastFactory.EXTRA_ROUTE);
				listener.onStartTracking(route);
				return;
			}

			if (action.equals(BroadcastFactory.ACTION_STOP)) {
				ExcursionReport report = (ExcursionReport) intent
						.getParcelableExtra(BroadcastFactory.EXTRA_REPORT);
				listener.onStopTracking(report);
			}

			if (action.equals(BroadcastFactory.ACTION_TRACKING)) {
				float completionIndex = intent.getFloatExtra(
						BroadcastFactory.EXTRA_COMPLETION_INDEX, -1);
				if (completionIndex != -1) {
					listener.onTrackingUpdate(completionIndex);
				}
			}

			if (action.equals(BroadcastFactory.ACTION_UPDATE)) {
				String typeName = intent
						.getStringExtra(BroadcastFactory.EXTRA_UPDATE);
				UpdateType updateType = UpdateType.valueOf(typeName);
				listener.onStatusUpdate(updateType);
			}
		}

	}
}

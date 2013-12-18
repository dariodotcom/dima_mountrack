package it.polimi.dima.dacc.mountainroutes.walktracker.receiver;

import it.polimi.dima.dacc.mountainroutes.walktracker.service.BroadcastFactory;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

public class TrackerListenerManager {

	private static final Map<Context, TrackerListenerManager> managers = new HashMap<Context, TrackerListenerManager>();
	private static final IntentFilter intentFilter = BroadcastFactory
			.getCompleteIntentFilter();

	public static TrackerListenerManager inject(Context context) {
		if (context == null) {
			throw new NullPointerException("context is null");
		}

		if (managers.keySet().contains(context)) {
			return managers.get(context);
		}

		TrackerListenerManager man = new TrackerListenerManager(context);
		managers.put(context, man);
		return man;
	}

	public static void clear(Context context) {
		if (context == null) {
			throw new NullPointerException("context is null");
		}

		if (!managers.keySet().contains(context)) {
			return;
		}

		managers.get(context).clear();
		managers.remove(context);
	}

	private Map<TrackerListener, TrackerReceiver> mappings;
	private LocalBroadcastManager lbcMan;
	private LaggardBackup backup;
	private TrackerReceiver backupReceiver;

	private TrackerListenerManager(Context context) {
		this.lbcMan = LocalBroadcastManager.getInstance(context);
		this.backup = new LaggardBackup();
		this.mappings = new HashMap<TrackerListener, TrackerReceiver>();

		backupReceiver = new TrackerReceiver(backup);
		lbcMan.registerReceiver(backupReceiver, intentFilter);
	}

	public void registerListener(TrackerListener listener) {
		if (listener == null) {
			throw new NullPointerException("Listener must not be null.");
		}

		if (mappings.keySet().contains(listener)) {
			return;
		}

		TrackerReceiver adapter = new TrackerReceiver(listener);
		mappings.put(listener, adapter);
		lbcMan.registerReceiver(adapter, intentFilter);
		listener.onRegister(backup);
	}

	public void unregisterListener(TrackerListener listener) {
		if (listener == null) {
			throw new NullPointerException("Listener must not be null.");
		}

		if (!mappings.keySet().contains(listener)) {
			return;
		}

		TrackerReceiver adapter = mappings.get(listener);
		mappings.remove(listener);
		lbcMan.unregisterReceiver(adapter);
		listener.onUnregister(backup);
	}

	private void clear() {
		for (TrackerListener listener : mappings.keySet()) {
			unregisterListener(listener);
		}

		lbcMan.unregisterReceiver(backupReceiver);
	}
}
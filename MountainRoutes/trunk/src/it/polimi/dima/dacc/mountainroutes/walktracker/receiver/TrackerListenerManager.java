package it.polimi.dima.dacc.mountainroutes.walktracker.receiver;

import it.polimi.dima.dacc.mountainroutes.walktracker.service.BroadcastFactory;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

public class TrackerListenerManager {

	private static TrackerListenerManager singletonInstance;
	private static Object singletonLock;
	
	static {
		singletonLock = new Object();
	}
	
	private static final IntentFilter intentFilter = BroadcastFactory
			.getCompleteIntentFilter();

	public static TrackerListenerManager getManager(Context context) {
		if (context == null) {
			throw new NullPointerException("context is null");
		}

		synchronized (singletonLock) {
			if(singletonInstance == null){
				Context appCtx = context.getApplicationContext();
				singletonInstance = new TrackerListenerManager(appCtx);
			}
			
			return singletonInstance;
		}
	}
	
	public static void unload(){
		synchronized (singletonLock) {
			singletonInstance.clear();
			singletonInstance = null;
		}
	}

	private Map<TrackerListener, TrackerReceiver> mappings;
	private LocalBroadcastManager lbcMan;
	private LaggardBackup backup;
	private TrackerReceiver backupReceiver;
	private boolean alive;

	private TrackerListenerManager(Context context) {
		this.lbcMan = LocalBroadcastManager.getInstance(context);
		this.backup = new LaggardBackup();
		this.mappings = new HashMap<TrackerListener, TrackerReceiver>();
		this.alive = true;
		
		backupReceiver = new TrackerReceiver(backup);
		lbcMan.registerReceiver(backupReceiver, intentFilter);
	}

	public void registerListener(TrackerListener listener) {
		if (listener == null) {
			throw new NullPointerException("Listener must not be null.");
		}

		if(!alive){
			return;
		}
		
		if (mappings.keySet().contains(listener)) {
			return;
		}

		TrackerReceiver adapter = new TrackerReceiver(listener);
		mappings.put(listener, adapter);
		lbcMan.registerReceiver(adapter, intentFilter);
		listener.onRegister(backup);
	}
	
	public boolean isAlive(){
		return alive;
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
		this.alive = false;
		
		for (TrackerListener listener : mappings.keySet()) {
			TrackerReceiver adapter = mappings.get(listener);
			lbcMan.unregisterReceiver(adapter);
			listener.onUnregister(backup);
		}

		mappings.clear();
		lbcMan.unregisterReceiver(backupReceiver);
	}
}
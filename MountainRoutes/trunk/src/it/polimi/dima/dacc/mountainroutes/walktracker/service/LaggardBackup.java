package it.polimi.dima.dacc.mountainroutes.walktracker.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

/**
 * Receiver that registers all updates from tracking service, computes an
 * aggregate and allows components that registers for updates after the tracker
 * started to be up to date
 */
public class LaggardBackup extends BroadcastReceiver {

	private LocalBroadcastManager mBroadcaster;

	public LaggardBackup(Context context) {
		this.mBroadcaster = LocalBroadcastManager.getInstance(context);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub

	}

	public void register() {
		mBroadcaster.registerReceiver(this,
				BroadcastFactory.getCompleteIntentFilter());

	}

	public void unregister() {
		mBroadcaster.unregisterReceiver(this);
	}
}

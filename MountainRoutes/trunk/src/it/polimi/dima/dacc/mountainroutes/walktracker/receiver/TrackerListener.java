package it.polimi.dima.dacc.mountainroutes.walktracker.receiver;

public interface TrackerListener extends TrackerListenerBase {

	void onRegister(LaggardBackup backup);
	void onUnregister();
}

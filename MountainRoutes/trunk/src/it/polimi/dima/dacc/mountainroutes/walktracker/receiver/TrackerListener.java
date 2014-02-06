package it.polimi.dima.dacc.mountainroutes.walktracker.receiver;

/**
 * Interface for view components that want to register themselves to
 * {@link TrackerListenerManager} 
 */
public interface TrackerListener extends TrackerListenerBase {

	void onRegister(LaggardBackup backup);

	void onUnregister(LaggardBackup backup);
}

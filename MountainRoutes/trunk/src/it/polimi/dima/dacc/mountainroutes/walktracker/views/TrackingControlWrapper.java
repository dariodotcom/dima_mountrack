package it.polimi.dima.dacc.mountainroutes.walktracker.views;

import android.util.Log;
import it.polimi.dima.dacc.mountainroutes.types.Route;
import it.polimi.dima.dacc.mountainroutes.walktracker.service.TrackingService.TrackingControl;

/**
 * Wrapper for a {@link TrackingControl} reference that performs nullchecks
 * before actually calling methods.
 * 
 * @author Chiara
 * 
 */
public class TrackingControlWrapper {

	private TrackingControl control;

	public void setControl(TrackingControl control) {
		this.control = control;
	}

	public void startTracking(Route route) {
		if (control == null) {
			notifyControlIsNull();
			return;
		}

		control.startTracking(route);
	}

	public void pause() {
		if (control == null) {
			notifyControlIsNull();
			return;
		}

		control.pause();
	}

	public void resume() {
		if (control == null) {
			notifyControlIsNull();
			return;
		}

		control.resume();
	}

	public void stop() {
		if (control == null) {
			notifyControlIsNull();
			return;
		}

		control.stop();
	}

	private void notifyControlIsNull() {
		Log.w(getClass().getName(), "called wrapper method but control is null.");
	}
}

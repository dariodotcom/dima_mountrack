package it.polimi.dima.dacc.mountainroutes.walktracker;

import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;

/**
 * Class that counts elapsing time.
 */
public class Timer {

	public static interface Listener {
		public void onTime(long millis);
	}

	private static enum State {
		READY, RUNNING, PAUSED, STOPPED;

		public void assertIs(State value) {
			if (this != value) {
				throw new IllegalStateException("Expected state " + value.name() + " but get " + this.name());
			}
		}
	}

	private static final long SAMPLE_INTERVAL = 500;

	private Handler handler;
	private Listener listener;
	private State currentState;
	private long elapsedMillis;
	private Long lastSampleInstant;

	public Timer(Looper looper, Listener listener) {
		if (listener == null) {
			throw new NullPointerException("listener is null");
		}

		if (looper == null) {
			looper = Looper.getMainLooper();
		}

		this.handler = new Handler(looper);
		this.listener = listener;
		this.currentState = State.READY;
		this.elapsedMillis = 0;
		this.lastSampleInstant = null;
	}

	public Timer(Listener listener) {
		this(null, listener);
	}

	public void start() {
		currentState.assertIs(State.READY);

		// Start timer
		handler.post(sampler);
		currentState = State.RUNNING;
	}

	public void pause() {
		currentState.assertIs(State.RUNNING);
		sample();
		lastSampleInstant = null;
		handler.removeCallbacks(sampler);
		currentState = State.PAUSED;
	}

	public void resume() {
		currentState.assertIs(State.PAUSED);
		handler.post(sampler);
		currentState = State.RUNNING;
	}

	public void stop() {
		currentState.assertIs(State.RUNNING);
		handler.removeCallbacks(sampler);
		currentState = State.STOPPED;
	}

	public synchronized long getMillis() {
		return elapsedMillis;
	}

	private synchronized void sample() {
		long now = SystemClock.elapsedRealtime();

		if (lastSampleInstant != null) {
			elapsedMillis += (now - lastSampleInstant);
			listener.onTime(elapsedMillis);
		}

		lastSampleInstant = now;
	}

	private Runnable sampler = new Runnable() {

		@Override
		public void run() {
			sample();
			handler.postDelayed(sampler, SAMPLE_INTERVAL);
		}
	};
}

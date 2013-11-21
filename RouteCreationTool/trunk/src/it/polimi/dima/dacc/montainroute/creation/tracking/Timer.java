package it.polimi.dima.dacc.montainroute.creation.tracking;

import android.os.Handler;
import android.os.SystemClock;

public class Timer {
	private final static int SAMPLE_TIME_MILLIS = 500;
	private Long elapsedTime = 0L, lastSampleInstant;
	private Handler handler = new Handler();
	private TrackingUpdateBroadcaster broadcaster;

	public Timer(TrackingUpdateBroadcaster broadcaster) {
		this.broadcaster = broadcaster;
	}

	public void start() {
		handler.postDelayed(sampler, 0);
	}

	public void stop() {
		handler.removeCallbacks(sampler);
	}

	private synchronized void sample() {
		long now = SystemClock.elapsedRealtime();

		if (lastSampleInstant != null) {
			elapsedTime += (now - lastSampleInstant);
		}

		lastSampleInstant = now;
		broadcaster.sendTimeUpdate(elapsedTime);
	}

	private Runnable sampler = new Runnable() {
		@Override
		public void run() {
			sample();
			handler.postDelayed(sampler, SAMPLE_TIME_MILLIS);
		}
	};

}
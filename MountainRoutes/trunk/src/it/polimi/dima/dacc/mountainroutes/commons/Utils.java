package it.polimi.dima.dacc.mountainroutes.commons;

public class Utils {

	private final static int MIN_GAP = 10;
	
	public static String formatMillis(long millis) {
		return formatSeconds((int) millis / 1000);
	}
	
	private static String pan(int n) {
		if (n < 10) {
			return "0" + n;
		}
		
		return String.valueOf(n);
	}

	public static String formatMinutes(int minutes) {
		int hours = minutes / 60;
		minutes = minutes % 60;

		String pattern = "%s:%s:%s";
		return String.format(pattern, pan(hours), pan(minutes), "00");
	}

	public static String formatSeconds(int seconds) {
		int minutes = seconds / 60;
		int hours = minutes / 60;
		minutes = minutes % 60;
		seconds = seconds % 60;

		String pattern = "%s:%s:%s";
		return String.format(pattern, pan(hours), pan(minutes), pan(seconds));
	}
	
	public static String formatGap(int gap){
		return Math.abs(gap) > MIN_GAP ? gap + "m" : "-";
	}
}

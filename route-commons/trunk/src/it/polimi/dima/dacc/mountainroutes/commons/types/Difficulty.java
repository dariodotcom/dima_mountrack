package it.polimi.dima.dacc.mountainroutes.commons.types;

public enum Difficulty {
	TURISTIC, EXCURSIONIST, ADVANCED, EQUIPPED;

	public static int indexOf(Difficulty difficulty) {
		Difficulty[] values = Difficulty.values();
		for (int i = 0; i < values.length; i++) {
			Difficulty d = values[i];
			if (d.equals(difficulty)) {
				return i;
			}
		}

		return -1;
	}
	
	public static Difficulty valueOf(int index){
		return Difficulty.values()[index];
	}
}

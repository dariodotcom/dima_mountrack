package it.polimi.dima.dacc.mountainroutes;

import android.content.Context;
import android.util.SparseArray;

public class StringRepository {

	private Context context;
	private SparseArray<String> strings;

	public StringRepository(Context context) {
		if (context == null) {
			throw new IllegalArgumentException("Context must not be null");
		}

		this.context = context;
		this.strings = new SparseArray<String>();
	}

	public void loadString(int id) {
		String s = context.getResources().getString(id);
		if (s == null) {
			throw new IllegalArgumentException("String " + id + " not found.");
		}
		strings.put(id, s);
	}

	public String getString(int id) {
		return strings.get(id);
	}

}

package it.polimi.dima.dacc.mountainroutes.loader;

public class LoadResult<E> {

	public final static int RESULT = 0;
	public final static int ERROR = 1;

	private E result;
	private LoadError error;
	private int type;

	public LoadResult(E result) {
		this.result = result;
		type = RESULT;
	}

	public LoadResult(LoadError error) {
		this.error = error;
		type = ERROR;
	}

	public E getResult() {
		return result;
	}

	public LoadError getError() {
		return error;
	}

	public int getType() {
		return type;
	}

	@Override
	public String toString() {
		switch (type) {
		case RESULT:
			return "RESULT: " + result;
		case ERROR:
			return "ERROR: " + error;
		default:
			return super.toString();
		}
	}
}

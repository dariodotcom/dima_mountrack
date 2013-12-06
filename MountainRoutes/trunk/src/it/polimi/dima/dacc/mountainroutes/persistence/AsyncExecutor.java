package it.polimi.dima.dacc.mountainroutes.persistence;

import it.polimi.dima.dacc.mountainroutes.contentloader.ContentErrorType;
import android.os.AsyncTask;

public class AsyncExecutor<E> {

	private Callback<E> callback;

	public AsyncExecutor(Callback<E> callback) {
		this.callback = callback;
	}

	@SuppressWarnings("unchecked")
	public Executor<E> execute(Task<E> task) {
		Executor<E> executor = new Executor<E>(callback);
		executor.execute(task);
		return executor;
	}

	private static class Executor<E> extends
			AsyncTask<Task<E>, Void, Container<E>> {

		private Callback<E> callback;

		public Executor(Callback<E> callback) {
			this.callback = callback;
		}

		@Override
		protected Container<E> doInBackground(Task<E>... tasks) {
			callback.onStart();
			if (tasks == null || tasks[0] == null) {
				return null;
			}

			Container<E> container = tasks[0].execute();
			return container;
		}

		@Override
		protected void onPostExecute(Container<E> result) {
			if (result == null) {
				return;
			}

			switch (result.getType()) {
			case Container.RESULT:
				callback.onResult(result.getResult());
				break;
			case Container.ERROR:
				callback.onError(result.getError());
				break;
			}
		}
	}

	public static class Container<E> {

		public static final int RESULT = 0, ERROR = 1;

		public Container(E result) {
			this.result = result;
			this.type = RESULT;
		}

		public Container(ContentErrorType error) {
			this.error = error;
			this.type = ERROR;
		}

		private int type;
		private E result;
		private ContentErrorType error;

		public int getType() {
			return type;
		}

		public E getResult() {
			return result;
		}

		public ContentErrorType getError() {
			return error;
		}
	}

	public static interface Task<E> {
		public Container<E> execute();
	}

	public static interface Callback<E> {
		public void onResult(E result);

		public void onStart();

		public void onError(ContentErrorType type);
	}
}

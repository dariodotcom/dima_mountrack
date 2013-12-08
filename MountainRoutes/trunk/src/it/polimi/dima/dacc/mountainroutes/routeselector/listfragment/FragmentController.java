package it.polimi.dima.dacc.mountainroutes.routeselector.listfragment;

import it.polimi.dima.dacc.mountainroutes.R;
import it.polimi.dima.dacc.mountainroutes.types.RouteSummaryList;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class FragmentController {

	private final static String TAG = "fragment-controller";
	private final static String STATE = "state";
	private final static String RESULT = "result";
	private final static String MESSAGE = "message";

	public static enum State {
		RESULT, LOADING, MESSAGE
	}

	private State currentState;
	private View[] panels = new View[3];

	private ListView resultList;
	private RouteListAdapter adapter;
	private RouteSummaryList currentResult;
	private TextView messageView;

	public FragmentController(View fragmentView, Context context) {

		// Load view elements
		resultList = (ListView) fragmentView.findViewById(R.id.route_list);
		adapter = new RouteListAdapter(context);
		resultList.setAdapter(adapter);
		messageView = (TextView) fragmentView.findViewById(R.id.message_view);

		panels[State.RESULT.ordinal()] = resultList;
		panels[State.LOADING.ordinal()] = fragmentView
				.findViewById(R.id.loading_overlay);
		panels[State.MESSAGE.ordinal()] = fragmentView
				.findViewById(R.id.message_overlay);
	}

	public void showLoading() {
		this.currentState = State.LOADING;
		showView(State.LOADING.ordinal());
	}

	public void showMessage(String message) {
		this.currentState = State.MESSAGE;
		this.messageView.setText(message);
		showView(State.MESSAGE.ordinal());
	}

	public void showResult(RouteSummaryList result) {
		this.currentState = State.RESULT;
		this.currentResult = result;
		this.adapter.clear();
		if (result != null) {
			this.adapter.addAll(result.asList());
		}
		this.adapter.notifyDataSetChanged();
		showView(State.RESULT.ordinal());
	}

	public void saveState(Bundle out) {
		out.putString(STATE, currentState.toString());
		switch (currentState) {
		case RESULT:
			out.putParcelable(RESULT, currentResult);
			Log.d(TAG, "saved result list");
			break;
		case MESSAGE:
			out.putString(MESSAGE, messageView.getText().toString());
			Log.d(TAG, "saved message");
		default:
			break;
		}
	}

	public void loadState(Bundle in) {
		if (in == null) {
			currentState = State.RESULT;
			return;
		}

		String stateName = in.getString(STATE);
		if (stateName == null) {
			return;
		}

		State state = State.valueOf(stateName);
		switch (state) {
		case RESULT:
			RouteSummaryList result = (RouteSummaryList) in
					.getParcelable(RESULT);
			Log.d(TAG, "loaded result list");
			showResult(result);
		case MESSAGE:
			String message = in.getString(MESSAGE);
			showMessage(message);
			Log.d(TAG, "loaded message");
			break;
		default:
			break;
		}
	}

	private void showView(int index) {
		for (int i = 0; i < panels.length; i++) {
			if (i == index && panels[i].getAlpha() != 1) {
				panels[i].animate().alpha(1).setDuration(250);
			} else if (i != index && panels[i].getAlpha() != 0) {
				panels[i].animate().alpha(0).setDuration(250);
			}
		}
	}
}

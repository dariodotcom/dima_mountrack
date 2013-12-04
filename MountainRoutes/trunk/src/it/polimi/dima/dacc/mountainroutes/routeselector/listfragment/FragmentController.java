package it.polimi.dima.dacc.mountainroutes.routeselector.listfragment;

import it.polimi.dima.dacc.mountainroutes.R;
import it.polimi.dima.dacc.mountainroutes.commons.types.RouteDescriptionList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class FragmentController {

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
	private RouteDescriptionList currentResult;
	private TextView messageView;

	public FragmentController(RouteListFragment fragment) {
		Activity fragmentActivity = fragment.getActivity();
		if (fragmentActivity == null) {
			throw new IllegalStateException(
					"Fragment has not been attached to activity");
		}

		View fragmentView = fragment.getView();
		if (fragmentView == null) {
			throw new IllegalStateException("Fragment view is null");
		}

		resultList = (ListView) fragmentView.findViewById(R.id.route_list);
		adapter = new RouteListAdapter(fragment.getActivity());
		resultList.setAdapter(adapter);
		messageView = (TextView) fragmentView.findViewById(R.id.message_view);
	}

	public void showLoading() {
		showView(State.LOADING.ordinal());
	}

	public void showMessage(String message) {
		this.messageView.setText(message);
		showView(State.MESSAGE.ordinal());
	}

	public void showResult(RouteDescriptionList result) {
		this.currentResult = result;
		this.adapter.clear();
		if (result != null) {
			this.adapter.addAll(result.getRouteDescriptions());
		}
		this.adapter.notifyDataSetChanged();
		showView(State.RESULT.ordinal());
	}

	public void saveState(Bundle out) {
		out.putString(STATE, currentState.toString());
		switch (currentState) {
		case RESULT:
			out.putParcelable(RESULT, currentResult);
			break;
		case MESSAGE:
			out.putString(MESSAGE, messageView.getText().toString());
		default:
			break;
		}
	}

	public void loadState(Bundle in) {
		String stateName = in.getString(STATE);
		if (stateName == null) {
			return;
		}

		State state = State.valueOf(stateName);
		switch (state) {
		case RESULT:
			RouteDescriptionList result = (RouteDescriptionList) in
					.getParcelable(RESULT);
			showResult(result);
		case MESSAGE:
			String message = in.getString(MESSAGE);
			showMessage(message);
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

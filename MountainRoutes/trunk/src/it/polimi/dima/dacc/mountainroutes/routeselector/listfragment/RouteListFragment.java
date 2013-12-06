package it.polimi.dima.dacc.mountainroutes.routeselector.listfragment;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import it.polimi.dima.dacc.mountainroutes.R;
import it.polimi.dima.dacc.mountainroutes.remotecontent.LoadError;
import it.polimi.dima.dacc.mountainroutes.routeselector.listfragment.OnRouteSelected.ItemClickAdapter;
import it.polimi.dima.dacc.mountainroutes.routeselector.sources.RouteSource;
import it.polimi.dima.dacc.mountainroutes.types.RouteSummary;
import it.polimi.dima.dacc.mountainroutes.types.RouteSummaryList;

public class RouteListFragment extends Fragment implements
		RouteSource.ResultObserver {

	private final static int LIST_VIEW = 0;
	private final static int MESSAGE_VIEW = 1;
	private final static int LOADING_VIEW = 2;

	private View[] panels = new View[3];

	private TextView messageContainer;
	private RouteListAdapter resultAdapter;
	private ListView listView;
	private RouteSource source;

	private String emptyMessage;
	private String gpsDisabledMessage;
	private String networkDisabledMessage;
	private String generalErrorMessage;
	private String internalErrorMessage;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View inflated = inflater.inflate(R.layout.route_list_fragment_2, null);

		panels[LOADING_VIEW] = inflated.findViewById(R.id.loading_overlay);
		panels[MESSAGE_VIEW] = inflated.findViewById(R.id.message_overlay);
		panels[LIST_VIEW] = inflated.findViewById(R.id.route_list);

		this.listView = (ListView) panels[LIST_VIEW];
		this.messageContainer = (TextView) inflated
				.findViewById(R.id.message_view);
		this.resultAdapter = new RouteListAdapter(this.getActivity());

		Context context = this.getActivity();

		// Load error messages
		emptyMessage = context.getString(R.string.no_result_message);
		generalErrorMessage = context.getString(R.string.general_error_message);
		internalErrorMessage = context
				.getString(R.string.internal_error_message);
		networkDisabledMessage = context
				.getString(R.string.network_unavailable_message);
		gpsDisabledMessage = context.getString(R.string.gps_disabled_message);

		listView.setAdapter(resultAdapter);

		return inflated;
	}

	public void setSource(RouteSource source) {
		this.source = source;
	}

	public void update() {
		if (this.source == null) {
			return;
		}

		this.source.loadRoutes(this);
	}

	public void setOnRouteSelectListener(OnRouteSelected listener) {
		OnItemClickListener l = new ItemClickAdapter(listener);
		this.listView.setOnItemClickListener(l);
	}

	@Override
	public void onResultReceived(RouteSummaryList result) {

		Log.d("list-fragment", "onResultReceived called");

		// If result is null, no search was performed
		if (result == null) {
			this.resultAdapter.clear();
			this.resultAdapter.notifyDataSetChanged();
			showPanel(LIST_VIEW);
			return;
		}

		List<RouteSummary> summaries = result.getRouteSummaries();
		if (summaries.isEmpty()) {
			messageContainer.setText(emptyMessage);
			showPanel(MESSAGE_VIEW);
			return;
		}

		RouteListAdapter adapter = new RouteListAdapter(getActivity());
		adapter.addAll(summaries);
		adapter.notifyDataSetChanged();
		this.resultAdapter = adapter;
		this.listView.setAdapter(adapter);
		showPanel(LIST_VIEW);
	}

	@Override
	public void onLoadStart() {
		Log.d("list-fragment", "onLoadStart called");
		showPanel(LOADING_VIEW);
	}

	@Override
	public void onError(LoadError error) {
		Log.d("list-fragment", "onError called");
		String message;
		switch (error) {
		case GPS_DISABLED:
			message = gpsDisabledMessage;
			break;
		case NETWORK_UNAVAILABLE:
			message = networkDisabledMessage;
			break;
		case RESPONSE_ERROR:
		case SERVER_ERROR:
			message = internalErrorMessage;
			break;
		default:
			message = generalErrorMessage;
			break;
		}

		messageContainer.setText(message);
		showPanel(MESSAGE_VIEW);
	}

	private void showPanel(int index) {
		Log.d("list-fragment", "show panel " + index);
		for (int i = 0; i < panels.length; i++) {
			View current = panels[i];

			if (index == i) {
				showView(current);
			} else {
				hideView(current);
			}
		}
	}

	private void showView(final View view) {
		view.animate().alpha(1).setDuration(250);
	}

	private void hideView(final View view) {
		view.animate().alpha(0).setDuration(250);
	}
}

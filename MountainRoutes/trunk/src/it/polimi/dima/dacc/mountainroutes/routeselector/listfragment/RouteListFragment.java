package it.polimi.dima.dacc.mountainroutes.routeselector.listfragment;

import java.util.List;

import android.content.Context;
import android.opengl.Visibility;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import it.polimi.dima.dacc.mountainroutes.R;
import it.polimi.dima.dacc.mountainroutes.commons.types.RouteDescription;
import it.polimi.dima.dacc.mountainroutes.commons.types.RouteDescriptionList;
import it.polimi.dima.dacc.mountainroutes.contentloader.ContentErrorType;
import it.polimi.dima.dacc.mountainroutes.routeselector.listfragment.OnRouteSelected.ItemClickAdapter;
import it.polimi.dima.dacc.mountainroutes.routeselector.sources.RouteSource;

public class RouteListFragment extends Fragment implements
		RouteSource.ResultObserver {

	private RelativeLayout loadingOverlay;
	private RelativeLayout messagePanel;
	private TextView messageView;
	private RouteListAdapter resultAdapter;
	private ListView listView;
	private RouteSource source;
	private String emptyMessage;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View inflated = inflater.inflate(R.layout.route_list_fragment_2, null);

		this.loadingOverlay = (RelativeLayout) inflated
				.findViewById(R.id.loading_overlay);
		this.messagePanel = (RelativeLayout) inflated
				.findViewById(R.id.message_overlay);
		this.messageView = (TextView) inflated.findViewById(R.id.message_view);
		this.listView = (ListView) inflated.findViewById(R.id.route_list);
		this.resultAdapter = new RouteListAdapter(this.getActivity());

		Context context = this.getActivity();
		emptyMessage = context.getString(R.id.emptylist_result);

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
	public void onResultReceived(RouteDescriptionList result) {
		messagePanel.setVisibility(View.INVISIBLE);

		// If result is null, no search was performed
		if (result == null) {
			this.resultAdapter.clear();
			this.resultAdapter.notifyDataSetChanged();
			listView.setVisibility(View.VISIBLE);
			messagePanel.setVisibility(View.GONE);
			hideLoadingOverlay();
			return;
		}

		List<RouteDescription> descriptions = result.getRouteDescriptions();
		if (descriptions.isEmpty()) {
			listView.setVisibility(View.GONE);
			messagePanel.setVisibility(View.VISIBLE);
			messageView.setText(emptyMessage);
			hideLoadingOverlay();
			return;
		}

		this.resultAdapter.clear();
		this.resultAdapter.addAll(descriptions);
		this.resultAdapter.notifyDataSetChanged();

		hideLoadingOverlay();
	}

	@Override
	public void onLoadStart() {
		showLoadingOverlay();
	}

	@Override
	public void onError(ContentErrorType error) {
		messageView.setText("Error occured");
		listView.setVisibility(View.GONE);
		messagePanel.setVisibility(View.VISIBLE);
	}

	private void showLoadingOverlay() {
		if (loadingOverlay.getVisibility() == View.VISIBLE) {
			return;
		}

		loadingOverlay.animate().alpha(1).setDuration(250)
				.withStartAction(new Runnable() {

					@Override
					public void run() {
						loadingOverlay.setVisibility(View.VISIBLE);
					}
				});
	}

	private void hideLoadingOverlay() {
		if (loadingOverlay.getVisibility() == View.GONE) {
			return;
		}

		loadingOverlay.animate().alpha(0).setDuration(250)
				.withEndAction(new Runnable() {

					@Override
					public void run() {
						loadingOverlay.setVisibility(View.GONE);
					}
				});
	}
}

package it.polimi.dima.dacc.mountainroutes.routeselector.listfragment;

import java.util.List;

import it.polimi.dima.dacc.mountainroute.commons.types.RouteDescription;
import it.polimi.dima.dacc.mountainroute.commons.types.RouteDescriptionList;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ViewAnimator;
import it.polimi.dima.dacc.mountainroutes.R;
import it.polimi.dima.dacc.mountainroutes.routeselector.listfragment.OnRouteSelected.ItemClickAdapter;
import it.polimi.dima.dacc.mountainroutes.routeselector.sources.RouteSource;
import it.polimi.dima.dacc.mountainroutes.routeselector.sources.RouteSourceError;

public class RouteListFragment extends Fragment implements
		RouteSource.ResultObserver {

	private static final int RESULT_PAGE_INDEX = 0;
	private static final int MESSAGE_PAGE_INDEX = 1;
	private static final int LOADING_PAGE_INDEX = 2;

	private ViewAnimator animator;
	private RouteListAdapter resultAdapter;
	private ListView listView;
	private RouteSource source;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View inflated = inflater.inflate(R.layout.route_list_fragment, null);

		int animatorId = R.id.route_list_animator;
		this.animator = (ViewAnimator) inflated.findViewById(animatorId);
		this.listView = (ListView) inflated.findViewById(R.id.route_list);
		this.resultAdapter = new RouteListAdapter(this.getActivity());

		listView.setAdapter(resultAdapter);

		return inflated;
	}

	private void showPanel(int index) {
		if (animator.getDisplayedChild() != index) {
			animator.setDisplayedChild(index);
		}
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

		// If result is null, no search was performed
		if (result == null) {
			this.resultAdapter.clear();
			this.resultAdapter.notifyDataSetChanged();
			this.showPanel(RESULT_PAGE_INDEX);
			return;
		}

		List<RouteDescription> descriptions = result.getRouteDescriptions();
		if (descriptions.isEmpty()) {
			// TODO set error message
			this.showPanel(MESSAGE_PAGE_INDEX);
			return;
		}

		this.resultAdapter.clear();
		this.resultAdapter.addAll(descriptions);
		this.resultAdapter.notifyDataSetChanged();

		this.showPanel(RESULT_PAGE_INDEX);
	}

	@Override
	public void onLoadStart() {
		this.showPanel(LOADING_PAGE_INDEX);
	}

	@Override
	public void onError(RouteSourceError error) {
		// TODO set error message
		this.showPanel(MESSAGE_PAGE_INDEX);
	}
}

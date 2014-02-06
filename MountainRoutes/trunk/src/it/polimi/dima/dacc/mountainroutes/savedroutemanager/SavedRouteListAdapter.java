package it.polimi.dima.dacc.mountainroutes.savedroutemanager;

import it.polimi.dima.dacc.mountainroutes.R;
import it.polimi.dima.dacc.mountainroutes.types.RouteID;
import it.polimi.dima.dacc.mountainroutes.types.RouteSummary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Adapter that displays a list of routes in a {@link ListView}.
 * @author Chiara
 *
 */
public class SavedRouteListAdapter extends ArrayAdapter<RouteSummary> {

	public SavedRouteListAdapter(Context context) {
		super(context, R.id.route_name);
	}

	private onDeleteRouteListener onDeleteListener;

	public void setOnDeleteRouteListener(onDeleteRouteListener listener) {
		this.onDeleteListener = listener;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;

		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.saved_route_list_element, null);
		}

		final RouteSummary summary = getItem(position);

		TextView routeName = (TextView) view.findViewById(R.id.route_name);
		Button button = (Button) view.findViewById(R.id.delete_button);

		routeName.setText(summary.getName());
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (onDeleteListener != null) {
					onDeleteListener.onDelete(summary.getId());
				}
			}
		});

		return view;
	}

	public static interface onDeleteRouteListener {
		public void onDelete(RouteID id);
	}
}

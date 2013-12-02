package it.polimi.dima.dacc.mountainroutes.routeselector.listfragment;

import it.polimi.dima.dacc.mountainroutes.R;
import it.polimi.dima.dacc.mountainroutes.commons.types.RouteDescription;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class RouteListAdapter extends ArrayAdapter<RouteDescription> {

	public RouteListAdapter(Context context) {
		super(context, R.layout.route_list_element);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View view = convertView;

		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.route_list_element, null);
		}

		RouteDescription desc = getItem(position);

		if (desc != null) {
			TextView name = (TextView) view.findViewById(R.id.route_name);
			name.setText(desc.getName());
		}

		return view;
	}
}

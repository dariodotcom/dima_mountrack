package it.polimi.dima.dacc.mountainroutes.routeselector.listfragment;

import it.polimi.dima.dacc.mountainroute.commons.types.RouteDescription;
import it.polimi.dima.dacc.mountainroutes.R;
import android.content.Context;
import android.widget.ArrayAdapter;

public class RouteListAdapter extends ArrayAdapter<RouteDescription> {

	public RouteListAdapter(Context context) {
		super(context, R.layout.route_list_element);
	}
}

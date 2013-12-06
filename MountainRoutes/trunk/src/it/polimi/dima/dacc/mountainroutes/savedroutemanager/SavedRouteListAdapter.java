package it.polimi.dima.dacc.mountainroutes.savedroutemanager;

import it.polimi.dima.dacc.mountainroutes.R;
import it.polimi.dima.dacc.mountainroutes.types.RouteSummaryList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

public class SavedRouteListAdapter extends ArrayAdapter<RouteSummaryList>{

	public SavedRouteListAdapter(Context context) {
		super(context, R.id.saved_route_name);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		
		if(view == null) {
			LayoutInflater inflater = LayoutInflater.from(getContext());
			inflater.inflate(R.layout.saved_route_list_element, parent);
		}
		
		TextureView routeName = (TextView) view.findViewById(R.id.saved_route_name);
		Button button = (Button) view.findViewById(R.id.delete_saved_route);
		
		
		
		return view;
	}
}

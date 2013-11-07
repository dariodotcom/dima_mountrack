package it.polimi.dima.dacc.montainroute.routecreationtool;

import java.util.List;

import com.google.android.gms.maps.model.LatLng;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LatLngArrayAdapter extends ArrayAdapter<LatLng> {
	int resource;

	public LatLngArrayAdapter(Context context, int _resource, List<LatLng> items) {
		super(context, _resource, items);
		resource = _resource;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// Create and inflate the View to display
		LinearLayout newView;
		if (convertView == null) {
			// Inflate a new view if this is not an update.
			newView = new LinearLayout(getContext());
			String inflater = Context.LAYOUT_INFLATER_SERVICE;
			LayoutInflater li;
			li = (LayoutInflater) getContext().getSystemService(inflater);
			li.inflate(resource, newView, true);
		} else {
			// Otherwise we’ll update the existing View
			newView = (LinearLayout) convertView;
		}

		LatLng point = getItem(position);
		TextView textView = (TextView) newView.findViewById(android.R.id.text1);
		textView.setText(String.format("(%s,%s)", point.latitude,
				point.longitude));

		return newView;
	}
}
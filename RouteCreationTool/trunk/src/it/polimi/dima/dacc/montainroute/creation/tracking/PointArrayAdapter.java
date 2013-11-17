package it.polimi.dima.dacc.montainroute.creation.tracking;

import it.polimi.dima.dacc.mountainroute.commons.types.Point;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PointArrayAdapter extends ArrayAdapter<Point> {
	int resource;

	public PointArrayAdapter(Context context, int _resource, List<Point> items) {
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

		Point point = getItem(position);
		TextView textView = (TextView) newView.findViewById(android.R.id.text1);
		textView.setText(point.toString());

		return newView;
	}
}
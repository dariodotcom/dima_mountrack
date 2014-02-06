package it.polimi.dima.dacc.mountainroutes.reportmanager;

import java.text.DateFormat;

import it.polimi.dima.dacc.mountainroutes.R;
import it.polimi.dima.dacc.mountainroutes.types.ExcursionReport;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

/**
 * Adapter that is used to display a list of excursion reports in a ListView
 */
public class ReportListAdapter extends ArrayAdapter<ExcursionReport> {

	public ReportListAdapter(Context context) {
		super(context, R.id.excursion_name, R.id.excursion_date);
	}

	private onDeleteExcursionListener onDeleteListener;

	public static interface onDeleteExcursionListener {
		public void onDelete(int excursionId);
	}

	public void setOnDeleteExcursionListener(onDeleteExcursionListener listener) {
		this.onDeleteListener = listener;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View view = convertView;

		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.report_list_element, null);
		}

		final ExcursionReport excursionReport = getItem(position);

		TextView excursionName = (TextView) view.findViewById(R.id.excursion_name);
		TextView excursionDate = (TextView) view.findViewById(R.id.excursion_date);
		Button button = (Button) view.findViewById(R.id.delete_button);

		excursionName.setText(excursionReport.getRouteName());
		DateFormat format = android.text.format.DateFormat.getDateFormat(getContext());
		excursionDate.setText(format.format(excursionReport.getDate()));

		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (onDeleteListener != null) {
					onDeleteListener.onDelete(excursionReport.getId());
				}
			}
		});

		return view;
	}

}

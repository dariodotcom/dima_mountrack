package it.polimi.dima.dacc.mountainroutes.routeselector.listfragment;

import it.polimi.dima.dacc.mountainroutes.commons.types.RouteDescription;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public interface OnRouteSelected {
	public void onRouteSelected(RouteDescription  description);

	/* Adapter to use this interface in methods that require ItemClickListener */
	public static class ItemClickAdapter implements OnItemClickListener {

		private OnRouteSelected originalListener;

		public ItemClickAdapter(OnRouteSelected originalListener) {
			this.originalListener = originalListener;
		}

		@Override
		public void onItemClick(AdapterView<?> adapter, View view, int index,
				long id) {
			RouteDescription description = (RouteDescription) adapter
					.getItemAtPosition(index);
			this.originalListener.onRouteSelected(description);
		}
	}
}

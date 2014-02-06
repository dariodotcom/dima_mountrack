package it.polimi.dima.dacc.mountainroutes.remote;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

/**
 * Singleton class that provides an instance of a remote content manager that
 * can be used to query tracks.
 */
public class RemoteContentManager {

	public final static String NAME_PARAM = "name";
	public final static String POSITION_PARAM = "position";
	public final static String ID_PARAM = "id";

	private static List<ContentProvider> availableProviders;

	static {
		availableProviders = new ArrayList<ContentProvider>();
		availableProviders.add(new DummyProvider());
	}

	private static RemoteContentManager instance;
	private ContentProvider currentProvider;

	public static RemoteContentManager getInstance() {
		if (instance == null) {
			instance = new RemoteContentManager();
		}

		return instance;
	}

	private RemoteContentManager() {
		this.currentProvider = new DummyProvider();
	}

	public void setProvider(int index) {
		this.currentProvider = availableProviders.get(index);
	}

	public List<ContentProvider> getAvailableProviders() {
		return availableProviders;
	}

	public RemoteContentConnector createConnector(Context context) {
		return new RemoteContentConnector(context, currentProvider);
	}

}

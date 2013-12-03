package it.polimi.dima.dacc.mountainroutes.contentloader;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

public class ContentLoader {

	public final static String NAME_PARAM = "name";
	public final static String POSITION_PARAM = "position";
	public final static String ID_PARAM = "id";

	private static List<ContentProvider> availableProviders;

	static {
		availableProviders = new ArrayList<ContentProvider>();
		availableProviders.add(new DummyProvider());
	}

	private static ContentLoader instance;
	private ContentProvider currentProvider;

	public static ContentLoader getInstance() {
		if (instance == null) {
			instance = new ContentLoader();
		}

		return instance;
	}

	private ContentLoader() {
		this.currentProvider = new DummyProvider();
	}

	public void setProvider(int index) {
		this.currentProvider = availableProviders.get(index);
	}

	public List<ContentProvider> getAvailableProviders() {
		return availableProviders;
	}

	public ContentConnector createConnector(Context context,
			LoaderObserver loader) {
		return new ContentConnector(context, currentProvider, loader);
	}

}

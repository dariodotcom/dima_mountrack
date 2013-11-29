package it.polimi.dima.dacc.mountainroutes.routeselector.pages;

import java.util.Locale;

import it.polimi.dima.dacc.mountainroutes.R;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class SelectorPagerAdapter extends FragmentStatePagerAdapter {

	private static final int PAGE_COUNT = 3;

	private static final int BY_NAME_PAGE_INDEX = 0;
	private static final int NEAR_ME_PAGE_INDEX = 1;
	private static final int SAVED_PAGE_INDEX = 2;

	private boolean showSavedRoutePanel;
	private Context context;

	public SelectorPagerAdapter(FragmentManager fm, Context context,
			boolean showSavedRoutePanel) {
		super(fm);
		this.context = context;
		this.showSavedRoutePanel = showSavedRoutePanel;
	}

	public SelectorPagerAdapter(FragmentManager manager, Context contex) {
		this(manager, contex, false);
	}

	@Override
	public Fragment getItem(int position) {
		switch (position) {
		case BY_NAME_PAGE_INDEX:
			return new ByNamePage();
		case NEAR_ME_PAGE_INDEX:
			return new NearMePage();
		case SAVED_PAGE_INDEX:
			return new SavedPage();
		default:
			return null;
		}
	}

	@Override
	public int getCount() {
		return showSavedRoutePanel ? PAGE_COUNT : PAGE_COUNT - 1;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		String title;

		switch (position) {
		case BY_NAME_PAGE_INDEX:
			title = context.getString(R.string.page_by_name_title);
			break;
		case NEAR_ME_PAGE_INDEX:
			title = context.getString(R.string.page_near_me_title);
			break;
		case SAVED_PAGE_INDEX:
			title = context.getString(R.string.page_saved_title);
			break;
		default:
			title = "";
		}

		Locale locale = Locale.getDefault();
		return title.toUpperCase(locale);
	}

}

package it.polimi.dima.dacc.mountainroutes.routeselector.pages;

import it.polimi.dima.dacc.mountainroutes.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class NearMePage extends Fragment{

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View inflated = inflater.inflate(R.layout.page_near_me, null);
		
		return inflated;
	}
	
}

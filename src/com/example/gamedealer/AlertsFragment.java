package com.example.gamedealer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * 
 */
public class AlertsFragment extends Fragment {

	public AlertsFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_alerts, container, false);
	}
	
	public void displayMessage(){
		Toast.makeText(getActivity(), "SELECTED FRAGMENT", Toast.LENGTH_SHORT).show();
	}

}

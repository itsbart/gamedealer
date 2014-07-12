package com.example.gamedealer;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FilterDialog extends DialogFragment {
	
	public FilterDialog() {
		// TODO Auto-generated constructor stub
	}
	
	//inflate
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//return inflater.inflate(R.id.filterDialog, null);
		return super.onCreateView(inflater, container, savedInstanceState);
	}
}

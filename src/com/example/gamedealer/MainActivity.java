package com.example.gamedealer;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

public class MainActivity extends ActionBarActivity implements TabListener {

	ViewPager viewPager;
	ActionBar actionBar;
	private String[] tabs = { "Hot Deals", "Feed", "Alerts" };
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//Get View Pager, responsible for changing views
		viewPager = (ViewPager) findViewById(R.id.pager);
		//set adapter
		viewPager.setAdapter(new TabsPagerAdapter(getSupportFragmentManager()));
		
		
		//synch changed tabs
		viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
			
			@Override
			public void onPageScrollStateChanged(int state) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void onPageSelected(int position) {
				// TODO Auto-generated method stub
				actionBar.setSelectedNavigationItem(position);
			}
			
			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
				// TODO Auto-generated method stub
			}
		});
		
		//Prepare action Bar
		actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(false);
			
		//Add/Create tabs to actionBar
		for(String tab_name : tabs) {
			actionBar.addTab(actionBar.newTab().setText(tab_name)
					.setTabListener(this));
		}
		
		//actionBar.getTabAt(0).
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		
		if (id == R.id.refresh_button) {
			
			if(viewPager.getCurrentItem() == 0){
			
				Fragment f = ((TabsPagerAdapter) viewPager.getAdapter()).getRegisteredFragment(viewPager.getCurrentItem());
				((HotDealsFrag) f).updateList();
			}
			
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}
	

	// TAB LISTENER METHODS
	
	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		viewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	}

}


class TabsPagerAdapter extends FragmentPagerAdapter {

	SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();
	
	public TabsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	
	//Get fragment at given position
	@Override
	public Fragment getItem(int index) {
		
		//Log.i("DEBUG", "registeredFragments Size: " + registeredFragments.size());
		
		switch(index) {

			case 0:
				return new HotDealsFrag();
			case 1:
				return new FeedFragment();
			case 2:
				return new AlertsFragment();
		}
		
		return null;
	}
		
	@Override
	public int getCount() {
		return 3;
	}
	
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		
		//Log.i("DEBUG", "instantiateItem Called | registeredFragments Size: "+ registeredFragments.size());
		//Log.w("DEBUG", registeredFragments.toString());
		
	     Fragment fragment = (Fragment) super.instantiateItem(container, position);
	     registeredFragments.put(position, fragment);
	     return fragment;
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		
		//Log.i("DEBUG", "destroyItem Called | registeredFragments Size: "+ registeredFragments.size());
		//Log.w("DEBUG", registeredFragments.toString());
		
	    registeredFragments.remove(position);
	    super.destroyItem(container, position, object);
	}
	  
	public Fragment getRegisteredFragment(int position) {
	    return registeredFragments.get(position);
	}  
	
}



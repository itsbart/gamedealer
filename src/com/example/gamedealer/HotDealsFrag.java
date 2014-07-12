package com.example.gamedealer;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.helpers.JSONPuller;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Use the
 * {@link HotDealsFrag#newInstance} factory method to create an instance of this
 * fragment.
 * 
 */
public class HotDealsFrag extends ListFragment {

	// UI
	ProgressDialog progressDialog;
	CustomArrayAdapter adapter;

	// AsyncTask components
	ArrayList<HashMap<String, String>> gamesList;

	private static final String TAG_SUCCESS = "success";
	private static final String TAG_GAMES = "games";
	private static final String TAG_APPID = "appid";
	private static final String TAG_TITLE = "title";
	private static final String TAG_INITIAL_PRICE = "initial_price";
	private static final String TAG_FINAL_PRICE = "final_price";
	private static final String TAG_DISCOUNT = "discount";

	// URL's

	private static final String getAllDealsURL = "http://192.168.8.104/android_connect/get_all_products.php";

	// jsonArray
	JSONArray games = null;

	public HotDealsFrag() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		gamesList = new ArrayList<HashMap<String, String>>();
		adapter = new CustomArrayAdapter(getActivity(),R.layout.simple_row_game);
		setListAdapter(adapter);
		//updateList();
	}

	public void updateList(){
		new LoadAllDeals(getAllDealsURL).execute();
	}
		
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_hot_deals, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		Log.i("ITEM CLICK", "CLICKED at position: " + position);
		Toast.makeText(getActivity(), "Position: " + position,
				Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * 
	 * Simple AsyncTask to load all data from database
	 * 
	 */

	private class LoadAllDeals extends AsyncTask<String, String, JSONObject> {

		private String URL = null;

		public LoadAllDeals(String url) {
			URL = url;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = new ProgressDialog(getActivity());
			progressDialog.setMessage("Getting Data..");
			progressDialog.setIndeterminate(false);
			progressDialog.setCancelable(true);
			progressDialog.show();
		}

		@Override
		protected JSONObject doInBackground(String... params) {

			// initialize helper class to pull json from url
			JSONPuller jsonPuller = new JSONPuller();

			// downloading JSON from URL
			JSONObject json = jsonPuller.getJSONfromURL(URL);
			
			return json;
		}

		@Override
		protected void onPostExecute(JSONObject json) {

			// data downloaded, dismiss dialog
			progressDialog.dismiss();

			if (json != null) {

				try {

					int success = json.getInt(TAG_SUCCESS);

					if (success == 1) {
						
						adapter.clear();

						// checking response
						Log.d("All games:", json.toString());

						// games found
						games = json.getJSONArray(TAG_GAMES);
						// JSONObject item;
						for (int i = 0; i < games.length(); i++) {

							JSONObject item = games.getJSONObject(i);

							String appid = item.getString(TAG_APPID);
							String title = item.getString(TAG_TITLE);
							String initialPrice = "$" + item
									.getString(TAG_INITIAL_PRICE);
							String finalPrice = "$"+ item.getString(TAG_FINAL_PRICE);
							String discount = "-" + item.getString(TAG_DISCOUNT)+"%";

							HashMap<String, String> node = new HashMap<String, String>();
							
							node.put(TAG_APPID, appid);
							node.put(TAG_TITLE, title);
							node.put(TAG_INITIAL_PRICE, initialPrice);
							node.put(TAG_FINAL_PRICE, finalPrice);
							node.put(TAG_DISCOUNT, discount);

							if(node != null) {gamesList.add(node);}
						}

						Log.i("DEBUG","COUNT: " + adapter.getCount());
						
						adapter.notifyDataSetChanged();
						
						/*
						 ListAdapter adapter2 = new SimpleAdapter(getActivity(), gamesList, 
								 R.layout.simple_row_game, new String[] {TAG_TITLE, 
							 TAG_INITIAL_PRICE, TAG_FINAL_PRICE, TAG_DISCOUNT}, 
							 new int[]{R.id.gameTitle, R.id.initialPrice, R.id.finalPrice, R.id.discount});
						 setListAdapter(adapter2); 
						 */
						
					} else {
						Toast.makeText(getActivity(), "No Data Found",
								Toast.LENGTH_SHORT).show();
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}else{
				Toast.makeText(getActivity(), "Could Not Establish Connection",
						Toast.LENGTH_SHORT).show();
			}
			
			Toast.makeText(getActivity(), "Data Updated",
					Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 
	 * @author Bartek
	 * ArrayAdapter applying custom row layout
	 * Includes ViewHolder Design Patter for better optimization
	 */
	
	@SuppressWarnings("rawtypes")
	class CustomArrayAdapter extends ArrayAdapter {

		Context context;

		@SuppressWarnings("unchecked")
		public CustomArrayAdapter(Context c, int textViewResourceId) {
			super(c, textViewResourceId, gamesList);

			this.context = c;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			//175% optimization
			View row = convertView;
			MyViewHolder holder = null;

			//if not created
			if(row == null){
			
				LayoutInflater inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

				row = inflater
						.inflate(R.layout.simple_row_game, parent, false);
				
				//get view holder
				holder = new MyViewHolder(row);
				//store
				row.setTag(holder);
			
			//recycle
			}else{
				//Log.i("DEBUG", "Recycle Called");
				holder = (MyViewHolder) row.getTag();
			}
			
			holder.textTitle.setText(gamesList.get(position).get(TAG_TITLE));
			
			holder.textInitial.setText(gamesList.get(position).get(TAG_INITIAL_PRICE));	
			holder.textInitial.setPaintFlags(holder.textInitial.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
			
			holder.textFinal.setText(gamesList.get(position).get(TAG_FINAL_PRICE));
			holder.textDiscount.setText(gamesList.get(position).get(TAG_DISCOUNT));
		
			return row;
		}
	
		/**
		 * 
		 * @author Bartek
		 * Inner private Class used for ListView optimization
		 * ViewHolder Design Patter
		 */
		
		private class MyViewHolder {
			
			TextView textTitle;
			TextView textInitial;
			TextView textFinal;
			TextView textDiscount;
			
			public MyViewHolder(View v) {
				textTitle = (TextView) v.findViewById(R.id.gameTitle);
				textInitial = (TextView) v.findViewById(R.id.initialPrice);
				textFinal = (TextView) v.findViewById(R.id.finalPrice);
				textDiscount = (TextView) v.findViewById(R.id.discount);
			}
			
		}
	}
	
}

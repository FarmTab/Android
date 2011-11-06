/*
 * Main application inventory page
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package net.FarmTab.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

public class InventoryActivity extends Activity {
	private static final String TAG = "InventoryActivity";
	
	private static final String INVENTORY_API_URL = "http://farmtab.net/delicious.php";
	
	private ArrayList<InventoryItem> mItems;
	private SharedPreferences mPreferences;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); 
	    setContentView(R.layout.icons_grid);
	    
	    mPreferences = getPreferences(MODE_PRIVATE);
	    
	    /* TODO: for demo only */
	    Preferences.storePrefs(mPreferences.edit(), "1", "OldMacDonald", true);
	    
	    
	    mItems = fetchInventory();
	    
	    GridView grid = (GridView) findViewById(R.id.itemsGrid);
	    GridAdapter mAdapter = new GridAdapter();
	    grid.setAdapter(mAdapter);
	
	    grid.setOnItemClickListener(new OnItemClickListener() {
	        @Override
	        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
	                long arg3) {
	        	
	        	InventoryItem item = new InventoryItem("pumpkin", true);
	        	
	            popupDialog(item);
	        }
	    });

    }
	
	void popupDialog(InventoryItem item) {
		
		PopupWindow pw;
		/*
		LayoutInflater inflater = (LayoutInflater) InventoryActivity.this
        				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		//Inflate the view from a predefined XML layout
		View layout = inflater.inflate(R.layout.quantity_input_popup,
		        (ViewGroup) findViewById(R.id.popup_element));
		// create a 300px width and 470px height PopupWindow
		pw = new PopupWindow(layout, 300, 470, true);
		// display the popup in the center
		pw.showAtLocation(layout, Gravity.CENTER, 0, 0);
		
		TextView mResultText = (TextView) layout.findViewById(R.id.scale_current_weight);
		Button cancelButton = (Button) layout.findViewById(R.id.end_data_send_button);
		cancelButton.setOnClickListener(new OnClickListener() {
		    public void onClick(View v) {
		        pw.dismiss();
		    }
		});
		
		*/
		if (item.isMeasuredByWeight) {
			// show scale interface
		} else {
			// show quantity interface
		}
	}
    
	public class GridAdapter extends BaseAdapter {
		private static final String TAG = "customAdapter";
		
		public GridAdapter() {
			
		}
	
	      @Override
	      public View getView(int position, View convertView, ViewGroup parent)  {
	         View MyView = convertView;
	         
	         if ( convertView == null ) {
	                                /*we define the view that will display on the grid*/
	            
	            //Inflate the layout
	            LayoutInflater li = getLayoutInflater();
	            MyView = li.inflate(R.layout.grid_item, null);
	            
	            // Add The Text!!!
	            TextView tv = (TextView)MyView.findViewById(R.id.grid_item_text);
	            tv.setText(getItem(position).name);
	            
	            // Add The Image!!!           
	            ImageView iv = (ImageView)MyView.findViewById(R.id.grid_item_image);
	            iv.setImageDrawable(getItem(position).getImage());
	         }
	         
	         return MyView;
	      }

	
	    public final int getCount() {
	        return 9;
	    }
	
	    public final InventoryItem getItem(int position) {
	        return mItems.get(position);
	    }
	
	    public final long getItemId(int position) {
	        return position;
	    }
	}
	
    /**
     * Fetch and parse the inventory into an ArrayList.
	 * 
     * @return parsed feed
     * @throws any human-readable Exception message
     */
    public ArrayList<InventoryItem> fetchInventory() throws FarmTabException {
        Log.d(TAG, "fetchFeed()");
        
        if (!hasInternetConnectivity())
                throw new FarmTabException(getString(R.string.cant_connect));

        // even if we fail, at least return an empty list
        ArrayList<InventoryItem> data = new ArrayList<InventoryItem>();
        

        
        
        
        /* TODO: Nur for test */
        InventoryItem i = new InventoryItem("Pumpkin", true);
        i.setURL("http://images.wikia.com/mariokart/images/4/45/Mkdd_giant_banana.jpg");
        i.unit = "lbs";
        data.add(i);
        
        
        
        
        
        /*
        HttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(INVENTORY_API_URL);;
        
        
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("farmid", mPreferences.getString(Preferences.PREFERENCE_FARMID, "")));
        pairs.add(new BasicNameValuePair("keycode", mPreferences.getString(Preferences.PREFERENCE_PRIVATEKEY, "")));
                
        
        try {
                httpPost.setEntity(new UrlEncodedFormEntity(pairs));
                
                Log.d(TAG, "executing response");
                HttpResponse response = client.execute(httpPost);

                int status = response.getStatusLine().getStatusCode();
                Log.d(TAG, "response status: " + status);
                
                if (status == HttpStatus.SC_OK ) {
                    data = parseJSON(response.getEntity().getContent());
                } else { // encompases 503 (server down temporarily), 404, etc. we don't care, since we can't get data 
                    Log.w(TAG, "Server down: " + status + " status code.");
                    throw new FarmTabException(getString(R.string.server_503));
                }
                
                if (data.size() == 0) 
                    throw new FarmTabException(getString(R.string.cant_connect));
                
        } catch (FarmTabException e) {
        	// we handle all of these, so we want the message passed along to the user
            throw e;
        } catch (ClientProtocolException e) {
            Log.e(TAG, "Protocol error: " + e.getCause());                  
            throw new FarmTabException(getString(R.string.cant_connect));
        } catch (Exception e) {
            // encompasses IOException, ParserConfigurationException, SAXException
            e.printStackTrace();
        } finally {
                // de-allocate when we're done with it
                client.getConnectionManager().shutdown();
        }
           */     
        return data;
    }
    
    public static boolean hasInternetConnectivity() {
        Context context = FarmTab.context();         
        /* NOT WORKING. Comment out for now.
         NetworkInfo info = (NetworkInfo) ((ConnectivityManager) context
        
                .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();

        if (info == null || !info.isConnected()) {
            return false;
        }
        if (info.isRoaming()) {
            // here is the roaming option you can change it if you want to
            // disable internet while roaming, just return false
            return false;
        }
        */
        return true;
    }
    
    public static ArrayList<InventoryItem> parseJSON(InputStream is) throws IOException, JSONException {
    	ArrayList<InventoryItem> data = new ArrayList<InventoryItem>();
    	
        BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
        StringBuilder sb = new StringBuilder();
        String line = null;

        while ((line = reader.readLine()) != null) {
            sb.append(line);
	    }
        
	    is.close();
	
	    String json = sb.toString();
	    JSONObject object = (JSONObject) new JSONTokener(json).nextValue();
	    
	    JSONArray items = object.getJSONArray("items");
	    Log.d(TAG, "Grabbed JSON object. Length: " + items.length());
	    
	    for (int i=0; i < items.length(); i++) {
	    	Log.d(TAG, "Adding item: ");
	    	JSONObject item = items.getJSONObject(i);
	    	
	    	InventoryItem ii = new InventoryItem(item.getString("name"), item.getBoolean("isMeasuredByWeight"));
	    	ii.setURL(item.getString("imgurl"));
	    	
	    	data.add(ii);
	    }

    	return data;
    }

}
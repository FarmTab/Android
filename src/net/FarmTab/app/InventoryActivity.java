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
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;

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
	    Preferences.storePrefs(mPreferences.edit(), "1", "123abc", true);
	    
	    
	    mItems = fetchInventory();
	    
	    GridView grid = (GridView) findViewById(R.id.itemsGrid);
	    grid.setAdapter(new GridAdapter());
	
	    grid.setOnItemClickListener(new OnItemClickListener() {
	        @Override
	        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
	                long arg3) {
	            //do some stuff here on click
	        }
	    });

    }
    
	public class GridAdapter extends BaseAdapter {
		private static final String TAG = "customAdapter";
	
	    public View getView(int position, View convertView, ViewGroup parent) {
		//create a basic imageview here or inflate a complex layout with
		//getLayoutInflator().inflate(R.layout...)
	        View item = new View(InventoryActivity.this);
	
	        //item.setImageResource(mItems.get(position).getImage());
	        final int w = (int) (36 * getResources().getDisplayMetrics().density + 0.5f);
	        item.setLayoutParams(new GridView.LayoutParams(w * 2, w * 2));
	        return item;
	    }
	
	    public final int getCount() {
	        return mItems.size();
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
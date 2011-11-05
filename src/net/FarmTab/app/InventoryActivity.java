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

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
	
	private ArrayList<InventoryItem> mItems;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); 
	    setContentView(R.layout.icons_grid);
	    
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
	
	public class InventoryItem extends View {
		
		private URL imgurl;
	
		public InventoryItem(Context context) {
			super(context);
		}
		
		
		/*
		 * Fetch image from the internet and return it
		 */
		public Drawable getImage() {
		    Bitmap x = null;
	
		    try {
			    HttpURLConnection connection = (HttpURLConnection)imgurl.openConnection();
			    connection.setRequestProperty("User-agent","Mozilla/5.0 (Linux; U; Android 0.5; en-us)");
				connection.connect();
			    InputStream input = connection.getInputStream();
			    x = BitmapFactory.decodeStream(input);
			} catch (IOException e) {
				Log.e(TAG, "Error fetching drawable for url " + imgurl + ", ");
				e.printStackTrace();
			}
	
			if (x != null)
				return new BitmapDrawable(x);
			else
				return FarmTab.resources().getDrawable(R.drawable.item_missing_picture);
		}
	}
}
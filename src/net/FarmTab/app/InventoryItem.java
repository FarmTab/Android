package net.FarmTab.app;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;

public class InventoryItem {
	private static String TAG = "InventoryItem";
		
	public String name;
	public String description;
	private URL imgurl;
	public String unit; // lbs, bushel, [empty, i.e. bare]
	public boolean isMeasuredByWeight; // as opposed to quantity

	public InventoryItem(String name, boolean isMeasuredByWeight) {
		this.name = name;
		this.isMeasuredByWeight = isMeasuredByWeight;
	}
	
    /**
     * wrapper for new URL constructor to avoid too many try-catch blocks
     */
    public void setURL(String loc) {
        try {
                imgurl = new URL(loc);
        } catch (MalformedURLException e) {
                Log.e(TAG, "Bad URL: " + loc + "\n" + e.getMessage());
        }
        throw new FarmTabException();
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
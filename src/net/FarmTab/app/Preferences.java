package net.FarmTab.app;

import android.content.SharedPreferences.Editor;
import android.util.Log;

public class Preferences {

    private static final String TAG = "Preferences";
    
    public static final String PREFERENCE_FETCH_COUNT = "20";
    
	static final String PREFERENCE_FARMID = "userFarmID";
	static final String PREFERENCE_PRIVATEKEY = "userKeycode";
	static final String PREFERENCE_REMEMBER = "keepMeLoggedIn";
	
	protected static void storePrefs(Editor editor, final String farmID,
	            final String privateKey, final boolean rememberMe) {
	    editor.putString(Preferences.PREFERENCE_FARMID, farmID);
	    editor.putString(Preferences.PREFERENCE_PRIVATEKEY, privateKey);
	    editor.putBoolean(Preferences.PREFERENCE_REMEMBER, rememberMe);
	    editor.commit();
	}
	
	protected static boolean logoutUser(Editor editor) {
	    Log.d(TAG, "Trying to log out.");
	    return editor.clear().commit();
	}

}

package ch.almana.android.sessionmodels.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.preference.PreferenceManager;
import ch.almana.android.sessionmodels.log.Logger;

/**
 * A front end for android preferences
 * 
 * @author vogtp
 * 
 */
public class SettingsSessionModels {

	private static final String HIDE_NOTCURRENT_MODELS = "HideNotcurrentModels";
	private static SettingsSessionModels instance = null;
	private final Context ctx;

	/**
	 * Get the singleton instance
	 * 
	 * @param ctx
	 *            as from <code>getContext()</code>
	 * @return the singleton instance of {@link SettingsSessionModels}
	 */
	public static SettingsSessionModels getInstance(Context ctx) {
		if (instance == null) {
			instance = new SettingsSessionModels(ctx);
		}
		return instance;
	}

	/**
	 * Do not use if you can acess a {@link Context}<br>
	 * Use {@link getInstance(Context ctx)} instead
	 * 
	 * @return
	 */
	public static SettingsSessionModels getInstance() {
		return instance;
	}

	// hide constructor use getinstance
	private SettingsSessionModels(Context context) {
		super();
		this.ctx = context.getApplicationContext();
	}

	protected SharedPreferences getPreferences() {
		return PreferenceManager.getDefaultSharedPreferences(ctx);
	}

	//	private SharedPreferences getLocalPreferences() {
	//		return context.getSharedPreferences(PREF_STORE_LOCAL, 0);
	//	}

	/**
	 * Get the version of the App
	 * 
	 * @return <code>versionName</code> from {@link PackageInfo}
	 */
	public String getVersionName() {
		try {
			PackageInfo pInfo = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0);
			return pInfo.versionName;
		} catch (NameNotFoundException e) {
			Logger.i("Cannot get cpu tuner version", e);
		}
		return "";
	}

	private String getStringPreference(int prefKey, String defValue) {
		return getPreferences().getString(ctx.getString(prefKey), defValue);
	}

	private boolean getBooleanPreference(int prefKey, boolean defValue) {
		return getPreferences().getBoolean(ctx.getString(prefKey), defValue);
	}

	/**
	 * Check if the holo theme is available
	 * 
	 * @return if {@link Build}<code>.VERSION.SDK_INT</code> is bigger or equal
	 *         as {@link Build}<code>.VERSION_CODES.HONEYCOMB</code>
	 */
	public boolean hasHoloTheme() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
	}

	public boolean isHideNotcurrentModels() {
		return getPreferences().getBoolean(HIDE_NOTCURRENT_MODELS, false);
	}

	public void setHideNotcurrentModels(boolean hide) {
		Editor editor = getPreferences().edit();
		editor.putBoolean(HIDE_NOTCURRENT_MODELS, hide);
		editor.commit();
	}

}

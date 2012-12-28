package ch.almana.android.sessionsmodels.model;

import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;

import android.support.v4.app.Fragment;
import ch.almana.android.sessionsmodels.view.ModelListFragment;
import ch.almana.android.sessionsmodels.view.models.ModelDetailFragment;
import ch.almana.android.sessionsmodels.view.models.SessionsFragment;

public  class BaseModel {
	private static final String DIRECTORY = "directory";
	private static final String IMAGE_PATH = "imagePath";
	private static final String NAME = "name";
	public String name;
	public File image;
	public File dir;

	public BaseModel(String name, File dir, File image) {
		this.name = name;
		this.dir = dir;
		this.image = image;
	}

	public BaseModel(JSONObject json) throws JSONException {
		readJson(json);
	}


	@Override
	public String toString() {
		return name;
	}

	public static Fragment getInstanceById(int id) {
		if (ModelListFragment.listItems.get(id) instanceof ModelModel) {
			return new ModelDetailFragment();
		} else if (ModelListFragment.listItems.get(id) instanceof SessionModel) {
			return new SessionsFragment();
		}
		return null;
	}

	public static Class getClassById(int id) {
		if (ModelListFragment.listItems.get(id) instanceof ModelModel) {
			return ModelDetailFragment.class;
		} else if (ModelListFragment.listItems.get(id) instanceof SessionModel) {
			return SessionsFragment.class;
		}
		return null;
	}

	public void readJson(JSONObject json) throws JSONException {
		name = json.getString(NAME);
		image = new File(json.optString(IMAGE_PATH));
		dir = new File(json.getString(DIRECTORY));
	}

	public JSONObject getJson() throws JSONException {
		JSONObject json = new JSONObject();
		json.put(NAME, name);
		json.put(IMAGE_PATH, image.getAbsolutePath());
		json.put(DIRECTORY, dir.getAbsolutePath());
		return json;

	}

}
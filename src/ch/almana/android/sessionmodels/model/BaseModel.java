package ch.almana.android.sessionmodels.model;

import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.support.v4.app.Fragment;
import ch.almana.android.sessionmodels.access.DirectoryAccess;
import ch.almana.android.sessionmodels.view.ModelListFragment;
import ch.almana.android.sessionmodels.view.gallery.GalleryOverviewFragment;
import ch.almana.android.sessionmodels.view.models.ModelDetailFragment;

public class BaseModel implements Comparable<BaseModel> {
	private static final String DIRECTORY = "directory";
	private static final String IMAGE_PATH = "imagePath";
	private static final String NAME = "name";
	private String name;
	private File image;
	private File dir;

	public BaseModel(String name, File dir, File image) {
		this.setName(name);
		this.setDir(dir);
		this.setImage(image);
	}

	public BaseModel(JSONObject json) throws JSONException {
		readJson(json);
	}


	@Override
	public String toString() {
		return getName();
	}

	public static Fragment getInstanceById(Context ctx, int id) {
		if (ModelListFragment.getListItems(ctx).get(id) instanceof ModelModel) {
			return new ModelDetailFragment();
		} else if (ModelListFragment.getListItems(ctx).get(id) instanceof SessionModel) {
			return new GalleryOverviewFragment();
		} else if (ModelListFragment.getListItems(ctx).get(id) instanceof PortfolioModel) {
			return new GalleryOverviewFragment();
		}
		return null;
	}

	public static Class getClassById(Context ctx, int id) {
		if (ModelListFragment.getListItems(ctx).get(id) instanceof ModelModel) {
			return ModelDetailFragment.class;
		} else if (ModelListFragment.getListItems(ctx).get(id) instanceof SessionModel) {
			return GalleryOverviewFragment.class;
		}
		return null;
	}

	public void readJson(JSONObject json) throws JSONException {
		name = json.optString(NAME);
		image = new File(json.optString(IMAGE_PATH));
		dir = new File(json.getString(DIRECTORY));
	}

	public JSONObject getJson() throws JSONException {
		JSONObject json = new JSONObject();
		json.put(NAME, name);
		if (image != null) {
			json.put(IMAGE_PATH, image.getAbsolutePath());
		}
		json.put(DIRECTORY, dir.getAbsolutePath());
		return json;

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public File getDefaultImage(boolean use) {
		File d = new File(dir, "model.png");
		if (use) {
			image = d;
		}
		return d;
	}
	public File getImage() {
		return image;
	}

	public void setImage(File image) {
		this.image = image;
	}

	public File getDir() {
		return dir;
	}

	public void setDir(File dir) {
		this.dir = dir;
	}

	public File getNoMediaFile() {
		return DirectoryAccess.getNoMediaFile(dir);
	}

	@Override
	public int compareTo(BaseModel another) {
		if (another == null) {
			return -1;
		}
		return name.compareTo(another.getName());
	}
}
package ch.almana.android.sessionsmodels.model;

import java.io.File;

import android.support.v4.app.Fragment;
import ch.almana.android.sessionsmodels.view.ModelListFragment;
import ch.almana.android.sessionsmodels.view.models.ModelDetailFragment;
import ch.almana.android.sessionsmodels.view.models.SessionsFragment;

public  class BaseModel {
	public String name;
	public File image;

	public BaseModel(String name, File image) {
		this.name = name;
		this.image = image;
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
}
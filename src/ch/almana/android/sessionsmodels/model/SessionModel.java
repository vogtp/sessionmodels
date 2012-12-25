package ch.almana.android.sessionsmodels.model;

import java.io.File;

public class SessionModel extends BaseModel {

	public File dir;

	public SessionModel(String name, File dir, File image) {
		super(name, image);
		this.dir = dir;
	}


}

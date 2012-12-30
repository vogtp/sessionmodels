package ch.almana.android.sessionsmodels.model;

import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;

public class SessionModel extends BaseModel {


	public SessionModel(String name, File dir, File image) {
		super(name, dir, image);
	}

	public SessionModel(JSONObject json) throws JSONException {
		super(json);
	}


}

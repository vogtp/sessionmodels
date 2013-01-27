package ch.almana.android.sessionmodels.model;

import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;

public class PortfolioModel extends SessionModel {

	public PortfolioModel(String name, File dir) {
		super(name, dir, null);
	}

	public PortfolioModel(JSONObject json) throws JSONException {
		super(json);
	}

}

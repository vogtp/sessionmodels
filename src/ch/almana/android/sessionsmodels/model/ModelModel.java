package ch.almana.android.sessionsmodels.model;

import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;

public class ModelModel extends BaseModel {

	private static final String ANSWERS = "answers";
	private AnswersList answers = AnswersList.getDefaultQuestions();

	public ModelModel(String name, File dir, File image) {
		super(name, dir, image);
	}

	public ModelModel(JSONObject json) throws JSONException {
		super(json);
		readJson(json);
	}

	public AnswersList getAnswers() {
		return answers;
	}

	@Override
	public void readJson(JSONObject json) throws JSONException {
		super.readJson(json);
		if (answers == null) {
			answers = AnswersList.getDefaultQuestions();
		}
		answers.readJson(json.getJSONArray(ANSWERS));
	}

	@Override
	public JSONObject getJson() throws JSONException {
		JSONObject json = super.getJson();
		json.put(ANSWERS, answers.getJson());
		return json;
	}
}
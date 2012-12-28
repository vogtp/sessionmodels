package ch.almana.android.sessionsmodels.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import ch.almana.android.sessionsmodels.log.Logger;

public class AnswersList extends ArrayList<AnswersModel> {

	private static final long serialVersionUID = 7212459667413242331L;

	public static AnswersList getDefaultQuestions() {
		AnswersList q = new AnswersList();
		q.add(new AnswersModel("Portrait", "Das Gesicht des Modells ist erkennbar."));
		q.add(new AnswersModel("Erotisch", "Sinnliche Fotos die mehr andeuten."));
		q.add(new AnswersModel("Teilakt"));
		q.add(new AnswersModel("Akt"));
		return q;
	}

	public void readJson(JSONArray jsonArray) throws JSONException {
		for (int i = 0; i < jsonArray.length(); i++) {
			add(new AnswersModel(jsonArray.getJSONObject(i)));
		}
		Logger.i("Loaded answers " + this);
	}

	public JSONArray getJson() throws JSONException {
		JSONArray jAry = new JSONArray();
		for (AnswersModel a : this) {
			jAry.put(a.getJson());
		}
		return jAry;
	}

	@Override
	public String toString() {
		String s = "";
		for (AnswersModel a : this) {
			s = s + a.getQuestion() + ": " + a.getAnswer() + "\n";
		}
		return s;
	}

}

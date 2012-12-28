package ch.almana.android.sessionsmodels.model;

import java.util.LinkedHashMap;

import org.json.JSONArray;
import org.json.JSONException;

import ch.almana.android.sessionsmodels.log.Logger;

public class AnswersList extends LinkedHashMap<String, AnswersModel> {

	private static final long serialVersionUID = 7212459667413242331L;

	public static AnswersList getDefaultQuestions() {
		AnswersList q = new AnswersList();
		q.put("Studio", new AnswersModel("Studio"));
		q.put("Outdoor", new AnswersModel("Outdoor"));
		q.put("Portrait", new AnswersModel("Portrait", "Das Gesicht des Modells ist erkennbar."));
		q.put("Erotisch", new AnswersModel("Erotisch", "Sinnliche Fotos die mehr andeuten."));
		q.put("Teilakt", new AnswersModel("Teilakt"));
		q.put("Akt", new AnswersModel("Akt"));
		return q;
	}

	public void readJson(JSONArray jsonArray) throws JSONException {
		for (int i = 0; i < jsonArray.length(); i++) {
			AnswersModel answersModel = new AnswersModel(jsonArray.getJSONObject(i));
			put(answersModel.getQuestion(), answersModel);
		}
		Logger.i("Loaded answers " + this);
	}

	public JSONArray getJson() throws JSONException {
		JSONArray jAry = new JSONArray();
		for (AnswersModel a : this.values()) {
			jAry.put(a.getJson());
		}
		return jAry;
	}

	@Override
	public String toString() {
		String s = "";
		for (AnswersModel a : values()) {
			s = s + a.getQuestion() + ": " + a.getAnswer() + "\n";
		}
		return s;
	}

	public AnswersModel[] getAnswers() {
		return values().toArray(new AnswersModel[values().size()]);
	}

}

package ch.almana.android.sessionmodels.model;

import org.json.JSONException;
import org.json.JSONObject;

public class AnswersModel {

	private static final String ANSWER = "answer";
	private static final String ANSWER_LONG = "answerLong";
	private static final String QUESTION_LONG = "questionLong";
	private static final String QUESTION = "question";

	public enum Answers {
		not_given, dont_know, no, rather_not, perhaps, yes, please,

	}

	private final String question;

	private final String questionLong;

	private Answers answer = Answers.not_given;
	private String answerLong;

	public AnswersModel(String question, String questionLong) {
		super();
		this.question = question;
		this.questionLong = questionLong;
	}

	public AnswersModel(String question) {
		this(question, "");
	}

	public AnswersModel(JSONObject answer) throws JSONException {
		super();
		this.question = answer.getString(QUESTION);
		this.questionLong = answer.optString(QUESTION_LONG);
		this.answer = Answers.valueOf(answer.optString(ANSWER));
		this.answerLong = answer.optString(ANSWER_LONG);
	}

	public JSONObject getJson() throws JSONException {
		JSONObject json = new JSONObject();
		json.put(QUESTION, question);
		json.put(QUESTION_LONG, questionLong);
		json.put(ANSWER, answer.toString());
		json.put(ANSWER_LONG, answerLong);
		return json;
	}

	public Answers getAnswer() {
		return answer;
	}

	public void setAnswer(Answers answer) {
		this.answer = answer;
	}

	public String getAnswerLong() {
		return answerLong;
	}

	public void setAnswerLong(String answerLong) {
		this.answerLong = answerLong;
	}

	@Override
	public String toString() {
		return question + ": " + answer.name();
	}

	public String getQuestion() {
		return question;
	}

	public String getQuestionLong() {
		return questionLong;
	}

}

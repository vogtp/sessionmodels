package ch.almana.android.sessionsmodels.model;

import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;

import ch.almana.android.util.StringUtils;

public class ModelModel extends BaseModel {
	public static final long NO_BIRTHDAY = Long.MIN_VALUE;

	private static final String BIRTHDAY = "birthday";
	private static final String NICKNAME = "nickname";
	private static final String ANSWERS = "answers";
	private AnswersList answers = AnswersList.getDefaultQuestions();
	private String nick;
	private long birthday = NO_BIRTHDAY;

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
		nick = json.optString(NICKNAME);
		birthday = json.optLong(BIRTHDAY);
	}

	@Override
	public JSONObject getJson() throws JSONException {
		JSONObject json = super.getJson();
		json.put(ANSWERS, answers.getJson());
		json.put(NICKNAME, nick);
		json.put(BIRTHDAY, birthday);
		return json;
	}

	public String getNick() {
		if (StringUtils.isEmpty(nick)) {
			return getName();
		}
		return nick;
	}

	public void setNick(String nick) {
		if (nick != null && nick.equals(getName())) {
			return;
		}
		this.nick = nick;
	}

	public long getBirthday() {
		return birthday;
	}

	public void setBirthday(long birthday) {
		this.birthday = birthday;
	}

	public boolean hasBirthday() {
		return birthday > ModelModel.NO_BIRTHDAY;
	}
}
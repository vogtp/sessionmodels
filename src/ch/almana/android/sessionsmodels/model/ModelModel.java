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
	private static final String TELEPHONE = "telephone";
	private static final String EMAIL = "email";
	private static final String DOES = "does";
	private static final String DONTS = "donts";
	private AnswersList answers = AnswersList.getDefaultQuestions();
	private String nick;
	private String telephone;
	private String email;
	private String does;
	private String donts;
	private long birthday = NO_BIRTHDAY;

	public ModelModel(String name, File dir) {
		this(name, dir, null);
	}
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
		email = json.optString(EMAIL);
		telephone = json.optString(TELEPHONE);
		does = json.optString(DOES);
		donts = json.optString(DONTS);
	}

	@Override
	public JSONObject getJson() throws JSONException {
		JSONObject json = super.getJson();
		json.put(ANSWERS, answers.getJson());
		json.put(NICKNAME, nick);
		json.put(BIRTHDAY, birthday);
		json.put(EMAIL, email);
		json.put(TELEPHONE, telephone);
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

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	public String getDoes() {
		return does;
	}
	public void setDoes(String does) {
		this.does = does;
	}
	public String getDonts() {
		return donts;
	}
	public void setDonts(String donts) {
		this.donts = donts;
	}
}
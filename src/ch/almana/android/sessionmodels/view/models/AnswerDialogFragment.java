package ch.almana.android.sessionmodels.view.models;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import ch.almana.android.sessionmodels.R;
import ch.almana.android.sessionmodels.model.AnswersModel;
import ch.almana.android.sessionmodels.model.AnswersModel.Answers;

public class AnswerDialogFragment extends DialogFragment {

	public static AnswerDialogFragment newInstance(AnswersModel answersModel, Callback callback) {
		return new AnswerDialogFragment(answersModel, callback);
	}

	public static interface Callback {
		public void onAnswerSelected(AnswersModel answersModel);
	}

	private TextView tvQuestion;
	private AnswersModel question;
	private TextView tvQuestionLong;
	private EditText etAnswerLong;
	private Spinner spAnswer;
	private Callback callback;

	protected AnswerDialogFragment() {
		super();
	}

	public AnswerDialogFragment(AnswersModel question, Callback callback) {
		this();
		this.question = question;
		this.callback = callback;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.answer_fragment, container, false);
		tvQuestion = (TextView) v.findViewById(R.id.tvQuestion);
		tvQuestionLong = (TextView) v.findViewById(R.id.tvQuestionLong);
		spAnswer = (Spinner) v.findViewById(R.id.spAnswer);
		spAnswer.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Answers answer = Answers.values()[position];
				question.setAnswer(answer);
				if (callback != null) {
					callback.onAnswerSelected(question);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});
		etAnswerLong = (EditText) v.findViewById(R.id.etAnswerLong);

		return v;
	}

	@Override
	public void onActivityCreated(Bundle arg0) {
		super.onActivityCreated(arg0);
		Dialog dialog = getDialog();
		if (dialog != null) {
			dialog.setTitle(R.string.model_question_title);
		}
		spAnswer.setAdapter(new ArrayAdapter<Answers>(getActivity(), android.R.layout.simple_spinner_dropdown_item, Answers.values()));
	}

	@Override
	public void onResume() {
		super.onResume();
		SpinnerAdapter adapter = spAnswer.getAdapter();
		for (int i = 0; i < adapter.getCount(); i++) {
			if (adapter.getItem(i).equals(question.getAnswer())) {
				spAnswer.setSelection(i);
			}
		}
		tvQuestion.setText(question.getQuestion());
		tvQuestionLong.setText(question.getQuestionLong());

		etAnswerLong.setText(question.getAnswerLong());
	}

}

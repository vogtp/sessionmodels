package ch.almana.android.sessionsmodels.view.models;

import java.io.FileNotFoundException;
import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import ch.almana.android.sessionsmodels.R;
import ch.almana.android.sessionsmodels.access.ModelAcess;
import ch.almana.android.sessionsmodels.helper.ImageHelper;
import ch.almana.android.sessionsmodels.log.Logger;
import ch.almana.android.sessionsmodels.model.AnswersModel;
import ch.almana.android.sessionsmodels.model.ModelModel;
import ch.almana.android.sessionsmodels.view.ModelDetailActivity;
import ch.almana.android.sessionsmodels.view.ModelListActivity;
import ch.almana.android.sessionsmodels.view.ModelListFragment;
import ch.almana.android.sessionsmodels.view.models.AnswerDialogFragment.Callback;

/**
 * A fragment representing a single Model detail screen. This fragment is either
 * contained in a {@link ModelListActivity} in two-pane mode (on tablets) or a
 * {@link ModelDetailActivity} on handsets.
 */
public class ModelDetailFragment extends Fragment {

	public static final String EXTRA_MODEL_ID = "item_id";

	private ModelModel model;

	private ImageView ivModelImage;

	private ListView lvAnswers;

	private EditText etModelNick;

	private EditText etModelName;

	private Button buAge;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public ModelDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments().containsKey(EXTRA_MODEL_ID)) {
			model = (ModelModel) ModelListFragment.getListItems(getActivity()).get(getArguments().getInt(EXTRA_MODEL_ID));
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_model_detail, container, false);
		etModelNick = ((EditText) rootView.findViewById(R.id.etModelNick));
		etModelName = ((EditText) rootView.findViewById(R.id.etModelName));
		ivModelImage = ((ImageView) rootView.findViewById(R.id.ivModelImage));
		lvAnswers = ((ListView) rootView.findViewById(R.id.lvAnswers));
		buAge = ((Button) rootView.findViewById(R.id.buAge));
		lvAnswers.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				DialogFragment newFragment = AnswerDialogFragment.newInstance(model.getAnswers().get(position), new Callback() {

					@Override
					public void onAnswerSelected(AnswersModel answersModel) {
						try {
							ModelAcess.saveModelInfo(model);
							lvAnswers.setAdapter(new AnswersAdapter(getActivity(), model.getAnswers()));
						} catch (Exception e) {
							Logger.e("Cannot save model", e);
						}
					}
				});
				newFragment.show(getFragmentManager(), "dialog");
			}
		});
		buAge.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Calendar birthday = Calendar.getInstance();
				if (model.hasBirthday()) {
					birthday.setTimeInMillis(model.getBirthday());
				} else {
					birthday.add(Calendar.YEAR, -25);
				}
				int dayOfMonth = birthday.get(Calendar.DAY_OF_MONTH);
				int monthOfYear = birthday.get(Calendar.MONTH);
				int year = birthday.get(Calendar.YEAR);
				OnDateSetListener callBack = new OnDateSetListener() {

					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
						Calendar birthday = Calendar.getInstance();
						birthday.set(Calendar.DAY_OF_MONTH, dayOfMonth);
						birthday.set(Calendar.MONTH, monthOfYear);
						birthday.set(Calendar.YEAR, year);
						model.setBirthday(birthday.getTimeInMillis());
						buAge.setText(getAge());
					}
				};
				DatePickerDialog dpd = new DatePickerDialog(getActivity(), callBack, year, monthOfYear, dayOfMonth);
				dpd.show();
			}
		});
		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();
		if (model != null) {
			etModelNick.setText(model.getNick());
			etModelName.setText(model.getName());
			buAge.setText(getAge());
			try {
				ivModelImage.setImageBitmap(ImageHelper.scaleImage(getActivity(), model.getImage(), 250));
			} catch (FileNotFoundException e) {
				Logger.w("Image not found", e);
			}
			lvAnswers.setAdapter(new AnswersAdapter(getActivity(), model.getAnswers()));
		}
	}

	private CharSequence getAge() {
		String age;
		if (model.hasBirthday()) {
			Calendar now = Calendar.getInstance();
			Calendar birthday = Calendar.getInstance();
			birthday.setTimeInMillis(model.getBirthday());
			long years = now.get(Calendar.YEAR) - birthday.get(Calendar.YEAR);
			now.set(Calendar.YEAR, birthday.get(Calendar.YEAR));
			if (now.before(birthday)) {
				years--;
			}
			age = Long.toString(years);
		} else {
			age = "?";
		}
		return "Age: " + age;
	}

	@Override
	public void onPause() {
		if (model != null) {
			model.setNick(etModelNick.getText().toString());
			model.setName(etModelName.getText().toString());
			try {
				ModelAcess.saveModelInfo(model);
			} catch (Exception e) {
				Logger.e("Cannot save model", e);
			}
		}
		super.onPause();
	}

}

package ch.almana.android.sessionsmodels.view.models;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Collection;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

	protected static final int CAPTURE_IMAGE = 1;

	private ModelModel model;

	private ImageView ivModelImage;

	private ListView lvAnswers;

	private EditText etModelNick;

	private EditText etModelName;

	private Button buAge;

	private EditText etEmail;

	private EditText etTel;

	private AnswersModel[] answers;

	private EditText etDoes;

	private EditText etDonts;

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
		etTel = ((EditText) rootView.findViewById(R.id.etTel));
		etEmail = ((EditText) rootView.findViewById(R.id.etEmail));
		etDoes = ((EditText) rootView.findViewById(R.id.etDoes));
		etDonts = ((EditText) rootView.findViewById(R.id.etDonts));
		ivModelImage = ((ImageView) rootView.findViewById(R.id.ivModelImage));
		lvAnswers = ((ListView) rootView.findViewById(R.id.lvAnswers));
		buAge = ((Button) rootView.findViewById(R.id.buAge));
		lvAnswers.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				DialogFragment newFragment = AnswerDialogFragment.newInstance(getModelAnswers()[position], new Callback() {

					@Override
					public void onAnswerSelected(AnswersModel answersModel) {
						try {
							ModelAcess.save(model);
							updateAnswersList();
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
		ivModelImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(intent, CAPTURE_IMAGE);
			}
		});
		return rootView;
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
	public void onResume() {
		super.onResume();
		if (model != null) {
			etModelNick.setText(model.getNick());
			etModelName.setText(model.getName());
			etEmail.setText(model.getEmail());
			etTel.setText(model.getTelephone());
			etDoes.setText(model.getDoes());
			etDonts.setText(model.getDonts());
			buAge.setText(getAge());
			Bitmap bm = null;
			if (model.getImage() != null) {
				try {
					bm = ImageHelper.scaleImage(getActivity(), model.getImage(), 250);
				} catch (FileNotFoundException e) {
					Logger.e("Cannot load image", e);
				}
				if (bm != null && bm.getWidth() < 70) {
					bm = null;
				}
			}
			if (bm == null) {
				ivModelImage.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.modelnoimage250));
			} else {
				ivModelImage.setImageBitmap(bm);
			}
			updateAnswersList();
		}
	}

	private void updateAnswersList() {
		lvAnswers.setAdapter(new AnswersAdapter(getActivity(), getModelAnswers()));
	}

	private AnswersModel[] getModelAnswers() {
		if (answers == null) {
			Collection<AnswersModel> a = model.getAnswers().values();
			answers = a.toArray(new AnswersModel[a.size()]);
		}
		return answers;
	}

	@Override
	public void onPause() {
		if (model != null) {
			model.setNick(etModelNick.getText().toString());
			model.setName(etModelName.getText().toString());
			model.setEmail(etEmail.getText().toString());
			model.setTelephone(etTel.getText().toString());
			model.setTelephone(etTel.getText().toString());
			model.setDoes(etDoes.getText().toString());
			model.setDonts(etDonts.getText().toString());
			try {
				ModelAcess.save(model);
			} catch (Exception e) {
				Logger.e("Cannot save model", e);
			}
		}
		super.onPause();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Uri uriImage;
		InputStream inputStream = null;
		if ((/*requestCode == SELECT_IMAGE || */requestCode == CAPTURE_IMAGE) && resultCode == Activity.RESULT_OK) {

			Bundle extras = data.getExtras();
			Bitmap bitmap = (Bitmap) extras.get("data");
			FileOutputStream out = null;
			try {
				out = new FileOutputStream(model.getDefaultImage(true));
				bitmap.compress(CompressFormat.PNG, 90, out);
			} catch (IOException e) {
				Logger.w("Can not cache file", e);
				//				getCacheFile(f, resolution).delete();
			} finally {
				if (out != null) {
					try {
						out.close();
					} catch (IOException e) {
						// ignore
					}
				}
			}
			//			CacheHelper.deleteCachedFile(getActivity());
			//			ivModelImage.setImageBitmap(mImageBitmap);

			//			uriImage = data.getData();
			//			try {
			//				FragmentActivity activity = getActivity();
			//				ContentResolver contentResolver = activity.getContentResolver();
			//				inputStream = contentResolver.openInputStream(uriImage);
			//				Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, null);
			//				ivModelImage.setImageBitmap(bitmap);
			//			} catch (NullPointerException e) {
			//				e.printStackTrace();
			//			} catch (FileNotFoundException e) {
			//				e.printStackTrace();
			//			} catch (Exception e) {
			//				e.printStackTrace();
			//			}
			//			ivModelImage.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
			//			ivModelImage.setAdjustViewBounds(true);
		}
	}
}

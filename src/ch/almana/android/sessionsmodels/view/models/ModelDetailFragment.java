package ch.almana.android.sessionsmodels.view.models;

import java.io.FileNotFoundException;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
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

	private TextView tvModelName;

	private ImageView ivModelImage;

	private ListView lvAnswers;

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
			// Load the dummy content specified by the fragment
			// arguments. In a real-world scenario, use a Loader
			// to load content from a content provider.
			model = (ModelModel) ModelListFragment.listItems.get(getArguments().getInt(EXTRA_MODEL_ID));
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_model_detail, container, false);
		tvModelName = ((TextView) rootView.findViewById(R.id.tvModelName));
		ivModelImage = ((ImageView) rootView.findViewById(R.id.ivModelImage));
		lvAnswers = ((ListView) rootView.findViewById(R.id.lvAnswers));
		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();

		// Show the dummy content as text in a TextView.
		if (model != null) {
			tvModelName.setText(model.name);
			try {
				ivModelImage.setImageBitmap(ImageHelper.scaleImage(getActivity(), model.image, 250));
			} catch (FileNotFoundException e) {
				Logger.w("Image not found", e);
			}
			lvAnswers.setAdapter(new AnswersAdapter(getActivity(), model.getAnswers()));
			lvAnswers.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					DialogFragment newFragment = AnswerDialogFragment.newInstance(model.getAnswers().get(position), new Callback() {

						@Override
						public void onAnswerSelected(AnswersModel answersModel) {
							// TODO Auto-generated method stub
							try {
								//								model.setA = answersModel;
								ModelAcess.saveModelInfo(model);
								lvAnswers.setAdapter(new AnswersAdapter(getActivity(), model.getAnswers()));
							} catch (Exception e) {
								Logger.e("Cannot save model", e);
							}
						}
					});
					newFragment.show(getFragmentManager(), "dialog");
					//					AnswersModel a = model.getAnswers().get(position);
					////					a.setAnswer(Answers.dont_know);
					//					try {
					//						ModelAcess.saveModelInfo(model);
					//						lvAnswers.setAdapter(new AnswersAdapter(getActivity(), model.getAnswers()));
					//					} catch (Exception e) {
					//						Logger.e("Cannot save model", e);
					//					}
				}
			});
		}
	}


}

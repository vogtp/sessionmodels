package ch.almana.android.sessionsmodels.view.models;

import java.io.FileNotFoundException;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import ch.almana.android.sessionsmodels.R;
import ch.almana.android.sessionsmodels.helper.ImageHelper;
import ch.almana.android.sessionsmodels.log.Logger;
import ch.almana.android.sessionsmodels.model.BaseModel;
import ch.almana.android.sessionsmodels.view.ModelDetailActivity;
import ch.almana.android.sessionsmodels.view.ModelListActivity;
import ch.almana.android.sessionsmodels.view.ModelListFragment;

/**
 * A fragment representing a single Model detail screen. This fragment is either
 * contained in a {@link ModelListActivity} in two-pane mode (on tablets) or a
 * {@link ModelDetailActivity} on handsets.
 */
public class ModelDetailFragment extends Fragment {
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	public static final String ARG_ITEM_ID = "item_id";

	/**
	 * The dummy content this fragment is presenting.
	 */
	private BaseModel mItem;

	private TextView tvModelName;

	private ImageView ivModelImage;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public ModelDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments().containsKey(ARG_ITEM_ID)) {
			// Load the dummy content specified by the fragment
			// arguments. In a real-world scenario, use a Loader
			// to load content from a content provider.
			mItem = ModelListFragment.listItems.get(getArguments().getInt(ARG_ITEM_ID));
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_model_detail, container, false);
		tvModelName = ((TextView) rootView.findViewById(R.id.tvModelName));
		ivModelImage = ((ImageView) rootView.findViewById(R.id.ivModelImage));
		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();

		// Show the dummy content as text in a TextView.
		if (mItem != null) {
			tvModelName.setText(mItem.name);
			try {
				ivModelImage.setImageBitmap(ImageHelper.scaleImage(getActivity(), mItem.image, 250));
			} catch (FileNotFoundException e) {
				Logger.w("Image not found", e);
			}
			//			ImageView ivModelImage = (ImageView) rootView.findViewById(R.id.ivModelImage);
			//			ivModelImage.setImageURI(Uri.fromFile(mItem.image));
		}
	}


}

package ch.almana.android.sessionsmodels.view.models;

import java.io.File;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SpinnerAdapter;
import ch.almana.android.sessionsmodels.R;
import ch.almana.android.sessionsmodels.helper.ImageHelper;
import ch.almana.android.sessionsmodels.model.SessionModel;
import ch.almana.android.sessionsmodels.view.ModelDetailActivity;
import ch.almana.android.sessionsmodels.view.ModelListActivity;
import ch.almana.android.sessionsmodels.view.ModelListFragment;

/**
 * A fragment representing a single Model detail screen. This fragment is either
 * contained in a {@link ModelListActivity} in two-pane mode (on tablets) or a
 * {@link ModelDetailActivity} on handsets.
 */
public class SessionsFragment extends Fragment {

	/**
	 * The dummy content this fragment is presenting.
	 */
	private SessionModel mItem;

	private LinearLayout llTop;

	private Gallery gallery;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public SessionsFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments().containsKey(ModelDetailFragment.ARG_ITEM_ID)) {
			// Load the dummy content specified by the fragment
			// arguments. In a real-world scenario, use a Loader
			// to load content from a content provider.
			mItem = (SessionModel) ModelListFragment.listItems.get(getArguments().getInt(ModelDetailFragment.ARG_ITEM_ID));
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_sessions, container, false);
		llTop = ((LinearLayout) rootView.findViewById(R.id.llTop));
		gallery = ((Gallery) rootView.findViewById(R.id.gallery1));
		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();

		// Show the dummy content as text in a TextView.
		if (mItem != null) {
			File[] images = mItem.dir.listFiles();
			SpinnerAdapter adapter = new ImageAdapter(images);
			gallery.setAdapter(adapter);
			//			llTop.removeAllViews();
			//			for (int i = 0; i < images.length; i++) {
			//				llTop.addView(getImage(images[i]));
			//			}

		}
	}
	
	private ImageView getImage(File image) {
		ImageView iv = new ImageView(getActivity());
		Bitmap b = BitmapFactory.decodeFile(image.getAbsolutePath());
		iv.setImageBitmap(ImageHelper.scaleImage(getActivity(), b, 250));
		//			ImageView ivModelImage = (ImageView) rootView.findViewById(R.id.ivModelImage);
		//			ivModelImage.setImageURI(Uri.fromFile(mItem.image));
		return iv;
	}

}

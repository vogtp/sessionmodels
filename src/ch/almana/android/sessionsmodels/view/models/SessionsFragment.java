package ch.almana.android.sessionsmodels.view.models;

import java.io.File;
import java.io.FileNotFoundException;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SpinnerAdapter;
import ch.almana.android.sessionsmodels.R;
import ch.almana.android.sessionsmodels.helper.ImageHelper;
import ch.almana.android.sessionsmodels.log.Logger;
import ch.almana.android.sessionsmodels.model.SessionModel;
import ch.almana.android.sessionsmodels.view.ImagePagerActivity;
import ch.almana.android.sessionsmodels.view.ModelDetailActivity;
import ch.almana.android.sessionsmodels.view.ModelListActivity;
import ch.almana.android.sessionsmodels.view.ModelListFragment;

/**
 * A fragment representing a single Model detail screen. This fragment is either
 * contained in a {@link ModelListActivity} in two-pane mode (on tablets) or a
 * {@link ModelDetailActivity} on handsets.
 */
public class SessionsFragment extends Fragment implements OnItemClickListener {

	private static boolean useGallery = false;

	/**
	 * The dummy content this fragment is presenting.
	 */
	private SessionModel mItem;

	//	private LinearLayout llTop;

	@SuppressWarnings("deprecation")
	private Gallery gallery;

	private ListView listview;

	private int sessionId;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public SessionsFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments().containsKey(ModelDetailFragment.EXTRA_MODEL_ID)) {
			sessionId = getArguments().getInt(ModelDetailFragment.EXTRA_MODEL_ID);
			mItem = (SessionModel) ModelListFragment.getListItems(getActivity()).get(sessionId);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(useGallery ? R.layout.fragment_sessions_gallery : R.layout.fragment_sessions, container, false);
		if (useGallery) {
			gallery = ((Gallery) rootView.findViewById(R.id.gallery1));
		} else {
			//			llTop = ((LinearLayout) rootView.findViewById(R.id.llTop));
			listview = ((ListView) rootView.findViewById(android.R.id.list));
			listview.setOnItemClickListener(this);
		}
		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();

		// Show the dummy content as text in a TextView.
		if (mItem != null) {
			File[] images = mItem.getDir().listFiles();
			if (useGallery) {
				SpinnerAdapter adapter = new ImageAdapter(images, 250);
				gallery.setAdapter(adapter);
			} else {
				//				llTop.removeAllViews();
				//				for (int i = 0; i < images.length; i++) {
				//					llTop.addView(getImage(images[i]));
				//				}
				listview.setAdapter(new ImageAdapter(images, 250));
			}

		}
	}

	private ImageView getImage(File image) {
		ImageView iv = new ImageView(getActivity());

		try {
			iv.setImageBitmap(ImageHelper.scaleImage(getActivity(), image, 250));
		} catch (FileNotFoundException e) {
			Logger.w("Cannot load image", e);
		}
		//			ImageView ivModelImage = (ImageView) rootView.findViewById(R.id.ivModelImage);
		//			ivModelImage.setImageURI(Uri.fromFile(mItem.image));
		return iv;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

		Intent i = new Intent(getActivity(), ImagePagerActivity.class);
		i.putExtra(ImagePagerActivity.EXTRA_SESSION_ID, sessionId);
		i.putExtra(ImagePagerActivity.EXTRA_IMAGE_ID, position);
		getActivity().startActivity(i);
	}

}

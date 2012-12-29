package ch.almana.android.sessionsmodels.view.gallery;

import java.io.File;
import java.io.IOException;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;
import ch.almana.android.sessionsmodels.R;
import ch.almana.android.sessionsmodels.helper.GalleryHelper;
import ch.almana.android.sessionsmodels.log.Logger;
import ch.almana.android.sessionsmodels.model.SessionModel;
import ch.almana.android.sessionsmodels.view.ModelListFragment;
import ch.almana.android.sessionsmodels.view.adapter.ImageAdapter;
import ch.almana.android.sessionsmodels.view.models.ModelDetailFragment;

public class GalleryOverviewFragment extends Fragment implements OnItemClickListener {

	private SessionModel session;

	private GridView listview;

	private int sessionId;

	public GalleryOverviewFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		if (getArguments().containsKey(ModelDetailFragment.EXTRA_MODEL_ID)) {
			sessionId = getArguments().getInt(ModelDetailFragment.EXTRA_MODEL_ID);
			session = (SessionModel) ModelListFragment.getListItems(getActivity()).get(sessionId);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.gallery_overview_fragment, container, false);
		listview = ((GridView) rootView.findViewById(android.R.id.list));
		listview.setOnItemClickListener(this);
		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();

		if (session != null) {
			File[] images = session.getDir().listFiles();
			listview.setAdapter(new ImageAdapter(images, 250));

		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

		Intent i = new Intent(getActivity(), ImagePagerActivity.class);
		i.putExtra(ImagePagerActivity.EXTRA_SESSION_ID, sessionId);
		i.putExtra(ImagePagerActivity.EXTRA_IMAGE_ID, position);
		getActivity().startActivity(i);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.gallery_overview_options, menu);
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
		menu.findItem(R.id.itemShowInGallery).setChecked(isShowInGallery());
	}

	private boolean isShowInGallery() {
		return !session.getNoMediaFile().exists();
	};
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.itemShowInGallery:
			if (isShowInGallery()) {
				try {
					session.getNoMediaFile().createNewFile();
					Logger.i("added .nomedia file");
					Toast.makeText(getActivity(), getString(R.string.removed_from_gallery, session.getName()), Toast.LENGTH_SHORT).show();
				} catch (IOException e) {
					Logger.e("Cannot create no media file", e);
				}
			} else {
				session.getNoMediaFile().delete();
				Logger.i("removed .nomedia file");
				Toast.makeText(getActivity(), session.getName() + " added to gallery", Toast.LENGTH_SHORT).show();
			}
			GalleryHelper.rescanDirectories(getActivity(), session.getDir());
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}

	}
	
}

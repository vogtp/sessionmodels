package ch.almana.android.sessionsmodels.view.gallery;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;
import ch.almana.android.sessionsmodels.R;
import ch.almana.android.sessionsmodels.access.DirectoryAccess;
import ch.almana.android.sessionsmodels.helper.GalleryHelper;
import ch.almana.android.sessionsmodels.helper.ImageHelper;
import ch.almana.android.sessionsmodels.log.Logger;
import ch.almana.android.sessionsmodels.model.SessionModel;
import ch.almana.android.sessionsmodels.view.ModelListFragment;
import ch.almana.android.sessionsmodels.view.adapter.ImageAdapter;
import ch.almana.android.sessionsmodels.view.models.ModelDetailFragment;

public class GalleryOverviewFragment extends Fragment implements OnItemClickListener {

	private SessionModel session;

	private AbsListView gridview;

	private int sessionId;

	private File[] images;

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
		gridview = ((AbsListView) rootView.findViewById(android.R.id.list));
		gridview.setOnItemClickListener(this);
		registerForContextMenu(gridview);
		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();

		if (session != null) {
			images = session.getDir().listFiles();
			gridview.setAdapter(new ImageAdapter(images, 250));
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
				Toast.makeText(getActivity(), getString(R.string.added_from_gallery, session.getName()), Toast.LENGTH_SHORT).show();
			}
			GalleryHelper.rescanDirectories(getActivity(), session.getDir());
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		getActivity().getMenuInflater().inflate(R.menu.gallery_context, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info;
		try {
			info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		} catch (ClassCastException e) {
			Logger.e("bad menuInfo", e);
			return false;
		}

		switch (item.getItemId()) {
		case R.id.itemPortfolio:
			File image = images[(int) info.id];
			addToPortfolio(image);
			return true;

		default:
			return super.onContextItemSelected(item);
		}

	}

	private void addToPortfolio(final File image) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				File portfolioFile = new File(DirectoryAccess.getPortfolioDir(), image.getName());
				FileOutputStream out = null;
				try {
					Bitmap bitmap = ImageHelper.scaleImage(getActivity(), image, ImageHelper.getDisplayWidth(getActivity()));
					out = new FileOutputStream(portfolioFile);
					bitmap.compress(CompressFormat.PNG, 90, out);
					out.flush();
				} catch (IOException e) {
					Logger.e("Cannot save to portfolio", e);
					Toast.makeText(getActivity(), getActivity().getString(R.string.error_saving_to_portfolio), Toast.LENGTH_LONG).show();
				} finally {
					if (out != null) {
						try {
							out.close();
						} catch (IOException e) {
							// ignore
						}
					}
				}
			}
		}).start();

	}
}

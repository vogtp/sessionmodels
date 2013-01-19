package ch.almana.android.sessionsmodels.view.gallery;

import java.io.File;
import java.io.IOException;

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
import ch.almana.android.sessionsmodels.access.PortfolioAccess;
import ch.almana.android.sessionsmodels.access.SessionAcess;
import ch.almana.android.sessionsmodels.helper.GalleryHelper;
import ch.almana.android.sessionsmodels.helper.GalleryStartHelper;
import ch.almana.android.sessionsmodels.log.Logger;
import ch.almana.android.sessionsmodels.model.PortfolioModel;
import ch.almana.android.sessionsmodels.model.SessionModel;
import ch.almana.android.sessionsmodels.view.ModelListFragment;
import ch.almana.android.sessionsmodels.view.models.ModelDetailFragment;

public class GalleryOverviewFragment extends Fragment implements OnItemClickListener {
	// http://stackoverflow.com/questions/6074270/built-in-gallery-in-specific-folder/8255674#8255674

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
		reload();
	}

	private void reload() {
		if (session != null) {
			if (getActivity() != null) {
				if (session instanceof PortfolioModel) {
					getActivity().setTitle(R.string.portfolio);
				} else if (session instanceof SessionModel) {
					getActivity().setTitle(getString(R.string.fragement_title_session, session.getName()));
				}

			}
			images = session.getDir().listFiles();
			gridview.setAdapter(new GalleryOverviewImageAdapter(images, 250));
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		//		Intent i = new Intent(getActivity(), ImagePagerActivity.class);
		//		i.putExtra(ImagePagerActivity.EXTRA_SESSION_ID, sessionId);
		//		i.putExtra(ImagePagerActivity.EXTRA_IMAGE_ID, position);
		//		getActivity().startActivity(i);
		GalleryStartHelper gallery = new GalleryStartHelper(getActivity());
		gallery.openGallery(session.getDir());
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
		int portfolioTextId = R.string.menuItemAddToPortfolio;
		if (PortfolioAccess.isInPortfolio(getImageFromMenuInfo(menuInfo))) {
			portfolioTextId = R.string.menuItemRemoveFromPortfolio;
		}
		menu.findItem(R.id.itemPortfolio).setTitle(portfolioTextId);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		File image = getImageFromMenuInfo(item.getMenuInfo());
		if (image == null) {
			return false;
		}
		switch (item.getItemId()) {
		case R.id.itemPortfolio:
			if (getString(R.string.menuItemAddToPortfolio).equals(item.getTitle())) {
				PortfolioAccess.addToPortfolio(getActivity(), image);
			}else {
				PortfolioAccess.removeFromPortfolio(image);
				reload();
			}
			return true;
		case R.id.itemUseAsGalleryImage:
			session.setImage(image);
			try {
				SessionAcess.save(session);
				ModelListFragment.sendListChangedBroadcast(getActivity());
			} catch (Exception e) {
				Logger.e("Cannot save session", e);
			}
			return true;

		default:
			return super.onContextItemSelected(item);
		}
	}

	private File getImageFromMenuInfo(ContextMenuInfo menuInfo) {
		AdapterView.AdapterContextMenuInfo info;
		try {
			info = (AdapterView.AdapterContextMenuInfo) menuInfo;
			return images[(int) info.id];
		} catch (ClassCastException e) {
			Logger.e("bad menuInfo", e);
			return null;
		}

	}

}

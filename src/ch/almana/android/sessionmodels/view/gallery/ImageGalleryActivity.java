package ch.almana.android.sessionmodels.view.gallery;

import java.io.File;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Gallery;
import android.widget.SpinnerAdapter;
import ch.almana.android.sessionmodels.R;
import ch.almana.android.sessionmodels.helper.ImageHelper;
import ch.almana.android.sessionmodels.model.SessionModel;
import ch.almana.android.sessionmodels.view.ModelListFragment;
import ch.almana.android.sessionmodels.view.adapter.ImageAdapter;

public class ImageGalleryActivity extends Activity {

	public static final String EXTRA_SESSION_ID = "EXTRA_SESSION_ID";
	public static final String EXTRA_IMAGE_ID = "EXTRA_IMAGE_ID";
	private int sessionId;
	private SessionModel session;
	@SuppressWarnings("deprecation")
	private Gallery gallery;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.image_pager);

		gallery = (Gallery) findViewById(R.id.gallery);

		sessionId = getIntent().getIntExtra(EXTRA_SESSION_ID, -1);
		if (sessionId > -1) {
			session = (SessionModel) ModelListFragment.getListItems(this).get(sessionId);
		}
		File[] images = session.getDir().listFiles();

		SpinnerAdapter adapter = new ImageAdapter(this, images, ImageHelper.getDisplayWidth(this));
		gallery.setAdapter(adapter);
	}

}

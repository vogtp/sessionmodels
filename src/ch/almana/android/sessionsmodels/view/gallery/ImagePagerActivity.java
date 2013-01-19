package ch.almana.android.sessionsmodels.view.gallery;

import java.io.File;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Gallery;
import android.widget.SpinnerAdapter;
import ch.almana.android.sessionsmodels.R;
import ch.almana.android.sessionsmodels.helper.ImageHelper;
import ch.almana.android.sessionsmodels.model.SessionModel;
import ch.almana.android.sessionsmodels.view.ModelListFragment;
import ch.almana.android.sessionsmodels.view.adapter.ImageAdapter;

public class ImagePagerActivity extends Activity {

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

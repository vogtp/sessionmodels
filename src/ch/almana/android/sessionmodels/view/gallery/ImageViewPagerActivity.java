package ch.almana.android.sessionmodels.view.gallery;

import java.io.File;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import ch.almana.android.sessionmodels.R;
import ch.almana.android.sessionmodels.model.SessionModel;
import ch.almana.android.sessionmodels.view.ModelListFragment;

public class ImageViewPagerActivity extends FragmentActivity {

	public static final String EXTRA_SESSION_ID = "EXTRA_SESSION_ID";
	public static final String EXTRA_IMAGE_ID = "EXTRA_IMAGE_ID";

	private int sessionId;
	private SessionModel session;
	private File[] images;
	private ViewPager viewPager;

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_view_pager_activity);

		sessionId = getIntent().getIntExtra(EXTRA_SESSION_ID, -1);
		if (sessionId > -1) {
			session = (SessionModel) ModelListFragment.getListItems(this).get(sessionId);
		}
		images = session.getDir().listFiles();

		viewPager = (ViewPager) findViewById(R.id.pager);
		viewPager.setAdapter(new ImageViewPagerAdapter(getSupportFragmentManager(), images));
		viewPager.setCurrentItem(getIntent().getIntExtra(EXTRA_SESSION_ID, 0));
	}

	
}

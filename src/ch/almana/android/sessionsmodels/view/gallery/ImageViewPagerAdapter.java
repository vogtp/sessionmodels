package ch.almana.android.sessionsmodels.view.gallery;

import java.io.File;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;
import ch.almana.android.sessionsmodels.log.Logger;

public class ImageViewPagerAdapter extends FragmentStatePagerAdapter {

	private final File[] images;

	public ImageViewPagerAdapter(FragmentManager fm, File[] images) {
		super(fm);
		this.images = images;
	}

	@Override
	public int getCount() {
		return images.length;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return true;
	}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}

	@Override
	public Fragment getItem(int position) {
		Logger.i("Display image " + position);
		ImageViewPagerItemFragment fragment = new ImageViewPagerItemFragment();
		Bundle args = new Bundle();
		args.putString(ImageViewPagerItemFragment.ARG_IMAGE_PATH, images[position].getAbsolutePath());
		fragment.setArguments(args);
		return fragment;
	}

}

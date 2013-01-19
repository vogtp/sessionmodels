package ch.almana.android.sessionsmodels.view.gallery;

import java.io.File;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import ch.almana.android.sessionsmodels.view.adapter.ImageAdapter;

public class GalleryOverviewImageAdapter extends ImageAdapter {

	public GalleryOverviewImageAdapter(Context ctx, File[] images, int imageSize) {
		super(ctx, images, imageSize);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = super.getView(position, convertView, parent);

		return v;
	}

}

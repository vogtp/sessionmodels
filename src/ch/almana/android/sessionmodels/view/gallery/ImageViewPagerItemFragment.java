package ch.almana.android.sessionmodels.view.gallery;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import ch.almana.android.sessionmodels.R;
import ch.almana.android.sessionmodels.log.Logger;

public class ImageViewPagerItemFragment extends Fragment {

	public static final String ARG_IMAGE_PATH = "ARG_IMAGE_PATH";
	private ImageView ivImage;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.imageviewpager_item_fragemnt, container, false);
		ivImage = (ImageView) v.findViewById(R.id.ivImage);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Bundle arguments = getArguments();
		if (arguments != null) {
			String imagePath = arguments.getString(ARG_IMAGE_PATH);
			if (imagePath != null) {
				Logger.d("Loading display fragment for " + imagePath);
				ivImage.setImageBitmap(BitmapFactory.decodeFile(imagePath));
			}
		}
	}

}

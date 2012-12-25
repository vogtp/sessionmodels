package ch.almana.android.sessionsmodels.view.models;

import java.io.File;

import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.SpinnerAdapter;
import ch.almana.android.sessionsmodels.helper.ImageHelper;

public class ImageAdapter implements SpinnerAdapter {


	private final File[] images;

	public ImageAdapter(File[] images) {
		super();
		this.images = images;
	}

	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
		// TODO Auto-generated method stub

	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getCount() {
		return images.length;
	}

	@Override
	public Object getItem(int position) {
		return images[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@SuppressWarnings("deprecation")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView iv = new ImageView(parent.getContext());
		//iv.setScaleType(ImageView.ScaleType.CENTER);
		iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
		//iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
		//iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
		//iv.setScaleType(ImageView.ScaleType.FIT_XY);
		//		iv.setScaleType(ImageView.ScaleType.FIT_END);

		// Set the Width & Height of the individual images
		iv.setLayoutParams(new Gallery.LayoutParams(150, 150));

		Bitmap b = BitmapFactory.decodeFile(images[position].getAbsolutePath());
		iv.setImageBitmap(ImageHelper.scaleImage(parent.getContext(), b, 150));
		// TODO Auto-generated method stub
		return iv;
	}

	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getViewTypeCount() {
		return 1;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		return null;
	}

}

package ch.almana.android.sessionsmodels.view;

import java.io.FileNotFoundException;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import ch.almana.android.sessionsmodels.R;
import ch.almana.android.sessionsmodels.helper.ImageHelper;
import ch.almana.android.sessionsmodels.log.Logger;
import ch.almana.android.sessionsmodels.model.BaseModel;

public class OverviewAdapter<T extends BaseModel> extends ArrayAdapter<BaseModel> {

	public OverviewAdapter(Context context, int resource, int textViewResourceId, List<BaseModel> objects) {
		super(context, resource, textViewResourceId, objects);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = super.getView(position, convertView, parent);
		ImageView iv = (ImageView)v.findViewById(R.id.ivPreview);
		try {
			BaseModel baseModel = getItem(position);
			iv.setImageBitmap(ImageHelper.scaleImage(parent.getContext(), baseModel.image, 50));
		} catch (FileNotFoundException e) {
			Logger.w("Image not found", e);
		}
		return v;
	}

}

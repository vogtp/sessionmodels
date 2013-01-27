package ch.almana.android.sessionmodels.view.adapter;

import java.io.FileNotFoundException;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import ch.almana.android.sessionmodels.R;
import ch.almana.android.sessionmodels.helper.ImageHelper;
import ch.almana.android.sessionmodels.log.Logger;
import ch.almana.android.sessionmodels.model.BaseModel;
import ch.almana.android.sessionmodels.model.ModelModel;
import ch.almana.android.sessionmodels.model.TitleModel;

public class OverviewAdapter<T extends BaseModel> extends ArrayAdapter<BaseModel> {

	public OverviewAdapter(Context context, int resource, int textViewResourceId, List<BaseModel> objects) {
		super(context, resource, textViewResourceId, objects);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = super.getView(position, convertView, parent);
		ImageView iv = (ImageView) v.findViewById(R.id.ivPreview);
		TextView tv = (TextView) v.findViewById(R.id.tvName);
		BaseModel baseModel = getItem(position);
		Bitmap bm = null;
		if (baseModel.getImage() != null) {
			try {
				bm = ImageHelper.scaleImage(parent.getContext(), baseModel.getImage(), 100);
			} catch (FileNotFoundException e) {
				Logger.w("Image not found", e);
			}
			if (bm != null && bm.getWidth() > 70) {
				iv.setImageBitmap(bm);
			} else {
				bm = null;
			}
		}
		if (bm == null) {
			iv.setImageDrawable(parent.getResources().getDrawable(R.drawable.modelnoimage100));
		}
		if (baseModel instanceof TitleModel) {
			v.setBackgroundColor(Color.LTGRAY);
			iv.setVisibility(View.GONE);
		} else {
			v.setBackgroundColor(parent.getContext().getResources().getColor(android.R.color.background_light));
			iv.setVisibility(View.VISIBLE);
		}
		if (baseModel instanceof ModelModel) {
			tv.setText(((ModelModel) baseModel).getNick());
		}
		return v;
	}
}

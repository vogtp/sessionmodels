package ch.almana.android.sessionsmodels.view.adapter;

import java.io.FileNotFoundException;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import ch.almana.android.sessionsmodels.R;
import ch.almana.android.sessionsmodels.helper.ImageHelper;
import ch.almana.android.sessionsmodels.log.Logger;
import ch.almana.android.sessionsmodels.model.BaseModel;
import ch.almana.android.sessionsmodels.model.ModelModel;
import ch.almana.android.sessionsmodels.model.TitleModel;

public class OverviewAdapter<T extends BaseModel> extends ArrayAdapter<BaseModel> {

	public OverviewAdapter(Context context, int resource, int textViewResourceId, List<BaseModel> objects) {
		super(context, resource, textViewResourceId, objects);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = super.getView(position, convertView, parent);
		ImageView iv = (ImageView)v.findViewById(R.id.ivPreview);
		TextView tv = (TextView) v.findViewById(R.id.tvName);
		try {
			BaseModel baseModel = getItem(position);
			if (baseModel.getImage() != null) {
				iv.setVisibility(View.VISIBLE);
				iv.setImageBitmap(ImageHelper.scaleImage(parent.getContext(), baseModel.getImage(), 100));
			} else {
				iv.setVisibility(View.GONE);
			}
			if (baseModel instanceof TitleModel) {
				v.setBackgroundColor(Color.LTGRAY);
			} else {
				v.setBackgroundColor(parent.getContext().getResources().getColor(android.R.color.background_light));
			}
			if (baseModel instanceof ModelModel) {
				tv.setText(((ModelModel) baseModel).getNick());
			}
		} catch (FileNotFoundException e) {
			Logger.w("Image not found", e);
		}
		return v;
	}

}

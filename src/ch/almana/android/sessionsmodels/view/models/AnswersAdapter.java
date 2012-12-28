package ch.almana.android.sessionsmodels.view.models;

import android.support.v4.app.FragmentActivity;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import ch.almana.android.sessionsmodels.model.AnswersModel;

public class AnswersAdapter extends ArrayAdapter<AnswersModel> implements ListAdapter {

	public AnswersAdapter(FragmentActivity activity, AnswersModel[] answersModels) {
		super(activity, android.R.layout.simple_list_item_1, android.R.id.text1, answersModels);
	}


}

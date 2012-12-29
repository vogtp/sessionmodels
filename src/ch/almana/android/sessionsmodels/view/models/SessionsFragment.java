package ch.almana.android.sessionsmodels.view.models;

import java.io.File;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import ch.almana.android.sessionsmodels.R;
import ch.almana.android.sessionsmodels.model.SessionModel;
import ch.almana.android.sessionsmodels.view.ImagePagerActivity;
import ch.almana.android.sessionsmodels.view.ModelListFragment;

public class SessionsFragment extends Fragment implements OnItemClickListener {

	private SessionModel session;

	private GridView listview;

	private int sessionId;

	public SessionsFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments().containsKey(ModelDetailFragment.EXTRA_MODEL_ID)) {
			sessionId = getArguments().getInt(ModelDetailFragment.EXTRA_MODEL_ID);
			session = (SessionModel) ModelListFragment.getListItems(getActivity()).get(sessionId);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_sessions, container, false);
		listview = ((GridView) rootView.findViewById(android.R.id.list));
		listview.setOnItemClickListener(this);
		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();

		if (session != null) {
			File[] images = session.getDir().listFiles();
			listview.setAdapter(new ImageAdapter(images, 250));

		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

		Intent i = new Intent(getActivity(), ImagePagerActivity.class);
		i.putExtra(ImagePagerActivity.EXTRA_SESSION_ID, sessionId);
		i.putExtra(ImagePagerActivity.EXTRA_IMAGE_ID, position);
		getActivity().startActivity(i);
	}

}

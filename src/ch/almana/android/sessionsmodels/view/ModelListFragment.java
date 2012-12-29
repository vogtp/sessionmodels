package ch.almana.android.sessionsmodels.view;

import java.util.ArrayList;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import ch.almana.android.sessionsmodels.R;
import ch.almana.android.sessionsmodels.access.ModelAcess;
import ch.almana.android.sessionsmodels.access.SessionAcess;
import ch.almana.android.sessionsmodels.model.BaseModel;
import ch.almana.android.sessionsmodels.model.TitleModel;
import ch.almana.android.sessionsmodels.view.adapter.OverviewAdapter;

public class ModelListFragment extends ListFragment {

	public static final String LIST_CHANGED = "LIST_CHANGED";

	private static final String STATE_ACTIVATED_POSITION = "activated_position";

	/**
	 * The fragment's current callback object, which is notified of list item
	 * clicks.
	 */
	private Callbacks mCallbacks = sDummyCallbacks;

	/**
	 * The current activated item position. Only used on tablets.
	 */
	private int mActivatedPosition = ListView.INVALID_POSITION;

	/**
	 * A callback interface that all activities containing this fragment must
	 * implement. This mechanism allows activities to be notified of item
	 * selections.
	 */
	public interface Callbacks {
		/**
		 * Callback for when an item has been selected.
		 */
		public void onItemSelected(int id);
	}

	/**
	 * A dummy implementation of the {@link Callbacks} interface that does
	 * nothing. Used only when this fragment is not attached to an activity.
	 */
	private static Callbacks sDummyCallbacks = new Callbacks() {
		@Override
		public void onItemSelected(int id) {
		}
	};

	private static ArrayList<BaseModel> listItems;

	private BroadcastReceiver listchangedReceiver;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public ModelListFragment() {
	}

	public static ArrayList<BaseModel> getListItems(Context ctx) {
		return getListItems(ctx, false);
	}
	public static ArrayList<BaseModel> getListItems(Context ctx, boolean reread) {
		if (listItems == null || reread) {
			listItems = new ArrayList<BaseModel>();
			listItems.add(new TitleModel(ctx.getString(R.string.sessions)));
			listItems.addAll(SessionAcess.getSessions(reread));
			listItems.add(new TitleModel(ctx.getString(R.string.models)));
			listItems.addAll(ModelAcess.getModels(reread));
		}
		return listItems;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		updateList(false);
	}

	private void updateList(boolean rereadFiles) {
		listItems = null;
		ArrayAdapter<BaseModel> adapter = new OverviewAdapter<BaseModel>(
				getActivity(),
				R.layout.list_item,
				R.id.tvName,
				getListItems(getActivity(), rereadFiles));
		setListAdapter(adapter);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		// Restore the previously serialized activated item position.
		if (savedInstanceState != null
				&& savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
			setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// Activities containing this fragment must implement its callbacks.
		if (!(activity instanceof Callbacks)) {
			throw new IllegalStateException("Activity must implement fragment's callbacks.");
		}
		if (listchangedReceiver == null) {
			listchangedReceiver = new BroadcastReceiver() {
				@Override
				public void onReceive(Context context, Intent intent) {
					updateList(true);
				}
			};
		}
		activity.registerReceiver(listchangedReceiver, new IntentFilter(LIST_CHANGED));
		mCallbacks = (Callbacks) activity;
	}

	@Override
	public void onDetach() {
		super.onDetach();
		if (listchangedReceiver != null) {
			getActivity().unregisterReceiver(listchangedReceiver);
		}
		// Reset the active callbacks interface to the dummy implementation.
		mCallbacks = sDummyCallbacks;
	}

	@Override
	public void onListItemClick(ListView listView, View view, int position, long id) {
		super.onListItemClick(listView, view, position, id);

		// Notify the active callbacks interface (the activity, if the
		// fragment is attached to one) that an item has been selected.
		mCallbacks.onItemSelected(position);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (mActivatedPosition != ListView.INVALID_POSITION) {
			// Serialize and persist the activated item position.
			outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
		}
	}

	/**
	 * Turns on activate-on-click mode. When this mode is on, list items will be
	 * given the 'activated' state when touched.
	 */
	public void setActivateOnItemClick(boolean activateOnItemClick) {
		// When setting CHOICE_MODE_SINGLE, ListView will automatically
		// give items the 'activated' state when touched.
		getListView().setChoiceMode(activateOnItemClick
				? ListView.CHOICE_MODE_SINGLE
				: ListView.CHOICE_MODE_NONE);
	}

	private void setActivatedPosition(int position) {
		if (position == ListView.INVALID_POSITION) {
			getListView().setItemChecked(mActivatedPosition, false);
		} else {
			getListView().setItemChecked(position, true);
		}

		mActivatedPosition = position;
	}

}

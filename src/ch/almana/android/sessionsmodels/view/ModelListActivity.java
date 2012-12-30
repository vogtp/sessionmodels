package ch.almana.android.sessionsmodels.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import ch.almana.android.sessionsmodels.R;
import ch.almana.android.sessionsmodels.access.ModelAcess;
import ch.almana.android.sessionsmodels.model.BaseModel;
import ch.almana.android.sessionsmodels.view.models.ModelDetailFragment;

/**
 * An activity representing a list of Models. This activity has different
 * presentations for handset and tablet-size devices. On handsets, the activity
 * presents a list of items, which when touched, lead to a
 * {@link ModelDetailActivity} representing item details. On tablets, the
 * activity presents the list of items and item details side-by-side using two
 * vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link ModelListFragment} and the item details (if present) is a
 * {@link ModelDetailFragment}.
 * <p>
 * This activity also implements the required
 * {@link ModelListFragment.Callbacks} interface to listen for item selections.
 */
public class ModelListActivity extends FragmentActivity
		implements ModelListFragment.Callbacks {

	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */
	private boolean mTwoPane;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_model_list);

		if (findViewById(R.id.model_detail_container) != null) {
			// The detail container view will be present only in the
			// large-screen layouts (res/values-large and
			// res/values-sw600dp). If this view is present, then the
			// activity should be in two-pane mode.
			mTwoPane = true;

			// In two-pane mode, list items should be given the
			// 'activated' state when touched.
			((ModelListFragment) getSupportFragmentManager()
					.findFragmentById(R.id.model_list))
					.setActivateOnItemClick(true);
			loadDetailsFragment(0);
		}

		// TODO: If exposing deep links into your app, handle intents here.
	}

	/**
	 * Callback method from {@link ModelListFragment.Callbacks} indicating that
	 * the item with the given ID was selected.
	 */
	@Override
	public void onItemSelected(int id) {
		if (mTwoPane) {
			// In two-pane mode, show the detail view in this activity by
			// adding or replacing the detail fragment using a
			// fragment transaction.
			loadDetailsFragment(id);

		} else {
			// In single-pane mode, simply start the detail activity
			// for the selected item ID.
			Intent detailIntent = new Intent(this, ModelDetailActivity.class);//BaseModel.getClassById(id));
			detailIntent.putExtra(ModelDetailFragment.EXTRA_MODEL_ID, id);
			startActivity(detailIntent);
		}
	}

	private void loadDetailsFragment(int id) {
		Bundle arguments = new Bundle();
		arguments.putInt(ModelDetailFragment.EXTRA_MODEL_ID, id);
		Fragment fragment = BaseModel.getInstanceById(this, id);

		fragment.setArguments(arguments);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.model_detail_container, fragment)
				.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.main_options, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.itemAddModel:
			ModelAcess.addModel(this);
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}

	}
}

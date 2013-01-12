package ch.almana.android.sessionsmodels.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import ch.almana.android.sessionsmodels.R;
import ch.almana.android.sessionsmodels.model.BaseModel;
import ch.almana.android.sessionsmodels.view.models.ModelDetailFragment;

/**
 * An activity representing a single Model detail screen. This activity is only
 * used on handset devices. On tablet-size devices, item details are presented
 * side-by-side with a list of items in a {@link ModelListActivity}.
 * <p>
 * This activity is mostly just a 'shell' activity containing nothing more than
 * a {@link ModelDetailFragment}.
 */
public class ModelDetailActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_model_detail);

		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);

		// savedInstanceState is non-null when there is fragment state
		// saved from previous configurations of this activity
		// (e.g. when rotating the screen from portrait to landscape).
		// In this case, the fragment will automatically be re-added
		// to its container so we don't need to manually add it.
		// For more information, see the Fragments API guide at:
		//
		// http://developer.android.com/guide/components/fragments.html
		//
		if (savedInstanceState == null) {
			// Create the detail fragment and add it to the activity
			// using a fragment transaction.
			Bundle arguments = new Bundle();
			int id = getIntent().getIntExtra(ModelDetailFragment.EXTRA_MODEL_ID, -1);
			arguments.putInt(ModelDetailFragment.EXTRA_MODEL_ID,
					id);
			Fragment fragment = BaseModel.getInstanceById(this, id);
			if (fragment != null) {
				fragment.setArguments(arguments);
				getSupportFragmentManager().beginTransaction()
						.add(R.id.model_detail_container, fragment)
						.commit();
			}
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpTo(this, new Intent(this, ModelListActivity.class));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}

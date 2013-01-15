package ch.almana.android.sessionsmodels.access;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;
import android.widget.Toast;
import ch.almana.android.sessionsmodels.R;
import ch.almana.android.sessionsmodels.log.Logger;
import ch.almana.android.sessionsmodels.model.ModelModel;
import ch.almana.android.sessionsmodels.view.ModelListFragment;

public class ModelAcess extends DirectoryAccess {

	public static final List<ModelModel> DUMMIES = new ArrayList<ModelModel>();

	private static List<ModelModel> items = new ArrayList<ModelModel>();

	private static Map<String, ModelModel> ITEM_MAP = new HashMap<String, ModelModel>();

	public static List<ModelModel> getModels(boolean reread, boolean hideNotcurrentModels) {

		if (reread || items.size() < 1) {
			items.clear();
			File modelsDir = getModelsDir();
			File[] models = modelsDir.listFiles(DirectoryAccess.directoryFilter);
			if (models == null) {
				return DUMMIES;
			}
			for (int i = 0; i < models.length; i++) {
				File m = models[i];
				ModelModel model = null;
				try {
					model = new ModelModel(readJsonInfo(m));
				} catch (Exception e) {
					Logger.e("Cannot parse json", e);
				}
				if (model == null) {
					model = new ModelModel(m.getName(), m, new File(m, "model.JPG"));
				}
				if (hideNotcurrentModels) {
					if (model.isCurrent()) {
						addItem(model);
					}
				} else {
					addItem(model);
				}
			}
		}
		return items;
	}

	private static void addItem(ModelModel item) {
		items.add(item);
		ITEM_MAP.put(item.getName(), item);
	}

	public static void addModel(final Context ctx) {
		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		builder.setTitle(R.string.dia_add_model_title);
		builder.setMessage(R.string.dia_add_model_msg);
		final EditText userInput = new EditText(ctx);
		builder.setView(userInput);
		builder.setCancelable(false);
		builder.setPositiveButton(android.R.string.ok,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						String nick = userInput.getText().toString();
						File modelDir = new File(getModelsDir(), nick);
						if (modelDir.exists()) {
							int i = 1;
							while (modelDir.exists()) {
								// change the directory name
								modelDir = new File(getModelsDir(), nick + i++);
							}
						}
						modelDir.mkdir();
						ModelModel model = new ModelModel(nick, modelDir);
						try {
							save(model);
							ModelListFragment.sendListChangedBroadcast(ctx);
						} catch (Exception e) {
							Logger.e("Cannot save model info after creating new model", e);
							Toast.makeText(ctx, R.string.dia_add_model_fail, Toast.LENGTH_LONG).show();
						}
					}
				});
		builder.setNegativeButton(android.R.string.cancel, null);

		AlertDialog alertDialog = builder.create();
		alertDialog.show();
	}

}
